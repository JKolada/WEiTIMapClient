package com.example.kuba.weitimap;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.kuba.weitimap.db.GroupPlanObject;
import com.example.kuba.weitimap.db.MyDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    static final int REQUEST_CELL_CLICK = 1;
    private static final String TAG = "MainActivityTAG";

    private DrawerLayout mDrawerLayout;
    private MyDatabase mDB;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
//            ab.setHomeAsUpIndicator(R.drawable.ic_menu); TODO
            ab.setDisplayHomeAsUpEnabled(true);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

         navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
            navigationView.setItemIconTintList(null);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);


        if (viewPager != null) {
            setupViewPager(viewPager);
            if (tabLayout != null) {
                tabLayout.setupWithViewPager(viewPager);
            }
        }

        MyDatabase mDbHelper = MyDatabase.getInstance(getApplicationContext());

        Intent intent = getIntent();
        if (intent == null) {
            Log.d(TAG, "Activity executed without intention.");
        }

        SharedPreferences prefs = this.getSharedPreferences(MyAndUtils.MY_PREFERENCES, 0);
        String clickedCellValue = prefs.getString(MyAndUtils.LAST_CLICKED_CELL_VALUE, "null");
        if (clickedCellValue != "null") {
            setNavigationPins(clickedCellValue);
        }


//        final String clicked_cell_value;
//        if (intent != null) {
//            if (intent.getAction().equals(MyAndUtils.MAIN_BACK_ACTION)) {
//
//
//                Bundle b = intent.getExtras();
//                String clicked_cell_value = b.getString(TimetableActivity.CLICKED_CELL_VALUE);
//                Log.d(TAG, "clicked_cell_value: " + clicked_cell_value);
//                if (clicked_cell_value != "" && clicked_cell_value != null) {
//                    setNavigationPins(clicked_cell_value);
//                }
//
//                ArrayList<String> stringArray = new ArrayList<String>(3);
//                stringArray = b.getStringArrayList(TimetableActivity.CELL_PARAMETERS);
//
//                String par = stringArray.get(0);
//                int row = Integer.parseInt(stringArray.get(1));
//                int col = Integer.parseInt(stringArray.get(2));
//
////TODO
////                switch (row) {
////
////                }
////
////                switch (col) {
////
////
////                }
////
////                switch (par) {
////
////                }
//
//
//
//            }
//        }

   }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CELL_CLICK == requestCode) {
            if (RESULT_OK == resultCode) {
                final String clicked_cell_value  = data.getStringExtra(TimetableActivity.CLICKED_CELL_VALUE);
                Log.d(TAG, "clicked_cell_value: " + clicked_cell_value);
                if (clicked_cell_value != null && clicked_cell_value != "") {
                    setNavigationPins(clicked_cell_value);
                }

            }
//            else {
//                // handle a case where no selection was made
//            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        MainFragment fragment;
        for (int i = 0; i < MyAndUtils.MAP_COUNT; i++) {
            fragment = new MainFragment();
            Bundle bundle = new Bundle();
            bundle.putString("floor_name", MyAndUtils.FLOOR_MAP_NAMES[i]);
            fragment.setArguments(bundle);
            adapter.addFragment(fragment, Integer.toString(i-1));
        }
        viewPager.setAdapter(adapter);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        final MainActivity mainActivity = this;
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        mDrawerLayout.closeDrawers();

                        if (menuItem.getItemId() == R.id.download_icon) {
                            invokeDownload(mainActivity);
                        } else if (menuItem.getItemId() == R.id.plan_icon) {
                            invokeTimetable(mainActivity);
                        }
                        return true;
                    }
                });
    }

    public void invokeTimetable(MainActivity mainAct) {
//        Intent intent = new Intent();
//        intent.setAction(MyAndUtils.TIMETABLE_ACTION);
//        intent.addCategory(MyAndUtils.CATEGORY_DEFAULT);
//        mainAct.startActivity(intent);

        Intent i = new Intent(this, TimetableActivity.class);
        startActivityForResult(i, REQUEST_CELL_CLICK);
    }

    public void invokeDownload(MainActivity mainAct) {
//        Intent intent = new Intent();
//        intent.setAction(MyAndUtils.DOWNLOAD_ACTION);
//        intent.addCategory(MyAndUtils.CATEGORY_DEFAULT);
//        mainAct.startActivity(intent);

        Intent i = new Intent(this, DownloadActivity.class);
        startActivityForResult(i, REQUEST_CELL_CLICK);

    }

    private void setNavigationPins(String text) {

        Pattern p = Pattern.compile("([A-Z]+)[ ]([WLCR])[ ]([0-9A-Z-]+)");
        Matcher m = p.matcher(text);
        boolean b = m.matches();
        if (navigationView != null) {
//            View blue_pin = navigationView.findViewById(R.id.arrow_blue);
            navigationView.getMenu().getItem(1).setTitle(text);

//            navigationView.getMenu().get


        }
    }

    static class FragmentAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mTitles = new ArrayList<>();

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }
    }
}
