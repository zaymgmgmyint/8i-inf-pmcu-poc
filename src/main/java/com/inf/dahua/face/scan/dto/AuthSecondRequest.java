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


//    public String getSignature() {
//        return signature;
//    }
//
//    public void setSignature(String signature) {
//        this.signature = signature;
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//    public String getRandomKey() {
//        return randomKey;
//    }
//    public void setRandomKey(String randomKey) {
//        this.randomKey = randomKey;
//    }
//    public String getEncryptType() {
//        return encryptType;
//    }
//    public void setEncryptType(String encryptType) {
//        this.encryptType = encryptType;
//    }
//    public String getIpAddress() {
//        return ipAddress;
//    }
//    public void setIpAddress(String ipAddress) {
//        this.ipAddress = ipAddress;
//    }
//    public String getClientType() {
//        return clientType;
//    }
//    public void setClientType(String clientType) {
//        this.clientType = clientType;
//    }
//    public String getPublicKey() {
//        return publicKey;
//    }
//    public void setPublicKey(String publicKey) {
//        this.publicKey = publicKey;
//    }
//    public String getUserType() {
//        return userType;
//    }
//    public void setUserType(String userType) {
//        this.userType = userType;
//    }
}
