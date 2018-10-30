package com.jhbb.android.filmesfamosos.constants;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MovieCategoryConstant {

    public static final int POPULAR = 0;
    public static final int TOP_RATED = 1;

    @IntDef({ POPULAR, TOP_RATED })
    @Retention(RetentionPolicy.SOURCE)
    public @interface MovieCategoryConstantInterface {}
}
