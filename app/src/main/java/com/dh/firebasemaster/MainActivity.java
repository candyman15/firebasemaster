package com.dh.firebasemaster;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private Button mBtnAppCrash;
    private Button mBtnAppCrashCustomLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        mBtnAppCrash = (Button)findViewById(R.id.btn_app_crash);
        mBtnAppCrash.setOnClickListener(this);

        mBtnAppCrashCustomLog = (Button)findViewById(R.id.btn_app_crash_custom_log);
        mBtnAppCrashCustomLog.setOnClickListener(this);

        FirebaseCrash.log("MainActivity created");

        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        mFirebaseRemoteConfig.fetch(0).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("", "Fetch Succeeded");
                    mFirebaseRemoteConfig.activateFetched();
                } else {
                    Log.e("", "Fetch failed", task.getException());
                }

                String hide_button_all = mFirebaseRemoteConfig.getString("hide_button_all");
                Log.d("","hide_button_all : "+ hide_button_all);
                buttonMode(hide_button_all);

            }
        });




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

    private void buttonMode(String mode){
        if("Y".equals(mode)){
            mBtnAppCrash.setVisibility(View.GONE);
            mBtnAppCrashCustomLog.setVisibility(View.GONE);
        }else{
            mBtnAppCrash.setVisibility(View.VISIBLE);
            mBtnAppCrashCustomLog.setVisibility(View.VISIBLE);
        }
    }
}
