package com.example.storage.service.storage.impl;

import com.example.storage.service.storage.NameOperation;

import java.util.concurrent.CompletableFuture;

public class Storage {
    protected interface HasFile {
        boolean check(String pathOrContainerName, String fileName);
    }

    protected CompletableFuture<String> fileRenameAsync(String path, String fileName, HasFile hasFile) {
        return CompletableFuture.supplyAsync(() -> {
            String extension = fileName.substring(fileName.lastIndexOf("."));
            String oldName = fileName.substring(0, fileName.lastIndexOf("."));
            String newFileName = NameOperation.characterRegulatory(oldName) + extension;

            int counter = 1;
            while (hasFile.check(path, newFileName)) {
                newFileName = NameOperation.characterRegulatory(oldName) + "-" + counter + extension;
                counter++;
            }
            return newFileName;
        });
    }
}