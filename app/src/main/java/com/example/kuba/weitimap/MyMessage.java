package com.example.kuba.weitimap;

import android.util.Log;

public class MyMessage {
    private static final String TAG = "MessageTAG";

    public enum MessageType {HANDSHAKE, GET_GROUP, SEND_GROUP}

    ;
    private MessageType MsgType;
    private String param;
    private String allMessage;
    private boolean isValid;

    public MessageType getMsgType() {
        return MsgType;
    }

    public MyMessage(MessageType msgtyp) {
        MsgType = msgtyp;
        isValid = false;
        configure();
    }

    public MyMessage(MessageType msgtyp, String par) {
        MsgType = msgtyp;
        isValid = false;
        configure();
        setParam(par);
    }

    public MyMessage(String msgtyp, String par) { // constructor with
        isValid = false;
        if (msgtyp.equals(MyAndUtils.HANDSHAKE_MSG_TYPE)) {
            MsgType = MessageType.HANDSHAKE;
            if (par.equals(MyAndUtils.EMAIL_ADDRESS)) {
                // isValid = true;
                configure();
                printStatus();
                return;
            } else {
                printStatus();
            }
        } else if (msgtyp.equals(MyAndUtils.GET_GROUP_MSG_TYPE))
            MsgType = MessageType.GET_GROUP;
        else if (msgtyp.equals(MyAndUtils.SEND_GROUP_MSG_TYPE))
            MsgType = MessageType.SEND_GROUP;
        else return;

        configure();
        setParam(par);
        printStatus();
    }

    public boolean setParam(String par) {
        if (isValid == true) {
            Log.d(TAG, "Parameter of the message is already set and message is valid");
            return false;
        }
        param = par;
        if (MsgType == MessageType.HANDSHAKE) return isValid;
        allMessage += param + ">";
        isValid = true;
        return isValid;
    }

    public String getParam() {
        if (isValid)
            return param;
        else
            return null;
    }

    public MessageType getType() {
        if (isValid)
            return MsgType;
        else
            return null;
    }

    public String toString() {
        if (isValid)
            return allMessage;
        else
            return null;
    }

    public boolean isValid() {
        return isValid;
    }

    private void printStatus() {
        if (isValid == true)
            Log.d(TAG, "Message \"" + toString() + "\" is valid.");
        else
            Log.d(TAG, "Message \"" + toString() + "\" is INVALID.");
    }

    private void configure() {
        allMessage = "<";
        switch (MsgType) {
            case HANDSHAKE:
                allMessage += MyAndUtils.HANDSHAKE_MSG_TYPE + '/' + MyAndUtils.EMAIL_ADDRESS + '>';
                isValid = true;
                break;
            case GET_GROUP:
                allMessage += MyAndUtils.GET_GROUP_MSG_TYPE + '/';
                break;
            case SEND_GROUP:
                allMessage += MyAndUtils.SEND_GROUP_MSG_TYPE + '/';
                break;
            default:
                break;
        }
    }
}