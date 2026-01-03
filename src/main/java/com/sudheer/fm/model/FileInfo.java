package com.sudheer.fm.model;

public class FileInfo {
    private Long id;
    private String fileName;

    public FileInfo(Long id, String fileName) {
        this.id = id;
        this.fileName = fileName;
    }

    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }
}

