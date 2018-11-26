package com.jhbb.android.filmesfamosos.service.repository;

import com.jhbb.android.filmesfamosos.service.model.VideosResultModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface VideosService {

    @GET("movie/{id}/videos") Call<VideosResultModel> getMovieVideosById(
            @Path("id") String movieId,
            @Query("api_key") String apiKey);
}