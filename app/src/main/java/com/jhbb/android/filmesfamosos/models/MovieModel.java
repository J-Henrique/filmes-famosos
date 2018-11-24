package com.jhbb.android.filmesfamosos.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "movies")
public class MovieModel implements Parcelable {

    @PrimaryKey
    @NonNull
    @SerializedName("id")
    private String id;

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

    @Ignore
    private boolean isFavorite;

    private MovieModel(Parcel in) {
        id = in.readString();
        title = in.readString();
        poster = in.readString();
        overview = in.readString();
        voteAverage = in.readString();
        releaseDate = in.readString();
        isFavorite = in.readByte() != 0;
    }

    public MovieModel() {

    }

    public String getId() { return id; }

    public void setId(String id) {
        this.id = id;
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

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(id);
        out.writeString(title);
        out.writeString(poster);
        out.writeString(overview);
        out.writeString(voteAverage);
        out.writeString(releaseDate);
        out.writeByte((byte) (isFavorite ? 1 : 0));
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

    @Override
    public String toString() {
        return "MovieModel{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", poster='" + poster + '\'' +
                ", overview='" + overview + '\'' +
                ", voteAverage='" + voteAverage + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", isFavorite='" + isFavorite + '\'' +
                '}';
    }
}