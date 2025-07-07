package com.inf.dahua.face.scan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Version information for the Dahua API.
 * Contains version details, build information, and API compatibility data.
 */
@Data
public class VersionInfo {
    @JsonProperty("version")
    private String version;
    
    @JsonProperty("buildDate")
    private String buildDate;
    
    @JsonProperty("apiVersion")
    private String apiVersion;

    @JsonProperty("lastVersion")
    private String lastVersion;

    @JsonProperty("updateUrl")
    private String updateUrl;
}
