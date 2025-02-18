package com.example.storage.service.storage.impl;

import com.example.storage.dto.FileResult;
import com.example.storage.service.storage.Storage;
import com.example.storage.service.storage.StorageService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

import java.util.List;
@Service
public class StorageServiceImpl implements StorageService {
    private final Storage storage;

    public StorageServiceImpl(@Qualifier("localStorageImpl") Storage storage) {
        this.storage = storage;
    }

    @Override
    public CompletableFuture<List<FileResult>> uploadFileAsync(String pathOrContainerName, List<MultipartFile> files) {
        return storage.uploadFileAsync(pathOrContainerName, files);
    }

    @Override
    public CompletableFuture<FileResult> singleUploadFileAsync(String pathOrContainerName, MultipartFile file) {
        return storage.singleUploadFileAsync(pathOrContainerName, file);
    }

    @Override
    public CompletableFuture<Void> deleteAsync(String pathOrContainerName, String fileName) {
        return storage.deleteAsync(pathOrContainerName, fileName);
    }

    @Override
    public CompletableFuture<Void> deleteAsync(String fullPath) {
        return storage.deleteAsync(fullPath);
    }

    @Override
    public List<String> getFiles(String pathOrContainerName) {
        return storage.getFiles(pathOrContainerName);
    }

    @Override
    public boolean hasFile(String pathOrContainerName, String fileName) {
        return storage.hasFile(pathOrContainerName, fileName);
    }

    @Override
    public String getStorageName() {
        return storage.getClass().getSimpleName();
    }
}
