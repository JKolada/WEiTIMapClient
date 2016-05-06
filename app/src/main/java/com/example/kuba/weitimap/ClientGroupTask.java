package com.example.kuba.weitimap;

import android.util.Log;

import com.example.kuba.weitimap.db.GroupPlanObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Kuba on 2016-05-02.
 */
public class ClientGroupTask implements Runnable {

    private final static String TAG = "ClientTaskTAG";
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
            outData = new DataOutputStream(mySocket.getOutputStream());
            inData = new DataInputStream(mySocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendMessage(new MyMessage(MyMessage.MessageType.HANDSHAKE));

        MyMessage handshakeMsg;
        while ((handshakeMsg = receiveMessageObj()) == null) {}

        if (handshakeMsg.getType() == MyMessage.MessageType.HANDSHAKE) {
            sendMessage(new MyMessage(MyMessage.MessageType.GET_GROUP, groupName));

            MyMessage doesGroupExists;
            while ((doesGroupExists = receiveMessageObj()) == null) {}

            switch (doesGroupExists.getParam()) {
                case MyAndUtils.GROUP_DOESNT_EXIST:
                    Log.d(TAG, "Group doesnt exist.");
                    shutdown();
                    break;
                case MyAndUtils.GROUP_EXISTS:
                    break;
                default:
                    Log.d(TAG, "Unknown GET_GROUP message.");
                    shutdown();
            }

            try {
                objIn = new ObjectInputStream(mySocket.getInputStream());
                Log.d(TAG, "made an object stream");
                GroupPlanObject gotGroup;
                while ((gotGroup = (GroupPlanObject) objIn.readObject()) == null) {}

//                while ((gotGroup = (GroupPlanObject) objIn.readObject()) != null) {
//                    Log.d(TAG, "Got GroupPlanObject for " + gotGroup.getGroupName() + " group.");
//                    break;
//                }
                Log.d(TAG, "Got GroupPlanObject for " + gotGroup.getGroupName() + " group.");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    objIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Log.d(TAG, "Communication has ended");
    }

    private void sendMessage(MyMessage msg) {
        sendMessageBytes(msg.toString());
    }

    private void sendMessageBytes(String message){
        byte[] msgBytes = message.getBytes();
        String hexString = MyAndUtils.bytesToHex(msgBytes);
        Log.d(TAG, "Hex of actual message:" + hexString);

        int msgLength = 4 + msgBytes.length;

        byte[] msgLengthBytes = ByteBuffer.allocate(4).putInt(msgLength).array();
        hexString = MyAndUtils.bytesToHex(msgLengthBytes);
        Log.d(TAG, "Hex of string length:" + hexString);

        byte[] combined = new byte[msgLengthBytes.length + msgBytes.length];
        System.arraycopy(msgLengthBytes, 0, combined, 0                    , msgLengthBytes.length);
        System.arraycopy(msgBytes,       0, combined, msgLengthBytes.length, msgBytes.length);

        hexString = MyAndUtils.bytesToHex(combined);
        Log.d(TAG, "Hex of overall message:" + hexString);

        try {
            outData.write(combined);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return;
    }

    private MyMessage receiveMessageObj() {
        String msgString = receivePrefixedMessage();
        if (msgString == null) return null;
        String regexp = "<(" + MyAndUtils.MSG_TYPES_REGEXP + ")/([\\S]+)>";
        Pattern pattern = Pattern.compile(regexp);
        Matcher m = pattern.matcher(msgString);
        boolean b = m.matches();
        if (!b) {
            Log.d(TAG, "Wrong message received");
            shutdown();
        }
        String msg_type = m.group(1);
        String msg_param = m.group(2);

        return new MyMessage(msg_type, msg_param);
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

    private void shutdown() {
        try {
            outData.close();
            inData.close();
            mySocket.close();
            System.out.println("Connection ended");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
