package com.egco428.mupts;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LogoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        /*Create Logo page for 1.5 second before entering the app*/

        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent loginIntent = new Intent(LogoActivity.this, LoginActivity.class);
                LogoActivity.this.startActivity(loginIntent);
                LogoActivity.this.finish();
            }
        }, 1500);
    }
}
