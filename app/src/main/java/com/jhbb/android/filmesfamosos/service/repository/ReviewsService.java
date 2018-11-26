package com.jhbb.android.filmesfamosos.service.repository;

import com.jhbb.android.filmesfamosos.service.model.ReviewsResultModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface ReviewsService {

    @GET("movie/{id}/reviews") Call<ReviewsResultModel> getMovieReviewsById(
            @Path("id") String movieId,
            @Query("api_key") String apiKey);
}
