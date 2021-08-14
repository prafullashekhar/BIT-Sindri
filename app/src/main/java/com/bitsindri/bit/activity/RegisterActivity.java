package com.bitsindri.bit.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bitsindri.bit.R;
import com.bitsindri.bit.methods.Constants;
import com.bitsindri.bit.methods.Methods;
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

    private TextView userName, userRoll, userRegNo, confirmPassword;
    private TextView email, password;
    private Button signUp;
    private ProgressDialog progressDialog;

    String strUserName;
    AutoCompleteTextView selectBatch;
    String strUserBatch;
    AutoCompleteTextView selectBranch;
    String strUserBranch;
    String strUserRoll;
    String strUserRegNo;
    String strUserEmail;
    String strUserPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_register);
            setTheme(R.style.Theme_BIT);
            // initialising
            email = findViewById(R.id.user_email);
            password = findViewById(R.id.user_password);
            signUp = findViewById(R.id.signup);
            userName = findViewById(R.id.user_name);
            userRoll = findViewById(R.id.user_roll);
            userRegNo = findViewById(R.id.user_registration);
            confirmPassword = findViewById(R.id.user_confirm_password);

//             setting dropdown for batches
            String[] batches = getResources().getStringArray(R.array.batch_list);
            ArrayAdapter<String> batchArrayAdapter = new ArrayAdapter<String>(this, R.layout.dropdown_item, batches);
            selectBatch = (AutoCompleteTextView) findViewById(R.id.select_batch);
            selectBatch.setAdapter(batchArrayAdapter);

            // setting dropdown for branches
            String[] branches = getResources().getStringArray(R.array.branch_list);
            ArrayAdapter<String> branchArrayAdapter = new ArrayAdapter<String>(this, R.layout.dropdown_item, branches);
            selectBranch = (AutoCompleteTextView) findViewById(R.id.select_branch);
            selectBranch.setAdapter(branchArrayAdapter);

            // instantiating firebase
            mAuth = FirebaseAuth.getInstance();
            mStore = FirebaseFirestore.getInstance();

            signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkUserData();
                }
            });
        } catch (Exception e) {
            Log.e("myTag", "" + e.getMessage());
        }
    }


    // function to check user data
    private void checkUserData() {
        strUserName = userName.getText().toString();
        strUserBatch = selectBatch.getText().toString();
        strUserBranch = selectBranch.getText().toString();
        strUserRoll = userRoll.getText().toString();
        strUserRegNo = userRegNo.getText().toString();
        strUserEmail = email.getText().toString().trim();
        strUserPassword = password.getText().toString();

        if (TextUtils.isEmpty(strUserName)) {
            userName.setError("Please fill this");
            userName.requestFocus();
        } else if (strUserBatch.equals("Batch")) {
            selectBatch.setError("Please fill this");
            selectBatch.requestFocus();
        } else if (strUserBatch.equals("Branch")) {
            selectBranch.setError("Please fill this");
            selectBranch.requestFocus();
        } else if (TextUtils.isEmpty(strUserRoll)) {
            userRoll.setError("Please fill this");
            userRoll.requestFocus();
        } else if (TextUtils.isEmpty(strUserRegNo)) {
            userRegNo.setError("Please fill this");
            userRegNo.requestFocus();
        } else if (TextUtils.isEmpty(strUserEmail)) {
            email.setError("Email cannot be empty");
            email.requestFocus();
        } else if (TextUtils.isEmpty(strUserPassword)) {
            password.setError("Password cannot be empty");
            password.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(strUserEmail).matches()) {
            email.setError("Please enter a valid email");
            email.requestFocus();
        } else if (strUserPassword.length() < 6) {
            password.setError("Too short password length");
            password.requestFocus();
        } else if (!strUserPassword.equals(confirmPassword.getText().toString())) {
            confirmPassword.setError("Password does not matched");
            confirmPassword.requestFocus();
        } else {
            addUser();
        }
    }

    // function to add user to the firebase database
    private void addUser() {

        // launching progress bar
        progressDialog = Methods.launchProgressDialog(progressDialog, RegisterActivity.this);

        mAuth.createUserWithEmailAndPassword(strUserEmail, strUserPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Registered successfully \nPlease check your email to verify", Toast.LENGTH_LONG).show();
                                UserId = mAuth.getCurrentUser().getUid();

                                registerUser();

                            } else {
                                Log.e(Constants.msg, "Unable to send verification mail - " + task.getException().getMessage());
                            }
                        }
                    });
                } else {
                    progressDialog.dismiss();
                    String registerErrorMsg = task.getException().getMessage();
                    Log.e(Constants.msg, "Not Registered - " + registerErrorMsg);

                    if (registerErrorMsg.equals("The email address is already in use by another account.")) {
                        email.setError("This email is already registered");
                        email.requestFocus();
                    } else {
                        Toast.makeText(RegisterActivity.this, registerErrorMsg, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    // register the user information in FireStore cloud database in collection named "user(Batch-Branch)" and document named as userid
    private void registerUser() {
        DocumentReference documentReference = mStore.collection("user(" + strUserBatch + "-" + strUserBranch + ")").document(UserId);
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
                Log.d(Constants.msg, "user profile is created");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.e(Constants.msg, "user profile is not created");
            }
        });
    }

}