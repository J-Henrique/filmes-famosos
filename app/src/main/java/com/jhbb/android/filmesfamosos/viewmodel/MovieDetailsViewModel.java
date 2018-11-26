package com.jhbb.android.filmesfamosos.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.jhbb.android.filmesfamosos.AppExecutors;
import com.jhbb.android.filmesfamosos.service.model.MovieModel;
import com.jhbb.android.filmesfamosos.service.model.ReviewModel;
import com.jhbb.android.filmesfamosos.service.model.VideoModel;
import com.jhbb.android.filmesfamosos.service.repository.ProjectRepository;

import java.util.List;

public class MovieDetailsViewModel extends AndroidViewModel {

    private String movieId;

    private LiveData<List<VideoModel>> videosObservable;
    private LiveData<List<ReviewModel>> reviewsObservable;

    private boolean sIsFavorite;

    public MovieDetailsViewModel(@NonNull Application application,
                                 String movieId) {
        super(application);
        this.movieId = movieId;
    }

    public boolean checkMovieIsFavorite(final String id) {
        return ProjectRepository.getInstance(getApplication()).checkMovieIsFavorite(id);
    }

    public void addToFavorites(final MovieModel movieModel) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                ProjectRepository.getInstance(getApplication()).addToFavorites(movieModel);
            }
        });
    }

    public void removeFromFavorites(final MovieModel movieModel) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                ProjectRepository.getInstance(getApplication()).removeFromFavorites(movieModel);
            }
        });
    }

    public LiveData<List<VideoModel>> getVideosByMovieId() {
        if (videosObservable == null) {
            videosObservable =
                    ProjectRepository.getInstance(getApplication()).getVideosByMovieId(this.movieId);
        }

        return videosObservable;
    }

    public LiveData<List<ReviewModel>> getReviewsByMovieId() {
        if (reviewsObservable == null) {
            reviewsObservable =
                    ProjectRepository.getInstance(getApplication()).getReviewsByMovieId(this.movieId);
        }

        return reviewsObservable;
    }
}
