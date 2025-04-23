package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final String uploadDir;

    public FileStorageService(@Value("${file.upload-dir:uploads/}") String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public String uploadFile(MultipartFile file) {
        try {
            String cleanFileName;
            if (file.getOriginalFilename() != null) {
                cleanFileName = file.getOriginalFilename().replaceAll("[^a-zA-Z0-9.-]", "_");
            } else {
                cleanFileName = "";
            }
            String fileName = UUID.randomUUID() + "_" + cleanFileName;
            Path filePath = Paths.get(uploadDir, fileName);

            Files.createDirectories(filePath.getParent());
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }
    }

    public Resource loadFile(String filePath) {
        try {
            if (filePath == null || filePath.isBlank())
                throw new ResourceNotFoundException("File path is null or blank");
            Path path = Paths.get(filePath);
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new ResourceNotFoundException("File does not exist or not read: " + filePath);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("File path error: " + filePath, e);
        }
    }
}