package com.example.daftartugas.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.daftartugas.dao.TugasDao;
import com.example.daftartugas.model.Tugas;

import java.util.ArrayList;
import java.util.List;

public class TugasDB extends SQLiteOpenHelper implements TugasDao {
    public static final String DBNAME = "vsga-hasnur.db";
    public static final int DBVERSION = 2;

    public TugasDB(@Nullable Context context) {
        super(context, DBNAME, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists tugas(" +
                "id integer primary key autoincrement," +
                "tugas text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("drop table if exists tugas");
            onCreate(db);
        }

    }

    @Override
    public long insert(Tugas tugas) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.putNull("id");
        values.put("tugas", tugas.getTugas());
        return db.insert("tugas", null, values);
    }

    @Override
    public void update(Tugas tugas) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", tugas.getId());
        values.put("tugas", tugas.getTugas());
        db.update("tugas", values, "id=?", new String[]{String.valueOf(tugas.getId())});
    }

    @Override
    public void delete(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("tugas", "id=?", new String[]{String.valueOf(id)});
    }

    @Override
    public Tugas getATugasById(int id) {
        Tugas result = null;
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.query("tugas", null, "id=?", new String[]{String.valueOf(id)}, null, null, null)) {
            if (c.moveToFirst())
                result = new Tugas(c.getInt(0), c.getString(1));
        }
        return result;
    }

    @Override
    public List<Tugas> getAllTugas() {
        List<Tugas> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.query("tugas", null, null, null, null, null, null)) {
            while (c.moveToNext()) {
                result.add(new Tugas(c.getInt(0), c.getString(1)));
            }
        }
        return result;
    }
}
