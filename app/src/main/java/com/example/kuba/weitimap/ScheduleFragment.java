package com.example.kuba.weitimap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Kuba on 2016-04-12.
 */
public class ScheduleFragment extends Fragment {

//    private static MainActivity mainActivity;
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
//        mainActivity = (MainActivity) getActivity();

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

        final TextView plan_p_1x1 = (TextView) timetable.findViewById(R.id.plan_p_1x1);
        plan_p_1x1.setOnClickListener(new View.OnClickListener() {
            int clicked = 0;
            @Override
            public void onClick(View v) {
                clicked++;
                plan_p_1x1.setText("clicked " + clicked + " times");
            }
        });


        return timetable;
    }


}
