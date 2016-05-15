package com.example.kuba.weitimap.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyDatabase extends SQLiteOpenHelper {


    private final String TAG = "MyDatabaseTAG";
    private static MyDatabase mInstance;
    private static final String LOG = "DatabaseHelperTAG";
    private static String group;

    private volatile SQLiteDatabase mDB;

    public static synchronized MyDatabase getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyDatabase(context.getApplicationContext());
        }
        return mInstance;
    }

    private MyDatabase(Context context) {
        super(context, MyDatabaseUtilities.DATABASE_NAME, null, MyDatabaseUtilities.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        mDB = db;
        setDatabase();

    }

    @Override
    public synchronized void close() {
        if (mInstance != null)
            mDB.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        resetDB(db);
    }


    private void setDatabase() {
        for (String i : MyDatabaseUtilities.CREATE_TABLE_STATEMENTS)
            mDB.execSQL(i);

        for (String i : MyDatabaseUtilities.CREATE_VIEW_STATEMENTS)
            mDB.execSQL(i);

        for (String i : MyDatabaseUtilities.INSERT_INTO_STATEMENT_LIST)
            mDB.execSQL(i);
    }

    private void resetDB(SQLiteDatabase db) {
        for (String i : MyDatabaseUtilities.TABLE_NAMES) {
            db.execSQL("DROP TABLE IF EXISTS " + i);
        }

        for (String i : MyDatabaseUtilities.TABLE_NAMES) {
            db.execSQL("DROP VIEW IF EXISTS " + i);
        }
    }

    public int[] getRoomDetails(String room_name) {
        int[] returnData = new int[3];

        String query = "SELECT pietro_sali, mapa_x, mapa_y FROM tb_sale WHERE nazwa_sali = '" + room_name + "'";
        mDB = getReadableDatabase();
        Cursor c = mDB.rawQuery(query, null);

        if (c != null && c.moveToFirst()) {
            for (int k = 0; k < 3; k++) {
                returnData[k] = c.getInt(k);
            }
//            Log.d(TAG, "getRoomDetails: " + returnData[0] + " " + returnData[1] + " " + returnData[2]);
        } else {
            Log.d(TAG, "Result set is closed");
            mDB.close();
            return null;
        }
        c.close();
        mDB.close();
        return returnData;
    }

    public GroupPlanObject getGroupPlanObject(String group_name) {
        mDB = getReadableDatabase();
        String query = "SELECT * FROM vw_plan WHERE nazwa_grupy = '" + group_name + "'";
        GroupPlanObject groupObject = null;
        Cursor c = mDB.rawQuery(query, null);

        groupObject = new GroupPlanObject(group_name);
//        Log.d(TAG, "getGroupPlanObject: " + group_name);
        if (c != null && c.moveToFirst()) {
            do {
                List<String> pojedyncze_zajecia = new ArrayList<String>();
                for (int k = 1; k < MyDatabaseUtilities.PLAN_VIEW_COL_NAMES.length; k++) {
                    pojedyncze_zajecia.add(c.getString(k));
                }
                groupObject.add(new LectureObj((ArrayList<String>) pojedyncze_zajecia));
//                String[] lecture_data = new LectureObj((ArrayList<String>) pojedyncze_zajecia).getLectureData();
//                Log.d(TAG, "getGroupObject: " + lecture_data[1] + " " + lecture_data[2] + " " + lecture_data[3] + " " + lecture_data[4] + " " + lecture_data[5]);
            } while (c.moveToNext());

        } else {
            Log.d(TAG, "Result set is closed");
            mDB.close();
            return null;
        }
        c.close();
        mDB.close();
        return groupObject;
    }

    public static String getDownloadedGroupName() {
        return group;
    }

    public void insertGroupPlan(GroupPlanObject groupToInsert) {
        mDB = getWritableDatabase();
        group = groupToInsert.getGroupName();
        removeGroup(group);
        insertGroup(group);
        List<LectureObj> lectureArray = groupToInsert.getLectureArray();
        for (LectureObj lecture : lectureArray) {
            String[] lecture_data = lecture.getLectureData();
//            Log.d(TAG, "insertGroupPlan: " + lecture_data[1] + " " + lecture_data[2] + " " + lecture_data[3] + " " + lecture_data[4] + " " + lecture_data[5]);
            String query =
                    "INSERT INTO tb_plan (grupa_id, dzien_tyg_id, godz_id, id_zajec, rodz_zajec, sala_id, parzystosc) " +
                            "SELECT a.grupa_id, b.dzien_tyg_id, " + lecture_data[2] + ", d.id_zajec, '" + lecture_data[5] + "', e.sala_id, '" + lecture_data[3] + "' " +
                            "FROM tb_grupy a, tb_dni_tyg b, tb_zajecia d, tb_sale e " +
                            "WHERE a.nazwa_grupy = '" + group + "' " +
                            "AND d.skrot_nazwy_zajec = '" + lecture_data[4] + "' " +
                            "AND e.nazwa_sali = '" + lecture_data[0] + "' " +
                            "AND b.nazwa_dnia = '" + lecture_data[1] + "'";
            mDB.execSQL(query);
        }
        close();
    }

    private void removeGroup(String groupName) {
        String query = "DELETE FROM tb_plan WHERE grupa_id = (SELECT grupa_id FROM tb_grupy WHERE nazwa_grupy = '" + groupName + "')";
        mDB.execSQL(query);
        query = "DELETE FROM tb_grupy WHERE nazwa_grupy = '" + groupName + "'";
        mDB.execSQL(query);
    }

    private void insertGroup(String group) {
        String query = "INSERT INTO tb_grupy (nazwa_grupy) VALUES ('" + group + "')";
        mDB.execSQL(query);
    }

    public LectureObj getLectureObj(String groupName, int hour_of_day, char par, String day_name) {
        String query =
                "SELECT * FROM vw_plan " +
                        "WHERE nazwa_grupy = '" + groupName + "' " +
                        "AND godz_id >= " + hour_of_day + " " +
                        "AND parzystosc IN ('X', '" + par + "') " +
                        "AND nazwa_dnia = '" + day_name + "' " +
                        "ORDER BY godz_id ASC";

        mDB = getReadableDatabase();
        Cursor c = mDB.rawQuery(query, null);
        LectureObj LectureObjRet = null;

        if (c != null && c.moveToFirst()) {
            List<String> pojedyncze_zajecia = new ArrayList<String>();
            for (int k = 1; k < MyDatabaseUtilities.PLAN_VIEW_COL_NAMES.length; k++) {
                pojedyncze_zajecia.add(c.getString(k));
            }
            LectureObjRet = new LectureObj((ArrayList<String>) pojedyncze_zajecia);
            String[] lecture_data = LectureObjRet.getLectureData();
            Log.d(TAG, "getGroupObject: " + lecture_data[1] + " " + lecture_data[2] + " " + lecture_data[3] + " " + lecture_data[4] + " " + lecture_data[5]);
        } else {
            Log.d(TAG, "Result set is closed");
            mDB.close();
            return null;
        }
        c.close();
        mDB.close();
        return LectureObjRet;
    }
}





