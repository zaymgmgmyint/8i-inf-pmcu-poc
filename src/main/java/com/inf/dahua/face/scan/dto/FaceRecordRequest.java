package com.inf.dahua.face.scan.dto;

public class FaceRecordRequest {
    private PageInfo pageInfo;
    private String startTime;
    private String endTime;
    private String locale = "en";
    
    public PageInfo getPageInfo() {
        return pageInfo;
    }
    
    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }
    
    public String getStartTime() {
        return startTime;
    }
    
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    
    public String getEndTime() {
        return endTime;
    }
    
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    
    public String getLocale() {
        return locale;
    }
    
    public void setLocale(String locale) {
        this.locale = locale;
    }
}
