package com.jhbb.android.filmesfamosos.models;

import com.google.gson.annotations.SerializedName;

public class VideosResultModel {

    @SerializedName("results")
    private VideoModel[] videoModels;

    public VideosResultModel(VideoModel[] videoModels) {
        this.videoModels = videoModels;
    }

    public VideoModel[] getVideoModels() {
        return videoModels;
    }
}
