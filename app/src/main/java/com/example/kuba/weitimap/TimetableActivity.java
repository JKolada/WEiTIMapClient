package com.example.kuba.weitimap;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kuba on 2016-04-05.
 */
public class TimetableActivity extends AppCompatActivity {

    ArrayList<TextView> timetableData = new ArrayList<TextView>();

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_timetable);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        ScheduleFragment fragment;


        fragment = new ScheduleFragment("even");
        Bundle bundle = new Bundle();
        bundle.putString("parity", "even");
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, MyAndUtils.parity.EVEN_WEEK);

        fragment = new ScheduleFragment("odd");
        bundle = new Bundle();
        bundle.putString("parity", "odd");
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, MyAndUtils.parity.ODD_WEEK);

        viewPager.setAdapter(adapter);
    }

    static class FragmentAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private MyAndUtils.parity mParity;


        public FragmentAdapter(FragmentManager fm) {
            super(fm);
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

//
//        thisView.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        thisView.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        LinearLayout linearLayout = (LinearLayout) vi.inflate(R.layout.activity_timetable, null);

//        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_timetable, null);

//        TableLayout tableLayout = (TableLayout) linearLayout.findViewById(R.id.timetable_layout);

////        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.timetable_layout);
////        TableLayout tableLayout = new TableLayout(getApplicationContext());
//        TableLayout tableLayout = (TableLayout) findViewById(R.id.timetable_layout);
//
//        TableLayout.LayoutParams textViewParams = new TableLayout.LayoutParams(
//                TableLayout.LayoutParams.MATCH_PARENT,
//                TableLayout.LayoutParams.MATCH_PARENT,
//                1.0f);
//
//
//        TableRow singleRow = new TableRow(this);
//
//        singleRow.setLayoutParams(textViewParams);
//
//        TextView singleTextView = new TextView(getApplicationContext());
//        singleTextView.setLayoutParams(textViewParams);
//        singleTextView.setText("8:15-9:00");
//        singleTextView.setPadding(1, 1, 1, 1);
//        singleRow.addView(singleTextView);
//
//        for (int i = 0; i < 5; i++) {
//            singleTextView = new TextView(getApplicationContext());
//            singleTextView.setLayoutParams(textViewParams);
//            singleRow.addView(singleTextView);
//        }
//
//        tableLayout.addView(singleRow);
//
//        TextView temp = new TextView(getApplicationContext());
//        temp.setText("Hello");
//        tableLayout.addView(temp);
//
//        setContentView(R.layout.activity_timetable);
////        linearLayout.addView(tableLayout);

//        linearLayout.inflate()
//        vi.inflate(R.layout.activity_timetable, tableLayout);
//      insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
//        tabLayout.addView(singleRow);
//    }

//    private void setTableLayout() {
////        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
////        LinearLayout linearLayout = (LinearLayout) vi.inflate(R.layout.activity_timetable, null);
////
////        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_timetable, null);
////
//////        TableLayout tableLayout = (TableLayout) linearLayout.findViewById(R.id.timetable_layout);
////
////        TableLayout tableLayout = new TableLayout(this);
////
////        TableLayout.LayoutParams textViewParams = new TableLayout.LayoutParams(
////                TableLayout.LayoutParams.MATCH_PARENT,
////                TableLayout.LayoutParams.MATCH_PARENT,
////                1.0f);
////
////        TableRow singleRow = new TableRow(this);
////
////        singleRow.setLayoutParams(textViewParams);
////
////        TextView singleTextView = new TextView(this);
////        singleTextView.setLayoutParams(textViewParams);
////        singleTextView.setText("8:15-9:00");
////        singleTextView.setPadding(1, 1, 1, 1);
////        singleRow.addView(singleTextView);
////
////        for (int i = 0; i < 5; i++) {
////            singleTextView = new TextView(this);
////            singleTextView.setLayoutParams(textViewParams);
////            singleRow.addView(singleTextView);
////        }
////
////        tableLayout.addView(singleRow);
////
////        TextView temp = new TextView(this);
////        temp.setText("Hello");
////        tableLayout.addView(temp);
////
////        linearLayout.addView(tableLayout);
//
////        linearLayout.inflate()
////        vi.inflate(R.layout.activity_timetable, tableLayout);
////      insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
////        tabLayout.addView(singleRow);
//
//    }

