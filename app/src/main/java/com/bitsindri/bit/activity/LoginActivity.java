package com.bitsindri.bit.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bitsindri.bit.MainActivity;
import com.bitsindri.bit.R;
import com.bitsindri.bit.methods.Constants;
import com.bitsindri.bit.methods.Methods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email, password;
    private TextView forgotPassword, signUp;
    private Button signIn;
    private ProgressDialog progressDialog;
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initialising
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        signIn = findViewById(R.id.signIn);
        forgotPassword = findViewById(R.id.forgot_password_text);
        signUp = findViewById(R.id.signUp);

        // instantiating firebase
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
                String strEmail = email.getText().toString().trim();

                // showing alert dialog if no email is provided
                if(TextUtils.isEmpty(strEmail)){
                    EditText forgotEmail;
                    Button forgotResetLink;
                    final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(LoginActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View view = inflater.inflate(R.layout.forgot_pass_dialog_box, null);

                    forgotEmail = view.findViewById(R.id.forgot_email);
                    forgotResetLink = view.findViewById(R.id.forgot_reset_link);
                    passwordResetDialog.setView(view);

                    forgotResetLink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(TextUtils.isEmpty(forgotEmail.getText().toString().trim())){
                                forgotEmail.setError("Please enter your email Id");
                                forgotEmail.requestFocus();
                            }else{
                                sendPasswordReset(forgotEmail.getText().toString());
                            }
                        }
                    });

                    dialog = passwordResetDialog.create();
                    dialog.getWindow().setBackgroundDrawableResource(
                            android.R.color.transparent
                    );
                    dialog.show();

                }else{
                    sendPasswordReset(strEmail);
                }
            }
        });

        // On signup click
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
        }else if(!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()){
            email.setError("Enter the correct EmailId");
            email.requestFocus();
        }else if(TextUtils.isEmpty(strPassword)){
            password.setError("Password cannot be empty");
            password.requestFocus();
        }else{
            // launching progress bar
            progressDialog = Methods.launchProgressDialog(progressDialog, LoginActivity.this);

            mAuth.signInWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    progressDialog.dismiss();
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
                        Log.e(Constants.msg, "Not Registered - "+errorMsg);

                        assert errorMsg != null;
                        if(errorMsg.equals("There is no user record corresponding to this identifier. The user may have been deleted.")){
                            String er = "This email is not a registered email \nIf you are new user first get registered";
                            Toast.makeText(LoginActivity.this, er, Toast.LENGTH_LONG ).show();
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

        // launching progress bar
        progressDialog = Methods.launchProgressDialog(progressDialog, LoginActivity.this);

        mAuth.sendPasswordResetEmail(resetEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                dialog.dismiss();
                Toast.makeText(LoginActivity.this, "Reset link is sent to your email", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Show password on eye click
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

    // handling onStart
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null && user.isEmailVerified()){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }
}