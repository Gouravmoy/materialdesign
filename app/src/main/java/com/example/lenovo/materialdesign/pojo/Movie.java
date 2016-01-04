package com.example.lenovo.materialdesign.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.lenovo.materialdesign.logging.L;

import java.util.Date;

/**
 * Created by lenovo on 1/1/2016.
 */
public class Movie implements Parcelable {
    private Long id;
    private String title;
    private Date releaseDateTheaatre;
    private int audienceScore;
    private String synopsis;
    private String urlThumbnail;
    private String urlSelf;
    private String urlCast;
    private String urlReviews;
    private String urlSimmilar;

    public Movie() {
    }

    public Movie(Parcel input) {
        id = input.readLong();
        title = input.readString();
        long dateMillis = input.readLong();
        releaseDateTheaatre = (dateMillis == -1 ? null : new Date(dateMillis));
        audienceScore = input.readInt();
        synopsis = input.readString();
        urlThumbnail = input.readString();
        urlSelf = input.readString();
        urlCast = input.readString();
        urlReviews = input.readString();
        urlSimmilar = input.readString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseDateTheaatre() {
        return releaseDateTheaatre;
    }

    public void setReleaseDateTheaatre(Date releaseDateTheaatre) {
        this.releaseDateTheaatre = releaseDateTheaatre;
    }

    public int getAudienceScore() {
        return audienceScore;
    }

    public void setAudienceScore(int audienceScore) {
        this.audienceScore = audienceScore;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getUrlThumbnail() {
        return urlThumbnail;
    }

    public void setUrlThumbnail(String urlThumbnail) {
        this.urlThumbnail = urlThumbnail;
    }

    public String getUrlSelf() {
        return urlSelf;
    }

    public void setUrlSelf(String urlSelf) {
        this.urlSelf = urlSelf;
    }

    public String getUrlCast() {
        return urlCast;
    }

    public void setUrlCast(String urlCast) {
        this.urlCast = urlCast;
    }

    public String getUrlReviews() {
        return urlReviews;
    }

    public void setUrlReviews(String urlReviews) {
        this.urlReviews = urlReviews;
    }

    public String getUrlSimmilar() {
        return urlSimmilar;
    }

    public void setUrlSimmilar(String urlSimmilar) {
        this.urlSimmilar = urlSimmilar;
    }

    @Override
    public int describeContents() {
        L.m("Describes Contents of Movie");
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeLong(releaseDateTheaatre == null ? -1 : releaseDateTheaatre.getTime());
        dest.writeInt(audienceScore);
        dest.writeString(synopsis);
        dest.writeString(urlThumbnail);
        dest.writeString(urlSelf);
        dest.writeString(urlCast);
        dest.writeString(urlReviews);
        dest.writeString(urlSimmilar);
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            L.m("create from parcel :Movie");
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
