package com.jhbb.android.filmesfamosos.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import android.support.annotation.NonNull;

import com.jhbb.android.filmesfamosos.service.model.MovieModel;
import com.jhbb.android.filmesfamosos.service.repository.ProjectRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<MovieModel>> popularMoviesObservable;
    private LiveData<List<MovieModel>> topRatedMoviesObservable;
    private LiveData<List<MovieModel>> favoritesMoviesObservable;

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<MovieModel>> getPopularMoviesObservable() {
        if (popularMoviesObservable == null) {
            popularMoviesObservable = ProjectRepository.getInstance(getApplication()).getPopularMovies();
        }

        return popularMoviesObservable;
    }

    public LiveData<List<MovieModel>> getTopRatedMoviesObservable() {
        if (topRatedMoviesObservable == null) {
            topRatedMoviesObservable = ProjectRepository.getInstance(getApplication()).getTopRatedMovies();
        }

        return topRatedMoviesObservable;
    }

    public LiveData<List<MovieModel>> getFavoritesMoviesObservable() {
        if (favoritesMoviesObservable == null) {
            favoritesMoviesObservable = ProjectRepository.getInstance(getApplication()).getFavoriteMovies();
        }

        return favoritesMoviesObservable;
    }
}
