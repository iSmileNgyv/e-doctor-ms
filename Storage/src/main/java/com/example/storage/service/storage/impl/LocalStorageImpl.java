package com.example.storage.service.storage.impl;

import com.example.storage.dto.FileResult;
import com.example.storage.service.storage.LocalStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocalStorageImpl extends Storage implements LocalStorage {

    @Override
    @Async
    public CompletableFuture<List<FileResult>> uploadFileAsync(String path, List<MultipartFile> files) {
        List<FileResult> datas = new ArrayList<>();
        Path uploadPath = Path.of("static", path);

        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory: " + uploadPath, e);
        }

        for (MultipartFile file : files) {
            try {
                String fileNewName = fileRenameAsync(path, file.getOriginalFilename(), this::hasFile).join();
                Path filePath = uploadPath.resolve(fileNewName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                datas.add(new FileResult(fileNewName, path + "/" + fileNewName));
            } catch (IOException e) {
                System.err.println("File upload failed: " + file.getOriginalFilename() + " | Error: " + e.getMessage());
            }
        }
        return CompletableFuture.completedFuture(datas);
    }

    @Override
    @Async
    public CompletableFuture<FileResult> singleUploadFileAsync(String path, MultipartFile file) {
        Path uploadPath = Path.of("static", path);

        try {
            Files.createDirectories(uploadPath);
            String fileNewName = fileRenameAsync(path, file.getOriginalFilename(), this::hasFile).join();
            Path filePath = uploadPath.resolve(fileNewName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return CompletableFuture.completedFuture(new FileResult(fileNewName, path + "/" + fileNewName));
        } catch (IOException e) {
            throw new RuntimeException("File upload failed: " + file.getOriginalFilename(), e);
        }
    }

    @Override
    @Async
    public CompletableFuture<Void> deleteAsync(String path, String fileName) {
        Path filePath = Path.of("static", path, fileName);
        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not delete file: " + filePath, e);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<Void> deleteAsync(String fullPath) {
        Path filePath = Path.of("static", fullPath);
        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not delete file: " + filePath, e);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public List<String> getFiles(String path) {
        try {
            return Files.list(Path.of("static", path))
                    .map(p -> p.getFileName().toString())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Could not list files in directory: " + path, e);
        }
    }

    @Override
    public boolean hasFile(String path, String fileName) {
        return Files.exists(Path.of("static", path, fileName));
    }
}
