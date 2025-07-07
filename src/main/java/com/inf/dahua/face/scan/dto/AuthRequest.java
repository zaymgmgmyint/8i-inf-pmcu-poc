package com.inf.dahua.face.scan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * DTO for Dahua authentication requests.
 * Used in the initial authentication phase with the Dahua API.
 */
@Data
public class AuthRequest {
    private String userName;
    private String ipAddress;
    @JsonProperty("clientType")
    private String clientType;
}
