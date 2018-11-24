package com.jhbb.android.filmesfamosos.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.jhbb.android.filmesfamosos.models.MovieModel;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movies ORDER BY 1")
    List<MovieModel> loadAllFavoriteMovies();

    @Query("SELECT 1 FROM movies WHERE id = :id")
    boolean checkMovieIsFavorite(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavoriteMovie(MovieModel movie);

    @Delete
    void removeFavoriteMovie(MovieModel movie);
}
