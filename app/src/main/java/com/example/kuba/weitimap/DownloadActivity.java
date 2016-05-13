package com.example.kuba.weitimap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.kuba.weitimap.db.MyDatabase;
import com.example.kuba.weitimap.server.ClientGetGroupTask;
import com.example.kuba.weitimap.server.SocketHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Created by Kuba on 2016-04-27.
 */
public class DownloadActivity extends Activity {
    final static String TAG = "DownloadActivityTAG";
    Button downloadButton;

    private DownloadActivity thisActivity;

    public void showToast(String msg, int length) {
        final String toastMessage = msg;
        final int toastLength = length;
        runOnUiThread(new Runnable() {
            public void run()
            {
                Toast.makeText(DownloadActivity.this, toastMessage, toastLength).show();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_layout);

        thisActivity = this;

//        Intent intent = getIntent();
        if (getIntent() == null) {
            Log.w(TAG, "Activity executed without intention");
        }

        TableLayout layout = (TableLayout) findViewById(R.id.download_layout);
        downloadButton = (Button) layout.findViewById(R.id.download_button);
        final EditText ipEditText = (EditText) layout.findViewById(R.id.address_ip);
        final EditText portEditText = (EditText) layout.findViewById(R.id.port_number);
        final EditText groupEditText = (EditText) layout.findViewById(R.id.group_name);

        ipEditText.setText(MyAndUtils.ASUS_VANTAGE_DEFAULT_IP); //SERVER_DEFAULT_IP
        portEditText.setText(MyAndUtils.SERVER_DEFAULT_PORT);
        groupEditText.setText("1E1");

        downloadButton.setOnClickListener(new View.OnClickListener() {
            private String ip;
            private String port;
            private String group;

            private Handler mHandler;

            private void showToast(String text) {
                Message msg = new Message();
                msg.obj = text;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onClick(View v) {

                ip = ipEditText.getText().toString();
                port = portEditText.getText().toString();
                group = groupEditText.getText().toString();

                mHandler = new Handler() {
                    @Override public void handleMessage(Message msg) {
                        String mString=(String)msg.obj;
                        Toast.makeText(getApplicationContext(), mString, Toast.LENGTH_LONG).show();
                    }
                };

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

                if (!message.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    thisActivity.setDownloadButtonEnable(false);

//                  System.setProperty("javax.net.ssl.trustStore", "clienttrust");
                    MyDatabase mDbHelper = MyDatabase.getInstance(getApplicationContext());
                    final ClientGetGroupTask connection = new ClientGetGroupTask(null, mDbHelper, group, thisActivity);
                    final SocketHandler socketHandler = new SocketHandler(ip, port, connection, thisActivity);
                    Thread socketThread = new Thread(socketHandler);
                    socketThread.start();

                }
            }

        });
    }

    public void setDownloadButtonEnable(boolean only_truth) {
        final boolean a = only_truth;
        thisActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (downloadButton!= null) {
                    if (a) {
                        downloadButton.setText("Download group plan");
                        downloadButton.setTextColor(Color.BLACK);
                        downloadButton.setEnabled(a);
                        downloadButton.setClickable(a);
                    } else {
                        downloadButton.setText("Downloading group plan");
                        downloadButton.setTextColor(Color.GRAY);
                        downloadButton.setEnabled(a);
                        downloadButton.setClickable(a);
                    }
                }
            }
        });

    }


    public void startTimetableActivity() {
        Intent intent = new Intent(MyAndUtils.TIMETABLE_ACTION);
        this.startActivity(intent);

    }
}