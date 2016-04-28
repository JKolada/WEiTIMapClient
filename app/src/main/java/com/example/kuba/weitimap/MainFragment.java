package com.example.kuba.weitimap;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davemorrissey.labs.subscaleview.ImageSource;

/**
 * Created by Kuba on 2016-03-31.
 */
public class MainFragment extends Fragment {

    private static String TAG = "MainFragmentTAG";
    private String floor_name;
//    private static MainActivity mainActivity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        floor_name = this.getArguments().getString("floor_name");

        View layout = inflater.inflate(R.layout.fragment_map_list, container, false);
        PinView SubImageView = (PinView) layout.findViewById(R.id.imagePinView);
        SubImageView.setImage(ImageSource.asset(floor_name));
        SubImageView.setPin(new PointF(1718f, 581f));

        return layout;
    }
}
