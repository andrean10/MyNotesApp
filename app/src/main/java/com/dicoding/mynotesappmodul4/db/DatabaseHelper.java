package com.dicoding.mynotesappmodul4.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// class yang bertanggung jawab dalam menciptakan database dengan tabel yang dibutuhkan
public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "dbnoteapp";

    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_NOTE = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT_NOT_NULL," +
                    " %s TEXT_NOT_NULL)",
            DatabaseContract.TABLE_NAME,
            DatabaseContract.NoteColumns._ID,
            DatabaseContract.NoteColumns.TITLE,
            DatabaseContract.NoteColumns.DESCRIPTION,
            DatabaseContract.NoteColumns.DATE
    );

    private static final String SQL_DElETE_ENTRIES = String.format("DROP TABLE IF EXISTS %s", DatabaseContract.TABLE_NAME);

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_NOTE);
    }

    // handle ketika terjadi perubahan pada tabel di dalam database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DElETE_ENTRIES);
        onCreate(db);
    }
}
