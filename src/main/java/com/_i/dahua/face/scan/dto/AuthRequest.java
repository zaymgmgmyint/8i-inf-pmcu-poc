package com._i.dahua.face.scan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthRequest {
    private String userName;
    private String ipAddress;
    @JsonProperty("clientType")
    private String clientType;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }
}
