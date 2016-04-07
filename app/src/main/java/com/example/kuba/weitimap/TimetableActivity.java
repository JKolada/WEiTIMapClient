package com.example.kuba.weitimap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kuba on 2016-04-05.
 */
public class TimetableActivity extends Activity {

    ArrayList<TextView> timetableData = new ArrayList<TextView>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTableLayout();
        setContentView(R.layout.activity_timetable);

//
//        thisView.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        thisView.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        LinearLayout linearLayout = (LinearLayout) vi.inflate(R.layout.activity_timetable, null);

//        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_timetable, null);

//        TableLayout tableLayout = (TableLayout) linearLayout.findViewById(R.id.timetable_layout);

//        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.timetable_layout);
//        TableLayout tableLayout = new TableLayout(getApplicationContext());
        TableLayout tableLayout = (TableLayout) findViewById(R.id.timetable_layout);

        TableLayout.LayoutParams textViewParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT,
                1.0f);


        TableRow singleRow = new TableRow(this);

        singleRow.setLayoutParams(textViewParams);

        TextView singleTextView = new TextView(getApplicationContext());
        singleTextView.setLayoutParams(textViewParams);
        singleTextView.setText("8:15-9:00");
        singleTextView.setPadding(1, 1, 1, 1);
        singleRow.addView(singleTextView);
//
//        for (int i = 0; i < 5; i++) {
//            singleTextView = new TextView(getApplicationContext());
//            singleTextView.setLayoutParams(textViewParams);
//            singleRow.addView(singleTextView);
//        }

        tableLayout.addView(singleRow);

        TextView temp = new TextView(getApplicationContext());
        temp.setText("Hello");
        tableLayout.addView(temp);

        setContentView(R.layout.activity_timetable);
//        linearLayout.addView(tableLayout);

//        linearLayout.inflate()
//        vi.inflate(R.layout.activity_timetable, tableLayout);
//      insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
//        tabLayout.addView(singleRow);
    }

    private void setTableLayout() {
//        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        LinearLayout linearLayout = (LinearLayout) vi.inflate(R.layout.activity_timetable, null);

        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_timetable, null);

//        TableLayout tableLayout = (TableLayout) linearLayout.findViewById(R.id.timetable_layout);

        TableLayout tableLayout = new TableLayout(this);

        TableLayout.LayoutParams textViewParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT,
                1.0f);

        TableRow singleRow = new TableRow(this);

        singleRow.setLayoutParams(textViewParams);

        TextView singleTextView = new TextView(this);
        singleTextView.setLayoutParams(textViewParams);
        singleTextView.setText("8:15-9:00");
        singleTextView.setPadding(1, 1, 1, 1);
        singleRow.addView(singleTextView);

        for (int i = 0; i < 5; i++) {
            singleTextView = new TextView(this);
            singleTextView.setLayoutParams(textViewParams);
            singleRow.addView(singleTextView);
        }

        tableLayout.addView(singleRow);

        TextView temp = new TextView(this);
        temp.setText("Hello");
        tableLayout.addView(temp);

        linearLayout.addView(tableLayout);

//        linearLayout.inflate()
//        vi.inflate(R.layout.activity_timetable, tableLayout);
//      insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
//        tabLayout.addView(singleRow);

    }
}
