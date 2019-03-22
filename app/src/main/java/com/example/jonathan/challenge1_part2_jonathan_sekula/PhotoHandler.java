package com.example.jonathan.challenge1_part2_jonathan_sekula;

import android.content.Context;
import android.database.Cursor;
import android.hardware.Camera;
import android.widget.Toast;


public class PhotoHandler implements Camera.PictureCallback {

    private final Context context;
    private dbHelper myDb;

    public PhotoHandler(Context ctx, dbHelper db) {
        context = ctx;
        myDb = db;
    }

    @Override
    public void onPictureTaken(byte[] image, Camera camera) {
        boolean isInsert = myDb.insertData(image);
        Cursor cursor = myDb.getAllData();
        if (isInsert)
            Toast.makeText(context, "Picture taken and saved to db at id " + String.valueOf(cursor.getCount()) + "...", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Failure to save to db...", Toast.LENGTH_SHORT).show();
    }

}