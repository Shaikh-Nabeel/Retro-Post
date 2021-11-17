package com.nabeel130.retropost;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivityReportLog";
    private ImageView image;
    private ProgressBar progressBar;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);

        Button chooseBtn = findViewById(R.id.chooseBtn);

        chooseBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            launcher.launch(intent);
        });

        image = findViewById(R.id.imageView);
        Button uploadImage = findViewById(R.id.uploadBtn);
        progressBar = findViewById(R.id.progressBar);
        uploadImage.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);

            RequestBody requestBody = RequestBody.create(file,MediaType.parse("image/*"));
            MultipartBody.Part parts = MultipartBody.Part.createFormData("image",file.getName(),requestBody);
            ImageApi retrofit = RetrofitSingleton.getInstance().create(ImageApi.class);

            Map<String,String> map = new HashMap<>();
            map.put("Client-ID","9a266fc287b9d3c");

            retrofit.uploadImg(map,parts).enqueue(new Callback<Response>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                    Log.d(TAG,"Success : Image upload");
                    Data data;
                    if (response.body() != null) {
                        data = response.body().getData();
                        Log.d(TAG, "image id: " + data.getId());
                        Log.d(TAG, "Account url: " + data.getAccountUrl()
                                + "\nname: " + data.getName()
                                + "\naccount id: "+ data.getAccountId()
                                + "\nLink : "+ data.getLink()
                        );

                        TextView urlTxt = findViewById(R.id.urlTextView);
                        String link = data.getLink();
                        urlTxt.setText("click :" +link);
                        urlTxt.setOnClickListener(v -> {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse(link));
                            startActivity(intent);
                        });
                    }else{
                        Log.d(TAG,"data is null "+response);
                        Toast.makeText(getApplicationContext(),"response null", Toast.LENGTH_SHORT)
                                .show();
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),"Not uploaded",Toast.LENGTH_SHORT)
                            .show();
                    t.printStackTrace();
                }
            });
        });
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null){
                    Uri uri = result.getData().getData();

                    try{
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        image.setImageBitmap(bitmap);

//                        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.Q) {
                            String[] projection = new String[]{MediaStore.Images.Media.DATA};
                            Cursor cursor =getApplicationContext().getContentResolver().query(uri, projection, null, null, null);

                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                                String filePath = cursor.getString(columnIndex);

                                Log.d(TAG, filePath);
                                file = new File(filePath);
                                Log.d(TAG, file.getName() + " " + file.getAbsolutePath());
                            } else {
                                Toast.makeText(getApplicationContext(),"Something went wrong.",Toast.LENGTH_SHORT)
                                        .show();
                                Log.d(TAG, "Cursor is null");
                            }
//                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(
                            getApplicationContext(),
                            "Unable to pick image",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
    );

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}