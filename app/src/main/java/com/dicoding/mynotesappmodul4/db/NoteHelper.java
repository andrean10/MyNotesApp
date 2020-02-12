package com.dicoding.mynotesappmodul4.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//  kelas yang akan mengakomodasi kebutuhan DML (Definition Modifying Language)
public class NoteHelper {
    private static final String DATABASE_TABLE = DatabaseContract.TABLE_NAME;
    private static DatabaseHelper databaseHelper;
    private static NoteHelper INSTANCE;

    private static SQLiteDatabase database;

    // construct
    private NoteHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    // method menginisiasi database
    /*
        Kelas di bawah menggunakan sebuah pattern yang bernama Singleton Pattern. Dengan singleton
        sebuah obyek hanya bisa memiliki sebuah instance. Sehingga tidak terjadi duplikasi instance.
        Syncronized di sini dipakai untuk menghindari duplikasi instance di semua Thread, karena bisa
        saja kita membuat instance di Thread yang berbeda.
     */
    public static NoteHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NoteHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    // method membuka dan menutup koneksi ke database
    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        databaseHelper.close();

        if (database.isOpen())
            database.close();
    }

    // method mengambil data
    public Cursor queryAll() {
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                DatabaseContract.NoteColumns._ID + " ASC");
    }

    // method mengambil data dengan id tertentu
    public Cursor queryById(String id) {
        return database.query(
                DATABASE_TABLE,
                null,
                DatabaseContract.NoteColumns._ID + " = ?",
                new String[]{id},
                null,
                null,
                null);
    }

    // method untuk menyimpan data
    public long insert(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    // method untuk memperbarui data
    public int update(String id, ContentValues values) {
        return database.update(
                DATABASE_TABLE,
                values,
                DatabaseContract.NoteColumns._ID + " = ?",
                new String[]{id});
    }

    // method untuk menghapus data
    public int deleteById(String id) {
        return database.delete(
                DATABASE_TABLE,
                DatabaseContract.NoteColumns._ID + " = ?",
                new String[]{id}
        );
    }
}
