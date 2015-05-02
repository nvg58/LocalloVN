package com.locol.locol.activities;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.locol.locol.R;
import com.locol.locol.adapters.ThingsAdapter;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.Calendar;
import java.util.List;


public class ComingSoonActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private ListView listView;
    private ThingsAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coming_soon);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_explore_primary)));

        ParseQueryAdapter.QueryFactory<ParseObject> parseQuery = new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("Event");
                Calendar calendar = Calendar.getInstance();
                query.whereGreaterThanOrEqualTo("start_date", calendar.getTime());
                calendar.add(Calendar.DAY_OF_YEAR, 1); // tomorrow
                query.whereLessThanOrEqualTo("start_date", calendar.getTime());
                query.orderByDescending("start_date");
                return query;
            }
        };
        adapter = new ThingsAdapter(this, parseQuery);
        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        adapter.loadObjects();
        adapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<ParseObject>() {
            @Override
            public void onLoading() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoaded(List<ParseObject> list, Exception e) {
                if (list.isEmpty())
                    Toast.makeText(ComingSoonActivity.this, "There are no coming soon events! Check back later!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_coming_soon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
