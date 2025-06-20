package com._i.dahua.face.scan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Version information for the Dahua API
 */
@Data
public class VersionInfo {
    @JsonProperty("version")
    private String version;
    
    @JsonProperty("buildDate")
    private String buildDate;
    
    @JsonProperty("apiVersion")
    private String apiVersion;
}
