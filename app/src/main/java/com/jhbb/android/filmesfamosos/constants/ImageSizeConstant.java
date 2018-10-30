package com.jhbb.android.filmesfamosos.constants;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ImageSizeConstant {

    public static final int SMALL = 0;
    public static final int STANDARD = 1;
    public static final int LARGE = 2;
    public static final int EXTRA_LARGE = 3;
    public static final int ORIGINAL = 4;

    @IntDef({ SMALL, STANDARD, LARGE, EXTRA_LARGE, ORIGINAL })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ImageSizeConstantInterface {}
}
