package com.example.kuba.weitimap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        View timetable_even, timetable_odd;

        if (mParity == null) {
            return null;
        } else {
            switch (mParity) {
                case EVEN_WEEK:
                    timetable_even = inflater.inflate(R.layout.fragment_timetable_even, container, false);
                    return timetable_even;

                case ODD_WEEK:
                     timetable_odd = inflater.inflate(R.layout.fragment_timetable_odd, container, false);
                    return timetable_odd;
            }
        }
        return null;
    }
}
