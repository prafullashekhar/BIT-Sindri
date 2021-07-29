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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;
    String UserId;

    private TextView userName, userBatch, userBranch, userRoll, userRegNo;
    private TextView email, password;
    private Button signUp;

    String strUserName;
    String strUserBatch;
    String strUserBranch;
    String strUserRoll;
    String strUserRegNo;
    String strUserEmail;
    String strUserPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // initialising
        email = findViewById(R.id.user_email);
        password = findViewById(R.id.user_password);
        signUp = findViewById(R.id.signup);
        userName = findViewById(R.id.user_name);
        userBatch = findViewById(R.id.user_batch);
        userBranch = findViewById(R.id.user_branch);
        userRoll = findViewById(R.id.user_roll);
        userRegNo = findViewById(R.id.user_registration);

        // instantiating firebase
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUserData();
            }
        });
    }


    // function to check user data
    private void checkUserData(){
        strUserName=userName.getText().toString();
        strUserBatch=userBatch.getText().toString();
        strUserBranch=userBranch.getText().toString();
        strUserRoll=userRoll.getText().toString();
        strUserRegNo=userRegNo.getText().toString();
        strUserEmail = email.getText().toString().trim();
        strUserPassword = password.getText().toString();

        if(TextUtils.isEmpty(strUserName)){
            userName.setError("Please give Name");
            userName.requestFocus();
        }else if(TextUtils.isEmpty(strUserBatch)){
            userBatch.setError("Please give Batch");
            userBatch.requestFocus();
        }else if(TextUtils.isEmpty(strUserBranch)){
            userBranch.setError("Please give Name");
            userBranch.requestFocus();
        }else if(TextUtils.isEmpty(strUserRoll)){
            userRoll.setError("Please give Name");
            userRoll.requestFocus();
        }else if(TextUtils.isEmpty(strUserRegNo)){
            userRegNo.setError("Please give Name");
            userRegNo.requestFocus();
        }else if(TextUtils.isEmpty(strUserEmail)){
            email.setError("Email cannot be empty");
            email.requestFocus();
        }else if(TextUtils.isEmpty(strUserPassword)){
            password.setError("Password cannot be empty");
            password.requestFocus();
        }else if(!Patterns.EMAIL_ADDRESS.matcher(strUserEmail).matches()){
            email.setError("Please enter a valid email");
            email.requestFocus();
        }else if(strUserPassword.length() < 6){
            password.setError("Too short password length");
            password.requestFocus();
        }else{
            addUser();
        }
    }

    // function to add user to the firebase database
    private void addUser(){
        mAuth.createUserWithEmailAndPassword(strUserEmail, strUserPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Registered successfully \nPlease check your email to verify", Toast.LENGTH_LONG).show();
                                UserId = mAuth.getCurrentUser().getUid();

                                registerUser();

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

    // register the user information in FireStore cloud database
    private void registerUser(){
        DocumentReference documentReference = mStore.collection("user(" + strUserBatch+"-"+strUserBranch+")").document(UserId);
        Map<String, Object> user = new HashMap<>();
        user.put("Name", strUserName);
        user.put("Email", strUserEmail);
        user.put("Batch", strUserBatch);
        user.put("Branch", strUserBranch);
        user.put("Roll", strUserRoll);
        user.put("RegNo", strUserRegNo);
        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("MSG", "user profile is created");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.e("MSG", "user profile is not created");
            }
        });
    }

}