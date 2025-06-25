package com.inf.dahua.face.scan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

/**
 * MQ configuration data received from Dahua API
 */
@Data
@ToString
public class MqConfigData {
    private static final String ADDR_DELIMITER = ":";
    private static final int DEFAULT_STOMP_PORT = 61613;
    private static final int DEFAULT_WSS_PORT = 15674;
    @JsonProperty("enableTls")
    private String enableTls;
    
    private String mqtt;
    private String amqp;
    private String stomp;
    private String wss;
    private String addr;
    
    @JsonProperty("userName")
    private String username;
    
    private String password;

    // Explicit getters and setters for all fields
    public String getEnableTls() {
        return enableTls;
    }

    public void setEnableTls(String enableTls) {
        this.enableTls = enableTls;
    }

    public String getMqtt() {
        return mqtt;
    }

    public void setMqtt(String mqtt) {
        this.mqtt = mqtt;
    }

    public String getAmqp() {
        return amqp;
    }

    public void setAmqp(String amqp) {
        this.amqp = amqp;
    }

    public String getStomp() {
        return stomp;
    }

    public void setStomp(String stomp) {
        this.stomp = stomp;
    }

    public String getWss() {
        return wss;
    }

    public void setWss(String wss) {
        this.wss = wss;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Additional methods needed by DahuaMqService
    
    /**
     * Check if SSL/TLS is enabled
     * @return true if SSL/TLS is enabled
     */
    public boolean isSsl() {
        return "1".equals(enableTls) || "true".equalsIgnoreCase(enableTls);
    }
    
    /**
     * Get the host part of the address
     * @return host name or IP
     */
    public String getHost() {
        if (addr == null || addr.isEmpty()) {
            return "localhost";
        }
        String[] parts = addr.split(ADDR_DELIMITER);
        return parts[0];
    }
    
    /**
     * Get the port number
     * @return port number
     */
    public int getPort() {
        if (addr == null || !addr.contains(ADDR_DELIMITER)) {
            return isSsl() ? DEFAULT_WSS_PORT : DEFAULT_STOMP_PORT;
        }
        try {
            String[] parts = addr.split(ADDR_DELIMITER);
            return Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            return isSsl() ? DEFAULT_WSS_PORT : DEFAULT_STOMP_PORT;
        }
    }
    
    /**
     * Get client ID for MQ connection
     * @return client ID
     */
    public String getClientId() {
        return "dahua-client-" + System.currentTimeMillis();
    }
}
