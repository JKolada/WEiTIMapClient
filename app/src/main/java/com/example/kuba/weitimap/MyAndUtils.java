package com.example.kuba.weitimap;

public class MyAndUtils {

    public final static String DOWNLOAD_NAME = "DOWNLOAD_PAGE";
    public final static String ASUS_VANTAGE_DEFAULT_IP = "192.168.0.172";
    public final static String SERVER_DEFAULT_IP = "192.168.0.42";
    public final static String SERVER_DEFAULT_PORT = "13131";
    public final static String GET_GROUP_REQUEST =  "GET_GROUP:_";

    final static String EMAIL_ADDRESS = "<HANDSHAKE/jakubkoladadev@gmail.com>";
    final static char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    final static String GET_GROUP = "<GET_GROUP/";

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
        EVEN_WEEK, ODD_WEEK;
    }
//        public getScheduleResId(parity p, int row, int column
//        {
//
//            String resString = new String();
//            switch (p) {
//            case EVEN_WEEK:
//                resString = "plan_p";
//                break;
//            case ODD_WEEK:
//                resString = "plan_n";
//                break;
//            }
//
//        };



    public final static int MAP_COUNT = 7; // {-1, 0, 1, 2, 3, 4, 5}
    public final static String[] FLOOR_MAP_NAMES =
                    {"piwnica.jpg",
                    "parter.jpg",
                    "pietro1.jpg",
                    "pietro2.jpg",
                    "pietro3.jpg",
                    "pietro4.jpg",
                    "pietro5.jpg"};
//
//    public int getMapCount() {
//        return MAP_COUNT;
//    }
//
//    public int getFloorName(int i){
//        return FLOOR_MAP_NAMES[i];
//    }

}
