package com.core.fmservice.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadFileResponse {
    private String id;
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
}
