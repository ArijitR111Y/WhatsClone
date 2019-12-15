package com.royarijit998.whatsclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private EditText phoneEditText, verifyCodeEditText;
    private Button verifyBtn;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseApp.initializeApp(MainActivity.this);

        if(isUserLoggedIn())
            startHomeActivity();

        phoneEditText = findViewById(R.id.phoneEditText);
        verifyCodeEditText = findViewById(R.id.verfyCodeEditText);
        verifyBtn = findViewById(R.id.verifyBtn);

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verificationId == null){
                    startPhoneNumberVerification();
                }else {
                    verifyPhoneNumberWithCode();
                }
            }
        });


        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {}

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationId = s;
                verifyBtn.setText("VERIFY CODE");

            }
        };


    }
//------------------------------------------------------------------------------------------------------------
    // Methods invoked when verifyBtn is clicked

    // Invoked when the 'send verification code' button has been clicked and verificationCode has not been received
    public void startPhoneNumberVerification(){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneEditText.getText().toString(),
                60,
                TimeUnit.SECONDS,
                MainActivity.this,
                callbacks
        );
    }

    // Invoked when the code has been received and verificationId is no longer an empty String
    public void verifyPhoneNumberWithCode(){
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, verifyCodeEditText.getText().toString());
        signInWithPhoneAuthCredential(phoneAuthCredential);
    }
//------------------------------------------------------------------------------------------------------------



    // To check if the current user is logged in (invoked at the beginning - such that authenticated users can be directly sent to the home activity)
    public boolean isUserLoggedIn(){
        return (FirebaseAuth.getInstance().getCurrentUser() != null);
    }


    // This is to switch from this login activity to the main home activity
    public void startHomeActivity(){
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
    }

    public void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential){
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful() && isUserLoggedIn()){
                    startHomeActivity();
                }else if(!task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Failed to Authorize Phone Number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
