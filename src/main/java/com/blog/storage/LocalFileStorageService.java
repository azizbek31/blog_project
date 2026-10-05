package com.blog.storage;

import com.blog.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class LocalFileStorageService implements FileStorageService {

    @Value("${app.storage.local-path}")
    private String basePath;

    @Override
    public String upload(MultipartFile file, String folder) {
        try {
            String extension = getExtension(file.getOriginalFilename());
            String objectKey = folder + "/" + UUID.randomUUID() + extension;

            Path targetPath = Path.of(basePath, objectKey);
            Files.createDirectories(targetPath.getParent());
            file.transferTo(targetPath);
            return objectKey;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file.", e);
        }
    }

    @Override
    public Resource load(String objectKey) {
        Path filePath = Path.of(basePath, objectKey);
        if (!Files.exists(filePath)) {
            throw new ResourceNotFoundException("Image not found");
        }
        try {
            return new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to load image", e);
        }
    }

    @Override
    public void delete(String objectKey) {
        try {
            Files.deleteIfExists(Path.of(basePath, objectKey));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file.", e);
        }
    }

    private String getExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
