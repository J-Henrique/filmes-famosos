package com.jhbb.android.filmesfamosos.models;

import com.google.gson.annotations.SerializedName;
import com.jhbb.android.filmesfamosos.models.MovieModel;

public class MoviesResultModel {

    @SerializedName("results")
    private MovieModel[] movieModels;

    public MoviesResultModel(MovieModel[] moviesArray) {
        movieModels = moviesArray;
    }

    public MovieModel[] getMovieModels() {
        return movieModels;
    }

}
