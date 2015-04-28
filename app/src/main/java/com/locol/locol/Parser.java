package com.locol.locol;

import com.locol.locol.pojo.FeedItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by GiapNV on 3/10/15.
 * Project LocoL
 */
public class Parser {

    public static final String KEY_EVENTS = "results";
    public static final String KEY_ID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_TIME = "time";
    public static final String KEY_DATE = "date";
    public static final String KEY_THUMBNAIL = "thumbnail_url";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_MAX_PARTICIPANTS = "max_participants";
    public static final String KEY_ORGANIZER = "organizer";
    public static final String KEY_DESCRIPTION = "description";
    public static final String TEXT_FORMAT = "text";
    public static final String HTML_FORMAT = "html";

    public static final String JOB_KEY = "key";

    public static ArrayList<FeedItem> parseJSONResponse(JSONArray response) {
        ArrayList<FeedItem> listFeedItems = new ArrayList<>();
        if (response != null && response.length() > 0) {
            try {
                JSONArray arrayFeedItems = response; //new JSONArray(response); //response.getJSONArray(KEY_EVENTS);

                for (int i = 0; i < arrayFeedItems.length(); i++) {
                    String id = Constants.NA;
                    String title = Constants.NA;
                    String startDate = Constants.NA;
                    String endDate = Constants.NA;
                    String urlThumbnail = Constants.NA;
                    String place = Constants.NA;
                    String category = Constants.NA;
                    String maxParticipants = Constants.NA;
                    String organizer = Constants.NA;
                    String description = Constants.NA;

                    JSONObject currentFeedItem = arrayFeedItems.getJSONObject(i);

                    //get the id of the current feedItem
//                    if (Utils.contains(currentFeedItem, KEY_ID)) {
//                        JSONObject objectID = currentFeedItem.getJSONObject(KEY_ID);
//                        if (Utils.contains(objectID, "$oid")) {
//                            id = objectID.getString("$oid");
//                        }
//                    }

                    id = new BigInteger(130, new SecureRandom()).toString();

                    //get the title of the current feedItem 
                    if (Utils.contains(currentFeedItem, KEY_TITLE)) {
                        title = currentFeedItem.getString(KEY_TITLE).replace("\'", "\"");
                    }

                    if (title.equals("")) continue;

                    String time = "";
                    String date = "";
                    //get the date for the current feedItem
                    if (Utils.contains(currentFeedItem, KEY_TIME)) {
                        time = currentFeedItem.getString(KEY_TIME).replace("-", "").replaceAll("\\s+", " ");
                    }

                    //get the date for the current feedItem
                    if (Utils.contains(currentFeedItem, KEY_DATE)) {
                        date = currentFeedItem.getString(KEY_DATE).replaceAll("\\s+", " ");
                    }

                    String[] partTimes = time.split(" ");
                    String[] partDates = date.split(" ");

                    if (partTimes.length >= 1 && partDates.length >= 1) {
                        startDate = (partDates.length == 2 ? partDates[1] : partDates[0]) + ' ' + partTimes[0];
                        endDate = (partDates.length == 2 ? partDates[1] : partDates[0]) + ' ' + (partTimes.length == 2 ? partTimes[1] : partTimes[0]);
                    }

                    // change start and end date format
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    DateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        Date sDate = dateFormat.parse(startDate);
                        Date eDate = dateFormat.parse(endDate);
                        startDate = newDateFormat.format(sDate);
                        endDate = newDateFormat.format(eDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    //get the url for the thumbnail to be displayed inside the current feedItem result
                    if (Utils.contains(currentFeedItem, KEY_THUMBNAIL)) {
                        urlThumbnail = currentFeedItem.getString(KEY_THUMBNAIL);
                    }

                    //get the feedItem venue
                    if (Utils.contains(currentFeedItem, KEY_LOCATION)) {
                        place = currentFeedItem.getString(KEY_LOCATION);
                    }

                    //get the feedItem category
                    if (Utils.contains(currentFeedItem, KEY_CATEGORY)) {
                        category = currentFeedItem.getString(KEY_CATEGORY);
                    }

                    //get the feedItem maxParticipants
                    if (Utils.contains(currentFeedItem, KEY_MAX_PARTICIPANTS)) {
                        maxParticipants = currentFeedItem.getString(KEY_MAX_PARTICIPANTS);
                    }

                    //get organizer
                    if (Utils.contains(currentFeedItem, KEY_ORGANIZER)) {
                        organizer = currentFeedItem.getString(KEY_ORGANIZER);
                    }

                    //get the feedItem description
                    if (Utils.contains(currentFeedItem, KEY_DESCRIPTION)) {
                        description = currentFeedItem.getString(KEY_DESCRIPTION);
                    }

                    FeedItem feedItem = new FeedItem();
                    feedItem.setId(id);
                    feedItem.setTitle(title);
                    feedItem.setStartDate(startDate);
                    feedItem.setEndDate(endDate);
                    feedItem.setUrlThumbnail(urlThumbnail);
                    feedItem.setLocation(place);
                    feedItem.setCategory(category);
                    feedItem.setMaxParticipants(maxParticipants);
                    feedItem.setOrganizer(organizer);
                    feedItem.setDescription(description);

                    if (!id.equals(Constants.NA) && !title.equals(Constants.NA)) {
                        listFeedItems.add(feedItem);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return listFeedItems;
    }

    public static String parseLatestJobID(JSONArray response) {
        String res = "13882/1/209";
        if (response != null && response.length() > 0) {
            try {
                JSONObject latest;
                latest = response.getJSONObject(0);
                res = latest.getString(JOB_KEY);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return res;
    }
} 