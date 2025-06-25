package com.inf.dahua.face.scan.controller;

import com.inf.dahua.face.scan.dto.AuthResponse;
import com.inf.dahua.face.scan.dto.MqDataResponse;
import com.inf.dahua.face.scan.service.DahuaApiClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Tag(name = "Dahua API", description = "Endpoints for interacting with Dahua API")
@RestController
@RequestMapping("/api/dahua")
public class DahuaController {
    private static final Logger log = LoggerFactory.getLogger(DahuaController.class);

    private final DahuaApiClient dahuaApiClient;

    public DahuaController(DahuaApiClient dahuaApiClient) {
        this.dahuaApiClient = dahuaApiClient;
    }

    @Operation(
        summary = "Get authentication token",
        description = "Performs two-step authentication with Dahua BRM system and returns an access token",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully authenticated and retrieved token",
                content = @Content(
                    mediaType = "text/plain",
                    examples = @ExampleObject(value = "F739227F17394B6C97819be5500976cb")
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Authentication failed"
            )
        }
    )
    @GetMapping("/token")
    @ResponseBody
    public String getToken() {
        return dahuaApiClient.getToken();
    }

    @GetMapping("/first-auth")
    @ResponseBody
    public AuthResponse getFirstAuthResponse() {
        return dahuaApiClient.getFirstLoginResponse();
    }

    @GetMapping("/second-auth")
    @ResponseBody
    public AuthResponse getSecondAuthResponse() {
        return dahuaApiClient.getSecondLoginResponse();
    }


    @Operation(summary = "Get MQ configuration from DSS")
    @GetMapping("/mq-config")
    public MqDataResponse getMqConfig() {
        String token = getToken();
        return dahuaApiClient.getMqData(token);
    }
}
