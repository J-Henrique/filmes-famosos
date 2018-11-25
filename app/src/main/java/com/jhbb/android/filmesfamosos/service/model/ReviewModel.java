package com.jhbb.android.filmesfamosos.service.model;

import com.google.gson.annotations.SerializedName;

public class ReviewModel {

    @SerializedName("author")
    public String author;

    @SerializedName("content")
    public String reviewContent;

    public String getAuthor() {
        return author;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    @Override
    public String toString() {
        return "ReviewModel{" +
                "author='" + author + '\'' +
                ", reviewContent='" + reviewContent + '\'' +
                '}';
    }
}
