package com.example.jonathan.challenge1_part2_jonathan_sekula;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView image;
    EditText image_id;
    Camera camera;
    dbHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new dbHelper(this);
        myDb.deleteAll();
        image = findViewById(R.id.image);
        image_id = findViewById(R.id.image_id);
        camera = Camera.open(findFrontFacingCamera());
    }

    public void TakePicture(View view) {
        camera.startPreview();
        camera.takePicture(null, null, new PhotoHandler(getApplicationContext(), myDb));
    }

    public void ViewPicture(View view) {
        if (image_id.getText().toString().equals(""))
            Toast.makeText(getApplicationContext(), "Enter an id...", Toast.LENGTH_SHORT).show();
        else {
            String text = image_id.getText().toString();
            boolean invalid_input = false;
            for (int i = 0; i < text.length(); ++i) {
                if (!Character.isDigit(text.charAt(i)))
                    invalid_input = true;
            }

            if (invalid_input)
                Toast.makeText(getApplicationContext(), "Enter a valid id...", Toast.LENGTH_SHORT).show();
            else {
                Cursor cursor = myDb.getAllData();
                if(cursor.getCount() == 0) {
                    Toast.makeText(getApplicationContext(), "Database is empty...", Toast.LENGTH_SHORT).show();
                }
                else {
                    boolean found = false;

                    while (cursor.moveToNext()) {
                        if (cursor.getInt(0) == Integer.parseInt(image_id.getText().toString())) {
                            found = true;
                            Bitmap bmp = BitmapFactory.decodeByteArray(cursor.getBlob(1), 0, cursor.getBlob(1).length);
                            image.setImageBitmap(bmp);
                            break;
                        }
                    }

                    if (!found) {
                        Toast.makeText(getApplicationContext(), "No image exists at this index...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    // from https://www.vogella.com/tutorials/AndroidCamera/article.html
    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                Log.d("tag", "Camera found");
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    @Override
    protected void onPause() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
        super.onPause();
    }
}

