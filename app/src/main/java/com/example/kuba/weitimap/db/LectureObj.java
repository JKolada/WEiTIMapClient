package com.example.kuba.weitimap.db;
import java.util.ArrayList;

public class LectureObj extends LectureParentObj {

    private String nazwa_sali;
    private String nazwa_dnia;
    private String id_godziny;
    private String parzystość;
    private String rodz_zajęć;

    LectureObj(ArrayList<String> poj_zajęcia) {
        nazwa_sali = poj_zajęcia.get(0);
        nazwa_dnia = poj_zajęcia.get(1);
        id_godziny = poj_zajęcia.get(2);
        parzystość = poj_zajęcia.get(3);
        skrót_nazwy_zajęć = poj_zajęcia.get(4);
        rodz_zajęć = poj_zajęcia.get(5);
    }

    protected String[] getLectureData() {
        final String[] lectureData = {nazwa_sali, nazwa_dnia, id_godziny, parzystość, skrót_nazwy_zajęć, rodz_zajęć};
        return lectureData;
    }

    Boolean isEven() {
        if (parzystość.charAt(0) == 'P') {
            return true;
        } else if (parzystość.charAt(0) == 'N') {
            return false;
        } else {
            return null;
        }
    }

}
