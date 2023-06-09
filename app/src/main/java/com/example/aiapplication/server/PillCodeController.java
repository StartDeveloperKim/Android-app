package com.example.aiapplication.server;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface PillCodeController {

    @Multipart
    @POST("detect")
    Call<ResponseBody> detectImage(@Part MultipartBody.Part image);
}
