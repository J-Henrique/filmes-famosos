package com.jhbb.android.filmesfamosos.utilities;

import com.jhbb.android.filmesfamosos.MoviesResultModel;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RetrofitClient {

    private static Retrofit retrofit;
    private static final String MOVIES_URL = "https://api.themoviedb.org/3/";

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(MOVIES_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public interface GetMoviesService {

        @GET("movie/popular")
        Call<MoviesResultModel> getPopularMovies(@Query("api_key") String apiKey);

        @GET("movie/top_rated")
        Call<MoviesResultModel> getTopRatedMovies(@Query("api_key") String apiKey);
    }
}
