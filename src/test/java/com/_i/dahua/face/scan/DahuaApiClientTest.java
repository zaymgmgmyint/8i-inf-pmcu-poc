package com._i.dahua.face.scan;

import com._i.dahua.face.scan.dto.MqDataResponse;
import com._i.dahua.face.scan.service.DahuaApiClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "dahua.api.base-url=${DAHUA_API_URL:https://180.180.217.182:443}",
    "dahua.api.username=${DAHUA_API_USERNAME:system}",
    "dahua.api.password=${DAHUA_API_PASSWORD:ismart123456}",
    "dahua.api.client-type=WINPC_V2",
    "dahua.api.ip-address=192.168.100.16"
})
public class DahuaApiClientTest {

    @Autowired
    private DahuaApiClient dahuaApiClient;

    @Test
    public void testAuthentication() {
        try {
            String token = dahuaApiClient.getToken();
            assertNotNull(token, "Authentication token should not be null");
            System.out.println("Authentication token: " + token);
            assertFalse(token.isEmpty(), "Authentication token should not be empty");
            System.out.println("Successfully authenticated. Token: " + token);
        } catch (Exception e) {
            fail("Authentication failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetMqConfig() {
        try {
            // First authenticate
            String token = dahuaApiClient.getToken();
            assertNotNull(token, "Authentication token should not be null");
            
            // Fetch MQ configuration
            MqDataResponse response = dahuaApiClient.getMqData(token);
            
            // Verify response
            assertNotNull(response, "MQ configuration response should not be null");
            System.out.println("MQ Configuration Response: " + response);
            
            // Check response code - 1000 is success, but some systems might return 1001
            if (response.getCode() != 1000 && response.getCode() != 1001) {
                fail(String.format("Unexpected response code: %d - %s", 
                    response.getCode(), 
                    response.getDesc() != null ? response.getDesc() : "No description"));
            }
            
            System.out.println("Successfully fetched MQ configuration. Status: " + response.getDesc());
            
            // Log available details
            System.out.println("Status: " + response.getDesc());
            System.out.println("MQTT: " + response.getData().getMqtt());
            System.out.println("AMQP: " + response.getData().getAmqp());
            System.out.println("STOMP: " + response.getData().getStomp());
            System.out.println("WSS: " + response.getData().getWss());
            System.out.println("Address: " + response.getData().getAddr());
            System.out.println("Username: " + response.getData().getUserName());
            System.out.println("Password: " + response.getData().getPassword());
            System.out.println("TLS Enabled: " + response.getData().getEnableTls());
            
        } catch (Exception e) {
            fail("Failed to fetch MQ configuration: " + e.getMessage());
        }
    }
}
