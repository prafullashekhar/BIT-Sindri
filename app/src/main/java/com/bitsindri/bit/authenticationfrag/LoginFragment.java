package com.bitsindri.bit.authenticationfrag;

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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bitsindri.bit.MainActivity;
import com.bitsindri.bit.R;
import com.bitsindri.bit.Repository.ProfileSharedPreferencesRepository;
import com.bitsindri.bit.ViewModel.ProfileSharedPreferencesViewModel;
import com.bitsindri.bit.activity.AuthenticationActivity;
import com.bitsindri.bit.databinding.FragmentLoginBinding;
import com.bitsindri.bit.methods.Constants;
import com.bitsindri.bit.methods.Methods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class LoginFragment extends Fragment {

    FragmentLoginBinding binding;

    public LoginFragment() {
    }

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private AlertDialog dialog;
    private SignUpButtonClickListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        // instantiating firebase
        mAuth = FirebaseAuth.getInstance();
        binding.password.setTransformationMethod(PasswordTransformationMethod.getInstance());

        // On signIn click
        binding.signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUser();
            }
        });

        // On forgotPassword click
        binding.forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail = binding.email.getText().toString().trim();

                // showing alert dialog if no email is provided
                if (TextUtils.isEmpty(strEmail)) {
                    EditText forgotEmail;
                    Button forgotResetLink;
                    final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(getContext());
                    LayoutInflater inflater = getLayoutInflater();
                    View view = inflater.inflate(R.layout.forgot_pass_dialog_box, null);

                    forgotEmail = view.findViewById(R.id.forgot_email);
                    forgotResetLink = view.findViewById(R.id.forgot_reset_link);
                    passwordResetDialog.setView(view);

                    forgotResetLink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (TextUtils.isEmpty(forgotEmail.getText().toString().trim())) {
                                forgotEmail.setError("Please enter your email Id");
                                forgotEmail.requestFocus();
                            } else {
                                sendPasswordReset(forgotEmail.getText().toString());
                            }
                        }
                    });

                    dialog = passwordResetDialog.create();
                    dialog.getWindow().setBackgroundDrawableResource(
                            android.R.color.transparent
                    );
                    dialog.show();

                } else {
                    sendPasswordReset(strEmail);
                }
            }
        });

        // On signup click
        binding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)
                listener.onSignUpClick(binding.signUp);
            }
        });

        // On show password click
        binding.passwordHideToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHidePass();
            }
        });
        return binding.getRoot();
    }

    // checks if the user data is in the database
    private void checkUser() {
        String strEmail = binding.email.getText().toString().trim();
        String strPassword = binding.password.getText().toString();

        if (TextUtils.isEmpty(strEmail)) {
            binding.email.setError("Email cannot be empty");
            binding.email.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
            binding.email.setError("Enter the correct EmailId");
            binding.email.requestFocus();
        } else if (TextUtils.isEmpty(strPassword)) {
            binding.password.setError("Password cannot be empty");
            binding.password.requestFocus();
        } else {
            // launching progress bar
            progressDialog = Methods.launchProgressDialog(progressDialog, getContext());

            mAuth.signInWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        if (mAuth.getCurrentUser().isEmailVerified()) {
                            ProfileSharedPreferencesRepository.getInstance(getActivity().getApplication()).getUser();
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getContext(), "Please first verify your email address through your email", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        String errorMsg = task.getException().getMessage();
                        Log.e(Constants.msg, "Not Registered - " + errorMsg);

                        assert errorMsg != null;
                        if (errorMsg.equals("There is no user record corresponding to this identifier. The user may have been deleted.")) {
                            String er = "This email is not a registered email \nIf you are new user first get registered";
                            Toast.makeText(getContext(), er, Toast.LENGTH_LONG).show();
                        } else if (errorMsg.equals("The email address is badly formatted.")) {
                            binding.email.setError("Enter the correct EmailId");
                            binding.email.requestFocus();
                        } else if (errorMsg.equals("The password is invalid or the user does not have a password.")) {
                            binding.password.setError("Incorrect password");
                            binding.password.requestFocus();
                        } else if (errorMsg.equals("We have blocked all requests from this device due to unusual activity. Try again later. [ Access to this account has been temporarily disabled due to many failed login attempts. You can immediately restore it by resetting your password or you can try again later. ]")) {
                            Toast.makeText(getContext(), "Too much unsuccessful attempts,\ntry after sometime", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Check you Internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    // function to reset password
    private void sendPasswordReset(String resetEmail) {

        // launching progress bar
        progressDialog = Methods.launchProgressDialog(progressDialog, getContext());

        mAuth.sendPasswordResetEmail(resetEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                dialog.dismiss();
                Toast.makeText(getContext(), "Reset link is sent to your email", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Show password on eye click
    public void showHidePass() {

        if (binding.password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
            binding.passwordHideToggle.setImageResource(R.drawable.ic_password_hide);
            //Show Password
            binding.password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            binding.passwordHideToggle.setImageResource(R.drawable.ic_password_show);
            //Hide Password
            binding.password.setTransformationMethod(PasswordTransformationMethod.getInstance());

        }
    }

    public void setSignUpClickListener(SignUpButtonClickListener listener){
        this.listener = listener;
    }
}
