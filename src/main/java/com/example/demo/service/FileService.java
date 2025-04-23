package com.example.demo.service;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.FileEntity;
import com.example.demo.model.FileEntityStatus;
import com.example.demo.repository.FileRepository;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    private final FileRepository fileRepository;
    private final FileStorageService fileStorageService;

    public FileService(FileRepository fileRepository, FileStorageService fileStorageService) {
        this.fileRepository = fileRepository;
        this.fileStorageService = fileStorageService;
    }

    public ApiResponse<FileEntity> saveFile(MultipartFile file) {
        try {
            String filePath = fileStorageService.uploadFile(file);

            FileEntity fileEntity = new FileEntity();
            fileEntity.setName(file.getOriginalFilename());
            fileEntity.setContentType(file.getContentType());
            fileEntity.setExtension(getExt(file.getOriginalFilename()));
            fileEntity.setSize(file.getSize());
            fileEntity.setUploadPath(filePath);
            fileEntity.setStatus(FileEntityStatus.INACTIVE);
            final FileEntity savedFile = fileRepository.save(fileEntity);

            return ApiResponse.success(savedFile);
        } catch (Exception e) {
            throw new RuntimeException("Save file error: " + e.getMessage());
        }
    }

    public FileEntity getFileById(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found: ID = " + fileId));
    }

    public Resource downloadFile(Long fileId) {
        FileEntity fileEntity = getFileById(fileId);
        return fileStorageService.loadFile(fileEntity.getUploadPath());
    }

    private String getExt(String fileName) {
        if (fileName == null || fileName.isBlank()) return null;
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
