package com.example.uploadfile;

public class FileUploadRequest {
    private String base64File;

    public FileUploadRequest(String base64File) {
        this.base64File = base64File;
    }

    public String getBase64File() {
        return base64File;
    }
}
