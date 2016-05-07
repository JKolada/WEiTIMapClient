package com.example.kuba.weitimap;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kuba.weitimap.db.GroupPlanObject;
import com.example.kuba.weitimap.db.MyDatabase;

/**
 * Created by Kuba on 2016-04-12.
 */
@SuppressLint("ValidFragment")
public class ScheduleFragment extends Fragment {

    private final String TAG = "ScheduleFragmentTAG";
    private static TimetableActivity mainActivity;
    private MyAndUtils.parity mParity;

    public ScheduleFragment(String parity) {
        super();
        switch (parity) {
            case "even":
                mParity = MyAndUtils.parity.EVEN_WEEK;
            case "odd":
                mParity = MyAndUtils.parity.EVEN_WEEK;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (TimetableActivity) getActivity();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View timetable;

        if (mParity == null) {
            return null;
        } else {
            switch (mParity) {
                case EVEN_WEEK:
                    timetable = inflater.inflate(R.layout.fragment_timetable_even, container, false);
                    break;
                default:
                    timetable = inflater.inflate(R.layout.fragment_timetable_odd, container, false);
                    break;
            }
        }

        MyDatabase mDbHelper = MyDatabase.getInstance(getActivity());
        GroupPlanObject toFillSchedule = mDbHelper.getGroupPlanObject(mDbHelper.getDownloadedGroupName());

        View.OnClickListener mainListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView) v).setText("clicked");
            }
        };

        String Rid_p, Rid_n;
        for (int row = 1; row < 13; row++) {
            for (int col = 1; col < 6; col++) {
                Rid_p = "plan_p_" + row + "x" + col;
                Rid_n = "plan_n_" + row + "x" + col;
                int resID = getResources().getIdentifier(Rid_p, "id", mainActivity.getPackageName());
                TextView scheduleCell = (TextView) timetable.findViewById(resID);
                scheduleCell.setOnClickListener(mainListener);
//                resID = getResources().getIdentifier(Rid_n, "id", mainActivity.getPackageName());
//                scheduleCell = (TextView) timetable.findViewById(resID);
//                scheduleCell.setOnClickListener(mainListener);
            }
        }

//        final TextView scheduleCell = (TextView) timetable.findViewById(R.id.plan_p_1x1);
//        scheduleCell.setOnClickListener(mainListener);

//        plan_p_1x1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                plan_p_1x1.setText("clicked ");
//            }
//        });


        return timetable;
    }



}
