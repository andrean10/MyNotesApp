package com.dicoding.consumerapp.helper;

import android.database.Cursor;

import com.dicoding.consumerapp.entity.Note;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.dicoding.consumerapp.db.DatabaseContract.NoteColumns.DATE;
import static com.dicoding.consumerapp.db.DatabaseContract.NoteColumns.DESCRIPTION;
import static com.dicoding.consumerapp.db.DatabaseContract.NoteColumns.TITLE;

// class yang berfungsi untuk mengkonversi objek cursor ke Arraylist
public class MappingHelper {

    public static ArrayList<Note> mapCursorToArrayList(Cursor noteCursor) {
        ArrayList<Note> notesList = new ArrayList<>();

        while (noteCursor.moveToNext()) {
            int id = noteCursor.getInt(noteCursor.getColumnIndexOrThrow(_ID));
            String title = noteCursor.getString(noteCursor.getColumnIndexOrThrow(TITLE));
            String description = noteCursor.getString(noteCursor.getColumnIndexOrThrow(DESCRIPTION));
            String date = noteCursor.getString(noteCursor.getColumnIndexOrThrow(DATE));
            notesList.add(new Note(id, title, description, date));
        }
        return notesList;
    }

    // model data Note menjadi cursor
    public static Note mapCursorToObject(Cursor notesCursor) {
        notesCursor.moveToFirst();
        int id = notesCursor.getInt(notesCursor.getColumnIndexOrThrow(_ID));
        String title = notesCursor.getString(notesCursor.getColumnIndexOrThrow(TITLE));
        String description = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DESCRIPTION));
        String date = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DATE));

        return new Note(id, title, description, date);
    }
}
