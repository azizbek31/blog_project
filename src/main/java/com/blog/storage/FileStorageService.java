package com.blog.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String upload(MultipartFile file, String folder);

    void delete(String objectKey);

    Resource load(String objectKey);
}
