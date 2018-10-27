package com.jhbb.android.filmesfamosos.utilities;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jhbb.android.filmesfamosos.MovieModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.List;

public class MoviesJsonUtils {

    private static final String TAG = MoviesJsonUtils.class.getSimpleName();

    public static MovieModel[] getMoviesListFromResponse(String httpResponse) throws JSONException {
        Gson gson = new Gson();

        JSONObject jsonObject = new JSONObject(httpResponse);
        JSONArray jsonArray = jsonObject.getJSONArray("results");

        return gson.fromJson(jsonArray.toString(), MovieModel[].class);
    }
}
