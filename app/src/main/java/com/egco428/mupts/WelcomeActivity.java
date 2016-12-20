package com.egco428.mupts;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    ViewPager viewPager;
    Button skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        PagerAdapter adapter = new ViewpagerAdapter(WelcomeActivity.this);
        viewPager.setAdapter(adapter);

        skip = (Button) findViewById(R.id.skipButton);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent clearBackLogin = new Intent(WelcomeActivity.this, LoginActivity.class);
                clearBackLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(clearBackLogin);
            }
        });
    }
}
