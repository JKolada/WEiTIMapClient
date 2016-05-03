package com.example.kuba.weitimap;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Kuba on 2016-05-02.
 */
public class ClientGroupTask implements Runnable {

    final static String TAG = "ClientTaskTAG";
    private Socket mySocket;
    private String groupName;

    ClientGroupTask(Socket socket, String groupname) {
        mySocket = socket;
        groupName = groupname;

//        SSLSession session = socket.getSession();
//        try {
//            Certificate[] cchain = session.getPeerCertificates();
//            System.out.println("The Certificates used by peer");
//            for (int i = 0; i < cchain.length; i++) {
//                System.out.println(((X509Certificate) cchain[i]).getSubjectDN());
//            }
//            Log.d(TAG, "Peer Log.d(TAG, host is " + session.getPeerHost());
//            Log.d(TAG, "Cipher is " + session.getCipherSuite());
//            Log.d(TAG, "Protocol is " + session.getProtocol());
//            Log.d(TAG, "ID is " + new BigInteger(session.getId()));
//            Log.d(TAG, "Session created in " + session.getCreationTime());
//            Log.d(TAG, "Session accessed in " + session.getLastAccessedTime());
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void run() {

            try {
                PrintWriter out =
                        new PrintWriter(mySocket.getOutputStream(), true);
                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(mySocket.getInputStream()));
                ObjectInputStream objIn =
                        new ObjectInputStream(mySocket.getInputStream());

                if (handshake(out, in)) {
                    getGroupPlan(out, in, objIn);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        Log.d(TAG, "Communication has ended");
    }

    private void getGroupPlan(PrintWriter out, BufferedReader in, ObjectInputStream objIn) {
    }


    private boolean handshake(PrintWriter out, BufferedReader in) {
        String clientInput;
        try {
            out.println(MyAndUtils.EMAIL_ADDRESS);
//            while (true /*(clientInput = in.readLine()) != null*/) {
                clientInput = in.readLine();
                Log.d(TAG,"received message: " + clientInput);
                if (clientInput != null && clientInput.equals(MyAndUtils.EMAIL_ADDRESS)) {
                    Log.d(TAG, "Handshake succeeded");
                    return true;
//                    break;
                } else {
                    Log.d(TAG, "Got wrong password");
                    return false;
                }
//                wait(500);
//            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//        catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        return false;
    }



}
