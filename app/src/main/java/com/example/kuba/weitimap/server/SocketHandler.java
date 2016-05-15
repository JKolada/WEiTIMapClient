package com.example.kuba.weitimap.server;

import android.util.Log;
import android.widget.Toast;

import com.example.kuba.weitimap.DownloadActivity;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Kuba on 2016-05-08.
 */
public class SocketHandler implements Runnable {
    private static final String TAG = "SocketHandlerTAG";

    private String ipString;
    private String portString;
    private ClientGetGroupTask connection;
    private DownloadActivity parentActivity;

    public SocketHandler(String ip, String port, ClientGetGroupTask conn, DownloadActivity activity) {
        ipString = ip;
        portString = port;
        connection = conn;
        parentActivity = activity;
    }

    @Override
    public void run() {
        Socket socket = null;
        try {
//          SSLSocketFactory sslFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
//          SSLSocket socket = (SSLSocket) sslFactory.createSocket(ip, Integer.parseInt(port));
            socket = new Socket();
            socket.connect(new InetSocketAddress(ipString, Integer.parseInt(portString)), 1000);
            Log.d(TAG, "Created socket");
        } catch (IOException e) {
            parentActivity.showToast("Connection failed", Toast.LENGTH_SHORT);
            Log.d(TAG, "Socket connection failed or timeout");
            parentActivity.setDownloadButtonEnable(true);
            e.printStackTrace();
        }
//            parentActivity.showToast("Connection succeeded", Toast.LENGTH_SHORT);

        if (socket != null) {
            connection.setSocket(socket);
            Thread clientThread = new Thread(connection);
            clientThread.start();
        }

    }

}
