package com.jhbb.android.filmesfamosos.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.jhbb.android.filmesfamosos.enums.MovieCategoryEnum;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIES_URL = "https://api.themoviedb.org/3";
    private static final String QUERY_PARAM = "api_key";
    private static final String POPULAR_ENDPOINT = "movie/popular";
    private static final String TOP_RATED_ENDPOINT = "movie/top_rated";



    public static URL buildUrl(MovieCategoryEnum movieCategory) {
        String endpoint = getEndpoint(movieCategory);

        Uri uri = Uri.parse(MOVIES_URL)
                .buildUpon()
                .appendEncodedPath(endpoint)
                .appendQueryParameter(QUERY_PARAM, ApiConfigUtils.API_KEY_VALUE)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, url.toString());

        return url;
    }

    public static String getResponseFromUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            Scanner scanner = new Scanner(urlConnection.getInputStream());
            scanner.useDelimiter("\\A");

            if(scanner.hasNext()) {
               return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    private static String getEndpoint(MovieCategoryEnum category) {
        String currentEndpoint = "";

        switch (category) {
            case POPULAR:
                currentEndpoint = POPULAR_ENDPOINT;
                break;
            case TOP_RATED:
                currentEndpoint = TOP_RATED_ENDPOINT;
                break;
        }

        return currentEndpoint;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
