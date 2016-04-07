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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;


public class MainActivity extends AppCompatActivity {

    final public int MAP_COUNT = 7; // {-1, 0, 1, 2, 3, 4, 5}
    final public String[] FLOOR_MAP_NAMES =
                                    {"piwnica.jpg",
                                    "parter.jpg",
                                    "pietro1.jpg",
                                    "pietro2.jpg",
                                    "pietro3.jpg",
                                    "pietro4.jpg",
                                    "pietro5.jpg"};
    DrawerLayout mDrawerLayout;

    PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//            this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        mDrawerLayout.setDrawerListener(toggle);
//        toggle.syncState();


//        ImageView mapView = (ImageView) findViewById(R.id.map_view);

//        Drawable bitmap = ContextCompat.getDrawable(getApplicationContext(), R.drawable.weiti_logo);
//        Drawable bitmap = getResources().getDrawable(R.id.weiti_logo);

//        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.temp);
//        Drawable drawable = new BitmapDrawable(bm);
//
//        mapView.setImageDrawable(drawable);

        // Attach a PhotoViewAttacher, which takes care of all of the zooming functionality.
//        mAttacher = new PhotoViewAttacher(mapView);

//
//        drawer.closeDrawer(GravityCompat.START);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        MainFragment fragment;

        for (int i = 0; i < MAP_COUNT; i++) {
            fragment = new MainFragment();
            Bundle bundle = new Bundle();
            bundle.putString("floor_name", FLOOR_MAP_NAMES[i]);
            fragment.setArguments(bundle);
            adapter.addFragment(fragment, Integer.toString(i-1));
            fragment = new MainFragment();
        }

        viewPager.setAdapter(adapter);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        mDrawerLayout.closeDrawers();

//                        if (menuItem.getItemId() == R.id.nav_viewpager) {
                            Intent intent = new Intent(MainActivity.this, TimetableActivity.class);
                            startActivity(intent);
//                        } else if (menuItem.getItemId() == R.id.nav_subsamplingScale) {
//                            Intent intent = new Intent(MainActivity.this, SubsamplingScaleActivity.class);
//                            startActivity(intent);
//                        } else if (menuItem.getItemId() == R.id.nav_gifview) {
//                            Intent intent = new Intent(MainActivity.this, CustomGifViewActivity.class);
//                            startActivity(intent);
//                        } else if (menuItem.getItemId() == R.id.nav_home) {
//                            Intent intent = new Intent(MainActivity.this, GifActivity.class);
//                            startActivity(intent);
//                        }
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
