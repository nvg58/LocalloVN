package com.locol.locol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by GiapNV on 3/10/15.
 * Project LocoL
 */
public class Parser {

    public static final String KEY_EVENTS = "feedItems";
    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "name";
    public static final String KEY_DATES = "start";
    public static final String KEY_THUMBNAIL = "logo_url";
    public static final String KEY_PLACE = "venue";
    public static final String KEY_DESCRIPTION = "description";
    public static final String TEXT_FORMAT = "text";

    public static ArrayList<FeedItem> parseJSONResponse(JSONObject response) {
        DateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.US);
        ArrayList<FeedItem> listFeedItems = new ArrayList<>();
        if (response != null && response.length() > 0) {
            try {
                JSONArray arrayFeedItems = response.getJSONArray(KEY_EVENTS);
                for (int i = 0; i < arrayFeedItems.length(); i++) {
                    long id = -1;
                    String title = Constants.NA;
                    String startDate = Constants.NA;
                    String urlThumbnail = Constants.NA;
                    String place = Constants.NA;
                    String description = Constants.NA;
                    JSONObject currentFeedItem = arrayFeedItems.getJSONObject(i);

                    //get the id of the current feedItem 
                    if (Utils.contains(currentFeedItem, KEY_ID)) {
                        id = currentFeedItem.getLong(KEY_ID);
                    }

                    //get the title of the current feedItem 
                    if (Utils.contains(currentFeedItem, KEY_TITLE)) {
                        JSONObject objectTitle = currentFeedItem.getJSONObject(KEY_TITLE);
                        if (Utils.contains(objectTitle, TEXT_FORMAT)) {
                            title = objectTitle.getString(TEXT_FORMAT);
                        }
                    }

                    //get the date for the current feedItem
                    if (Utils.contains(currentFeedItem, KEY_DATES)) {
                        JSONObject objectStartDates = currentFeedItem.getJSONObject(KEY_DATES);

                        if (Utils.contains(objectStartDates, "local")) {
                            startDate = objectStartDates.getString("local");
                        }
                    }

                    //get the url for the thumbnail to be displayed inside the current feedItem result
                    if (Utils.contains(currentFeedItem, KEY_THUMBNAIL)) {
                        urlThumbnail = currentFeedItem.getString(KEY_THUMBNAIL);
                    }

                    //get the feedItem venue
                    if (Utils.contains(currentFeedItem, KEY_PLACE)) {
                        place = currentFeedItem.getJSONObject(KEY_PLACE).getJSONObject("address").getString("address_1");
                    }

                    //get the feedItem description
                    if (Utils.contains(currentFeedItem, KEY_DESCRIPTION)) {
                        JSONObject objectDesc = currentFeedItem.getJSONObject(KEY_DESCRIPTION);
                        if (Utils.contains(objectDesc, TEXT_FORMAT)) {
                            description = objectDesc.getString(TEXT_FORMAT);
                        }
                    }

                    FeedItem feedItem = new FeedItem();
                    feedItem.setId(id);
                    feedItem.setTitle(title);
                    Date date = null;
                    try {
                        date = dateFormat.parse(startDate);
                    } catch (ParseException e) {
                        //a parse exception generated here will store null in the release date, be sure to handle it 
                    }
                    feedItem.setDate(date.toString());
                    feedItem.setUrlThumbnail(urlThumbnail);
                    feedItem.setPlace(place);
                    feedItem.setDescription(description);
//                    L.t(getActivity(), feedItem + ""); 
                    if (id != -1 && !title.equals(Constants.NA)) {
                        listFeedItems.add(feedItem);
                    }
                }


            } catch (JSONException e) {

            }
//                L.t(getActivity(), listFeedItems.size() + " rows fetched"); 
        }
        return listFeedItems;
    }
} 