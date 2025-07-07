package com.inf.dahua.face.scan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Request object for MQ operations.
 * Contains configuration for message queue consumption and acknowledgment.
 */
@Data
public class MqDataRequest {
    @JsonProperty("topic")
    private String topic;
    
    @JsonProperty("groupId")
    private String groupId = "default-group";
    
    @JsonProperty("offset")
    private String offset = "latest";
    
    @JsonProperty("limit")
    private int limit = 10;
    
    @JsonProperty("autoAck")
    private boolean autoAck = true;
    
    @JsonProperty("timeout")
    private long timeout = 5000;
}
