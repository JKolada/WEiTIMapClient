package com.example.kuba.weitimap;

public class MyAndUtils {
    public final static String DOWNLOAD_NAME = "DOWNLOAD_PAGE";

    public enum parity {
        EVEN_WEEK, ODD_WEEK;
    }

    public final static String SERVER_DEFAULT_IP = "192.168.0.42";
    public final static String SERVER_DEFAULT_PORT = "13131";


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
