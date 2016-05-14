package com.example.kuba.weitimap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kuba.weitimap.db.GroupPlanObject;
import com.example.kuba.weitimap.db.MyDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Kuba on 2016-04-05.
 */
public class TimetableActivity extends AppCompatActivity {

    public static final String TAG = "TimetableActivityTAG";
    public static final String CLICKED_CELL_VALUE = "TimetableActivity.CLICKED_CELL_VALUE";
    public static final String CLICKED_CELL_CELL_ARRAY = "TimetableActivity.CELL_PARAMETERS";

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
//            invokeMain(this);
            Intent returnIntent = new Intent();
            setResult(RESULT_OK, returnIntent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

//    public void invokeMain(TimetableActivity mainAct) {
//        Intent intent = new Intent();
//        intent.setAction(MyAndUtils.MAIN_BACK_ACTION);
//        intent.addCategory(MyAndUtils.CATEGORY_DEFAULT);
//        mainAct.startActivity(intent);
//    }

    public void returnWithCellClicked(int EditTextId, String text) {

//        ArrayList<String> params = new ArrayList<String>(3);

        ArrayList<String> paramArray = new ArrayList<String>(3);

        char[] parity = {'p', 'n'};
        for (char par : parity) {
            for (int row = 1; row < 13; row++) {
                for (int col = 1; col < 6; col++) {
                    String Rid = "plan_" + par + "_" + row + "x" + col;
                    int resID = getResources().getIdentifier(Rid, "id", this.getPackageName());
                    if (EditTextId == resID) {
                        paramArray.add(Character.toString(par));
                        paramArray.add(Integer.toString(row));
                        paramArray.add(Integer.toString(col));
                        Log.d(TAG, "EditText params: " + Character.toString(par) + " " + Integer.toString(row) + " " + Integer.toString(col));
                    }
                }
            }
        }

        SharedPreferences prefs = this.getSharedPreferences(MyAndUtils.MY_PREFERENCES, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(MyAndUtils.LAST_CLICKED_CELL_VALUE, text);
        editor.putString(MyAndUtils.LAST_CLICKED_CELL_ARRAY + "PAR", paramArray.get(0));
        editor.putString(MyAndUtils.LAST_CLICKED_CELL_ARRAY + "ROW", paramArray.get(1));
        editor.putString(MyAndUtils.LAST_CLICKED_CELL_ARRAY + "COL", paramArray.get(2));
        editor.commit();

        Intent i = new Intent();

        Bundle b = new Bundle();
        b.putString(CLICKED_CELL_VALUE, text);
        b.putStringArrayList(CLICKED_CELL_CELL_ARRAY, paramArray);

        i.putExtras(b);

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
