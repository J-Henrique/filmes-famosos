package com.jhbb.android.filmesfamosos.utilities;

import android.util.Log;

import com.jhbb.android.filmesfamosos.MovieModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MoviesJsonUtils {

    private static final String TAG = MoviesJsonUtils.class.getSimpleName();

    public static MovieModel[] getMoviesListFromResponse(String httpResponse) throws JSONException {
        JSONObject jsonObject = new JSONObject(httpResponse);
        MovieModel[] moviesList = null;

        if (jsonObject.has("results")) {
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            int arrayLength = jsonArray.length();

            moviesList = new MovieModel[arrayLength];
            for (int i = 0; i < arrayLength; i++) {
                MovieModel movieModel = new MovieModel();
                JSONObject object = jsonArray.getJSONObject(i);

                movieModel.setTitle(object.getString("title"));
                movieModel.setPoster(object.getString("poster_path"));
                movieModel.setOverview(object.getString("overview"));
                movieModel.setVoteAverage(object.getString("vote_average"));
                movieModel.setReleaseDate(object.getString("release_date"));

                moviesList[i] = movieModel;
            }
        }

        return moviesList;
    }
}
