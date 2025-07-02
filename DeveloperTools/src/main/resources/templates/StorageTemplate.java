package com.example.storage.service.storage.impl;

import com.example.storage.dto.FileResult;
import com.example.storage.service.storage.StorageService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

// @Service
public class {{CLASS_NAME}} extends Storage implements StorageService {

    @Override
    @Async
    public CompletableFuture<List<FileResult>> uploadFileAsync(String pathOrContainerName, List<MultipartFile> files) {
        // TODO: Implement uploadFileAsync for {{CLASS_NAME}}
        return CompletableFuture.completedFuture(List.of());
    }

    @Override
    @Async
    public CompletableFuture<FileResult> singleUploadFileAsync(String pathOrContainerName, MultipartFile file) {
        // TODO: Implement singleUploadFileAsync for {{CLASS_NAME}}
        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<Void> deleteAsync(String pathOrContainerName, String fileName) {
        // TODO: Implement deleteAsync with path and fileName for {{CLASS_NAME}}
        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<Void> deleteAsync(String fullPath) {
        // TODO: Implement deleteAsync with fullPath for {{CLASS_NAME}}
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public List<String> getFiles(String pathOrContainerName) {
        // TODO: Implement getFiles for {{CLASS_NAME}}
        return List.of();
    }

    @Override
    public boolean hasFile(String pathOrContainerName, String fileName) {
        // TODO: Implement hasFile for {{CLASS_NAME}}
        return false;
    }

    @Override
    public String getStorageName() {
        return "{{CLASS_NAME}}";
    }
}
