package com.locol.locol;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.locol.locol.networks.VolleySingleton;
import com.locol.locol.pojo.Account;
import com.manuelpeinado.fadingactionbar.view.ObservableScrollable;
import com.manuelpeinado.fadingactionbar.view.OnScrollChangedCallback;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DetailsEventActivity extends ActionBarActivity implements OnScrollChangedCallback {
    Bundle extras;

    private Toolbar mToolbar;
    private Drawable mActionBarBackgroundDrawable;
    private View mHeader;
    private int mLastDampedScroll;
    private int mInitialStatusBarColor;
    private int mFinalStatusBarColor;

    private final static String TAG = "DetailsEventActivity";

    private String title;
    private String startDate;
    private String endDate;
    private String place;
    private String category;
    private String maxParticipants;
    private String organizer;
    private String description;
    private String urlThumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_event);

        extras = getIntent().getExtras();
        title = extras.getString("EXTRA_FEED_TITLE");
        startDate = extras.getString("EXTRA_FEED_START_DATE");
        endDate = extras.getString("EXTRA_FEED_END_DATE");
        place = extras.getString("EXTRA_FEED_PLACE");
        category = extras.getString("EXTRA_FEED_CATEGORY");
        maxParticipants = extras.getString("EXTRA_FEED_MAX_PARTICIPANTS");
        organizer = extras.getString("EXTRA_FEED_ORGANIZER");
        description = extras.getString("EXTRA_FEED_DESCRIPTION");
        urlThumbnail = extras.getString("EXTRA_FEED_URL_THUMBNAIL");

        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        mActionBarBackgroundDrawable = mToolbar.getBackground();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mInitialStatusBarColor = Color.BLACK;
        mFinalStatusBarColor = getResources().getColor(R.color.colorPrimaryDark);
        mHeader = findViewById(R.id.eventThumbnail);
        ObservableScrollable scrollView = (ObservableScrollable) findViewById(R.id.scrollEventDetails);
        scrollView.setOnScrollChangedCallback(this);
        onScroll(-1, 0);

        if (extras != null) {
            NetworkImageView thumbnail = (NetworkImageView) findViewById(R.id.eventThumbnail);
            thumbnail.setDefaultImageResId(R.drawable.placeholder);
            thumbnail.setImageUrl(urlThumbnail, VolleySingleton.getInstance().getImageLoader());

            final TextView tvTitle = (TextView) findViewById(R.id.eventTitle);
            tvTitle.setText(title);

            TextView tvPlace = (TextView) findViewById(R.id.eventPlace);
            tvPlace.setText(place);

            TextView tvDate = (TextView) findViewById(R.id.eventDate);
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            DateFormat newDateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
            try {
                Date sDate = dateFormat.parse(startDate);
                Date eDate = dateFormat.parse(endDate);
                tvDate.setText("Từ " + newDateFormat.format(sDate) + " đến\n" + newDateFormat.format(eDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            WebView wvDetails = (WebView) findViewById(R.id.eventDescription);
            wvDetails.loadDataWithBaseURL(null, "<style>img{display: inline;height: auto;max-width: 100%;}</style>" + description, "text/html", "UTF-8", null);
//            wvDetails.loadData(description, "text/html; charset=UTF-8",null);

            //            tvDetails.setText(Html.fromHtml(
//                    description,
//                    new URLImageParser(tvDetails, this),
//                    null));

            TextView tvCat = (TextView) findViewById(R.id.eventCategory);
            tvCat.setText(category);

            TextView tvOrganizer = (TextView) findViewById(R.id.organizer);
            tvOrganizer.setText(organizer);

            Button btnAddToCalendar = (Button) findViewById(R.id.btnAddToCalendar);
            btnAddToCalendar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    try {
                        Calendar start = DateToCalendar(dateFormat.parse(startDate));
                        Calendar end = DateToCalendar(dateFormat.parse(endDate));
                        addEvent(DetailsEventActivity.this, title, place, start, end);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });

            ImageButton btnDirection = (ImageButton) findViewById(R.id.btnDirection);
            btnDirection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GPSTracker gps = new GPSTracker(DetailsEventActivity.this);
                    if (gps.canGetLocation()) {
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?saddr=" + gps.getLatitude() + "," + gps.getLongitude() + "&daddr=" + category + "," + maxParticipants));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                        startActivity(intent);
                    } else {
                        gps.showSettingsAlert();
                    }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_item_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String playStoreLink = "https://play.google.com/store/apps/details?id=com.ea.game.pvz2_row";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Join event " + title + " with " + Account.getUserName());
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Install \"LocoL\" to view more: " + playStoreLink);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
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

}
