package com.jhbb.android.filmesfamosos.service.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.jhbb.android.filmesfamosos.BuildConfig;
import com.jhbb.android.filmesfamosos.service.database.AppDatabase;
import com.jhbb.android.filmesfamosos.service.model.MovieModel;
import com.jhbb.android.filmesfamosos.service.model.MoviesResultModel;
import com.jhbb.android.filmesfamosos.service.model.ReviewModel;
import com.jhbb.android.filmesfamosos.service.model.ReviewsResultModel;
import com.jhbb.android.filmesfamosos.service.model.VideoModel;
import com.jhbb.android.filmesfamosos.service.model.VideosResultModel;

import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProjectRepository {

    private static Retrofit retrofit;
    private static final String MOVIES_URL = "https://api.themoviedb.org/3/";
    private static final String TAG = ProjectRepository.class.getSimpleName();

    private static ProjectRepository projectRepository;

    private MovieService movieService;
    private ReviewsService reviewsService;
    private VideosService videoService;
    private AppDatabase database;

    private ProjectRepository(Application application) {
        OkHttpClient okClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okClient)
                .baseUrl(MOVIES_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        movieService = retrofit.create(MovieService.class);
        reviewsService = retrofit.create(ReviewsService.class);
        videoService = retrofit.create(VideosService.class);

        database = AppDatabase.getInstance(application.getApplicationContext());
    }

    public synchronized static ProjectRepository getInstance(Application application) {
        if (projectRepository == null) {
            if (projectRepository == null) {
                projectRepository = new ProjectRepository(application);
            }
        }
        return projectRepository;
    }

    public LiveData<List<MovieModel>> getPopularMovies() {
        final MutableLiveData<List<MovieModel>> data = new MutableLiveData<>();

        Call<MoviesResultModel> call = movieService.getPopularMovies(BuildConfig.ApiKey);
        call.enqueue(new Callback<MoviesResultModel>() {
            @Override
            public void onResponse(Call<MoviesResultModel> call, Response<MoviesResultModel> response) {
                Log.v(TAG, "onResponse: " + response.body());
                MovieModel[] moviesArray = response.body().getMovieModels();

                data.setValue(Arrays.asList(moviesArray));
            }

            @Override
            public void onFailure(Call<MoviesResultModel> call, Throwable t) {
                Log.v(TAG, "onFailure");

                data.setValue(null);
            }
        });

        return data;
    }

    public LiveData<List<MovieModel>> getTopRatedMovies() {
        final MutableLiveData<List<MovieModel>> data = new MutableLiveData<>();

        Call<MoviesResultModel> call = movieService.getTopRatedMovies(BuildConfig.ApiKey);
        call.enqueue(new Callback<MoviesResultModel>() {
            @Override
            public void onResponse(Call<MoviesResultModel> call, Response<MoviesResultModel> response) {
                Log.v(TAG, "onResponse: " + response.body());
                MovieModel[] moviesArray = response.body().getMovieModels();

                data.setValue(Arrays.asList(moviesArray));
            }

            @Override
            public void onFailure(Call<MoviesResultModel> call, Throwable t) {
                Log.v(TAG, "onFailure");

                data.setValue(null);
            }
        });

        return data;
    }

    public LiveData<List<MovieModel>> getFavoriteMovies() {
        return database.movieDao().loadAllFavoriteMovies();
    }

    public boolean checkMovieIsFavorite(String id) {
        return database.movieDao().checkMovieIsFavorite(id);
    }

    public void addToFavorites(MovieModel movieModel) {
        database.movieDao().insertFavoriteMovie(movieModel);
    }

    public void removeFromFavorites(MovieModel movieModel) {
        database.movieDao().removeFavoriteMovie(movieModel);
    }

    public LiveData<List<VideoModel>> getVideosByMovieId(String movieId) {
        final MutableLiveData<List<VideoModel>> data = new MutableLiveData<>();

        Call<VideosResultModel> call = videoService.getMovieVideosById(movieId, BuildConfig.ApiKey);
        call.enqueue(new retrofit2.Callback<VideosResultModel>() {
            @Override
            public void onResponse(Call<VideosResultModel> call, Response<VideosResultModel> response) {
                Log.v(TAG, "onResponse: " + response.body());

                VideoModel[] videoModels = response.body().getVideoModels();
                data.setValue(Arrays.asList(videoModels));
            }

            @Override
            public void onFailure(Call<VideosResultModel> call, Throwable t) {
                Log.v(TAG, "onFailure");

                data.setValue(null);
            }
        });

        return data;
    }

    public LiveData<List<ReviewModel>> getReviewsByMovieId(String movieId) {
        final MutableLiveData<List<ReviewModel>> data = new MutableLiveData<>();

        Call<ReviewsResultModel> call = reviewsService.getMovieReviewsById(movieId, BuildConfig.ApiKey);
        call.enqueue(new retrofit2.Callback<ReviewsResultModel>() {
            @Override
            public void onResponse(Call<ReviewsResultModel> call, Response<ReviewsResultModel> response) {
                Log.v(TAG, "onResponse: " + response.body());

                ReviewModel[] reviewModels = response.body().getReviewModels();
                data.setValue(Arrays.asList(reviewModels));
            }

            @Override
            public void onFailure(Call<ReviewsResultModel> call, Throwable t) {
                Log.v(TAG, "onFailure");

                data.setValue(null);
            }
        });

        return data;
    }
}
