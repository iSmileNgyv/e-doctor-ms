package com.example.storage.service.storage;

import com.example.storage.dto.FileResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;
import java.util.List;
public interface Storage {
    CompletableFuture<List<FileResult>> uploadFileAsync(String pathOrContainerName, List<MultipartFile> files);
    CompletableFuture<FileResult> singleUploadFileAsync(String pathOrContainerName, MultipartFile file);
    CompletableFuture<Void> deleteAsync(String pathOrContainerName, String fileName);
    CompletableFuture<Void> deleteAsync(String fullPath);
    List<String> getFiles(String pathOrContainerName);
    boolean hasFile(String pathOrContainerName, String fileName);
}
