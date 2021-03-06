package com.jhbb.android.filmesfamosos.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.jhbb.android.filmesfamosos.BuildConfig;
import com.jhbb.android.filmesfamosos.constants.MovieCategoryConstant;

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


    @Deprecated
    public static URL buildUrl(int movieCategory) {
        String endpoint = getEndpoint(movieCategory);

        Uri uri = Uri.parse(MOVIES_URL)
                .buildUpon()
                .appendEncodedPath(endpoint)
                .appendQueryParameter(QUERY_PARAM, BuildConfig.ApiKey)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
            Log.v(TAG, url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    @Deprecated
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

    @Deprecated
    private static String getEndpoint(int category) {
        String currentEndpoint = "";

        switch (category) {
            case MovieCategoryConstant.POPULAR:
                currentEndpoint = POPULAR_ENDPOINT;
                break;
            case MovieCategoryConstant.TOP_RATED:
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
