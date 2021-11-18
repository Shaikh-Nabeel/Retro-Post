package com.nabeel130.retropost;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSingleton {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.imgur.com/";

    public static Retrofit getInstance(){
        if(retrofit == null){

//            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//            interceptor.level(HttpLoggingInterceptor.Level.BODY);
//            OkHttpClient client = new OkHttpClient.Builder(). .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
