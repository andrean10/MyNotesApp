package com.dicoding.mynotesappmodul4.db;

import android.provider.BaseColumns;

// class yang berguna untuk pengaksesan nama tabel dan nama kolom tabel
public class DatabaseContract {

    static String TABLE_NAME = "note";

    public static final class NoteColumns implements BaseColumns {

        public static String TITLE = "title";
        public static String DESCRIPTION = "description";
        public static String DATE = "date";
    }
}
