package com.example.dk_task_01.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ImageApi {
    @GET("search/photos")
    Call<ImageResponse> searchImages(
            @Query("query") String query,
            @Query("client_id") String apiKey,
            @Query("page") int page,
            @Query("per_page") int perPage
    );
}
