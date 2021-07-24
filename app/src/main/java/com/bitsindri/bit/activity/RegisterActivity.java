package com.bitsindri.bit.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bitsindri.bit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private TextView email, password;
    private Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // initialising
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signUp = findViewById(R.id.signup);

        mAuth = FirebaseAuth.getInstance();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });
    }


    // function to validate and add user to the firebase database
    private void addUser(){
        String strEmail = email.getText().toString().trim();
        String strPassword = password.getText().toString();

        // validating the user
        if(TextUtils.isEmpty(strEmail)){
            email.setError("Email cannot be empty");
            email.requestFocus();
        }else if(TextUtils.isEmpty(strPassword)){
            password.setError("Password cannot be empty");
            password.requestFocus();
        }else if(!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()){
            email.setError("Please enter a valid email");
            email.requestFocus();
        }else if(strPassword.length() < 6){
            password.setError("Too short password length");
            password.requestFocus();
        } else{
            // add the user
            mAuth.createUserWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this, "Registered successfully \nPlease check your email to verify", Toast.LENGTH_LONG).show();
                                    email.setText("");
                                    password.setText("");
                                }else{
                                    Log.e("MSG", "Unable to send verification mail - "+task.getException().getMessage());
                                }
                            }
                        });
                    }else{
                        String registerErrorMsg = task.getException().getMessage();
                        Log.e("MSG", "Not Registered - "+registerErrorMsg);

                        if(registerErrorMsg.equals("The email address is already in use by another account.")){
                            email.setError("This email is already registered");
                            email.requestFocus();
                        }else{
                            Toast.makeText(RegisterActivity.this, registerErrorMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }

    }

}