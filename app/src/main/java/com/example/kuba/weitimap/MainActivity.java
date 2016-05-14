package com.example.kuba.weitimap;


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

import com.example.kuba.weitimap.db.LectureObj;
import com.example.kuba.weitimap.db.MyDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
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

        mDB = MyDatabase.getInstance(getApplicationContext());

        Intent intent = getIntent();
        if (intent == null) {
            Log.d(TAG, "Activity executed without intention.");
        }

        setRedNavPin();

        SharedPreferences prefs = this.getSharedPreferences(MyAndUtils.MY_PREFERENCES, 0);
        String clickedCellValue = prefs.getString(MyAndUtils.LAST_CLICKED_CELL_VALUE, null);

        if (clickedCellValue == null) return;
        else {
            String par = prefs.getString(MyAndUtils.LAST_CLICKED_CELL_ARRAY + "PAR", null);
            String rowStr = prefs.getString(MyAndUtils.LAST_CLICKED_CELL_ARRAY + "ROW", null);
            String colStr = prefs.getString(MyAndUtils.LAST_CLICKED_CELL_ARRAY + "COL", null);

            if (par == null || rowStr == null || colStr == null) return;
            else {
                int row = Integer.parseInt(rowStr) + 7;
                int col = Integer.parseInt(colStr);

                String dayName;
                dayName = MyAndUtils.WEEK_DAYS_IDS[col-1].substring(0, 1).toUpperCase()
                        + MyAndUtils.WEEK_DAYS_IDS[col-1].substring(1);

                if (clickedCellValue != "null") {
                    setBlueNavPin(dayName + " " + row + ":15 " + clickedCellValue);
                }
            }
        }


   }

    @Override
    public void onResume() {
        super.onResume();
//TODO
        setRedNavPin();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
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
                final ArrayList<String> params = data.getStringArrayListExtra(TimetableActivity.CLICKED_CELL_CELL_ARRAY);

                Log.d(TAG, "clicked_cell_value: " + clicked_cell_value);
                if (clicked_cell_value != null && clicked_cell_value != "") {
                    String par = params.get(0);
                    int row = Integer.parseInt(params.get(1)) + 7;
                    int col = Integer.parseInt(params.get(2));

                    String dayName;
                    dayName = MyAndUtils.WEEK_DAYS_IDS[col].substring(0, 1).toUpperCase()
                        + MyAndUtils.WEEK_DAYS_IDS[col].substring(1, MyAndUtils.WEEK_DAYS_IDS[col].length());

                    setBlueNavPin(dayName + " " + row + ":15 " + clicked_cell_value);
                }
            }
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
                        } else if (menuItem.getItemId() == R.id.arrow_red) {
//                            invokeTimetable(mainActivity);
                        } else if (menuItem.getItemId() == R.id.arrow_blue) {
//                            invokeTimetable(mainActivity);
                        }
                        return true;
                    }
                });
    }

    public void invokeTimetable(MainActivity mainAct) {
        Intent i = new Intent(this, TimetableActivity.class);
        startActivityForResult(i, REQUEST_CELL_CLICK);
    }

    public void invokeDownload(MainActivity mainAct) {
        Intent i = new Intent(this, DownloadActivity.class);
        startActivityForResult(i, REQUEST_CELL_CLICK);
    }

    private void setBlueNavPin(String text) {
        Pattern p = Pattern.compile(MyAndUtils.CELL_TEXT_REGEXP);
        Matcher m = p.matcher(text);
        boolean b = m.matches();
        if (navigationView != null) {
            navigationView.getMenu().getItem(1).setTitle(text);
        }
    }

    private void setRedNavPin() {

        SharedPreferences prefs = this.getSharedPreferences(MyAndUtils.MY_PREFERENCES, 0);
        String groupName = prefs.getString(MyAndUtils.LAST_INSERTED_GROUP_NAME, "null");

        if (groupName == "null") return;

        Calendar c = Calendar.getInstance();

        int month = c.get(Calendar.MONTH);
        int day_of_month = c.get(Calendar.DAY_OF_MONTH);

        char par;
//        if
//                (
//                    (month == Calendar.MAY && ((day_of_month >= 16 && day_of_month <= 20) || (day_of_month >= 30)) )
//                    || (month == Calendar.JUNE && (day_of_month >= 13 && day_of_month <= 14))
//                )
//            par = 'P';
//        else if
//                (
//                    (month == Calendar.MAY && ((day_of_month >= 23 && day_of_month <= 27)))
//                    || (month == Calendar.JUNE && ((day_of_month >= 6 && day_of_month <= 10) || (day_of_month >= 15 && day_of_month <= 16)))
//                )
//            par = 'N';
//        else return;

        int hour_of_day = c.get(Calendar.HOUR_OF_DAY);

        int minute = c.get(Calendar.MINUTE);
        if ((minute > 30 || hour_of_day == 7) && hour_of_day != 19) hour_of_day += 1;

        int day_of_week = c.get(Calendar.DAY_OF_WEEK);

        String day_name;
        switch (day_of_week) {
            case Calendar.MONDAY:
                day_name = "poniedziałek";
                break;
            case Calendar.TUESDAY:
                day_name = "wtorek";
                break;
            case Calendar.WEDNESDAY:
                day_name = "środa";
                break;
            case Calendar.THURSDAY:
                day_name = "czwartek";
                break;
            case Calendar.FRIDAY:
                day_name = "piątek";
                break;
            default:
                day_name = "";
//                return;

        }

//        if (navigationView != null) {
//            if (hour_of_day < 5)
//                navigationView.getMenu().getItem(0).setTitle("It's too early");
//            else if (hour_of_day > 19)
//                navigationView.getMenu().getItem(0).setTitle("It's after classes");
//            return;
//        }

        if (hour_of_day < 8) hour_of_day = 8;

        par = 'N';
        Log.d(TAG, "getLectureObj: " + groupName + " " + hour_of_day + " " + par + " " + day_name); //TODO DELETE
        mDB = MyDatabase.getInstance(getApplicationContext());

        hour_of_day = 8; par = 'P'; day_name = "poniedziałek"; //TODO DELETE

        LectureObj lecture = mDB.getLectureObj(groupName, hour_of_day, par, day_name);


        String[] lectureData = new String[6];
        if (lecture == null) {
            Log.d(TAG, "wrócił null!");
            return;
        }
        else {
            lectureData = lecture.getLectureData();
            Log.d(TAG, lectureData[0] + " " + lectureData[1] + " " + lectureData[2] + " " + lectureData[3] + " " + lectureData[4] + " " + lectureData[5]);
            //{nazwa_sali, nazwa_dnia, id_godziny, parzystość, skrót_nazwy_zajęć, rodz_zajęć};

            if (navigationView != null) {
                navigationView.getMenu().getItem(0).setTitle(lectureData[2] + ":15 " + lectureData[4] + " " + lectureData[5] + " " + lectureData[0]);
            }
        }


//        MainFragment fragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(MyAndUtils.MAIN_FRAGMENT_TAG);
//        fragment.setPin(lectureData[0]);

//        Intent intent = new Intent(this, OnetimeAlarmReceiver.class);
//
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, REQUEST_CODE, intent, 0);
//
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + alarm_time , pendingIntent);
//        System.out.println("Time Total ----- "+(System.currentTimeMillis()+total_mili));
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
