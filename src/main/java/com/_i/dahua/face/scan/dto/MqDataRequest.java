package com._i.dahua.face.scan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MqDataRequest {
    @JsonProperty("topic")
    private String topic;
    @JsonProperty("groupId")
    private String groupId;
    @JsonProperty("offset")
    private String offset;
    @JsonProperty("limit")
    private int limit = 10;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

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
