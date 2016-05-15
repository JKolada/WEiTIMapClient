package com.example.kuba.weitimap;

import android.content.SharedPreferences;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.example.kuba.weitimap.db.MyDatabase;

import static com.example.kuba.weitimap.MyAndUtils.BLUE_PIN_ROOM;
import static com.example.kuba.weitimap.MyAndUtils.MY_PREFERENCES;
import static com.example.kuba.weitimap.MyAndUtils.RED_PIN_ROOM;

/**
 * Created by Kuba on 2016-03-31.
 */
public class MainFragment extends Fragment {

    private static String TAG = "MainFragmentTAG";
    private String floor_name;
    private PinView SubImageView;
    private MainActivity mainActivity;

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
        mainActivity = (MainActivity) getActivity();
        state = imageState.ORIENTATION_0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        floor_name = this.getArguments().getString("floor_name");

        View layout = inflater.inflate(R.layout.fragment_map_list, container, false);
        SubImageView = (PinView) layout.findViewById(R.id.imagePinView);
        SubImageView.setImage(ImageSource.asset(floor_name));

//        final FloatingActionButton fab = (FloatingActionButton) layout.findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (state) {
//                    case ORIENTATION_0:
//                        SubImageView.resetPins();
//                        SubImageView.setOrientation(SubsamplingScaleImageView.ORIENTATION_90);
//                        state = imageState.ORIENTATION_90;
//                        fab.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.arrow_horizontal, null));
//                        break;
//                    case ORIENTATION_90:
//                        SubImageView.resetPins();
//                        SubImageView.setOrientation(SubsamplingScaleImageView.ORIENTATION_0);
//                        state = imageState.ORIENTATION_0;
//                        fab.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.arrow_vertical, null));
////
//                        break;
//                }
//            }
//        });


        setPins();

//        SubImageView.setPin(new PointF(1718f, 581f), PinView.pinColor.PIN_RED);
//
//        SubImageView.setPin(new PointF(1018f, 581f), PinView.pinColor.PIN_BLUE);

        return layout;
    }

    private void setPins() {

        int room_floor = 0;

        if (floor_name.equals(MyAndUtils.FLOOR_MAP_NAMES[0])) {
            room_floor = -1;
        } else if (floor_name.equals(MyAndUtils.FLOOR_MAP_NAMES[1])) {
            room_floor = 0;
        } else if (floor_name.equals(MyAndUtils.FLOOR_MAP_NAMES[2])) {
            room_floor = 1;
        } else if (floor_name.equals(MyAndUtils.FLOOR_MAP_NAMES[3])) {
            room_floor = 2;
        } else if (floor_name.equals(MyAndUtils.FLOOR_MAP_NAMES[4])) {
            room_floor = 3;
        } else if (floor_name.equals(MyAndUtils.FLOOR_MAP_NAMES[5])) {
            room_floor = 4;
        } else if (floor_name.equals(MyAndUtils.FLOOR_MAP_NAMES[6])) {
            room_floor = 5;
        }

        MyDatabase mDB = MyDatabase.getInstance(mainActivity);
        SharedPreferences prefs = mainActivity.getSharedPreferences(MY_PREFERENCES, 0);
        String redPinRoom = prefs.getString(RED_PIN_ROOM, null);

        if (redPinRoom != null) {
            int[] roomDetails = new int[3];
            roomDetails = mDB.getRoomDetails(redPinRoom);
            if (roomDetails[0] == room_floor) {
                SubImageView.setPin(new PointF(roomDetails[1], roomDetails[2]), PinView.pinColor.PIN_RED);
            }
        }

        String bluePinRoom = prefs.getString(BLUE_PIN_ROOM, null);

        if (bluePinRoom != null) {
            int[] roomDetails = new int[3];
            roomDetails = mDB.getRoomDetails(bluePinRoom);
            if (roomDetails[0] == room_floor) {
                SubImageView.setPin(new PointF(roomDetails[1], roomDetails[2]), PinView.pinColor.PIN_BLUE);
            }
        }
    }

}
