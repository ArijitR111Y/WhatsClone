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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private EditText phoneEditText, verifyCodeEditText;
    private Button verifyBtn;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String verificationId;
    private final String ISO = "+91";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseApp.initializeApp(LoginActivity.this);

        if(isUserLoggedIn())
            startHomeActivity();

        phoneEditText = findViewById(R.id.phoneEditText);
        verifyCodeEditText = findViewById(R.id.verfyCodeEditText);
        verifyBtn = findViewById(R.id.verifyBtn);

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phoneEditText.getText().toString().charAt(0) != '+')
                    phoneEditText.setText(ISO + phoneEditText.getText().toString());

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
                LoginActivity.this,
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
                    final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid());
                    dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // If data corresponding to the current users UID exists in the document, then we directly switch to the HomeActivity
                            if(!dataSnapshot.exists()){
                                HashMap<String, String> map = new HashMap<>();
                                map.put("name", (new StringBuffer("User")).append(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).toString());
                                map.put("phone", FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                                dbRef.setValue(map);
                            }
                            startHomeActivity();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    startHomeActivity();
                }else if(!task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Failed to Authorize Phone Number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
