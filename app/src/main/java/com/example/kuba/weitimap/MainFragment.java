package com.example.kuba.weitimap;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

/**
 * Created by Kuba on 2016-03-31.
 */
public class MainFragment extends Fragment {

    private static String TAG = "MainFragmentTAG";
    private String floor_name;
    private PinView SubImageView;

    private enum imageState {ORIENTATION_90, ORIENTATION_0}
    private imageState state;


//    public void setPin(String pin) {
//        if (SubImageView != null && floor_name == pin) {
//            SubImageView.setPin(new PointF(1718f, 581f));
//        }
//        return;
//    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mainActivity = (MainActivity) getActivity();
        state = imageState.ORIENTATION_0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        floor_name = this.getArguments().getString("floor_name");

        View layout = inflater.inflate(R.layout.fragment_map_list, container, false);
        SubImageView = (PinView) layout.findViewById(R.id.imagePinView);
        SubImageView.setImage(ImageSource.asset(floor_name));
//        SubImageView.setPin(new PointF(1718f, 581f));

//        final FloatingActionButton fab = (FloatingActionButton) layout.findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (state) {
//                    case ORIENTATION_0:
//                        SubImageView.setOrientation(SubsamplingScaleImageView.ORIENTATION_90);
//                        state = imageState.ORIENTATION_90;
//                        fab.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.arrow_horizontal, null));
////                        SubImageView.setPin(new PointF(1718f, 581f));
//                        break;
//                    case ORIENTATION_90:
//                        SubImageView.setOrientation(SubsamplingScaleImageView.ORIENTATION_0);
//                        state = imageState.ORIENTATION_0;
//                        fab.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.arrow_vertical, null));
////                        SubImageView.setPin(new PointF(1718f, 581f));
//                        break;
//                }
//            }
//        });

//        setPin("parter.jpg");

//        SubImageView.setPin(new PointF(1718f, 581f), PinView.pinColor.PIN_RED);

        SubImageView.setPin(new PointF(1018f, 581f), PinView.pinColor.PIN_BLUE);

        return layout;
    }

}
