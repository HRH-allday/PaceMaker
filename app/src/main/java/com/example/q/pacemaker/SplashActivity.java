package com.example.q.pacemaker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 1500;

    private ImageView splash_logo;
    private TextView splash_title;
    private TextView splash_body;
    private TextView splash_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splash_title = (TextView) findViewById(R.id.splash_title);
        splash_logo = (ImageView) findViewById(R.id.splash_logo);
        splash_body = (TextView) findViewById(R.id.splash_body);
        splash_description = (TextView) findViewById(R.id.splash_description);

        splash_title.setTypeface(App.NanumBarunGothicBold);
        splash_body.setTypeface(App.NanumBarunGothicLight);
        splash_description.setTypeface(App.NanumBarunGothicUltraLight);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
            }
        },SPLASH_TIME_OUT);
    }
}