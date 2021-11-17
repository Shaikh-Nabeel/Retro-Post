package com.nabeel130.retropost;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ImageApi {


    @Multipart
    @POST("3/upload")
    Call<Response> uploadImg(
            @HeaderMap Map<String,String> map,
            @Part MultipartBody.Part part
    );

}
