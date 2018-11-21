package com.jhbb.android.filmesfamosos.models;

import com.google.gson.annotations.SerializedName;

public class ReviewsResultModel {

    @SerializedName("results")
    public ReviewModel[] reviewModels;

    public ReviewsResultModel(ReviewModel[] reviewsArray) {
        this.reviewModels = reviewsArray;
    }

    public ReviewModel[] getReviewModels() { return reviewModels; }
}
