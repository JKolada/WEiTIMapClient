package com.example.kuba.weitimap;

public class MyAndUtils {

//    public enum clientResult {GROUP_INSERTED, SOCKET_ERROR, SOCKET_THREAD_ERROR, GROUP_DOESNT_EXIST}

    public final static String DOWNLOAD_NAME = "DOWNLOAD_PAGE";
    public final static String ASUS_VANTAGE_DEFAULT_IP = "192.168.0.172";
    public final static String SERVER_DEFAULT_IP = "192.168.0.42";
    public final static String SERVER_DEFAULT_PORT = "13131";

    public final static String EMAIL_ADDRESS = "jakubkoladadev@gmail.com";
    public final static String GROUP_EXISTS = "GROUP_EXISTS";
    public final static String GROUP_DOESNT_EXIST = "GROUP_DOESNT_EXIST";

    public final static String HANDSHAKE_MSG_TYPE = "HANDSHAKE";
    public final static String GET_GROUP_MSG_TYPE = "GET_GROUP";
    public final static String SEND_GROUP_MSG_TYPE = "SEND_GROUP";

    public final static String MSG_TYPES_REGEXP =   HANDSHAKE_MSG_TYPE + '|' +
                                                    GET_GROUP_MSG_TYPE + '|' +
                                                    SEND_GROUP_MSG_TYPE;

    public final static String[] WEEK_DAYS_IDS = {"mon", "tue", "wed", "thu", "fri"};
    public final static String[] HOURS_IDS = {"_8_9", "_9_10", "_10_11", "_11_12", "_12_13",
                            "_13_14", "_14_15", "_15_16", "_16_17", "_17_18", "_18_19", "_19_20"};

    private final static char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public enum parity {
        EVEN_WEEK, ODD_WEEK
    }

    public final static int MAP_COUNT = 7; // {-1, 0, 1, 2, 3, 4, 5}
    public final static String[] FLOOR_MAP_NAMES =
                    {"piwnica.jpg",
                    "parter.jpg",
                    "pietro1.jpg",
                    "pietro2.jpg",
                    "pietro3.jpg",
                    "pietro4.jpg",
                    "pietro5.jpg"};
}
