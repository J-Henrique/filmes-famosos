package com.jhbb.android.filmesfamosos.utilities;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.jhbb.android.filmesfamosos.models.MoviesResultModel;
import com.jhbb.android.filmesfamosos.models.ReviewsResultModel;
import com.jhbb.android.filmesfamosos.models.VideosResultModel;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class RetrofitClient {

    private static Retrofit retrofit;
    private static final String MOVIES_URL = "https://api.themoviedb.org/3/";

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            OkHttpClient okClient = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new StethoInterceptor())
                    .build();

            retrofit = new Retrofit.Builder()
                    .client(okClient)
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

    public interface GetVideosService {

        @GET("movie/{id}/videos")
        Call<VideosResultModel> getMovieVideosById(
                @Path("id") String movieId,
                @Query("api_key") String apiKey);
    }

    public interface GetReviewsService {

        @GET("movie/{id}/reviews")
        Call<ReviewsResultModel> getMovieReviewsById(
                @Path("id") String movieId,
                @Query("api_key") String apiKey);
    }


}
