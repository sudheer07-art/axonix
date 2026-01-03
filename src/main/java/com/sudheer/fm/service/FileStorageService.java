package com.sudheer.fm.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;

@Service
public class FileStorageService {

    private final Path root = Paths.get("uploads");

    public FileStorageService() throws Exception {
        Files.createDirectories(root);
    }

    public String saveFile(MultipartFile file) throws Exception {
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = root.resolve(filename);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return path.toString();
    }

    public Resource loadFile(String path) throws Exception {
        Path file = Paths.get(path);
        return new UrlResource(file.toUri());
    }

    public void deleteFile(String path) throws Exception {
        Files.deleteIfExists(Paths.get(path));
    }
}
