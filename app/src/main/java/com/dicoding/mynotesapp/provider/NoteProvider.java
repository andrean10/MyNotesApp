package com.dicoding.mynotesapp.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.dicoding.mynotesapp.db.NoteHelper;

import static com.dicoding.mynotesapp.db.DatabaseContract.AUTHORITY;
import static com.dicoding.mynotesapp.db.DatabaseContract.NoteColumns.CONTENT_URI;
import static com.dicoding.mynotesapp.db.DatabaseContract.TABLE_NAME;

// class content provider yang berfungsi untuk menyediakan pengambilan data pada content resolver
// dari aplikasi lain.
// noteProvider hanya sebagai jembatan untuk mengambil data dari Database yang ada pada method class NoteHelper
// CRUD tetap masih dibeban tugaskan ke notehelper akan tetapi kelebihannya adalah NoteProvider bisa
// menjembatani aplikasi lain yang ingin mengakses database yang ada pada aplikasi ini
public class NoteProvider extends ContentProvider {

    /*
       Integer digunakan sebagai identifier antara select all sama select by id
    */
    private static final int NOTE = 1;
    private static final int NOTE_ID = 2;
    private NoteHelper noteHelper;

    // definisi Uri Matcher , dan pengecekan apakah bersifat all atau ada tambahan id-nya.
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    /*
        Uri matcher untuk mempermudah identifier dengan menggunakan integer misal :
        - uri com.dicoding.picodiploma.mynotesapp dicocokan dengan integer 1
        - uri com.dicoding.picodiploma.mynotesapp/# dicocokan dengan integer 2
     */
    static {
        //content://com.dicoding.picodiploma.mynotesapp/note
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, NOTE);

        //content://com.dicoding.picodiploma.mynotesapp/note/id
        sUriMatcher.addURI(AUTHORITY,
                TABLE_NAME + "/#",
                NOTE_ID);
    }

    @Override
    public boolean onCreate() {
        noteHelper = NoteHelper.getInstance(getContext());
        noteHelper.open();
        return true;
    }

    /*
        Method query digunakan ketika ingin menjalankan query Select
        Return cursor
    */
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case NOTE:
                cursor = noteHelper.queryAll();
                break;
            // getlastpathsegment artinya mengambil nilai yang ada di belakang Uri
            case NOTE_ID:
                cursor = noteHelper.queryById(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long added;
        switch (sUriMatcher.match(uri)) {
            case NOTE:
                added = noteHelper.insert(values);
                break;
            default:
                added = 0;
                break;
        }

        getContext().getContentResolver().notifyChange(CONTENT_URI, null);

        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int update(Uri uri, ContentValues values, String s, String[] strings) {
        int updated;
        switch (sUriMatcher.match(uri)) {
            case NOTE_ID:
                updated = noteHelper.update(uri.getLastPathSegment(), values);
                break;
            default:
                updated = 0;
                break;
        }

        // berfungsi untuk memberitahukan kepada semua aplikasi yang bisa mengakses content provider
        // pada aplikasi ini, ada perubahan yang terjadi.
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);

        return updated;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case NOTE_ID:
                deleted = noteHelper.deleteById(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }

        getContext().getContentResolver().notifyChange(CONTENT_URI, null);

        return deleted;
    }
}
