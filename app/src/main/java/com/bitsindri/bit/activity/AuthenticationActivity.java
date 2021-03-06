package com.bitsindri.bit.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.bitsindri.bit.MainActivity;
import com.bitsindri.bit.R;
import com.bitsindri.bit.authenticationfrag.LoginFragment;
import com.bitsindri.bit.authenticationfrag.RegisterFragment;
import com.bitsindri.bit.authenticationfrag.SignUpButtonClickListener;
import com.bitsindri.bit.databinding.ActivityAuthenticationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationActivity extends AppCompatActivity implements SignUpButtonClickListener {

    private ActivityAuthenticationBinding binding;

    private FirebaseAuth mAuth;
    LoginFragment fragment;
    private boolean isRegisterActive = false;

    @Override
    public void onBackPressed() {
        if(isRegisterActive){
            getSupportFragmentManager().beginTransaction().replace(R.id.authentication_container,fragment).commit();
            binding.authenticationContainer.setAnimation(AnimationUtils.loadAnimation(AuthenticationActivity.this,R.anim.up_to_down));
            isRegisterActive = false;
        }
        else super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
            binding.authenticationContainer.setAnimation(AnimationUtils.loadAnimation(AuthenticationActivity.this,R.anim.down_to_up));
            isRegisterActive = true;
        }
    }

}