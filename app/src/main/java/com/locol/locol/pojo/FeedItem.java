package com.locol.locol.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by GiapNV on 3/11/15.
 * Project LocoL
 */
public class FeedItem implements Parcelable {
    private long id;
    private String title;
    private String date;
    private String urlThumbnail;
    private String place;
    private String description;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlThumbnail() {
        return urlThumbnail;
    }

    public void setUrlThumbnail(String urlThumbnail) {
        this.urlThumbnail = urlThumbnail;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return getId() + "\n" + getTitle() + "\n" + getDescription() + "\n" + getPlace() + "\n" + getUrlThumbnail();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(date);
        dest.writeString(urlThumbnail);
        dest.writeString(place);
        dest.writeString(description);
    }
}
