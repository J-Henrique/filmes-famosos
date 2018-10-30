package com.jhbb.android.filmesfamosos;

import com.google.gson.annotations.SerializedName;

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
