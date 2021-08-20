package com.bitsindri.bit.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
import com.bitsindri.bit.authenticationfrag.LoginFragment;
import com.bitsindri.bit.authenticationfrag.RegisterFragment;
import com.bitsindri.bit.authenticationfrag.SignUpButtonClickListener;
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

public class AuthenticationActivity extends AppCompatActivity implements SignUpButtonClickListener {

    private FirebaseAuth mAuth;
    LoginFragment fragment;
    private boolean isRegisterActive = false;

    @Override
    public void onBackPressed() {
        if(isRegisterActive){
            getSupportFragmentManager().beginTransaction().replace(R.id.authentication_container,fragment).commit();
            isRegisterActive = false;
        }
        else super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        mAuth = FirebaseAuth.getInstance();
        fragment = new LoginFragment();
        fragment.setSignUpClickListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.authentication_container,fragment).commit();
    }



    // handling onStart
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null && user.isEmailVerified()){
            startActivity(new Intent(AuthenticationActivity.this, MainActivity.class));
        }
    }

    @Override
    public void onSignUpClick(View view) {
        if(view.getId() == R.id.signUp) {
            RegisterFragment registerFragment = new RegisterFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.authentication_container, registerFragment).commit();
            isRegisterActive = true;
        }
    }

}