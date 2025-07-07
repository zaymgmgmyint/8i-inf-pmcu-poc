package com.inf.dahua.face.scan.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Response DTO for Dahua authentication.
 * Handles both standard and dynamic properties from the authentication response.
 */
@Data
public class AuthResponse {
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<>();

    private String realm;
    @JsonProperty("randomKey")
    private String randomKey;
    @JsonProperty("encryptType")
    private String encryptType;
    private String publicKey;
    private String token;
    private Integer duration;
    private String credential;
    private String userId;
    private String userGroupId;
    private String serviceAbilty;
    private VersionInfo versionInfo;
    private String emapUrl;
    private String sipNum;
    private String pocId;
    private String pocPassword;
    private String sipPassword;
    private Integer tokenRate;
    private String secretKey;
    private String secretVector;
    private String reused;
    private String userLevel;
    private String personId;
    private String houseHolder;
    private String forceResetPassword;
    private String loginDateUtc;

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        additionalProperties.put(name.toLowerCase(), value);
    }

    public Object getAdditionalProperty(String name) {
        return additionalProperties.get(name.toLowerCase());
    }

    // Nested class for version info
    public static class VersionInfo {
        private String lastVersion;
        private String updateUrl;
        private String patchVersion;
        private String patchUrl;

        // Getters and setters
        public String getLastVersion() { return lastVersion; }
        public void setLastVersion(String lastVersion) { this.lastVersion = lastVersion; }
        public String getUpdateUrl() { return updateUrl; }
        public void setUpdateUrl(String updateUrl) { this.updateUrl = updateUrl; }
        public String getPatchVersion() { return patchVersion; }
        public void setPatchVersion(String patchVersion) { this.patchVersion = patchVersion; }
        public String getPatchUrl() { return patchUrl; }
        public void setPatchUrl(String patchUrl) { this.patchUrl = patchUrl; }
    }
}
