package com.example.kuba.weitimap;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
            public void run() {
                Toast.makeText(DownloadActivity.this, toastMessage, toastLength).show();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_layout);

        thisActivity = this;

        TableLayout layout = (TableLayout) findViewById(R.id.download_layout);
        downloadButton = (Button) layout.findViewById(R.id.download_button);
        final EditText ipEditText = (EditText) layout.findViewById(R.id.address_ip);
        final EditText portEditText = (EditText) layout.findViewById(R.id.port_number);
        final EditText groupEditText = (EditText) layout.findViewById(R.id.group_name);

        ipEditText.setText(MyAndUtils.THINKPAD_DEFAULT_IP); //ASUS_VANTAGE_DEFAULT_IP THINKPAD_DEFAULT_IP
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
                    @Override
                    public void handleMessage(Message msg) {
                        String mString = (String) msg.obj;
                        Toast.makeText(getApplicationContext(), mString, Toast.LENGTH_LONG).show();
                    }
                };

                String message = "";
                Pattern p = Pattern.compile(MyAndUtils.IP_REGEXP);
                Matcher m = p.matcher(ip);

                if (!m.matches()) {
                    message = "Invalid ip address";
                }

                p = Pattern.compile(MyAndUtils.PORT_REGEXP);
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
                if (downloadButton != null) {
                    if (a) {
                        downloadButton.setText("Download group plan");
                        downloadButton.setTextColor(Color.BLACK);
                        downloadButton.setEnabled(true);
                        downloadButton.setClickable(true);
                    } else {
                        downloadButton.setText("Downloading group plan");
                        downloadButton.setTextColor(Color.GRAY);
                        downloadButton.setEnabled(false);
                        downloadButton.setClickable(false);
                    }
                }
            }
        });

    }


    public void startTimetableActivity() {

        SharedPreferences prefs = this.getSharedPreferences(MyAndUtils.MY_PREFERENCES, 0);
        String oldGroupName = prefs.getString(MyAndUtils.LAST_INSERTED_GROUP_NAME, "null");
        String newGroupName = MyDatabase.getDownloadedGroupName();

        if (!oldGroupName.equals(newGroupName)) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove(MyAndUtils.LAST_CLICKED_CELL_VALUE);
            editor.putString(MyAndUtils.LAST_INSERTED_GROUP_NAME, MyDatabase.getDownloadedGroupName());
            editor.commit();
        }

        Intent i = new Intent(this, TimetableActivity.class);
        startActivityForResult(i, MainActivity.REQUEST_CELL_CLICK);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (MainActivity.REQUEST_CELL_CLICK == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}