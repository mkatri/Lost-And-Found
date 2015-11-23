package edu.gatech.cc.lostandfound.mobile.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.api.client.googleapis.extensions.android.gms.auth
        .GoogleAccountCredential;

import edu.gatech.cc.lostandfound.mobile.R;
import edu.gatech.cc.lostandfound.mobile.adapter.ViewPagerAdapter;
import edu.gatech.cc.lostandfound.mobile.network.Api;


public class MainActivity extends AppCompatActivity implements NavigationView
        .OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {
    static final String[] titles = {"Lost", "Found", "My Posts"};
    private static final String AUDIENCE =
            "server:client_id:305182125868-mfi6dkvas2urslsqk63n0b95visda9oe" +
                    ".apps.googleusercontent.com";
    public GoogleAccountCredential credential;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    CoordinatorLayout rootLayout;
    ViewPager viewPager;
    FloatingActionButton fabBtn;
    NavigationView navigation;
    ImageView userImage;
    TextView username;
    TextView emailaddress;
    SearchView searchView;
    MenuItem searchItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initiate toolbar and navigation drawer
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(titles[0]);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this,
                drawerLayout, toolbar, R.string.drawer_open_description, R
                .string.drawer_close_description);
        drawerLayout.setDrawerListener(drawerToggle);

        //initiate user info
        navigation = (NavigationView) findViewById(R.id.navigation);
        navigation.setNavigationItemSelectedListener(this);
        View headerLayout = navigation.inflateHeaderView(R.layout
                .navigation_drawer_header);
        userImage = (ImageView) headerLayout.findViewById(R.id.userImage);
        username = (TextView) headerLayout.findViewById(R.id.username);
        emailaddress = (TextView) headerLayout.findViewById(R.id.emailaddress);
        String userEmailAddress = getSharedPreferences("LostAndFound", 0)
                .getString(Constants.PREF_ACCOUNT_NAME, null);
        emailaddress.setText(userEmailAddress);
        //initiate viewpager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter
                (getSupportFragmentManager(), titles.length);
        viewPager.addOnPageChangeListener(this);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(0);


        //initiate fab
        fabBtn = (FloatingActionButton) findViewById(R.id.fabBtn);
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(this, PinDropActivity.class);
                if (viewPager.getCurrentItem() == 0) {
                    startReportLostActivity();
                } else if (viewPager.getCurrentItem() == 1) {
                    startReportFoundActivity();
                }
            }
        });


        /**
         * For test only
         */
//        Test.loadReportedLostObjects(this);
//        Test.loadReportedFoundObjects(this);

        credential = GoogleAccountCredential
                .usingAudience(this,
                        AUDIENCE);
        credential.setSelectedAccountName(userEmailAddress);

        Api.initialize(credential);
    }

    public void startReportLostActivity() {
        Intent i = new Intent(this, ReportLostActivity.class);
        startActivity(i);
    }

    public void startReportFoundActivity() {
        Intent i = new Intent(this, ReportFoundActivity.class);
        startActivity(i);
    }


    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        Log.i("mylog", "hahhahaha");
//        drawerToggle.onConfigurationChanged(newConfig);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchItem = menu.findItem(R.id.action_search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_search:
                getSupportActionBar().setDisplayShowCustomEnabled(true);
                //enable it to display a
                // custom view in the action bar.
                item.setVisible(false);
                getSupportActionBar().setCustomView(R.layout.search_view);
                //add the custom view
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                //hide the title

                searchView = (SearchView) getSupportActionBar().getCustomView
                        ().findViewById(R.id.searchView); //the text editor


                //this is a listener to do a search when the user clicks on
                // search button
                searchView.setQueryHint("Enter here");
                searchView.setOnQueryTextFocusChangeListener(new View
                        .OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            getSupportActionBar().setDisplayShowCustomEnabled
                                    (false);
                            getSupportActionBar().setDisplayShowTitleEnabled
                                    (true);
                            searchItem.setVisible(true);
                        }
                    }
                });
                searchView.setOnQueryTextListener(new SearchView
                        .OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        Fragment fragment = ((ViewPagerAdapter) viewPager
                                .getAdapter()).getItem(viewPager
                                .getCurrentItem());
                        if (viewPager.getCurrentItem() == 0) {

                        } else {

                        }

                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });
                searchView.setIconified(false);
                searchView.setFocusable(true);
                searchView.requestFocusFromTouch();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onItemSelected(int itemID) {
        switch (itemID) {
            case R.id.menu_item_lost:
                viewPager.setCurrentItem(0);
                break;
            case R.id.menu_item_found:
                viewPager.setCurrentItem(1);
                break;
            case R.id.menu_item_my_posts:
                viewPager.setCurrentItem(2);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        drawerLayout.closeDrawers();
        onItemSelected(item.getItemId());
        return false;
    }

    @Override
    public void onPageSelected(int position) {
        getSupportActionBar().setTitle(titles[position]);
        switch (position) {
            case 0:
            case 1:
                if (searchView != null) {
                    searchView.clearFocus();
                }
                searchItem.setVisible(true);
                fabBtn.setVisibility(View.VISIBLE);
                break;
            case 2:
                if (searchView != null) {
                    searchView.clearFocus();
                }
                searchItem.setVisible(false);
                fabBtn.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int
            positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
