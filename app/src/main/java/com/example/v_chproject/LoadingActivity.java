package com.example.v_chproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class LoadingActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST = 1;
    private static final int RESULT_LOAD_IMAGE = 0;
    Button loadingButton, savingButton, sharingButton, filterButton;
    ImageView imageView, imageFilter;
    String currentImage = "";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        //ask for storage perrmission
        if (ContextCompat.checkSelfPermission(LoadingActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoadingActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(LoadingActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(LoadingActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
        } else {
            //do nothing
        }
        imageView = (ImageView) findViewById(R.id.imageView);
        imageFilter = (ImageView) findViewById(R.id.imageFilter);

        loadingButton = (Button) findViewById(R.id.loadingButton);
        filterButton = (Button) findViewById(R.id.filterButton);
        savingButton = (Button) findViewById(R.id.savingButton);
        sharingButton = (Button) findViewById(R.id.sharingButton);

        filterButton.setEnabled(false);
        savingButton.setEnabled(false);
        sharingButton.setEnabled(false);

        loadingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);

            }
        });
        savingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View content = findViewById(R.id.lay);
                Bitmap bitmap = getScreenShot(content);
                currentImage = "image" + System.currentTimeMillis() + ".png";
                store(bitmap, currentImage);
                sharingButton.setEnabled(true);

            }
        });
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageFilter.setImageResource(R.drawable.ic_filter2);
                savingButton.setEnabled(true);

            }
        });

        sharingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareImage(currentImage);

            }
        });


    }

    //get the filtered image
    private static Bitmap getScreenShot(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    //saving the image in internal storage
    private void store(Bitmap bm, String filename) {
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FILTEREDIMAGES";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();

        }
        File file = new File(dirPath, filename);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fos);

            fos.flush();
            fos.close();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //sharing
    private void shareImage(String fileName) {
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FILTEREDIMAGES";
        Uri uri = Uri.fromFile(new File(dirPath, fileName));
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, "Share via"));
        } catch (Exception e) {
            Toast.makeText(this, "No sharing app found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            filterButton.setEnabled(true);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch (requestCode){
            case MY_PERMISSION_REQUEST:{
                if (grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(LoadingActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) ==PackageManager.PERMISSION_GRANTED) {
                        //do nothing
                    } else {
                        Toast.makeText(this, "No permission granted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        }}



    public void settingButton(View view) {
        Intent goToSetting = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(goToSetting);
    }
}
