package com.inf.dahua.face.scan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * MQ configuration data received from Dahua API.
 * Contains connection details and authentication information.
 */
@Data
public class MqConfigData {
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
}
