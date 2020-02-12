package com.dicoding.mynotesappmodul4.helper;

import android.database.Cursor;

import com.dicoding.mynotesappmodul4.db.DatabaseContract;
import com.dicoding.mynotesappmodul4.entity.Note;

import java.util.ArrayList;

// class yang berfungsi untuk mengkonversi objek cursor ke Arraylist
public class MappingHelper {

    public static ArrayList<Note> mapCursorToArrayList(Cursor noteCursor) {
        ArrayList<Note> notesList = new ArrayList<>();

        while (noteCursor.moveToNext()) {
            int id = noteCursor.getInt(noteCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns._ID));
            String title = noteCursor.getString(noteCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.TITLE));
            String description = noteCursor.getString(noteCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.DESCRIPTION));
            String date = noteCursor.getString(noteCursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.DATE));
            notesList.add(new Note(id, title, description, date));
        }
        return notesList;
    }
}
