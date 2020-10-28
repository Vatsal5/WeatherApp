package com.androidstudio.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread thread = new Thread()
        {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(4000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally {
                    startActivity(new Intent(splash.this,MainActivity.class));
                    overridePendingTransition(R.anim.slide_down,R.anim.slide_up);
                    splash.this.finish();
                }
            }
        };
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }
}
