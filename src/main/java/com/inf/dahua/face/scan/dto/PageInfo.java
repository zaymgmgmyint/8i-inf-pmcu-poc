package com.inf.dahua.face.scan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo {
    private int pageNo;
    private int pageSize;
    
    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
    
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
