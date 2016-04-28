package com.example.kuba.weitimap;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Kuba on 2016-03-31.
 */
public class MainFragment extends Fragment {

    private static String TAG = "MainFragmentTAG";
    private String floor_name;
    private static MainActivity mainActivity;
//    private SubsamplingScaleImageView SubImageView;
    private FrameLayout frameLayout;

//    SubsamplingScaleImageView SubImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        floor_name = this.getArguments().getString("floor_name");

        View layout;

        if  (floor_name == MyAndUtils.DOWNLOAD_NAME) {
            layout = inflater.inflate(R.layout.download_layout, container, false);
            final Button downloadButton = (Button) layout.findViewById(R.id.download_button);
            final EditText ipEditText = (EditText) layout.findViewById(R.id.address_ip);
            final EditText portEditText = (EditText) layout.findViewById(R.id.port_number);
            final EditText groupEditText = (EditText) layout.findViewById(R.id.group_name);

            ipEditText.setText(MyAndUtils.SERVER_DEFAULT_IP);
            portEditText.setText(MyAndUtils.SERVER_DEFAULT_PORT);
            groupEditText.setText("1E1");

//            downloadButton.setOnClickListener(new downloadBtnListener(ipEditText, portEditText, groupEditText));

            downloadButton.setOnClickListener(new View.OnClickListener() {
                private String ip;
                private String port;
                private String group;

                @Override
                public void onClick(View v) {
                    ip = ipEditText.getText().toString();
                    port = portEditText.getText().toString();
                    group = groupEditText.getText().toString();

                    String message = "";
                    Pattern p = Pattern.compile("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$");
                    Matcher m = p.matcher(ip);

                    if (!m.matches()) {
                        message = "Invalid ip address";
                    }

                    p = Pattern.compile("^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$");
                    m = p.matcher(port);
                    if (!m.matches()) {
                        if (message == "") {
                            message = "Invalid port number";
                        } else {
                            message = message + " and port number";
                        }
                    }

                    if (message != "") {
                        Toast toast = Toast.makeText(getActivity().getBaseContext(), message, Toast.LENGTH_LONG);
                        toast.show();
                    } else {
                        downloadButton.setText("Download group plan, properly clicked");
                        Log.d(TAG, "((MainActivity) getActivity()).getGroupPlan(ip, Integer.parseInt(port), group)");
                        ((MainActivity) getActivity()).getGroupPlan(ip, Integer.parseInt(port), group);
                    }

                }
            });

        } else {
            layout = inflater.inflate(R.layout.fragment_map_list, container, false);
            PinView SubImageView = (PinView) layout.findViewById(R.id.imagePinView);
            SubImageView.setImage(ImageSource.asset(floor_name));
            SubImageView.setPin(new PointF(1718f, 581f));
        }
        return layout;
    }
    
//    class downloadBtnListener implements View.OnClickListener {
//            private EditText ipEditText;
//            private EditText portEditText;
//            private EditText groupEditText;
//
//            private String ip;
//            private String port;
//            private String group;
//
//            public void downloadBtnListener(EditText ipText, EditText portText, EditText groupText) {
//                ipEditText = ipText;
//                portEditText = portText;
//                groupEditText = groupText;
//            }
//
//            @Override
//            public void onClick(View v) {
//                ip = ipEditText.getText().toString();
//                port = portEditText.getText().toString();
//                group = groupEditText.getText().toString();
//
//                String message = "";
//                Pattern p = Pattern.compile("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$");
//                Matcher m = p.matcher(ip);
//
//                if (!m.matches()) {
//                    message = "Invalid ip address";
//                }
//
//                p = Pattern.compile("^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$");
//                m = p.matcher(port);
//                if (!m.matches()) {
//                    if (message == "") {
//                        message = "Invalid port number";
//                    } else {
//                        message = message + " and port number";
//                    }
//                }
//
//                if (message != "") {
//                    Toast toast = Toast.makeText(getActivity().getBaseContext(), message, Toast.LENGTH_LONG);
//                    toast.show();
//                } else {
//                    downloadButton.setText("Download group plan, properly clicked");
//                    ((MainActivity)getActivity()).getGroupPlan(ip, Integer.parseInt(port), group);
//                }
//
//            }
//        }
//    }
    


}
