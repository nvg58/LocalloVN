package com.locol.locol;

import android.util.Log;

import com.locol.locol.pojo.FeedItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by GiapNV on 3/10/15.
 * Project LocoL
 */
public class Parser {

    public static final String KEY_EVENTS = "events";
    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "name";
    public static final String KEY_START_DATE = "start";
    public static final String KEY_END_DATE = "end";
    public static final String KEY_THUMBNAIL = "logo_url";
    public static final String KEY_PLACE = "venue";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_ORGANIZER = "organizer";
    public static final String KEY_DESCRIPTION = "description";
    public static final String TEXT_FORMAT = "text";
    public static final String HTML_FORMAT = "html";

    public static ArrayList<FeedItem> parseJSONResponse(JSONObject response) {
        ArrayList<FeedItem> listFeedItems = new ArrayList<>();
        if (response != null && response.length() > 0) {
            try {
                Log.d("parseJSONResponse: ", response.toString());
                JSONArray arrayFeedItems = response.getJSONArray(KEY_EVENTS);

                for (int i = 0; i < arrayFeedItems.length(); i++) {
                    long id = -1;
                    String title = Constants.NA;
                    String startDate = Constants.NA;
                    String endDate = Constants.NA;
                    String urlThumbnail = Constants.NA;
                    String place = Constants.NA;
                    String latitude = Constants.NA;
                    String longitude = Constants.NA;
                    String organizer = Constants.NA;
                    String description = Constants.NA;
                    JSONObject currentFeedItem = arrayFeedItems.getJSONObject(i);

                    //get the id of the current feedItem 
                    if (Utils.contains(currentFeedItem, KEY_ID)) {
                        id = currentFeedItem.getLong(KEY_ID);
                    }

                    //get the title of the current feedItem 
                    if (Utils.contains(currentFeedItem, KEY_TITLE)) {
                        JSONObject objectTitle = currentFeedItem.getJSONObject(KEY_TITLE);
                        if (Utils.contains(objectTitle, HTML_FORMAT)) {
                            title = objectTitle.getString(HTML_FORMAT);
                        }
                    }

                    //get the date for the current feedItem
                    if (Utils.contains(currentFeedItem, KEY_START_DATE)) {
                        JSONObject objectStartDates = currentFeedItem.getJSONObject(KEY_START_DATE);

                        if (Utils.contains(objectStartDates, "local")) {
                            startDate = objectStartDates.getString("local");
                        }
                    }

                    //get the date for the current feedItem
                    if (Utils.contains(currentFeedItem, KEY_END_DATE)) {
                        JSONObject objectEndDates = currentFeedItem.getJSONObject(KEY_END_DATE);

                        if (Utils.contains(objectEndDates, "local")) {
                            endDate = objectEndDates.getString("local");
                        }
                    }

                    //get the url for the thumbnail to be displayed inside the current feedItem result
                    if (Utils.contains(currentFeedItem, KEY_THUMBNAIL)) {
                        urlThumbnail = currentFeedItem.getString(KEY_THUMBNAIL);
                    }

                    //get the feedItem venue
                    if (Utils.contains(currentFeedItem, KEY_PLACE)) {
                        place = currentFeedItem.getJSONObject(KEY_PLACE).getJSONObject("address").getString("address_1");
                        if (place.equalsIgnoreCase("null")) {
                            place = Constants.NA;
                        }
                    }

                    //get the feedItem latitude
                    if (Utils.contains(currentFeedItem, KEY_PLACE)) {
                        latitude = currentFeedItem.getJSONObject(KEY_PLACE).getString(KEY_LATITUDE);
                    }

                    //get the feedItem longitude
                    if (Utils.contains(currentFeedItem, KEY_PLACE)) {
                        longitude = currentFeedItem.getJSONObject(KEY_PLACE).getString(KEY_LONGITUDE);
                    }

                    //get organizer
                    if (Utils.contains(currentFeedItem, KEY_ORGANIZER)) {
                        organizer = currentFeedItem.getJSONObject(KEY_ORGANIZER).getString("name");
                    }

                    //get the feedItem description
                    if (Utils.contains(currentFeedItem, KEY_DESCRIPTION)) {
                        JSONObject objectDesc = currentFeedItem.getJSONObject(KEY_DESCRIPTION);
                        if (Utils.contains(objectDesc, HTML_FORMAT)) {
                            description = objectDesc.getString(HTML_FORMAT);
                        }
                    }

                    FeedItem feedItem = new FeedItem();
                    feedItem.setId(id);
                    feedItem.setTitle(title);
                    feedItem.setStartDate(startDate);
                    feedItem.setEndDate(endDate);
                    feedItem.setUrlThumbnail(urlThumbnail);
                    feedItem.setPlace(place);
                    feedItem.setLatitude(latitude);
                    feedItem.setLongitude(longitude);
                    feedItem.setOrganizer(organizer);
                    feedItem.setDescription(description);

                    if (id != -1 && !title.equals(Constants.NA)) {
                        listFeedItems.add(feedItem);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return listFeedItems;
    }
} 