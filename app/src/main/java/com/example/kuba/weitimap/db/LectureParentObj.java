package com.example.kuba.weitimap.db;

import java.io.Serializable;

public abstract class LectureParentObj implements Serializable {

    private static final long serialVersionUID = -826240034398882484L;
    protected String skrót_nazwy_zajęć;

    public abstract String[] getLectureData();
}
