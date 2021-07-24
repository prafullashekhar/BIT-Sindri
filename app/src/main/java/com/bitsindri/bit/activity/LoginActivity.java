package com.bitsindri.bit.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bitsindri.bit.MainActivity;
import com.bitsindri.bit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView email, password;
    private Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initialising
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signIn = findViewById(R.id.signIn);

        mAuth = FirebaseAuth.getInstance();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUser();
            }
        });

    }

    // checks if the user data is in the database
    private void checkUser(){
        String strEmail = email.getText().toString().trim();
        String strPassword = password.getText().toString();

        if(TextUtils.isEmpty(strEmail)){
            email.setError("Email cannot be empty");
            email.requestFocus();
        }else if(TextUtils.isEmpty(strPassword)){
            password.setError("Password cannot be empty");
            password.requestFocus();
        }else{
            mAuth.signInWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Log.d("MSG", "Logged in successfully");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else{
                        String errorMsg = task.getException().getMessage();
                        Log.e("MSG", "Not Registered - "+errorMsg);

                        if(errorMsg.equals("There is no user record corresponding to this identifier. The user may have been deleted.")){
                            email.setError("This email is not a registered email \nIf you are new user first get registered");
                            email.requestFocus();
                        } else if(errorMsg.equals("The email address is badly formatted.")){
                            email.setError("Enter the correct EmailId");
                            email.requestFocus();
                        } else if(errorMsg.equals("The password is invalid or the user does not have a password.")){
                            password.setError("Incorrect password");
                            password.requestFocus();
                        } else if(errorMsg.equals("We have blocked all requests from this device due to unusual activity. Try again later. [ Access to this account has been temporarily disabled due to many failed login attempts. You can immediately restore it by resetting your password or you can try again later. ]")){
                            Toast.makeText(LoginActivity.this, "Too much unsuccessful attempts,\ntry after sometime", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Check you Internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
}