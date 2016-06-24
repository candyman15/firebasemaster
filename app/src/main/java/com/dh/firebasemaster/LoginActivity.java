package com.dh.firebasemaster;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by imc050 on 2016. 6. 23..
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = "LoginActivity";

    private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Button mBtnLogin;
    private Button mBtnJoin;
    private EditText mEtId;
    private EditText mEtPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;
        mAuth = FirebaseAuth.getInstance();


        mBtnLogin = (Button)findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(this);
        mBtnJoin = (Button)findViewById(R.id.btn_join);
        mBtnJoin.setOnClickListener(this);
        mEtId = (EditText)findViewById(R.id.et_id);
        mEtPw = (EditText)findViewById(R.id.et_pw);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_join:
                if(!"".equals(mEtId.getText().toString()) && !"".equals(mEtPw.getText().toString())) {
                    //Create Account
                    mAuth.createUserWithEmailAndPassword(mEtId.getText().toString(),mEtPw.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                            if (!task.isSuccessful()) {
                                Toast.makeText(mContext, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    }).addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(mContext, e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                break;
            case R.id.btn_login:
                if(!"".equals(mEtId.getText().toString()) && !"".equals(mEtPw.getText().toString())) {
                    //Login
                    mAuth.signInWithEmailAndPassword(mEtId.getText().toString(), mEtPw.getText().toString())
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                                    if (!task.isSuccessful()) {
                                        Log.w(TAG, "signInWithEmail", task.getException());
                                        Toast.makeText(mContext, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            }).addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(mContext, e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
        }
    }
}
