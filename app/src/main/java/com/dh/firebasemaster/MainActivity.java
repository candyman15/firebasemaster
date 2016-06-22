package com.dh.firebasemaster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAnalytics mFirebaseAnalytics;
    private Button mBtnAppCrash;
    private Button mBtnAppCrashCustomLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mBtnAppCrash = (Button)findViewById(R.id.btn_app_crash);
        mBtnAppCrash.setOnClickListener(this);

        mBtnAppCrashCustomLog = (Button)findViewById(R.id.btn_app_crash_custom_log);
        mBtnAppCrashCustomLog.setOnClickListener(this);

        FirebaseCrash.log("MainActivity created");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_app_crash:
                String crash = "crash";
                int crashNum = Integer.parseInt(crash);
                break;
            case R.id.btn_app_crash_custom_log:
                FirebaseCrash.report(new Exception("My first Android non-fatal error"));
                break;
            default:
                break;
        }
    }
}
