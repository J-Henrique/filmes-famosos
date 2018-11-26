package com.jhbb.android.filmesfamosos.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class MovieDetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final String movieId;
    private final Application application;

    public MovieDetailsViewModelFactory(Application application, String movieId) {
        this.application = application;
        this.movieId = movieId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MovieDetailsViewModel(application, movieId);
    }
}
