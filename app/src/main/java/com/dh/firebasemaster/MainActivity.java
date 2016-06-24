package com.dh.firebasemaster;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = "MainActivity";

    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private FirebaseStorage mFirebaseStorage;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button mBtnAppCrash;
    private Button mBtnAppCrashCustomLog;
    private Button mBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mBtnAppCrash = (Button)findViewById(R.id.btn_app_crash);
        mBtnAppCrash.setOnClickListener(this);

        mBtnAppCrashCustomLog = (Button)findViewById(R.id.btn_app_crash_custom_log);
        mBtnAppCrashCustomLog.setOnClickListener(this);

        mBtnLogin = (Button)findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(this);

        FirebaseCrash.log("MainActivity created");

        //Remote-Config Fetch//
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults); // Setting Default value
        mFirebaseRemoteConfig.fetch(0).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Fetch Succeeded");
                    mFirebaseRemoteConfig.activateFetched();
                } else {
                    Log.e(TAG, "Fetch failed", task.getException());
                }

                String hide_button_all = mFirebaseRemoteConfig.getString("hide_button_all");
                Log.d("","hide_button_all : "+ hide_button_all);
                buttonMode(hide_button_all);

            }
        });

        //Auth//
        //Auth Status//
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    //User is signed in
                    Log.d(TAG,"onAuthStateChanged:sigin_in :" + user.getUid());
                    mBtnLogin.setVisibility(View.GONE);
                }else{
                    //User is signed out
                    Log.d(TAG,"onAuthStateChanged:sigin_out");
                    mBtnLogin.setVisibility(View.VISIBLE);
                }
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthListener); //Check Auth State
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_app_crash:
                //Error Case
                String crash = "crash";
                int crashNum = Integer.parseInt(crash);
                break;
            case R.id.btn_app_crash_custom_log:
                FirebaseCrash.report(new Exception("My first Android non-fatal error"));
                break;
            case R.id.btn_login:
                Intent i = new Intent(this,LoginActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }

    //hide button
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
