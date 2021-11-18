package com.nabeel130.retropost;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ImageApi {


    @Multipart
    @POST("3/upload")
    Call<Response> uploadImg(
            @HeaderMap Map<String,String> map,
            @Part MultipartBody.Part part
    );

    @DELETE("3/image/{deletehash}")
    Call<Void> deleteImage(@HeaderMap Map<String,String> map,@Path("deletehash") String deletehash);

    @GET("3/image/{id}")
    Call<Response> getImageById(@HeaderMap Map<String,String> map, @Path("id") String id);

}
