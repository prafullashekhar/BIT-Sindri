package com.bitsindri.bit.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bitsindri.bit.MainActivity;
import com.bitsindri.bit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email, password;
    private TextView forgotPassword, signUp;
    private Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        // initialising
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        signIn = findViewById(R.id.signIn);
        forgotPassword = findViewById(R.id.forgot_password_text);
        signUp = findViewById(R.id.signUp);

        mAuth = FirebaseAuth.getInstance();

        // On signIn click
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUser();
            }
        });

        // On forgotPassword click
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("MSG", "forgot");
                String strEmail = email.getText().toString().trim();

                // showing alert dialog if no email is provided
                if(TextUtils.isEmpty(strEmail)){
                    EditText resetMail = new EditText(v.getContext());
                    final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                    passwordResetDialog.setTitle("Reset Password?");
                    passwordResetDialog.setMessage("Enter your Email to receive password reset link");
                    passwordResetDialog.setView(resetMail);

                    passwordResetDialog.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(TextUtils.isEmpty(resetMail.getText().toString().trim())){
                                email.setError("Please enter your email Id");
                                email.requestFocus();
                            }else{
                                sendPasswordReset(resetMail.getText().toString());
                            }

                        }
                    });

                    passwordResetDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    passwordResetDialog.create().show();

                }else{
                    sendPasswordReset(strEmail);
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
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
                        if(mAuth.getCurrentUser().isEmailVerified()){
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }else{
                            Toast.makeText(LoginActivity.this, "Please first verify your email address through your email", Toast.LENGTH_LONG).show();
                        }

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


    // function to reset password
    private void sendPasswordReset(String resetEmail){
        mAuth.sendPasswordResetEmail(resetEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(LoginActivity.this, "Reset link is set to your email", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void showHidePass(View view){

        if(view.getId()==R.id.password_hide_toggle){

            if(password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.ic_password_hide);

                //Show Password
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.ic_password_show);

                //Hide Password
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }
    }
}