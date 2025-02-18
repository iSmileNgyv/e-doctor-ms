package com.example.storage.controller;

import com.example.storage.dto.FileResult;
import com.example.storage.service.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/v1/storage")
@RequiredArgsConstructor
public class StorageController {
    private final StorageService storageService;

    @PostMapping("/upload")
    public CompletableFuture<ResponseEntity<List<FileResult>>> uploadFiles(
            @RequestParam("path") String path,
            @RequestParam("files") List<MultipartFile> files) {

        return storageService.uploadFileAsync(path, files)
                .thenApply(ResponseEntity::ok);
    }
}
