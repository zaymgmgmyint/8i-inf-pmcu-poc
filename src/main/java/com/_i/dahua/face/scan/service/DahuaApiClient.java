package com._i.dahua.face.scan.service;

import com._i.dahua.face.scan.dto.*;
import com._i.dahua.face.scan.util.InsecureTrustManagerFactory;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DahuaApiClient {
    private static final Logger log = LoggerFactory.getLogger(DahuaApiClient.class);

    @Value("${dahua.api.base-url}")
    private String baseUrl;
    
    @Value("${dahua.api.username}")
    private String username;
    
    @Value("${dahua.api.password}")
    private String password;
    
    @Value("${dahua.api.client-type:WINPC_V2}")
    private String clientType;
    
    @Value("${dahua.api.ip-address:}")
    private String ipAddress;
    
    private WebClient webClient;
    
    @PostConstruct
    public void init() {
        try {
            // Create HTTP client that trusts all certificates
            SslContext sslContext = SslContextBuilder
                    .forClient()
                    .trustManager(InsecureTrustManagerFactory.getTrustManagers()[0])
                    .build();

            HttpClient httpClient = HttpClient.create()
                    .secure(spec -> spec.sslContext(sslContext));

            this.webClient = WebClient.builder()
                    .baseUrl(baseUrl)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .clientConnector(new ReactorClientHttpConnector(httpClient))
                    .build();
        } catch (Exception e) {
            log.error("Failed to initialize WebClient: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize WebClient", e);
        }
    }

    public String getToken() {
        try {
            log.info("Starting authentication with username: {}", username);
            
            // First authentication step - get challenge
            AuthRequest authRequest = new AuthRequest();
            authRequest.setUserName(username);
            authRequest.setIpAddress(ipAddress);
            authRequest.setClientType(clientType);

            log.info("Sending first auth request to {}/brms/api/v1.0/accounts/authorize", baseUrl);
            log.info("Request body: username={}, clientType={}, ipAddress={}", 
                    username, clientType, ipAddress);
            
            // First request - expect 401 with challenge data
            AuthResponse firstResponse = webClient.post()
                    .uri("/brms/api/v1.0/accounts/authorize")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(authRequest)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() && status.value() != 401, response -> {
                        log.error("Unexpected error in first auth request: {}", response.statusCode());
                        return response.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .flatMap(body -> {
                                    log.error("Error response body: {}", body);
                                    return Mono.error(new RuntimeException("First auth request failed: " + 
                                            response.statusCode() + " - " + body));
                                });
                    })
                    .bodyToMono(AuthResponse.class)
                    .onErrorResume(WebClientResponseException.class, e -> {
                        if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                            try {
                                // Parse the error response as AuthResponse
                                ObjectMapper mapper = new ObjectMapper();
                                String responseBody = e.getResponseBodyAsString();
                                log.debug("Received 401 response with body: {}", responseBody);
                                
                                AuthResponse challenge = mapper.readValue(responseBody, AuthResponse.class);
                                log.info("Successfully parsed challenge data - realm: {}, randomKey: {}", 
                                        challenge.getRealm(), challenge.getRandomKey());
                                return Mono.just(challenge);
                            } catch (Exception ex) {
                                log.error("Failed to parse challenge data: {}", ex.getMessage());
                                log.error("Response body: {}", e.getResponseBodyAsString(), ex);
                                return Mono.error(new RuntimeException("Failed to parse challenge data", ex));
                            }
                        }
                        log.error("Error in first auth request: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
                        return Mono.error(e);
                    })
                    .block();

            if (firstResponse == null) {
                throw new RuntimeException("Failed to get authentication challenge: Empty response");
            }

            log.info("Successfully received auth challenge - realm: {}, randomKey: {}, encryptType: {}", 
                    firstResponse.getRealm(), firstResponse.getRandomKey(), firstResponse.getEncryptType());

            // Second authentication step
            String signature = generateSignature(
                    firstResponse.getRealm(),
                    firstResponse.getRandomKey()
            );
            log.debug("Generated signature: {}", signature);

            AuthSecondRequest secondRequest = new AuthSecondRequest();
            secondRequest.setSignature(signature);
            secondRequest.setUserName(username);
            secondRequest.setRandomKey(firstResponse.getRandomKey());
            secondRequest.setEncryptType(firstResponse.getEncryptType() != null ? firstResponse.getEncryptType() : "MD5");
            secondRequest.setClientType(clientType);
            secondRequest.setIpAddress(ipAddress);
            secondRequest.setUserType("0");
            
            // Set public key if available from challenge
            if (firstResponse.getPublicKey() != null) {
                secondRequest.setPublicKey(firstResponse.getPublicKey());
                log.debug("Included public key in second request");
            }

            log.info("Sending second auth request with signature");
            log.debug("Second request body: {}", secondRequest);
            
            AuthResponse secondResponse = webClient.post()
                    .uri("/brms/api/v1.0/accounts/authorize")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(secondRequest)
                    .retrieve()
                    .onStatus(status -> status.isError(), response -> {
                        log.error("Second auth request failed with status: {}", response.statusCode());
                        return response.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .flatMap(body -> {
                                    log.error("Error response body: {}", body);
                                    String errorMessage = String.format(
                                            "Second auth request failed with status %d: %s",
                                            response.statusCode().value(), body);
                                    log.error(errorMessage);
                                    return Mono.error(new RuntimeException(errorMessage));
                                });
                    })
                    .bodyToMono(AuthResponse.class)
                    .onErrorResume(WebClientResponseException.class, e -> {
                        String errorMessage = String.format("Second auth request failed with status %d: %s", 
                                e.getStatusCode().value(), e.getResponseBodyAsString());
                        log.error(errorMessage);
                        log.debug("Full error response: {}", e.getResponseBodyAsString());
                        return Mono.error(new RuntimeException(errorMessage, e));
                    })
                    .onErrorResume(e -> {
                        log.error("Unexpected error during second auth request", e);
                        return Mono.error(new RuntimeException("Unexpected error during authentication", e));
                    })
                    .block();

            if (secondResponse == null) {
                throw new RuntimeException("Authentication failed: Empty response received");
            }
            
            if (secondResponse.getToken() == null) {
                log.error("Authentication failed: No token in response. Full response: {}", secondResponse);
                throw new RuntimeException("Authentication failed: No token received in response");
            }

            log.info("Successfully authenticated user: {}", username);
            log.debug("Auth token: {}", secondResponse.getToken());
            log.debug("Auth response details: userId={}, userGroupId={}, duration={}s", 
                    secondResponse.getUserId(), 
                    secondResponse.getUserGroupId(),
                    secondResponse.getDuration());
                    
            return secondResponse.getToken();

        } catch (Exception e) {
            String errorMsg = "Authentication failed: " + e.getMessage();
            log.error(errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getFaceRecords(String token, int page, int pageSize, String startTime, String endTime) {
        try {
            FaceRecordRequest request = new FaceRecordRequest();
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPageNo(page);
            pageInfo.setPageSize(pageSize);
            request.setPageInfo(pageInfo);
            
            if (startTime != null && !startTime.isEmpty()) {
                request.setStartTime(startTime);
            }
            if (endTime != null && !endTime.isEmpty()) {
                request.setEndTime(endTime);
            }

            Map<String, Object> response = webClient.post()
                    .uri("/brms/api/v1.0/face/snapshot/record/page")
                    .header("X-Subject-Token", token)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            
            return response != null ? response : Map.of();

        } catch (WebClientResponseException e) {
            log.error("Failed to fetch face records: {} - {}", e.getStatusCode().value(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to fetch face records: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            log.error("Unexpected error while fetching face records: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch face records", e);
        }
    }
    
    /**
     * Fetches MQ configuration from DSS
     * @param token Authentication token
     * @return MQ configuration response
     */
    public MqDataResponse getMqData(String token) {
        String url = baseUrl + "/brms/api/v1.0/BRM/Config/GetMqConfig";
        
        try {
            log.info("Fetching MQ configuration");
            log.debug("Sending request to {} with token: {}...{}", 
                    url, token.substring(0, 5), token.substring(token.length() - 5));
            
            // First get the raw response as string for debugging
            log.info("Sending MQ configuration POST request to: {}", url);
            log.debug("Request headers - X-Subject-Token: {}...{}", 
                    token.substring(0, Math.min(5, token.length())), 
                    token.substring(Math.max(0, token.length() - 5)));
            
            // Create an empty JSON object for the request body
            Map<String, Object> requestBody = Map.of();
            
            String rawResponse = webClient.post()
                    .uri(url)
                    .header("X-Subject-Token", token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .exchangeToMono(clientResponse -> {
                        // Log response status and headers
                        log.debug("MQ config response status: {}", clientResponse.statusCode());
                        clientResponse.headers().asHttpHeaders().forEach((name, values) -> 
                            log.debug("Response header - {}: {}", name, values));
                            
                        return clientResponse.bodyToMono(String.class)
                            .defaultIfEmpty("No response body")
                            .flatMap(body -> {
                                log.debug("Raw MQ configuration response body: {}", body);
                                if (clientResponse.statusCode().isError()) {
                                    log.error("Failed to fetch MQ configuration. Status: {}, Response: {}", 
                                            clientResponse.statusCode(), body);
                                    return Mono.error(new RuntimeException("Failed to fetch MQ configuration. Status: " + 
                                            clientResponse.statusCode() + ", Response: " + body));
                                }
                                return Mono.just(body);
                            });
                    })
                    .block();
                    
            log.debug("Raw MQ configuration response: {}", rawResponse);
            
            if (rawResponse == null || rawResponse.trim().isEmpty()) {
                log.error("Received empty response from MQ configuration endpoint");
                return new MqDataResponse();
            }
            
            try {
                // Configure ObjectMapper to be more lenient with type coercion
                ObjectMapper objectMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
                    .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
                
                // First parse to a generic map to see the actual structure
                Map<String, Object> responseMap = objectMapper.readValue(rawResponse, new TypeReference<Map<String, Object>>() {});
                log.debug("Raw response map: {}", responseMap);
                
                // Now parse to our DTO
                MqDataResponse response = objectMapper.convertValue(responseMap, MqDataResponse.class);
                
                log.info("Successfully fetched MQ configuration. Response code: {}", response.getCode());
                
                // Log MQ configuration if available
                if (response.getData() != null) {
                    log.debug("MQ Configuration - MQTT: {}, AMQP: {}, STOMP: {}, WSS: {}, Addr: {}", 
                            response.getData().getMqtt(), 
                            response.getData().getAmqp(), 
                            response.getData().getStomp(), 
                            response.getData().getWss(), 
                            response.getData().getAddr());
                }
                
                // If the response indicates failure, log more details
                if (response.getCode() != 1000 && response.getCode() != 1001) {
                    log.warn("MQ configuration request failed with code: {}, message: {}", 
                            response.getCode(), response.getDesc());
                } else if (response.getData() == null || response.getData().getMqtt() == null) {
                    log.warn("MQ configuration data is null or incomplete. Response code: {}, message: {}", 
                            response.getCode(), response.getDesc());
                }
                
                return response;
                
            } catch (Exception e) {
                log.error("Error parsing MQ configuration response: {}", e.getMessage(), e);
                throw new RuntimeException("Error parsing MQ configuration: " + e.getMessage(), e);
            }
            
        } catch (WebClientResponseException e) {
            String errorBody = e.getResponseBodyAsString();
            log.error("Failed to fetch MQ data: {} - {}", e.getStatusCode().value(), errorBody);
            throw new RuntimeException("Failed to fetch MQ data: " + errorBody, e);
        } catch (Exception e) {
            log.error("Unexpected error while fetching MQ data: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch MQ data: " + e.getMessage(), e);
        }
    }

    private String generateSignature(String realm, String randomKey) throws NoSuchAlgorithmException {
        String temp1 = md5(password);
        String temp2 = md5(username + temp1);
        String temp3 = md5(temp2);
        String temp4 = md5(username + ":" + realm + ":" + temp3);
        return md5(temp4 + ":" + randomKey);
    }

    private String md5(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
