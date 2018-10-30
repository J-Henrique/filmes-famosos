package com.jhbb.android.filmesfamosos.utilities;

import com.google.gson.Gson;
import com.jhbb.android.filmesfamosos.models.MovieModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Deprecated
public class MoviesJsonUtils {

    private static final String TAG = MoviesJsonUtils.class.getSimpleName();

    @Deprecated
    public static MovieModel[] getMoviesListFromResponse(String httpResponse) throws JSONException {
        Gson gson = new Gson();

        JSONObject jsonObject = new JSONObject(httpResponse);
        JSONArray jsonArray = jsonObject.getJSONArray("results");

        return gson.fromJson(jsonArray.toString(), MovieModel[].class);
    }
}
