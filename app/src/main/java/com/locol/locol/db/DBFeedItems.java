package com.locol.locol.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.locol.locol.pojo.FeedItem;

import java.util.ArrayList;

/**
 * Created by GiapNV on 3/13/15.
 * Project LocoL
 */
public class DBFeedItems {
    private FeedItemsHelper mHelper;
    private SQLiteDatabase mDatabase;

    public DBFeedItems(Context context) {
        mHelper = new FeedItemsHelper(context);
        mDatabase = mHelper.getWritableDatabase();
    }

    public void updateLovedFeedItem(String title, int isLoved) {
        String sql = "UPDATE " + FeedItemsHelper.TABLE_FEED_ITEMS + " SET "
                + FeedItemsHelper.COLUMN_LOVED + " = " + isLoved
                + " WHERE " + FeedItemsHelper.COLUMN_TITLE + " = \'" + title + "\';";

        mDatabase.execSQL(sql);
    }

    public void updateJoiningFeedItem(String title, int joining) {
        String sql = "UPDATE " + FeedItemsHelper.TABLE_FEED_ITEMS + " SET "
                + FeedItemsHelper.COLUMN_JOINING + " = " + joining
                + " WHERE " + FeedItemsHelper.COLUMN_TITLE + " = \'" + title + "\';";

        mDatabase.execSQL(sql);
    }

    public int getLovedFeedItem(String title) {
        int res = 0;
        String[] columns = {FeedItemsHelper.COLUMN_TITLE, FeedItemsHelper.COLUMN_LOVED};
        Cursor cursor = mDatabase.query(FeedItemsHelper.TABLE_FEED_ITEMS, columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex(FeedItemsHelper.COLUMN_TITLE)).equals(title))
                    res = cursor.getInt(cursor.getColumnIndex(FeedItemsHelper.COLUMN_LOVED));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return res;
    }

    public int getJoiningFeedItem(String title) {
        int res = 0;
        String[] columns = {FeedItemsHelper.COLUMN_TITLE, FeedItemsHelper.COLUMN_JOINING};
        Cursor cursor = mDatabase.query(FeedItemsHelper.TABLE_FEED_ITEMS, columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex(FeedItemsHelper.COLUMN_TITLE)).equals(title))
                    res = cursor.getInt(cursor.getColumnIndex(FeedItemsHelper.COLUMN_JOINING));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return res;
    }

    public void insertFeedItems(ArrayList<FeedItem> listFeedItems, boolean clearPrevious) {
        if (clearPrevious) {
            deleteAll();
        }
        //create a sql prepared statement
        String sql = "INSERT INTO " + FeedItemsHelper.TABLE_FEED_ITEMS + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);";
        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        for (int i = 0; i < listFeedItems.size(); i++) {
            FeedItem currentFeedItem = listFeedItems.get(i);
            statement.clearBindings();

            //for a given column index, simply bind the data to be put inside that index
            statement.bindString(2, currentFeedItem.getId());
            statement.bindString(3, currentFeedItem.getTitle());
            statement.bindString(4, currentFeedItem.getStartDate());
            statement.bindString(5, currentFeedItem.getEndDate());
            statement.bindString(6, currentFeedItem.getUrlThumbnail());
            statement.bindString(7, currentFeedItem.getLocation());
            statement.bindString(8, currentFeedItem.getCategory());
            statement.bindString(9, currentFeedItem.getMaxParticipants());
            statement.bindString(10, currentFeedItem.getOrganizer());
            statement.bindString(11, currentFeedItem.getDescription());
            statement.bindLong(12, currentFeedItem.isLoved());
            statement.bindLong(13, currentFeedItem.isJoining());
            statement.execute();
        }
        //set the transaction as successful and end the transaction
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public ArrayList<FeedItem> getAllFeedItems() {
        ArrayList<FeedItem> listFeedItems = new ArrayList<>();

        //get a list of columns to be retrieved, we need all of them
        String[] columns = {
                FeedItemsHelper.COLUMN_ID,
                FeedItemsHelper.COLUMN_UUID,
                FeedItemsHelper.COLUMN_TITLE,
                FeedItemsHelper.COLUMN_START_DATE,
                FeedItemsHelper.COLUMN_END_DATE,
                FeedItemsHelper.COLUMN_URL_THUMBNAIL,
                FeedItemsHelper.COLUMN_PLACE,
                FeedItemsHelper.COLUMN_CATEGORY,
                FeedItemsHelper.COLUMN_MAX_PARTICIPANTS,
                FeedItemsHelper.COLUMN_ORGANIZER,
                FeedItemsHelper.COLUMN_DESCRIPTION,
                FeedItemsHelper.COLUMN_LOVED,
                FeedItemsHelper.COLUMN_JOINING,
        };
        Cursor cursor = mDatabase.query(FeedItemsHelper.TABLE_FEED_ITEMS, columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {

                //create a new feedItem object and retrieve the data from the cursor to be stored in this feedItem object
                FeedItem feedItem = new FeedItem();
                //each step is a 2 part process, find the index of the column first, find the data of that column using
                //that index and finally set our blank feedItem object to contain our data
                feedItem.setId(cursor.getString(cursor.getColumnIndex(FeedItemsHelper.COLUMN_UUID)));
                feedItem.setTitle(cursor.getString(cursor.getColumnIndex(FeedItemsHelper.COLUMN_TITLE)));
                feedItem.setStartDate(cursor.getString(cursor.getColumnIndex(FeedItemsHelper.COLUMN_START_DATE)));
                feedItem.setEndDate(cursor.getString(cursor.getColumnIndex(FeedItemsHelper.COLUMN_END_DATE)));
                feedItem.setUrlThumbnail(cursor.getString(cursor.getColumnIndex(FeedItemsHelper.COLUMN_URL_THUMBNAIL)));
                feedItem.setLocation(cursor.getString(cursor.getColumnIndex(FeedItemsHelper.COLUMN_PLACE)));
                feedItem.setCategory(cursor.getString(cursor.getColumnIndex(FeedItemsHelper.COLUMN_CATEGORY)));
                feedItem.setMaxParticipants(cursor.getString(cursor.getColumnIndex(FeedItemsHelper.COLUMN_MAX_PARTICIPANTS)));
                feedItem.setOrganizer(cursor.getString(cursor.getColumnIndex(FeedItemsHelper.COLUMN_ORGANIZER)));
                feedItem.setDescription(cursor.getString(cursor.getColumnIndex(FeedItemsHelper.COLUMN_DESCRIPTION)));
                feedItem.setLoved(cursor.getInt(cursor.getColumnIndex(FeedItemsHelper.COLUMN_LOVED)));
                feedItem.setJoining(cursor.getInt(cursor.getColumnIndex(FeedItemsHelper.COLUMN_JOINING)));
                //add the feedItem to the list of feedItem objects which we plan to return
                listFeedItems.add(feedItem);
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return listFeedItems;
    }

    public void deleteAll() {
        mDatabase.delete(FeedItemsHelper.TABLE_FEED_ITEMS, null, null);
    }

    private static class FeedItemsHelper extends SQLiteOpenHelper {
        public static final String TABLE_FEED_ITEMS = "feed_items";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_UUID = "uuid";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_START_DATE = "start_date";
        public static final String COLUMN_END_DATE = "end_date";
        public static final String COLUMN_URL_THUMBNAIL = "thumbnail_url";
        public static final String COLUMN_PLACE = "location";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_MAX_PARTICIPANTS = "max_participants";
        public static final String COLUMN_ORGANIZER = "organizer";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_LOVED = "loved";
        public static final String COLUMN_JOINING = "joining";
        private static final String CREATE_TABLE_FEED_ITEM = "CREATE TABLE " + TABLE_FEED_ITEMS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_UUID + " TEXT," +
                COLUMN_TITLE + " TEXT," +
                COLUMN_START_DATE + " DATETIME," +
                COLUMN_END_DATE + " DATETIME," +
                COLUMN_URL_THUMBNAIL + " TEXT," +
                COLUMN_PLACE + " TEXT," +
                COLUMN_CATEGORY + " TEXT," +
                COLUMN_MAX_PARTICIPANTS + " TEXT," +
                COLUMN_ORGANIZER + " TEXT," +
                COLUMN_DESCRIPTION + " TEXT," +
                COLUMN_LOVED + " INTEGER," +
                COLUMN_JOINING + " INTEGER" +
                ")";
        private static final String DB_NAME = "feed_items_db";
        private static final int DB_VERSION = 1;
        private Context mContext;

        public FeedItemsHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE_FEED_ITEM);
                Log.d("DBFeedItems", CREATE_TABLE_FEED_ITEM);
            } catch (SQLiteException exception) {
                exception.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(" DROP TABLE " + TABLE_FEED_ITEMS + " IF EXISTS;");
                onCreate(db);
            } catch (SQLiteException exception) {
                exception.printStackTrace();
            }
        }
    }
}

