package com.inf.dahua.face.scan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for pagination information.
 * Used across various requests to handle paginated results.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo {
    private int pageNo;
    private int pageSize;
}
