package com.example.kuba.weitimap;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Toast;

import com.example.kuba.weitimap.db.MyDatabase;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivityTAG";

    DrawerLayout mDrawerLayout;
    MyDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);


        if (viewPager != null) {
            setupViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
        }

        MyDatabase mDbHelper = new MyDatabase(this);
//        mDbHelper.

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FragmentManager fm = getSupportFragmentManager();
//
//                MainFragment fragment = (MainFragment)fm.findFragmentById(R.id.);
//                fragment.yourPublicMethod();
//            }
//        });

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

    public void getGroupPlan(String ip, int port, String groupname) {
//        Toast toast = Toast.makeText(getBaseContext(), "ip = " + ip + ", port = " + port + ", group name = " + groupname , Toast.LENGTH_LONG);
//        toast.show();
        Socket socket = null;
        try {
            Log.d(TAG, "Socket connecting trial");
            socket = new Socket(ip, port);
            Log.d(TAG, "Socket connected");
//            PrintWriter out =
//                    new PrintWriter(socket.getOutputStream(), true);
//            BufferedReader in =
//                    new BufferedReader(
//                            new InputStreamReader(socket.getInputStream()));
//            BufferedReader stdIn =
//                    new BufferedReader(
//                            new InputStreamReader(System.in));
        } catch (IOException e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(getBaseContext(), "Connection failed" , Toast.LENGTH_LONG);
            toast.show();
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
//
//        fragment = new MainFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("floor_name", MyAndUtils.DOWNLOAD_NAME);
//        fragment.setArguments(bundle);
//        adapter.addFragment(fragment, "D");

        viewPager.setAdapter(adapter);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        mDrawerLayout.closeDrawers();

                        if (menuItem.getItemId() == R.id.download_icon) {
                           Intent intent = new Intent(MainActivity.this, DownloadActivity.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.plan_icon) {
                            Intent intent = new Intent(MainActivity.this, TimetableActivity.class);
                            startActivity(intent);
                        }
//                      } else if (menuItem.getItemId() == R.id.nav_subsamplingScale) {
//                            Intent intent = new Intent(MainActivity.this, SubsamplingScaleActivity.class);
//                            startActivity(intent);
//                        } else if (menuItem.getItemId() == R.id.nav_gifview) {
//                            Intent intent = new Intent(MainActivity.this, CustomGifViewActivity.class);
//                            startActivity(intent);
//                        } else if (menuItem.getItemId() == R.id.nav_home) {
//                            Intent intent = new Intent(MainActivity.this, GifActivity.class);
//                            startActivity(intent);

                        return true;
                    }
                });
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
