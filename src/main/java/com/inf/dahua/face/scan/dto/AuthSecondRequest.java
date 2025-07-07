package com.inf.dahua.face.scan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthSecondRequest {
    private String signature;
    private String userName;
    @JsonProperty("randomKey")
    private String randomKey;
    @JsonProperty("encryptType")
    private String encryptType;
    private String ipAddress;
    private String clientType;
    @JsonProperty("publicKey")
    private String publicKey;
    private String userType = "0";

    private String secretKey;

    private String secretVector;



}
