package com.jhbb.android.filmesfamosos.service.repository;

import com.jhbb.android.filmesfamosos.service.model.MoviesResultModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface MovieService {

    @GET("movie/popular")
    Call<MoviesResultModel> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MoviesResultModel> getTopRatedMovies(@Query("api_key") String apiKey);
}
