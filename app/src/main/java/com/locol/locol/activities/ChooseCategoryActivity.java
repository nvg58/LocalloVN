package com.locol.locol.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.locol.locol.viewa.CheckableRelativeLayout;
import com.locol.locol.R;
import com.locol.locol.adapters.CategoryAdapter;
import com.locol.locol.helpers.Preferences;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.HashSet;
import java.util.List;


public class ChooseCategoryActivity extends ActionBarActivity {
    public static final int MAX_NEEDED_CATS = 3;
    private Toolbar toolbar;
    private HashSet<String> selectedCat = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("What interest you?");
        getSupportActionBar().setIcon(getResources().getDrawable(R.mipmap.ic_launcher));

        GridView gridview = (GridView) findViewById(R.id.gridview);
        ParseQueryAdapter.QueryFactory<ParseObject> parseQuery = new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("Category");
                return query;
            }
        };
        final CategoryAdapter adapter = new CategoryAdapter(this, parseQuery);
        adapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<ParseObject>() {
            @Override
            public void onLoading() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoaded(List<ParseObject> list, Exception e) {
                progressBar.setVisibility(View.GONE);
            }
        });
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String cat = ((TextView) v.findViewById(R.id.hidden_save_id)).getText().toString();

                CheckableRelativeLayout layout = (CheckableRelativeLayout) v.findViewById(R.id.category_item);
                layout.setChecked(!layout.isChecked());

                adapter.notifyDataSetChanged();

                if (layout.isChecked()) selectedCat.add(cat);
                else selectedCat.remove(cat);

                Preferences.saveToPreferences(ChooseCategoryActivity.this, "_user_category", "id", selectedCat);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_next:
                if (selectedCat.size() <= MAX_NEEDED_CATS) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

                    // Setting Dialog Message
                    alertDialog.setMessage("You have to choose at least three categories to continue!");

                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                } else {
                    for (String catId: selectedCat) {
                        ParseUser user = ParseUser.getCurrentUser();
                        ParseRelation<ParseObject> relation = user.getRelation("interest");
                        relation.add(ParseObject.createWithoutData("Category", catId));
                        user.saveInBackground();
                    }

                    startActivity(new Intent(this, MyNavigationDrawer.class));
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
