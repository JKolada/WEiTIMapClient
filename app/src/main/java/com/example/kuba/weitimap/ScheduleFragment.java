package com.example.kuba.weitimap;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kuba.weitimap.db.GroupPlanObject;
import com.example.kuba.weitimap.db.LectureObj;
import com.example.kuba.weitimap.db.MyDatabase;

import java.util.List;

@SuppressLint("ValidFragment")
public class ScheduleFragment extends Fragment {

    private final String TAG = "ScheduleFragmentTAG";
    private static TimetableActivity mainActivity;
    private MyAndUtils.parity mParity;
    private View timetable;

    public ScheduleFragment(String parity) {
        super();
        switch (parity) {
            case "even":
                mParity = MyAndUtils.parity.EVEN_WEEK;
                break;
            case "odd":
                mParity = MyAndUtils.parity.ODD_WEEK;
                break;
            default:
                Log.e(TAG, "Parity error");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (TimetableActivity) getActivity();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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

        cleanCells();
        String group_name = mDbHelper.getDownloadedGroupName();
        if (group_name != null) {
            GroupPlanObject toFillSchedule = mDbHelper.getGroupPlanObject(group_name);
            fillCells(toFillSchedule);
        };

        View.OnClickListener mainListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.returnWithCellClicked(v.getId(), (String) ((TextView) v).getText());
            }
        };
        plugCellsListener(mainListener);

        return timetable;
    }

    private void plugCellsListener(View.OnClickListener listener) {
        String Rid_base = "plan_" + getParityChar() + "_";
        for (int row = 1; row < 13; row++) {
            for (int col = 1; col < 6; col++) {
                String Rid = Rid_base + row + "x" + col;
                int resID = getResources().getIdentifier(Rid, "id", mainActivity.getPackageName());
                TextView scheduleCell;
                if (((scheduleCell = (TextView) timetable.findViewById(resID)).getText() != "")) {
                    scheduleCell.setOnClickListener(listener);
                }
            }
        }
    }

    private char getParityChar() {
        switch (mParity) {
            case EVEN_WEEK:
                return 'p';
            default:
                return 'n';
        }
    }

    private void cleanCells() {
        char parChar = getParityChar();
        String RidString;

            RidString = parChar + "_timetable_header";
            int resID = getResources().getIdentifier(RidString , "id", mainActivity.getPackageName());
            ((TextView) timetable.findViewById(resID)).setGravity(Gravity.CENTER);

            for (String s: MyAndUtils.WEEK_DAYS_IDS) {
                RidString = parChar + "_" + s;
                resID = getResources().getIdentifier(RidString, "id", mainActivity.getPackageName());
                ((TextView) timetable.findViewById(resID)).setGravity(Gravity.CENTER);
            }

            for (String s: MyAndUtils.HOURS_IDS) {
                RidString = parChar + s;
                resID = getResources().getIdentifier(RidString, "id", mainActivity.getPackageName());
                TextView textView = (TextView) timetable.findViewById(resID);
                textView.setGravity(Gravity.CENTER);
                int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 25, getResources().getDisplayMetrics());
                textView.setHeight(height);
            }

        String Rid_base = "plan_" + parChar + "_";
        for (int row = 1; row < 13; row++) {
            for (int col = 1; col < 6; col++) {
                String Rid = Rid_base + row + "x" + col;
                resID = getResources().getIdentifier(Rid, "id", mainActivity.getPackageName());
                TextView textView = (TextView) timetable.findViewById(resID);
                textView.setText("");
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
                int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics());
                textView.setHeight(height);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                }
            }
        }
    }

    private void fillCells(GroupPlanObject toFillSchedule) {

        char parChar = getParityChar();
        int resID = getResources().getIdentifier(parChar + "_timetable_header" , "id", mainActivity.getPackageName());
        String header;
        switch (mParity) {
            case EVEN_WEEK:
                header = toFillSchedule.getGroupName() + " group plan - even week";
                ((TextView) timetable.findViewById(resID)).setText(header);
                break;
            default:
                header = toFillSchedule.getGroupName() + " group plan - odd week";
                ((TextView) timetable.findViewById(resID)).setText(header);
        }

        List<LectureObj> lectureObjList = toFillSchedule.getLectureArray();

        for (LectureObj a: lectureObjList) {
            String[] data = a.getLectureData();
//            Log.d(TAG, "fillCells: " + data[1] + " " + data[2] + " " + data[3] + " " + data[4] + " " + data[5]);

            parChar = Character.toUpperCase(getParityChar());
            String parString = "" + parChar;

            if (data[3].equals("X")){
                switch (mParity) {
                    case EVEN_WEEK:
                        data[3] = "P";
                        break;
                    default:
                        data[3] = "N";
                        break;
                }
            } else if (!data[3].equals(parString)) continue;
            fillCell(data);
        }
    }

    private void fillCell(String[] data) {
        //{nazwa_sali, nazwa_dnia, id_godziny, parzystość, skrót_nazwy_zajęć, rodz_zajęć}
        // data[0]     data[1]     data[2]     data[3]     data[4]            data[5]

        String rID = "plan_";

        if (data[3].equals("P")) rID += "p_";
        else rID += "n_";

        rID += (Integer.parseInt(data[2]) - 7);

        switch (data[1] /* nazwa_dnia */) {
            case "poniedziałek":
                rID += "x1";
                break;
            case "wtorek":
                rID += "x2";
                break;
            case "środa":
                rID += "x3";
                break;
            case "czwartek":
                rID += "x4";
                break;
            case "piątek":
                rID += "x5";
                break;
        }

        int resID = getResources().getIdentifier(rID, "id", mainActivity.getPackageName());
        TextView scheduleCell = (TextView) timetable.findViewById(resID);
        String temp = data[4] + " " + data[5] + " " + data[0];
        scheduleCell.setText(temp);
    }
}
