package com.locol.locol;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

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

    public void insertFeedItems(ArrayList<FeedItem> listFeedItems, boolean clearPrevious) {
        if (clearPrevious) {
            deleteAll();
        }
        //create a sql prepared statement
        String sql = "INSERT INTO " + FeedItemsHelper.TABLE_FEED_ITEMS + " VALUES (?,?,?,?,?,?);";
        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        for (int i = 0; i < listFeedItems.size(); i++) {
            FeedItem currentFeedItem = listFeedItems.get(i);
            statement.clearBindings();
            //for a given column index, simply bind the data to be put inside that index
            statement.bindString(2, currentFeedItem.getTitle());
            statement.bindString(3, currentFeedItem.getDate());
            statement.bindString(4, currentFeedItem.getUrlThumbnail());
            statement.bindString(5, currentFeedItem.getPlace());
            statement.bindString(6, currentFeedItem.getDescription());
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
                FeedItemsHelper.COLUMN_UID,
                FeedItemsHelper.COLUMN_TITLE,
                FeedItemsHelper.COLUMN_START_DATE,
                FeedItemsHelper.COLUMN_URL_THUMBNAIL,
                FeedItemsHelper.COLUMN_PLACE,
                FeedItemsHelper.COLUMN_DESCRIPTION
        };
        Cursor cursor = mDatabase.query(FeedItemsHelper.TABLE_FEED_ITEMS, columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {

                //create a new feedItem object and retrieve the data from the cursor to be stored in this feedItem object
                FeedItem feedItem = new FeedItem();
                //each step is a 2 part process, find the index of the column first, find the data of that column using
                //that index and finally set our blank feedItem object to contain our data
                feedItem.setTitle(cursor.getString(cursor.getColumnIndex(FeedItemsHelper.COLUMN_TITLE)));
                feedItem.setDate(cursor.getString(cursor.getColumnIndex(FeedItemsHelper.COLUMN_START_DATE)));
                feedItem.setUrlThumbnail(cursor.getString(cursor.getColumnIndex(FeedItemsHelper.COLUMN_URL_THUMBNAIL)));
                feedItem.setPlace(cursor.getString(cursor.getColumnIndex(FeedItemsHelper.COLUMN_PLACE)));
                feedItem.setDescription(cursor.getString(cursor.getColumnIndex(FeedItemsHelper.COLUMN_DESCRIPTION)));
                //add the feedItem to the list of feedItem objects which we plan to return
                listFeedItems.add(feedItem);
            }
            while (cursor.moveToNext());
        }
        return listFeedItems;
    }

    public void deleteAll() {
        mDatabase.delete(FeedItemsHelper.TABLE_FEED_ITEMS, null, null);
    }

    private static class FeedItemsHelper extends SQLiteOpenHelper {
        public static final String TABLE_FEED_ITEMS = "feed_items";
        public static final String COLUMN_UID = "_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_START_DATE = "start_date";
        public static final String COLUMN_URL_THUMBNAIL = "url_thumbnail";
        public static final String COLUMN_PLACE = "place";
        public static final String COLUMN_DESCRIPTION = "description";
        private static final String CREATE_TABLE_FEED_ITEM = "CREATE TABLE " + TABLE_FEED_ITEMS + " (" +
                COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TITLE + " TEXT," +
                COLUMN_START_DATE + " TEXT," +
                COLUMN_URL_THUMBNAIL + " TEXT," +
                COLUMN_PLACE + " TEXT," +
                COLUMN_DESCRIPTION + " TEXT" +
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

