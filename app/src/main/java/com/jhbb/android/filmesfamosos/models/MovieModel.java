package com.jhbb.android.filmesfamosos.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MovieModel implements Parcelable {

    @SerializedName("title")
    private String title;

    @SerializedName("poster_path")
    private String poster;

    @SerializedName("overview")
    private String overview;

    @SerializedName("vote_average")
    private String voteAverage;

    @SerializedName("release_date")
    private String releaseDate;

    private MovieModel(Parcel in) {
        title = in.readString();
        poster = in.readString();
        overview = in.readString();
        voteAverage = in.readString();
        releaseDate = in.readString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(title);
        out.writeString(poster);
        out.writeString(overview);
        out.writeString(voteAverage);
        out.writeString(releaseDate);
    }

    public static final Parcelable.Creator<MovieModel> CREATOR = new Parcelable.Creator<MovieModel>() {

        @Override
        public MovieModel createFromParcel(Parcel parcel) {
            return new MovieModel(parcel);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };
}