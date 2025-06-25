package com.inf.dahua.face.scan.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class AuthResponse {
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();
    
    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        additionalProperties.put(name.toLowerCase(), value);
    }
    
    public Object getAdditionalProperty(String name) {
        return additionalProperties.get(name.toLowerCase());
    }
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
    private String enablePasswordValidityPeriod;
    private String passwordRemainValidityPeriod;
    private String loginIpChanged;
    private String lastLoginIp;

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

    // Getters and setters
    public String getRealm() { return realm; }
    public void setRealm(String realm) { this.realm = realm; }
    public String getRandomKey() { return randomKey; }
    public void setRandomKey(String randomKey) { this.randomKey = randomKey; }
    public String getEncryptType() { return encryptType; }
    public void setEncryptType(String encryptType) { this.encryptType = encryptType; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    public String getCredential() { return credential; }
    public void setCredential(String credential) { this.credential = credential; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserGroupId() { return userGroupId; }
    public void setUserGroupId(String userGroupId) { this.userGroupId = userGroupId; }
    public String getServiceAbilty() { return serviceAbilty; }
    public void setServiceAbilty(String serviceAbilty) { this.serviceAbilty = serviceAbilty; }
    public VersionInfo getVersionInfo() { return versionInfo; }
    public void setVersionInfo(VersionInfo versionInfo) { this.versionInfo = versionInfo; }
    public String getEmapUrl() { return emapUrl; }
    public void setEmapUrl(String emapUrl) { this.emapUrl = emapUrl; }
    public String getSipNum() { return sipNum; }
    public void setSipNum(String sipNum) { this.sipNum = sipNum; }
    public String getPocId() { return pocId; }
    public void setPocId(String pocId) { this.pocId = pocId; }
    public String getPocPassword() { return pocPassword; }
    public void setPocPassword(String pocPassword) { this.pocPassword = pocPassword; }
    public String getSipPassword() { return sipPassword; }
    public void setSipPassword(String sipPassword) { this.sipPassword = sipPassword; }
    public Integer getTokenRate() { return tokenRate; }
    public void setTokenRate(Integer tokenRate) { this.tokenRate = tokenRate; }
    public String getSecretKey() { return secretKey; }
    public void setSecretKey(String secretKey) { this.secretKey = secretKey; }
    public String getSecretVector() { return secretVector; }
    public void setSecretVector(String secretVector) { this.secretVector = secretVector; }
    public String getReused() { return reused; }
    public void setReused(String reused) { this.reused = reused; }
    public String getUserLevel() { return userLevel; }
    public void setUserLevel(String userLevel) { this.userLevel = userLevel; }
    public String getPersonId() { return personId; }
    public void setPersonId(String personId) { this.personId = personId; }
    public String getHouseHolder() { return houseHolder; }
    public void setHouseHolder(String houseHolder) { this.houseHolder = houseHolder; }
    public String getForceResetPassword() { return forceResetPassword; }
    public void setForceResetPassword(String forceResetPassword) { this.forceResetPassword = forceResetPassword; }
    public String getLoginDateUtc() { return loginDateUtc; }
    public void setLoginDateUtc(String loginDateUtc) { this.loginDateUtc = loginDateUtc; }
    public String getEnablePasswordValidityPeriod() { return enablePasswordValidityPeriod; }
    public void setEnablePasswordValidityPeriod(String enablePasswordValidityPeriod) { this.enablePasswordValidityPeriod = enablePasswordValidityPeriod; }
    public String getPasswordRemainValidityPeriod() { return passwordRemainValidityPeriod; }
    public void setPasswordRemainValidityPeriod(String passwordRemainValidityPeriod) { this.passwordRemainValidityPeriod = passwordRemainValidityPeriod; }
    public String getLoginIpChanged() { return loginIpChanged; }
    public void setLoginIpChanged(String loginIpChanged) { this.loginIpChanged = loginIpChanged; }
    public String getLastLoginIp() { return lastLoginIp; }
    public void setLastLoginIp(String lastLoginIp) { this.lastLoginIp = lastLoginIp; }
    
    public String getPublicKey() {
        if (publicKey == null) {
            // Try to get from additional properties with any case
            Object value = additionalProperties.get("publickey");
            if (value == null) {
                value = additionalProperties.get("publicKey");
            }
            if (value != null) {
                publicKey = value.toString();
            }
        }
        return publicKey;
    }
    
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
        additionalProperties.put("publickey", publicKey);
        additionalProperties.put("publicKey", publicKey);
    }
}
