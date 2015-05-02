package com.locol.locol.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import com.locol.locol.R;
import com.locol.locol.fragments.ExploreFragment;
import com.locol.locol.fragments.FeedFragment;
import com.locol.locol.fragments.ProfileFragment;
import com.locol.locol.pojo.Account;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

/**
 * Created by GiapNV on 3/9/15.
 * Project LocoL
 */
public class MyNavigationDrawer extends ActionBarActivity { //MaterialNavigationDrawer {

    private Toolbar toolbar;
    public final static String TAG = "MyNavigationDrawer";

    static final String STATE_AVATAR = "avatar";
    static final String STATE_COVER = "cover";
    static final String STATE_PATH = "objPath";

    public static final String PREF_FILE_NAME = "user_info";
    public static final String KEY_USER_AVATAR = "avatar";

//    @Override
//    public void init(Bundle savedInstanceState) {
//        this.disableLearningPattern();
//        this.enableToolbarElevation();
//
//        // add accounts
//        final MaterialAccount account = new MaterialAccount(
//                MyNavigationDrawer.this.getResources(),
//                Account.getUserName(),
//                Account.getUserEmail(),
//                R.drawable.profile,
//                R.drawable.header);
//        this.addAccount(account);
//
//        this.addSection(newSection(
//                "Feed",
//                R.mipmap.ic_event_grey600_24dp,
//                new FeedFragment()).setSectionColor(getResources().getColor(R.color.color_feed_primary),
//                getResources().getColor(R.color.color_feed_drimary_dark)));
//
//        this.addSection(newSection(
//                "Explore",
//                R.mipmap.ic_explore_grey600_24dp,
//                new ExploreFragment()).setSectionColor(getResources().getColor(R.color.color_explore_primary),
//                getResources().getColor(R.color.color_explore_primary_dark)));
//
//        this.addSection(newSection(
//                "Profile",
//                R.mipmap.ic_account_box_grey600_24dp,
//                new ProfileFragment()).setSectionColor(getResources().getColor(R.color.color_account_primary),
//                getResources().getColor(R.color.color_account_primary_dark)));
//
//        this.addBottomSection(newSection(
//                "Settings",
//                new Intent(MyNavigationDrawer.this, SettingActivity.class)));
//
//        this.addBottomSection(newSection(
//                "Help & Feedback",
//                new FeedFragment()));
//
//        this.addBottomSection(newSection(
//                "About Us",
//                new FeedFragment()));
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        toolbar.setTitle(getResources().getString(R.string.drawer_item_feed));
        Fragment f = FeedFragment.newInstance(getResources().getString(R.string.drawer_item_feed));
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();

        // Create the AccountHeader
        AccountHeader.Result headerResult = new AccountHeader()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withName(Account.getUserName()).withEmail(Account.getUserEmail()).withIcon(getResources().getDrawable(R.drawable.profile))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_item_feed)
                                .withIdentifier(1000)
                                .withIcon(GoogleMaterial.Icon.gmd_event),
                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_item_explore)
                                .withIdentifier(2000)
                                .withIcon(GoogleMaterial.Icon.gmd_explore),
                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_item_profile)
                                .withIdentifier(3000)
                                .withIcon(GoogleMaterial.Icon.gmd_account_box),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withName(R.string.drawer_item_settings)
                                .withIcon(GoogleMaterial.Icon.gmd_settings),
                        new SecondaryDrawerItem()
                                .withName(R.string.drawer_item_help_and_feedback)
                                .withIcon(GoogleMaterial.Icon.gmd_question_answer),
                        new SecondaryDrawerItem()
                                .withName(R.string.drawer_item_about_us)
                                .withIcon(GoogleMaterial.Icon.gmd_live_help)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                if (drawerItem != null) {
                    if (drawerItem instanceof Nameable) {
                        toolbar.setTitle(((Nameable) drawerItem).getNameRes());
                        Fragment f = new Fragment();
                        if (toolbar.getTitle().toString().equals(getResources().getString(R.string.drawer_item_feed))) {
                            f = FeedFragment.newInstance(getResources().getString(((Nameable) drawerItem).getNameRes()));
                        }
                        if (toolbar.getTitle().toString().equals(getResources().getString(R.string.drawer_item_explore))) {
                            f = ExploreFragment.newInstance(getResources().getString(((Nameable) drawerItem).getNameRes()));
                        }
                        if (toolbar.getTitle().toString().equals(getResources().getString(R.string.drawer_item_profile))) {
                            f = ProfileFragment.newInstance(getResources().getString(((Nameable) drawerItem).getNameRes()));
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();

                    }
                }
            }
        })
                .build();
    }
}
