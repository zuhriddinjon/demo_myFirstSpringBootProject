package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "content_type")
    private String contentType;
    @Column(name = "extension")
    private String extension;
    @Column(name = "size")
    private Long size;
    @Column(name = "hash_id")
    private String hashId;
    @Column(name = "upload_path")
    private String uploadPath;
    @Column(name = "status")
    private FileEntityStatus status;
}