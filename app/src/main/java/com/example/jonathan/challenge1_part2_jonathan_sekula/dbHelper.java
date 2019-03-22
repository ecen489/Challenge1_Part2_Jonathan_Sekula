package com.example.jonathan.challenge1_part2_jonathan_sekula;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class dbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "picturesDB.db";
    private static final String TABLE_NAME = "Pictures";
    private static final String COLUMN_ID = "PictureID";
    private static final String COLUMN_PICTURE = "Picture";

    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID +
                " INTEGER PRIMARY KEY, " + COLUMN_PICTURE + " BLOB)";
        db.execSQL(CREATE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }


    public boolean insertData(byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = this.getAllData();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, cursor.getCount()+1);
        contentValues.put(COLUMN_PICTURE, image);

        long test = db.insert(TABLE_NAME,null ,contentValues);

        if(test == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME);
    }

}
