package com.example.kuba.weitimap;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import java.io.IOException;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Kuba on 2016-04-27.
 */
public class DownloadActivity extends Activity {
    final static String TAG = "DownloadActivityTAG";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_layout);

        TableLayout layout = (TableLayout) findViewById(R.id.download_layout);
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

                if (message != "") {
                    Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    downloadButton.setText("Download group plan, properly clicked");

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Socket socket = new Socket(ip, Integer.parseInt(port));
                                showToast("Connection succeeded");
                            } catch (IOException e) {
                                showToast("Connection failed");
                                e.printStackTrace();
                            }
                        }
                    }).start();


//            PrintWriter out =
//                    new PrintWriter(socket.getOutputStream(), true);
//            BufferedReader in =
//                    new BufferedReader(
//                            new InputStreamReader(socket.getInputStream()));
//            BufferedReader stdIn =
//                    new BufferedReader(
//                            new InputStreamReader(System.in));

                }

            }
        });


    }
}