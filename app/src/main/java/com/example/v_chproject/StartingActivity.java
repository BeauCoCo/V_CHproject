package com.example.v_chproject;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class StartingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        final TextView informationTextView = (TextView) findViewById(R.id.informationTextView);
        informationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                informationTextView.setPaintFlags(informationTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                String blog = "https://vchphotoeditor.blogspot.com/";
                Uri webaddress = Uri.parse(blog);
                Intent goToBlog = new Intent(Intent.ACTION_VIEW, webaddress);
                startActivity(goToBlog);
            }
        });
    }

    public void startingButton(View view) {
        Intent goToLoadingActivity = new Intent(this, LoadingActivity.class);
        startActivity(goToLoadingActivity);
    }
}
