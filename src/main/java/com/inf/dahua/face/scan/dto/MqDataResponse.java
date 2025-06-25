package com.inf.dahua.face.scan.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MqDataResponse {
    @JsonProperty("code")
    private int code;
    
    @JsonProperty("desc")
    private String desc;
    
    @JsonProperty("data")
    private MqConfigData data;
    
    public boolean isSuccess() {
        // Both 0 and 1000 indicate success in the Dahua API
        return code == 0 || code == 1000;
    }
    
    public void setData(MqConfigData data) {
        this.data = data;
    }
    
    public int getCode() {
        return code;
    }
    
    public MqConfigData getData() {
        return data;
    }
    
    public void setCode(int code) {
        this.code = code;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MqConfigData {
        @JsonProperty("enableTls")
        private String enableTls;
        
        @JsonProperty("mqtt")
        private String mqtt;  // Changed from String to String to handle IP:port format
        
        @JsonProperty("amqp")
        private String amqp;
        
        @JsonProperty("stomp")
        private String stomp;
        
        @JsonProperty("wss")
        private String wss;
        
        @JsonProperty("addr")
        private String addr;
        
        @JsonProperty("userName")
        private String userName;
        
        @JsonProperty("password")
        private String password;

        // Getters and Setters
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


        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
