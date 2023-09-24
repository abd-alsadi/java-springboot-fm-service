package com.core.fmservice.iservices;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import com.core.fmservice.responses.UploadFileResponse;

public interface IFileStorageService {
    public UploadFileResponse upload(MultipartFile file);
    public Resource download(String id);
}
