package com.example.dk_task_01;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.dk_task_01.model.Image;
import com.example.dk_task_01.model.ImageApi;
import com.example.dk_task_01.model.ImageResponse;

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private String currentImageUrl;
    private String imagePageUrl = "https://unsplash.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText editText =findViewById(R.id.searchEditText);
        Button search =findViewById(R.id.search);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.unsplash.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ImageApi imageApi = retrofit.create(ImageApi.class);
        Button like = findViewById(R.id.like);
        Button dislike = findViewById(R.id.dislike);
        Button browser = findViewById(R.id.browser);
        Button download = findViewById(R.id.download);
        Button authorBtn = findViewById(R.id.authorBtn);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = editText.getText().toString();
                if (!query.isEmpty()) {
                    fetchImages(imageApi, query);
                }
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = editText.getText().toString();
                if (!query.isEmpty()) {
                    fetchImages(imageApi, query);
                }
            }
        });

        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = editText.getText().toString();
                if (!query.isEmpty()) {
                    fetchImages(imageApi, query);
                }
            }
        });

        browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagePageUrl != null) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(imagePageUrl));
                    startActivity(browserIntent);
                }
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentImageUrl != null) {
                    downloadImage(currentImageUrl);
                } else {
                    showAlertDialog("Error", "No image to download");
                }
            }
        });

        authorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog("РАЗРАБОТАЛ", "Дима Кашпир АС-64");
            }
        });
    }

    private void fetchImages(ImageApi imageApi, String searchTerm) {

        imageView = findViewById(R.id.image);

        final String apiKey = "VP_nOrFNQHRLyGBxpbMOci_H-msBK_16dcGj5c-Z3RU";
        final int pageNumber = 1;
        final int imagesPerPage = 30;

        Call<ImageResponse> imageRequest = imageApi.searchImages(searchTerm, apiKey, pageNumber, imagesPerPage);
        imageRequest.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                if (response.isSuccessful()) {
                    ImageResponse imageResponse = response.body();
                    if (imageResponse != null && imageResponse.getResults() != null && !imageResponse.getResults().isEmpty()) {
                        List<Image> imagesList = imageResponse.getResults();

                        int randomImageIndex = new Random().nextInt(imagesList.size());
                        Image randomImage = imagesList.get(randomImageIndex);

                        String imageUrl = randomImage.getUrls() != null ? randomImage.getUrls().getSmall() : null;
                        if (imageUrl != null) {
                            currentImageUrl = imageUrl;
                            imagePageUrl = "https://unsplash.com/photos/" + randomImage.getId();
                            Glide.with(MainActivity.this).load(currentImageUrl).into(imageView);
                        } else {
                            showAlertDialog("Error", "Image URL is null");
                        }
                    } else {
                        showAlertDialog("Error", "No images found");
                    }
                } else {
                    showAlertDialog("Error", "Failed to retrieve images");
                }
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAlertDialog(String title, String message) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    private void downloadImage(String imageUrl) {
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(imageUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        downloadManager.enqueue(request);
    }
}