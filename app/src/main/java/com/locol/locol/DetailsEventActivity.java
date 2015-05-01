package com.locol.locol;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.locol.locol.networks.VolleySingleton;
import com.locol.locol.pojo.Account;
import com.manuelpeinado.fadingactionbar.view.ObservableScrollable;
import com.manuelpeinado.fadingactionbar.view.OnScrollChangedCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DetailsEventActivity extends ActionBarActivity implements OnScrollChangedCallback {
    Bundle extras;

    private Toolbar mToolbar;
    private Drawable mActionBarBackgroundDrawable;
    private View mHeader;
    private int mLastDampedScroll;

    private final static String TAG = "DetailsEventActivity";

    private String title;
    private Long startDate;
    private Long endDate;
    private String place;
    private String category;
    private String maxParticipants;
    private String organizer;
    private String description;
    private String url_thumbnail;
    private int loved;
    private int joining;

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_event);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        extras = getIntent().getExtras();
        title = extras.getString("EXTRA_FEED_TITLE");
        startDate = extras.getLong("EXTRA_FEED_START_DATE");
        endDate = extras.getLong("EXTRA_FEED_END_DATE");
        place = extras.getString("EXTRA_FEED_PLACE");
        category = extras.getString("EXTRA_FEED_CATEGORY");
        maxParticipants = extras.getString("EXTRA_FEED_MAX_PARTICIPANTS");
        organizer = extras.getString("EXTRA_FEED_ORGANIZER");
        description = extras.getString("EXTRA_FEED_DESCRIPTION");
        url_thumbnail = extras.getString("EXTRA_FEED_URL_THUMBNAIL");
        loved = extras.getInt("EXTRA_FEED_LOVED");
        joining = extras.getInt("EXTRA_FEED_JOINING");

        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        mActionBarBackgroundDrawable = mToolbar.getBackground();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mHeader = findViewById(R.id.event_thumbnail);
        ObservableScrollable scrollView = (ObservableScrollable) findViewById(R.id.scroll_event_details);
        scrollView.setOnScrollChangedCallback(this);
        onScroll(-1, 0);

        if (extras != null) {
            NetworkImageView thumbnail = (NetworkImageView) findViewById(R.id.event_thumbnail);
            thumbnail.setDefaultImageResId(R.drawable.placeholder);
            thumbnail.setImageUrl(url_thumbnail, VolleySingleton.getInstance().getImageLoader());

            final TextView tvTitle = (TextView) findViewById(R.id.event_title);
            tvTitle.setText(title);

            TextView tvPlace = (TextView) findViewById(R.id.event_place);
            tvPlace.setText(place);

            TextView tvDate = (TextView) findViewById(R.id.event_date);
            DateFormat newDateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
            Date sDate = new Date(startDate);
            Date eDate = new Date(endDate);
            tvDate.setText("From " + newDateFormat.format(sDate) + "\nto" + newDateFormat.format(eDate));

            WebView wvDetails = (WebView) findViewById(R.id.event_description);
            wvDetails.loadDataWithBaseURL(
                    null,
                    "<style>img {display: inline; height: auto; max-width: 100%;}</style>" + description,
                    "text/html",
                    "UTF-8",
                    null);

            TextView tvCat = (TextView) findViewById(R.id.event_category);
            tvCat.setText(category);

            TextView tvOrganizer = (TextView) findViewById(R.id.organizer);
            tvOrganizer.setText(organizer);

            Button btnAddToCalendar = (Button) findViewById(R.id.btn_add_to_calendar);
            btnAddToCalendar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar start = DateToCalendar(new Date(startDate));
                    Calendar end = DateToCalendar(new Date(startDate));
                    addEvent(DetailsEventActivity.this, title, place, start, end);
                }
            });

            ImageButton btnDirection = (ImageButton) findViewById(R.id.btn_direction);
            btnDirection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GPSTracker gps = new GPSTracker(DetailsEventActivity.this);
                    if (gps.canGetLocation()) {
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?saddr=" + gps.getLatitude() + "," + gps.getLongitude() + "&daddr=" + place));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                        startActivity(intent);
                    } else {
                        gps.showSettingsAlert();
                    }
                }
            });

            final Button btnYes = (Button) findViewById(R.id.btn_yes);
            final Button btnMaybe = (Button) findViewById(R.id.btn_maybe);
            final TextView rvspTitle = (TextView) findViewById(R.id.rvsp_title);

            loved = MainApplication.getWritableDatabase().getLovedFeedItem(title);
            joining = MainApplication.getWritableDatabase().getJoiningFeedItem(title);
            if (joining == 1) {
                rvspTitle.setText(R.string.going_text);
                rvspTitle.setAllCaps(false);

                btnYes.setVisibility(View.GONE);
                btnMaybe.setVisibility(View.GONE);
            }

            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnYes.setVisibility(View.GONE);
                    if (btnMaybe.getVisibility() != View.GONE) {
                        btnMaybe.setVisibility(View.GONE);
                    }
                    rvspTitle.setText(R.string.going_text);
                    rvspTitle.setAllCaps(false);

                    MainApplication.getWritableDatabase().updateJoiningFeedItem(title, 1);
                }
            });
            btnMaybe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnMaybe.setVisibility(View.GONE);
                }
            });
        }
    }

    public void addEvent(Context ctx, String title, String place, Calendar start, Calendar end) {
        Log.d(TAG, "AddUsingIntent.addEvent()");
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("title", title);
        intent.putExtra("eventLocation", place);
        intent.putExtra("beginTime", start.getTimeInMillis());
        intent.putExtra("endTime", end.getTimeInMillis());
        intent.putExtra("allDay", false);
        ctx.startActivity(intent);
    }

    public static Calendar DateToCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details_event, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menu_item_share) {
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentTitle(title)
                        .setContentDescription(Account.getUserName() + " has using LocoL. Install to explore more!")
//                                Account.getUserName() + " has loved \"" + title + "\"")
                        .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                        .setImageUrl(Uri.parse(url_thumbnail))
                        .build();

                shareDialog.show(linkContent);
            }
            return true;
        }

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TAG", "onResume");
    }

    @Override
    public void onScroll(int l, int scrollPosition) {
        int headerHeight = mHeader.getHeight() - mToolbar.getHeight();
        float ratio = 0;
        if (scrollPosition > 0 && headerHeight > 0)
            ratio = (float) Math.min(Math.max(scrollPosition, 0), headerHeight) / headerHeight;

        updateActionBarTransparency(ratio);
        updateParallaxEffect(scrollPosition);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void updateActionBarTransparency(float scrollRatio) {
        int newAlpha = (int) (scrollRatio * 255);
        mActionBarBackgroundDrawable.setAlpha(newAlpha);
        mToolbar.setBackground(mActionBarBackgroundDrawable);
    }

    private void updateParallaxEffect(int scrollPosition) {
        float damping = 0.5f;
        int dampedScroll = (int) (scrollPosition * damping);
        int offset = mLastDampedScroll - dampedScroll;
        mHeader.offsetTopAndBottom(-offset);

        mLastDampedScroll = dampedScroll;
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }
}
