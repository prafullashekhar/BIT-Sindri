package com.bitsindri.bit.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bitsindri.bit.R;

public class OnBoardingActivity extends AppCompatActivity {

    Button signIn, Register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        // initialising
        signIn = findViewById(R.id.signin);
        Register = findViewById(R.id.register);

//        signIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(OnBoardingActivity.this, LoginActivity.class));
//            }
//        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OnBoardingActivity.this, RegisterActivity.class));
            }
        });
    }
}