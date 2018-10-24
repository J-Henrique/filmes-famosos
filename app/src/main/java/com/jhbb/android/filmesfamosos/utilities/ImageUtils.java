package com.jhbb.android.filmesfamosos.utilities;

import android.net.Uri;
import android.nfc.Tag;
import android.util.Log;

import com.jhbb.android.filmesfamosos.enums.ImageSizeEnum;

import java.net.MalformedURLException;
import java.net.URL;

public class ImageUtils {

    private static final String TAG = ImageUtils.class.getSimpleName();

    private static final String TMDB_URL = "https://image.tmdb.org/t/p/";

    private static String getImageSize(ImageSizeEnum sizeEnum) {
        String imageSize = "";

        switch (sizeEnum) {
            case SMALL:
                imageSize = "w92";
                break;
            case STANDARD:
                imageSize = "w185";
                break;
            case LARGE:
                imageSize = "w500";
                break;
            case EXTRA_LARGE:
                imageSize = "w780";
                break;
            case ORIGINAL:
                imageSize = "original";
                break;
        }

        return imageSize;
    }

    public static URL buildImageUrl(ImageSizeEnum imageSizeEnum, String imagePath) {
        String imageSize = getImageSize(imageSizeEnum);

        Uri uri = Uri.parse(TMDB_URL)
                .buildUpon()
                .appendEncodedPath(imageSize)
                .appendEncodedPath(imagePath)
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
}
