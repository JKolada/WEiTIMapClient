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
//    private int db_errors_num = 0;

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
        // checkTables(true);
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

//    Boolean isSet() {
//        return isSet;
//    }

    private void setDatabase() {
        for (String i: MyDatabaseUtilities.CREATE_TABLE_STATEMENTS)
            mDB.execSQL(i);

        for (String i: MyDatabaseUtilities.CREATE_VIEW_STATEMENTS)
            mDB.execSQL(i);

        for (String i: MyDatabaseUtilities.INSERT_INTO_STATEMENT_LIST)
            mDB.execSQL(i);
    }

//    private int checkTables (boolean reset) {
//        return checkTables(reset, "ALL");
//    }

//    private int checkTables (boolean reset, String table_name_to_drop) {
//        ResultSet result;
//        int table_num = 0;
//        String table_name_temp;
//        try {
//            result = mConnection.createStatement().executeQuery("SELECT name FROM sqlite_master WHERE type='table' ORDER BY name");
//            while(result.next()) {
//                table_name_temp = result.getString("name");
//                System.out.println(table_name_temp + " exists.");
//                table_num++;
//                if (reset = true) {
//                    if ((table_name_to_drop.equals(table_name_temp)) || (table_name_to_drop.equals("ALL")))
////                        mConnection.createStatement().execute("DROP TABLE " + table_name_temp);
//                    System.out.println(table_name_temp + " has been dropped.");
//                    table_num--;
//                }
//                result.close();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            db_errors_num++ ;
//        }
//
//        if (table_num > 0) {
//            System.out.println("Error: Some tables still exist." );
//        }
//        return table_num;
//
//
//    }

    private void resetDB(SQLiteDatabase db) {
        for(String i: MyDatabaseUtilities.TABLE_NAMES) {
            db.execSQL("DROP TABLE IF EXISTS " + i);
        }

        for(String i: MyDatabaseUtilities.TABLE_NAMES) {
            db.execSQL("DROP VIEW IF EXISTS " + i);
        }
    }

//    public String[] getGroupNames() {
//        String query = "SELECT nazwa_grupy FROM tb_grupy";
//        List<String> nazwy_grup = new ArrayList<String>();
//        try {
//            ResultSet nazwy_grup_rs = mConnection.createStatement().executeQuery(query);
//            while (nazwy_grup_rs.next() ) {
//                nazwy_grup.add(nazwy_grup_rs.getString("nazwa_grupy"));
//            }
//            nazwy_grup_rs.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return nazwy_grup.toArray(new String[nazwy_grup.size()]);
//    }
//
    public GroupPlanObject getGroupPlanObject(String group_name) {
        mDB = getReadableDatabase();
        String query = "SELECT * FROM vw_plan WHERE nazwa_grupy = '" + group_name +"'";
        GroupPlanObject groupObject = null;
    //      List<String> pojedyncze_zajecia = new ArrayList<String>();
        Cursor c = mDB.rawQuery(query, null);

        groupObject = new GroupPlanObject(group_name);
        if (c != null && c.moveToFirst()) {
            do  {
                List<String> pojedyncze_zajecia = new ArrayList<String>();
                for (int k = 1; k < MyDatabaseUtilities.PLAN_VIEW_COL_NAMES.length; k++) {
                    pojedyncze_zajecia.add(c.getString(k));
                    Log.d(TAG, c.getString(k));
                }
                groupObject.add(new LectureObj((ArrayList<String>) pojedyncze_zajecia));
                c.moveToNext();
//                if (c.isClosed()) break;
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

    public String getDownloadedGroupName() {
        return group;
    }

    public void insertGroupPlan(GroupPlanObject groupToInsert) {
//        fillMDB();
        mDB = getWritableDatabase();
        group = groupToInsert.getGroupName();
        Log.d(TAG, group);
        removeGroup(group);
        insertGroup(group);
        List<LectureObj> lectureArray = groupToInsert.getLectureArray();
        for (LectureObj lecture : lectureArray) {
            String[] lecture_data = lecture.getLectureData();
            //{nazwa_sali, nazwa_dnia, id_godziny, parzystość, skrót_nazwy_zajęć, rodz_zajęć}
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

//
//    public void updatePlanCell(int row, int col, GroupPlanObject planObject, char p, String cellValue) {
//
//        if (cellValue == null) {		//
//            String nazwa_grupy = planObject.getGroupName();
//
//            String query = "DELETE FROM tb_plan WHERE 1=1 " +
//                    "AND grupa_id = (SELECT grupa_id FROM tb_grupy WHERE nazwa_grupy = '" + nazwa_grupy +"') "+
//                    "AND dzien_tyg_id  = " + (col) + " " +
//                    "AND godz_id = " + (row+8) + " " +
//                    "AND parzystosc IN ('" + p +"', 'X')";
////            executeQuery(query);
//
//        } else {
//            Pattern pattern = Pattern.compile("([A-Z]+)[ ]([WLCR])[ ]([0-9A-Z-]+)");
//            Matcher m = pattern.matcher(cellValue);
//            m.matches();
//
//            String nazwa_zajec = m.group(1);
//            String rodzaj_zajec = m.group(2);
//            String nazwa_sali = m.group(3);
//
//            String query =
//                    "INSERT INTO tb_plan (grupa_id, dzien_tyg_id, godz_id, id_zajec, rodz_zajec, sala_id, parzystosc) " +
//                            "SELECT a.grupa_id, " + (col) + ", " + (row+8) + ", d.id_zajec, '" + rodzaj_zajec + "', e.sala_id, '" + p + "' " +
//                            "FROM tb_grupy a, tb_zajecia d, tb_sale e " +
//                            "WHERE a.nazwa_grupy = '" + planObject.getGroupName() + "' " +
//                            "AND d.skrot_nazwy_zajec = '" + nazwa_zajec + "' " +
//                            "AND e.nazwa_sali = '" + nazwa_sali +"'";
//
////            executeQuery(query);
//        };
//    }
}





