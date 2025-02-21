package com.example.storage.service.storage.impl;

import com.example.storage.dto.FileResult;
import com.example.storage.service.storage.GoogleDriveStorage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;
@Service
public class GoogleDriveStorageImpl extends Storage implements GoogleDriveStorage {
    @Override
    public CompletableFuture<List<FileResult>> uploadFileAsync(String pathOrContainerName, List<MultipartFile> files) {
        return null;
    }

    @Override
    public CompletableFuture<FileResult> singleUploadFileAsync(String pathOrContainerName, MultipartFile file) {
        return null;
    }

    @Override
    public CompletableFuture<Void> deleteAsync(String pathOrContainerName, String fileName) {
        return null;
    }

    @Override
    public CompletableFuture<Void> deleteAsync(String fullPath) {
        return null;
    }

    @Override
    public List<String> getFiles(String pathOrContainerName) {
        return List.of();
    }

    @Override
    public boolean hasFile(String pathOrContainerName, String fileName) {
        return false;
    }
}
