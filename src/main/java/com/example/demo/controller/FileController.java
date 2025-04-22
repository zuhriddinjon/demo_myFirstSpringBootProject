package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.FileEntity;
import com.example.demo.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;


    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<FileEntity>> saveFile(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(fileService.saveFile(file));
    }

}
