package com.inf.dahua.face.scan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Request object for MQ operations
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
    private long timeout = 5000; // 5 seconds

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
