package com.bryansilverio.serviciorest;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT=1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent IHome=new Intent(SplashScreenActivity.this,MainActivity.class);
                startActivity(IHome);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
