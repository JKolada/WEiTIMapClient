package com.example.kuba.weitimap;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * Created by Kuba on 2016-05-02.
 */
public class ClientGroupTask implements Runnable {

    final static String TAG = "ClientTaskTAG";
    private Socket mySocket;
    private String groupName;

    private PrintWriter out;
    private BufferedReader in;

    private ObjectInputStream objIn;

    private DataOutputStream outData;
    private DataInputStream inData;

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
                out = new PrintWriter(mySocket.getOutputStream(), true);
                in = new BufferedReader( new InputStreamReader(mySocket.getInputStream()));

                outData = new DataOutputStream(mySocket.getOutputStream());
                inData = new DataInputStream(mySocket.getInputStream());

                objIn = new ObjectInputStream(mySocket.getInputStream());

//                if (handshake(out, in)) {
//                    getGroupPlan(out, in, objIn);
//                }
//
//                out.print("CLOSE_SOCKET");
                while (true) {
                    Log.d(TAG, receivePrefixedMessage());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        Log.d(TAG, "Communication has ended");
    }

    private void getGroupPlan(PrintWriter out, BufferedReader in, ObjectInputStream objIn) {
        String clientInput;
        String groupRequest = MyAndUtils.GET_GROUP_REQUEST + groupName;
        out.println(groupRequest);
        Log.d(TAG, groupRequest + " - request sent");
        while (true) {
            try {
                clientInput = in.readLine();
                if (clientInput != null) {
                    Log.d(TAG,"received message: " + clientInput);
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    private String receivePrefixedMessage() {
        byte[] prefixBuffer = new byte[4];
        int prefixBytesToRead = 4;
        int prefixBytesRead = 0;

        // prefix reading
        while (prefixBytesToRead > 0) {
            try {
                int n = inData.read(prefixBuffer, prefixBytesRead, prefixBytesToRead);
                if (n == 0) {
                    return null;
                }
                prefixBytesToRead -= n;
                prefixBytesRead += n;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String hexString = MyAndUtils.bytesToHex(prefixBuffer);
        Log.d(TAG, "Hex of message's length: " + hexString);
        // end of prefix reading

        final ByteBuffer b = ByteBuffer.wrap(new String(prefixBuffer).getBytes());
//        b.order(ByteOrder.BIG_ENDIAN);
        int dataLength = b.getInt() - 4;
        Log.d(TAG, "DataLength: " + dataLength);

        // actual message reading
        int dataBytesToRead = dataLength;
        int dataBytesRead = 0;
        // if dataLenght < 0 || > INFINITY ... throw something throwable

        byte[] dataBuffer = new byte[dataLength];
        while (dataBytesToRead > 0) {
            int n;
            try {
                n = inData.read(dataBuffer, dataBytesRead, dataBytesToRead);
                Log.d(TAG, "read bytes number: " + n);
                if (n == 0) return null;
                dataBytesRead += n;
                dataBytesToRead -= n;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        // end of actual message reading

        hexString = MyAndUtils.bytesToHex(dataBuffer);
        Log.d(TAG, "Hex actual message: " + hexString);

        String ret = new String(dataBuffer);
        System.out.println(ret);
        return ret;
    }


}
