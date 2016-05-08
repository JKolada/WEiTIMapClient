package com.example.kuba.weitimap;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kuba.weitimap.db.MyDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kuba on 2016-04-05.
 */
public class TimetableActivity extends AppCompatActivity {
    public static final String CLICKED_CELL_VALUE = "com.example.kuba.weitimap.TimetableActivity.CLICKED_CELL_VALUE";
    ArrayList<TextView> timetableData = new ArrayList<TextView>();
    FragmentAdapter mFragmentAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_timetable);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_timetable);

        if (viewPager != null) {
            setupViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void returnWithCellClicked(String text) {
        Intent i = new Intent();
        i.putExtra(CLICKED_CELL_VALUE, text);
        setResult(RESULT_OK, i);
        finish();
    }

    private void setupViewPager(ViewPager viewPager) {

        MyDatabase mDbHelper = MyDatabase.getInstance(getApplicationContext());

        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), mDbHelper);
        ScheduleFragment fragment;

        fragment = new ScheduleFragment("even");
        Bundle bundle = new Bundle();
        bundle.putString("parity", "even");
        fragment.setArguments(bundle);
        mFragmentAdapter.addFragment(fragment, MyAndUtils.parity.EVEN_WEEK);

        fragment = new ScheduleFragment("odd");
        bundle = new Bundle();
        bundle.putString("parity", "odd");
        fragment.setArguments(bundle);
        mFragmentAdapter.addFragment(fragment, MyAndUtils.parity.ODD_WEEK);

        viewPager.setAdapter(mFragmentAdapter);
    }


    static class FragmentAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private MyAndUtils.parity mParity;
        private MyDatabase mDB;

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        public FragmentAdapter(FragmentManager fm, MyDatabase DB) {
            super(fm);
            mDB = DB;
        }

        public void addFragment(Fragment fragment, MyAndUtils.parity p) {
            mFragments.add(fragment);
            mParity = p;
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
            switch (position) {
                case 0:
                    return "even week";
                case 1:
                    return "odd week";
            }
            return null;
        }

    }
}
