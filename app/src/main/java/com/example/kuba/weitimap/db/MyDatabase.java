package com.example.kuba.weitimap.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyDatabase extends SQLiteOpenHelper {

    private Boolean isSet = false;
    private int db_errors_num = 0;

    public MyDatabase(Context context) {
        super(context, MyDatabaseUtilities.DATABASE_NAME,
                null, MyDatabaseUtilities.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // try {
        //     Class.forName("org.sqlite.JDBC");
        // } catch (ClassNotFoundException e) {
        //     e.printStackTrace();
        // }

        // try {
        //     mConnection = DriverManager.getConnection("jdbc:sqlite:test.db");
        //     System.out.println("Opened database successfully");
        //     Log.d("db","opened");

        // } catch (SQLException e) {
        //     db_errors_num++ ;
        //     e.printStackTrace();
        //     db_errors_num++ ;
        // }

        // checkTables(true);
        setDatabase(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        resetDB(db);
    }

    Boolean isSet() {
        return isSet;
    }

    private void setDatabase(SQLiteDatabase db) {

        for (String i: MyDatabaseUtilities.CREATE_TABLE_STATEMENTS) {
            db.execSQL(i);
        }

        for (String i: MyDatabaseUtilities.CREATE_VIEW_STATEMENTS) {
            db.execSQL(i);
        }

        for (String i: MyDatabaseUtilities.INSERT_INTO_STATEMENT_LIST) {
            db.execSQL(i);
        }

        // if (db_errors_num == 0 ) {
        //     System.out.println("Database is set properly.\n");
        // } else {
        //     System.out.println("Database has initialization failures. Number of SQLException caught: " + db_errors_num);
        // }

        // try {
        //     if (!mConnection.isClosed())
        //         return 1;
        //     else
        //         return 0;
        // } catch (SQLException e) {
        //     e.printStackTrace();
        //     db_errors_num++ ;
        // }
    }


//    private int checkTables (boolean reset) {
//        return checkTables(reset, "ALL");
//    }
//
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
//    public GroupPlanObject getGroupPlanObject(String group_name) {
//        String query = "SELECT * FROM vw_plan WHERE nazwa_grupy = '" + group_name +"'";
//        GroupPlanObject groupObject = null;
////		List<String> pojedyncze_zajecia = new ArrayList<String>();
//        try {
//            ResultSet zajeciaRS = mConnection.createStatement().executeQuery(query);
//            groupObject = new GroupPlanObject(group_name);
//            if (!zajeciaRS.isClosed()) {
//                while (zajeciaRS.next()) {
//                    List<String> pojedyncze_zajecia = new ArrayList<String>();
//                    for (int k = 2; k <= MyDatabaseUtilities.PLAN_VIEW_COL_NAMES.length; k++) {
//                        pojedyncze_zajecia.add(zajeciaRS.getString(k));
////		    			System.out.println(zajeciaRS.getString(k));  //TO DELETE
//                    }
//                    groupObject.add(new LectureObj((ArrayList<String>) pojedyncze_zajecia));
//                }
//
//            } else {
//                System.out.println("Result set is null"); //TO DELETE
//            }
//            zajeciaRS.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return groupObject;
//    }



    public void addGroup(String groupName) {
        String query = "INSERT INTO tb_grupy (nazwa_grupy) VALUES ('" + groupName + "')";
//        executeQuery(query);
    }

    public void removeGroup(String groupName) {
        String query = "DELETE FROM tb_plan WHERE grupa_id = (SELECT grupa_id FROM tb_grupy WHERE nazwa_grupy = '" + groupName + "')";
//        executeQuery(query);
        query = "DELETE FROM tb_grupy WHERE nazwa_grupy = '" + groupName + "'";
//        executeQuery(query);
    }

    public void updatePlanCell(int row, int col, GroupPlanObject planObject, char p, String cellValue) {

        if (cellValue == null) {		//
            String nazwa_grupy = planObject.getGroupName();

            String query = "DELETE FROM tb_plan WHERE 1=1 " +
                    "AND grupa_id = (SELECT grupa_id FROM tb_grupy WHERE nazwa_grupy = '" + nazwa_grupy +"') "+
                    "AND dzien_tyg_id  = " + (col) + " " +
                    "AND godz_id = " + (row+8) + " " +
                    "AND parzystosc IN ('" + p +"', 'X')";
//            executeQuery(query);

        } else {
            Pattern pattern = Pattern.compile("([A-Z]+)[ ]([WLCR])[ ]([0-9A-Z-]+)");
            Matcher m = pattern.matcher(cellValue);
            m.matches();

            String nazwa_zajec = m.group(1);
            String rodzaj_zajec = m.group(2);
            String nazwa_sali = m.group(3);

            String query =
                    "INSERT INTO tb_plan (grupa_id, dzien_tyg_id, godz_id, id_zajec, rodz_zajec, sala_id, parzystosc) " +
                            "SELECT a.grupa_id, " + (col) + ", " + (row+8) + ", d.id_zajec, '" + rodzaj_zajec + "', e.sala_id, '" + p + "' " +
                            "FROM tb_grupy a, tb_zajecia d, tb_sale e " +
                            "WHERE a.nazwa_grupy = '" + planObject.getGroupName() + "' " +
                            "AND d.skrot_nazwy_zajec = '" + nazwa_zajec + "' " +
                            "AND e.nazwa_sali = '" + nazwa_sali +"'";

//            executeQuery(query);
        };
    }

//	LecturesTableObject getLectureTableObject() { //TODO 
//		String query = "SELECT * FROM VW_LECTURES ORDER BY 1";
//		LecturesTableObject lecturesTable = null;	
//		try {
//	    	ResultSet lecturesRS = mConnection.createStatement().executeQuery(query);
//			lecturesTable = new LecturesTableObject();
//			if (!lecturesRS.isClosed()) {
//			    while (lecturesRS.next()) {	 
//			    	List<String> pojedyncze_zajecia = new ArrayList<String>();	
//		    		for (int k = 0; k < 6; k++) {
//		    			pojedyncze_zajecia.add(lecturesRS.getString(k+1));
////		    			System.out.println(lecturesRS.getString(k+1));  //TO DELETE
//		    		}
//		    		lecturesTable.add(new RoomObj((ArrayList<String>) pojedyncze_zajecia );		    	  	    	
//		    	} 
//		    		
//			} else {
//	    		System.out.println("Result set is null"); //TO DELETE
//	    	}
//		    lecturesRS.close();
//	    } catch (SQLException e) {
//    		e.printStackTrace();
//    	}
//	    return lecturesTable;
//	}

//	KonsulTableObject getKonsulTableObject() { //TODO 
//		String query = "SELECT * FROM VW_KONSUL ORDER BY 1";
//		KonsulTableObject konsulTable = null;	
//		try {
//	    	ResultSet konsulRS = mConnection.createStatement().executeQuery(query);
//			konsulTable = new KonsulTableObject();
//			if (!konsulRS.isClosed()) {
//			    while (konsulRS.next()) {	 
//			    	List<String> pojedyncze_konsul = new ArrayList<String>();	
//		    		for (int k = 0; k < 6; k++) {
//		    			pojedyncze_konsul.add(konsulRS.getString(k+1));
////		    			System.out.println(konsulRS.getString(k+1));  //TO DELETE
//		    		}
//		    		konsulTable.add(new KonsulObj((ArrayList<String>) pojedyncze_konsul );		    	  	    	
//		    	} 
//		    		
//			} else {
//	    		System.out.println("Result set is null"); //TO DELETE
//	    	}
//		    konsulRS.close();
//	    } catch (SQLException e) {
//   		e.printStackTrace();
//   	}
//	    return konsulTable;
//	}

}





