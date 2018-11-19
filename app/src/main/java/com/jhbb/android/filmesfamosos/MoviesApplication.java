package com.jhbb.android.filmesfamosos;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class MoviesApplication extends Application {

    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);
    }
}
