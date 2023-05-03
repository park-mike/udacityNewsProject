package com.example.android.udacitynewsappdraft;

import android.os.Parcel;
import android.os.Parcelable;

public class News implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<News> CREATOR = new Parcelable.Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };
    private String mTitle;
    private String mAuthor;
    private String mUrl;

    public News(String Title, String Author, String webUrl) {
        mTitle = Title;
        mAuthor = Author;
        mUrl = webUrl;
    }

    protected News(Parcel in) {
        mTitle = in.readString();
        mAuthor = in.readString();
        mUrl = in.readString();
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getUrl() {
        return mUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mAuthor);
        dest.writeString(mUrl);
    }
}