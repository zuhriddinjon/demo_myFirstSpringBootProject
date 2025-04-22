package com.example.demo.service;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.FileEntity;
import com.example.demo.model.FileEntityStatus;
import com.example.demo.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

@Service
public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    private final String uploadDir = System.getProperty("user.dir") + "/uploads/";

    public ApiResponse<FileEntity> saveFile(MultipartFile file) {
        try {
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();
            String filePath = UUID.randomUUID() + file.getOriginalFilename();
            File dest = new File(uploadDir + filePath);
            file.transferTo(dest);

            FileEntity fileEntity = new FileEntity();
            fileEntity.setName(file.getOriginalFilename());
            fileEntity.setContentType(file.getContentType());
            fileEntity.setExtension(getExt(file.getOriginalFilename()));
            fileEntity.setSize(file.getSize());
            fileEntity.setUploadPath(dest.getPath());
            fileEntity.setStatus(FileEntityStatus.INACTIVE);
            final FileEntity savedFile = fileRepository.save(fileEntity);

            return ApiResponse.success(savedFile);
        } catch (Exception e) {
            logger.error("Save file error: {}", e.getMessage(), e);
            return ApiResponse.error(500, "File save error");
        }
    }

    public ResponseEntity<?> getFileById(Long fileId) {
        try {
            final FileEntity fileEntity = fileRepository.findById(fileId).orElseThrow();
            final String filePath = fileEntity.getUploadPath();
            final File file = new File(filePath);
            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
            }

            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename(file.getName()).build());
            headers.setContentType(MediaType.valueOf(fileEntity.getContentType()));

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);

        } catch (Exception e) {
            logger.error("Get file error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found: " + e.getMessage());
        }
    }

    private String getExt(String fileName) {
        if (fileName == null) return null;
        if (fileName.isBlank()) return null;
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
