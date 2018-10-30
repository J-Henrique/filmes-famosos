package com.jhbb.android.filmesfamosos.utilities;

import android.net.Uri;
import android.util.Log;

import com.jhbb.android.filmesfamosos.constants.ImageSizeConstant;

import java.net.MalformedURLException;
import java.net.URL;

public class ImageUtils {

    private static final String TAG = ImageUtils.class.getSimpleName();

    private static final String TMDB_URL = "https://image.tmdb.org/t/p/";

    private static String getImageSize(int sizeEnum) {
        String imageSize = "";

        switch (sizeEnum) {
            case ImageSizeConstant.SMALL:
                imageSize = "w92";
                break;
            case ImageSizeConstant.STANDARD:
                imageSize = "w185";
                break;
            case ImageSizeConstant.LARGE:
                imageSize = "w500";
                break;
            case ImageSizeConstant.EXTRA_LARGE:
                imageSize = "w780";
                break;
            case ImageSizeConstant.ORIGINAL:
                imageSize = "original";
                break;
        }

        return imageSize;
    }

    public static URL buildImageUrl(int imageSizeEnum, String imagePath) {
        String imageSize = getImageSize(imageSizeEnum);

        Uri uri = Uri.parse(TMDB_URL)
                .buildUpon()
                .appendEncodedPath(imageSize)
                .appendEncodedPath(imagePath)
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
}
