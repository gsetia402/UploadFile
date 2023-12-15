package com.example.uploadfile;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FileUploadService {
    @POST("upload")
    Call<Void> uploadFile(@Body FileUploadRequest request);
}
