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
    private String startDate;
    private String endDate;
    private String urlThumbnail;
    private String place;
    private String latitude;
    private String longitude;
    private String organizer;
    private String description;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
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
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeString(urlThumbnail);
        dest.writeString(place);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(description);
    }
}
