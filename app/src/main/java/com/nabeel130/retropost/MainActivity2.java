package com.nabeel130.retropost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
/*







        This File is not in use

        ------------------------------ IGNORE --------------------------------








 */
public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        String TAG = "MainActivityReport";

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String id = bundle.getString("id");
        if(id == null){
            Log.d(TAG, "String id is null");
            return;
        }

        Map<String,String> map = new HashMap<>();
        map.put("Client-ID","9a266fc287b9d3c");

        ImageView imageView = findViewById(R.id.imageView);

        RetrofitSingleton.getInstance().create(ImageApi.class).getImageById(map,id).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(@NonNull Call<Response> call, @NonNull retrofit2.Response<Response> response) {
                 if(response.body() != null){
                     Data data = response.body().getData();
                     Log.d(TAG,data.getId() +" -- "+data.getLink()+" -- "+data.getName());
                     if(data.getLink() != null){
                         Glide.with(imageView.getContext()).load(data.getLink()).into(imageView);
                     }
                 }else{
                     Toast.makeText(getApplicationContext(),
                             "Response null",
                             Toast.LENGTH_SHORT).show();
                 }
            }

            @Override
            public void onFailure(@NonNull Call<Response> call, @NonNull Throwable t) {
                t.printStackTrace();
                Toast.makeText(
                        getApplicationContext(),
                        "Failed to load image",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}