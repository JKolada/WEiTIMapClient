package com.example.kuba.weitimap.db;

public final class MyDatabaseUtilities {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PlanDatabase.db";

    // 'CREATE TABLES' STATEMENTS // // // // // // // //

    private final static String CREATE_TB_PRACOWNICY =
            "CREATE TABLE IF NOT EXISTS tb_pracownicy (" +
                    "	pracownik_id INTEGER PRIMARY KEY," +
                    "	imie TEXT NOT NULL," +
                    "	nazwisko TEXT NOT NULL" +
                    ")";

    private final static String CREATE_TB_GRUPY =
            "CREATE TABLE IF NOT EXISTS tb_grupy (" +
                    "	grupa_id INTEGER PRIMARY KEY," +
                    "	nazwa_grupy TEXT NOT NULL UNIQUE" +
                    ")";

    private final static String CREATE_TB_DNI_TYG =
            "CREATE TABLE IF NOT EXISTS tb_dni_tyg (" +
                    "	dzien_tyg_id INTEGER PRIMARY KEY," +
                    "	nazwa_dnia TEXT NOT NULL UNIQUE" +
                    ")";

    private final static String CREATE_TB_GODZINY =
            "CREATE TABLE IF NOT EXISTS tb_godziny (" +
                    "	godz_id INTEGER PRIMARY KEY," +
                    "	godziny TEXT NOT NULL" +
                    ")";

    private final static String CREATE_TB_SALE =
            "CREATE TABLE IF NOT EXISTS tb_sale (" +
                    "	sala_id INTEGER PRIMARY KEY," +
                    "	nazwa_sali TEXT NOT NULL UNIQUE, " +
                    "	pietro_sali INTEGER NOT NULL," +
                    "	mapa_x INTEGER NOT NULL," +
                    "	mapa_y INTEGER NOT NULL" +
                    ")";

    private final static String CREATE_TB_ZAJECIA =
            "CREATE TABLE IF NOT EXISTS tb_zajecia (" +
                    "	id_zajec INTEGER PRIMARY KEY," +
                    "	skrot_nazwy_zajec TEXT NOT NULL," +
                    "	nazwa_zajec TEXT NOT NULL," +
                    "	id_wykladowcy TEXT," +
                    "	FOREIGN KEY (id_wykladowcy) REFERENCES tb_pracownicy(pracownik_id)" +
                    ")";

    private final static String CREATE_TB_PLAN =
            "CREATE TABLE IF NOT EXISTS tb_plan (" +
                    "	grupa_id INTEGER NOT NULL,"  +
                    "	dzien_tyg_id INTEGER NOT NULL,"  +
                    "	godz_id INTEGER NOT NULL,"  +
                    "	id_zajec INTEGER NOT NULL,"  +
                    "	rodz_zajec VARCHAR(1) NOT NULL DEFAULT 'X',"  +
                    "	sala_id INTEGER NOT NULL," +
                    "	parzystosc VARCHAR(1) NOT NULL DEFAULT 'X',"  +
                    "	CHECK (parzystosc = 'P' OR parzystosc = 'N' OR parzystosc = 'X'),"  +
                    "	CHECK (rodz_zajec IN ('W', 'L', 'C', 'R')),"  +
                    "	FOREIGN KEY (grupa_id) REFERENCES tb_grupy(grupa_id),"  +
                    "	FOREIGN KEY (sala_id) REFERENCES tb_sale(sala_id)," +
                    "	FOREIGN KEY (dzien_tyg_id) REFERENCES tb_dni_tyg(dzien_tyg_id),"  +
                    "	FOREIGN KEY (godz_id) REFERENCES tb_godziny(godz_id),"  +
                    "	FOREIGN KEY (id_zajec) REFERENCES tb_zajecia(id_zajec)"  +
                    ")";

    private final static String CREATE_TB_PLAN_KONSUL =
            "CREATE TABLE IF NOT EXISTS tb_plan_konsul (" +
                    "	dzien_tyg_id INTEGER NOT NULL," +
                    "	sala_id INTEGER NOT NULL," +
                    "	godz_id INTEGER NOT NULL," +
                    "	pracownik_id INTEGER NOT NULL," +
                    "	FOREIGN KEY (dzien_tyg_id) REFERENCES tb_dni_tyg(dzien_tyg_id)," +
                    "	FOREIGN KEY (sala_id) REFERENCES tb_sale(sala_id)," +
                    "	FOREIGN KEY (godz_id) REFERENCES tb_godziny(godz_id)," +
                    "	FOREIGN KEY (pracownik_id) REFERENCES tb_pracownicy(pracownik_id)" +
                    ")";

    // VIEWS // // // // // // // // // // // // // // // // // // // //  // // //

    public final static String CREATE_PLAN_VIEW =
            "CREATE VIEW IF NOT EXISTS VW_PLAN " +
                    "AS SELECT b.nazwa_grupy, c.nazwa_sali, d.nazwa_dnia, e.godz_id, a.parzystosc, f.skrot_nazwy_zajec, a.rodz_zajec " +
                    "FROM tb_plan a " +
                    "LEFT JOIN tb_grupy b 	ON (a.grupa_id = b.grupa_id) " +
                    "LEFT JOIN tb_sale c  	ON (a.sala_id = c.sala_id) " +
                    "LEFT JOIN tb_dni_tyg d  ON (a.dzien_tyg_id = d.dzien_tyg_id) " +
                    "LEFT JOIN tb_godziny e  ON (a.godz_id = e.godz_id) " +
                    "LEFT JOIN tb_zajecia f  ON (a.id_zajec = f.id_zajec)";

    public final static String CREATE_LECTURES_VIEW =
            "CREATE VIEW IF NOT EXISTS VW_LECTURES " +
                    "AS SELECT a.id_zajec, a.skrot_nazwy_zajec, a.nazwa_zajec, a.id_wykladowcy, b.imie, b.nazwisko " +
                    "FROM tb_zajecia a " +
                    "LEFT JOIN tb_pracownicy b " +
                    "ON (a.id_wykladowcy = b.pracownik_id)";

    public final static String CREATE_KONSULTACJE_VIEW =
            "CREATE VIEW IF NOT EXISTS VW_KONSUL " +
                    "AS SELECT b.imie, b.nazwisko, c.nazwa_dnia, d.godziny, e.nazwa_sali " +
                    "FROM tb_plan_konsul a " +
                    "LEFT JOIN tb_pracownicy b ON (a.pracownik_id = b.pracownik_id) " +
                    "LEFT JOIN tb_dni_tyg c ON (a.dzien_tyg_id = c.dzien_tyg_id) " +
                    "LEFT JOIN tb_godziny d ON (a.godz_id = d.godz_id) " +
                    "LEFT JOIN tb_sale e ON (a.sala_id = e.sala_id) ";

    public final static String PLAN_VIEW_COL_NAMES[] =
            {"nazwa_grupy", "nazwa_sali", "nazwa_dnia", "godziny", "parzystosc", "nazwa_zajec", "rodz_zajec"};

    public final static String LECTURES_VIEW_COL_NAMES[] =
            {"id_zajec", "skrot_nazwy_zajec", "nazwa_zajec", "id_wykladowcy", "imie", "nazwisko"};

    public final static String KONSUL_VIEW_COL_NAMES[] =
            {"imie", "nazwisko", "nazwa_dnia", "godziny", "nazwa_sali"};

    // INSERTS // // // // // // // // // // // // // // // // // // // // // // //

    public final static String TB_DNI_TYG_INSERTS =
            "INSERT INTO tb_dni_tyg (nazwa_dnia) VALUES " +
                    "('poniedziałek'), " +
                    "('wtorek')," +
                    "('środa'), " +
                    "('czwartek'), " +
                    "('piątek')";

    public final static String TB_GODZINY_INSERTS =
            "INSERT INTO tb_godziny (godz_id, godziny) VALUES " +
                    "(8, '08:15-09:00'), " +
                    "(9, '09:15-10:00'), " +
                    "(10,'10:15-11:00'), " +
                    "(11,'11:15-12:00'), " +
                    "(12,'12:15-13:00'), " +
                    "(13,'13:15-14:00'), " +
                    "(14,'14:15-15:00'), " +
                    "(15,'15:15-16:00'), " +
                    "(16,'16:15-17:00'), " +
                    "(17,'17:15-18:00'), " +
                    "(18,'18:15-19:00'), " +
                    "(19,'19:15-20:00')";

    // TEST 'INSERT INTO' STATEMENTS
    public final static String TB_GRUPY_TEST_INSERT_0 =
            "INSERT INTO tb_grupy (nazwa_grupy) VALUES ('1E1')";

    public final static String TB_GRUPY_TEST_INSERT_1 =
            "INSERT INTO tb_grupy (nazwa_grupy) VALUES ('2T2')";

    public final static String TB_ZAJECIA_TEST_INSERTS = "INSERT INTO tb_zajecia (skrot_nazwy_zajec, nazwa_zajec) VALUES "
            + "('ALGTM', 'Algebra i teoria mnogości'), "
            + "('ANL1', 'Analiza 1'), " + "('ORM', 'Orientacja - M'), "
            + "('PPOM', 'Podstawy pomiarów'), "
            + "('PRAWO', 'Podstawy prawa - ochrona własności intelektualnej'), "
            + "('PRM', 'Podstawy programowania (M)'), "
            + "('ULOG', 'Układy logiczne'), "
            + "('WF1', 'Wychowanie fizyczne 1'), "
            + "('WFI', 'Wstęp do fizyki'), "
            + "('AISDI', 'Algorytmy i struktury danych (I)'), "
            + "('ARKO', 'Architektura komputerów'), "
            + "('FKS', 'Fizyka kwantowa i statystyczna'), "
            + "('FPPI', 'Fizyczne podstawy przetwarzania informacji'), "
            + "('JA', 'Język angielski 2 - poziom B2'), "
            + "('MPS', 'Metody probabilistyczne i statystyka'), "
            + "('PTT', 'Podstawy telekomunikacji'), "
            + "('TSI', 'Teoria sygnałów i informacji'), "
            + "('WF3', 'Wychowanie fizyczne 3'), "
            + "('CSK', 'Cyfrowe systemy komórkowe'), "
            + "('MR', 'Miernictwo radioelektroniczne'), "
            + "('SYTE', 'Systemy telewizyjne')";

    public final static String TB_SALE_TEST_INSERT = "INSERT INTO tb_sale (nazwa_sali, pietro_sali, mapa_x, mapa_y) VALUES "
            + "('011', -1, 4279, 1992), " + "('04b', -1, 718, 1938), "
            + "('08', -1, 3641, 1708), " + "('10', 0, 6863, 3000), "
            + "('102', 1, 652, 2054), " + "('104-AR', 1, 1497, 2618), "
            + "('105-AR', 1, 1497, 2618), " + "('106', 1, 1909, 1995), "
            + "('117', 1, 5040, 2011), " + "('118-AL', 1, 5376, 2571), "
            + "('120', 1, 6020, 1999), " + "('122', 1, 6566, 1997), "
            + "('133', 1, 5380, 756), " + "('139', 1, 4243, 598), "
            + "('161', 1, 1505, 782), " + "('164', 1, 711, 870), "
            + "('168', 1, 351, 890), " + "('277', 2, 733, 479), "
            + "('313', 3, 2361, 1993), " + "('411', 4, 2168, 1991), "
            + "('412', 4, 2443, 1990), " + "('418', 4, 4249, 1994), "
            + "('429', 4, 6613, 1993), " + "('430', 4, 6549, 1823), "
            + "('?', 0, 0, 0), " + "('DS202', 2, 765, 1991)";

    public final static String[] CREATE_TABLE_STATEMENTS = {
            CREATE_TB_PRACOWNICY,
            CREATE_TB_GRUPY,
            CREATE_TB_DNI_TYG,
            CREATE_TB_GODZINY,
            CREATE_TB_SALE,
            CREATE_TB_ZAJECIA,
            CREATE_TB_PLAN,
            CREATE_TB_PLAN_KONSUL
    };

    public final static String[] CREATE_VIEW_STATEMENTS = {
            CREATE_PLAN_VIEW,
            CREATE_LECTURES_VIEW,
            CREATE_KONSULTACJE_VIEW
    };

    public final static String[] INSERT_INTO_STATEMENT_LIST = {
            TB_DNI_TYG_INSERTS,
            TB_GODZINY_INSERTS,
            TB_GRUPY_TEST_INSERT_0,
            TB_GRUPY_TEST_INSERT_1,
            TB_ZAJECIA_TEST_INSERTS,
            TB_SALE_TEST_INSERT
    };

    public final static String[] TABLE_NAMES = {
            "tb_pracownicy",
            "tb_grupy",
            "tb_dni_tyg",
            "tb_godziny",
            "tb_sale",
            "tb_zajecia",
            "tb_plan",
            "tb_plan_konsul"
    };

    public final static String[] VIEW_NAMES = {"vw_plan", "vw_lectures", "vw_konsul"};

    public final static String[] INSERT_STATEMENT_NAMES = {
            "TB_DNI_TYG_INSERTS",
            "TB_GODZINY_INSERTS",
            "TB_GRUPY_TEST_INSERT_1E1",
            "TB_GRUPY_TEST_INSERT_2T2",
            "TB_ZAJECIA_TEST_INSERTS",
            "TB_SALE_TEST_INSERT",
            "TB_PLAN_TEST_INS_0",
            "TB_PLAN_TEST_INS_1",
            "TB_PLAN_TEST_INS_2",
            "TB_PLAN_TEST_INS_3",
            "TB_PLAN_TEST_INS_4",
            "TB_PLAN_TEST_INS_5",
            "TB_PLAN_TEST_INS_6",
            "TB_PLAN_TEST_INS_7",
            "TB_PLAN_TEST_INS_8",
            "TB_PLAN_TEST_INS_9",
            "TB_PLAN_TEST_INS_10"
    };

}