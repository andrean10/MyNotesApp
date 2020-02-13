package com.dicoding.mynotesapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dicoding.mynotesapp.entity.Note;
import com.dicoding.mynotesapp.helper.MappingHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.dicoding.mynotesapp.db.DatabaseContract.NoteColumns.CONTENT_URI;
import static com.dicoding.mynotesapp.db.DatabaseContract.NoteColumns.DATE;
import static com.dicoding.mynotesapp.db.DatabaseContract.NoteColumns.DESCRIPTION;
import static com.dicoding.mynotesapp.db.DatabaseContract.NoteColumns.TITLE;

public class NoteAddUpdateActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtTitle, edtDescription;
    private Button btnSubmit;

    private Boolean isEdit = false;
    private Note note;
    private int position;
    private Uri uriWithId;

    public static final String EXTRA_POSITION = "extra_position";
    public static final String EXTRA_NOTE = "extra_note";
    public static final int REQUEST_ADD = 100;
    public static final int REQUEST_UPDATE = 200;
    private final int ALERT_DIALOG_CLOSE = 10;
    private final int ALERT_DIALOG_DELETE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add_update);

        edtTitle = findViewById(R.id.edt_title);
        edtDescription = findViewById(R.id.edt_description);
        btnSubmit = findViewById(R.id.btn_submit);

        note = getIntent().getParcelableExtra(EXTRA_NOTE);

        if (note != null) {
            position = getIntent().getIntExtra(EXTRA_POSITION, 0);
            isEdit = true;
        } else {
            note = new Note();
        }

        String actionBarTitle;
        String btnTitle;

        if (isEdit) {
            // Uri yang di dapatkan disini akan digunakan untuk ambil data dari provider
            // content://com.dicoding.mynotesapp/note/id
            uriWithId = Uri.parse(CONTENT_URI + "/" + note.getId());
            if (uriWithId != null) {
                Cursor cursor = getContentResolver().query(
                        uriWithId,
                        null,
                        null,
                        null,
                        null);

                // karena dia cursor, maka harus dirubah menjadi object agar bisa ditampilkan pada edit text
                if (cursor != null) {
                    note = MappingHelper.mapCursorToObject(cursor);
                    cursor.close();
                }
            }

            actionBarTitle = "Ubah";
            btnTitle = "Update";

            if (note != null) {
                edtTitle.setText(note.getTitle());
                edtDescription.setText(note.getDescripton());
            }
        } else {
            actionBarTitle = "Tambah";
            btnTitle = "Simpan";
        }

        setActionBarTitle(actionBarTitle);

        btnSubmit.setText(btnTitle);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_submit) {
            String title = edtTitle.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();

            if (TextUtils.isEmpty(title)) {
                edtTitle.setError("Field can not be blank");
                return;
            }

            // Gunakan contentvalues untuk menampung data
            ContentValues values = new ContentValues();
            values.put(TITLE, title);
            values.put(DESCRIPTION, description);

            // ubah data | logika CRUD
            if (isEdit) {
                // Gunakan uriWithId untuk update
                // content://com.dicoding.mynotesapp/note/id
                // getcontentresolver berfungsi untuk meneruskan kepada contentprovider yang ada di class NoteProvider
                // dan memanggil method yang sesuai.
                getContentResolver().update(
                        uriWithId,
                        values,
                        null,
                        null);
                Toast.makeText(NoteAddUpdateActivity.this, "Satu item berhasil diedit", Toast.LENGTH_SHORT).show();
                finish();

                // tambah data
            } else {
                note.setDate(getCurrentDate());
                values.put(DATE, getCurrentDate());
                // Gunakan content uri untuk insert
                // content://com.dicoding.mynotesapp/note/
                getContentResolver().insert(CONTENT_URI, values);
                Toast.makeText(NoteAddUpdateActivity.this, "Satu item berhasil disimpan", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEdit) {
            getMenuInflater().inflate(R.menu.menu_form, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    // pada saat item di tekan baik delete maupun back maka akan memunculkan alert_dialog
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                showAlertDialog(ALERT_DIALOG_DELETE);
                break;
            case android.R.id.home:
                showAlertDialog(ALERT_DIALOG_CLOSE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // method ini berfungsi untuk menampilkan ALERT_DIALOG
    @Override
    public void onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE);
    }

    // method untuk menampilkan alert dialog
    /*
        Konfirmasi dialog sebelum proses batal atau hapus
        close = 10
        delete = 20
    */
    private void showAlertDialog(int type) {
        final boolean isDialogClose = type == ALERT_DIALOG_CLOSE;
        String dialogTitle, dialogMessage;

        if (isDialogClose) {
            dialogTitle = "Batal";
            dialogMessage = "Apakah anda ingin membatalkan perubahan pada form?";
        } else {
            dialogTitle = "Hapus Note";
            dialogMessage = "Apakah anda yakin ingin menghapus item ini?";
        }

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NoteAddUpdateActivity.this);

        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (isDialogClose) {
                            finish();
                        } else {
                            // Gunakan uriWithId untuk delete
                            // content://com.dicoding.mynotesapp/note/id
                            getContentResolver().delete(uriWithId, null, null);
                            Toast.makeText(NoteAddUpdateActivity.this, "Satu item berhasil dihapus", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void setActionBarTitle(String actionBarTitle) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(actionBarTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();

        return dateFormat.format(date);
    }
}