package com.bitsindri.bit.Repository;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.bitsindri.bit.methods.Constants;
import com.bitsindri.bit.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileSharedPreferencesRepository {

    private final SharedPreferences sharedPreference;
    private User user;

    private static ProfileSharedPreferencesRepository INSTANCE;
    Context context;

    public static ProfileSharedPreferencesRepository getInstance(Application application){
        if(INSTANCE == null){
            INSTANCE = new ProfileSharedPreferencesRepository(application);
        }
        return INSTANCE;
    }

    public ProfileSharedPreferencesRepository(Application application){
        // sharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = application;
        sharedPreference = application.getSharedPreferences(Constants.SHARED_PREF_FILE, Context.MODE_PRIVATE);
    }

    public MutableLiveData<User> getUser(){
        if(user == null)
            loadData();
        MutableLiveData<User> mutableUser = new MutableLiveData<>();
        mutableUser.setValue(user);
        return mutableUser;
    }

    // updates the user date in shared preferences and updates it also in online database
    public void updateUser(User updatedUser){
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putString(Constants.NAME, updatedUser.getName());
        editor.putString(Constants.BATCH, updatedUser.getBatch());
        editor.putString(Constants.EMAIL, updatedUser.getEmail());
        editor.putString(Constants.BRANCH, updatedUser.getBranch());
        editor.putString(Constants.DOB, updatedUser.getDob());
        editor.putString(Constants.ROLL, updatedUser.getRollNo());
        editor.putString(Constants.REG, updatedUser.getRegNo());
        editor.putString(Constants.PROFILE_PIC, updatedUser.getProfilePic());
        editor.putString(Constants.CLUB, updatedUser.getClub());
        editor.putString(Constants.CODECHEF, updatedUser.getCodechefUrl());
        editor.putString(Constants.CODEFORCES, updatedUser.getCodefrocesUrl());
        editor.putString(Constants.GITHUB, updatedUser.getGithubUrl());
        editor.putString(Constants.LINKEDIN, updatedUser.getLinkedInUrl());
        editor.putString(Constants.FACEBOOK, updatedUser.getFacebookUrl());
        editor.putString(Constants.INSTAGRAM, updatedUser.getInstaUrl());
        editor.putString(Constants.ABOUT, updatedUser.getAbout());
        editor.apply();

        uploadData(updatedUser);
    }


    // functions to load and upload data from fire store database
    private void loadData() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        user = new User();
        assert currentUser != null;
        DocumentReference reference = db.collection("Users").document(currentUser.getUid());

        reference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            user.setName(documentSnapshot.getString("Name"));
                            user.setEmail(documentSnapshot.getString("Email"));
                            user.setBatch(documentSnapshot.getString("Batch"));
                            user.setBranch(documentSnapshot.getString("Branch"));
                            user.setRollNo(documentSnapshot.getString("Roll"));
                            user.setRegNo(documentSnapshot.getString("RegNo"));
                            user.setDob(documentSnapshot.getString("DOB"));
                            user.setProfilePic(documentSnapshot.getString("ProfilePic"));
                            user.setClub(documentSnapshot.getString("Club"));
                            user.setCodechefUrl(documentSnapshot.getString("Codechef"));
                            user.setCodefrocesUrl(documentSnapshot.getString("Codeforces"));
                            user.setGithubUrl(documentSnapshot.getString("Github"));
                            user.setLinkedInUrl(documentSnapshot.getString("LinkedIn"));
                            user.setFacebookUrl(documentSnapshot.getString("Facebook"));
                            user.setInstaUrl(documentSnapshot.getString("Instagram"));
                            user.setAbout(documentSnapshot.getString("About"));

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(Constants.msg, "Unable to get user data "+e.toString());
            }
        });

    }

    private void uploadData(User updatedUser) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        assert currentUser != null;
        DocumentReference reference = db.collection("Users").document(currentUser.getUid());

        Map<String, Object> map = new HashMap<>();
        map.put("Name", updatedUser.getName());
        map.put("Email", updatedUser.getEmail());
        map.put("Batch", updatedUser.getBatch());
        map.put("Branch", updatedUser.getBranch());
        map.put("Roll", updatedUser.getRollNo());
        map.put("RegNo", updatedUser.getRegNo());
        map.put("DOB", updatedUser.getDob());
        map.put("ProfilePic", updatedUser.getProfilePic());
        map.put("Club", updatedUser.getClub());
        map.put("Codechef", updatedUser.getCodechefUrl());
        map.put("Codeforces", updatedUser.getCodefrocesUrl());
        map.put("Github", updatedUser.getGithubUrl());
        map.put("LinkedIn", updatedUser.getLinkedInUrl());
        map.put("Facebook", updatedUser.getFacebookUrl());
        map.put("Instagram", updatedUser.getInstaUrl());
        map.put("About", updatedUser.getAbout());

        reference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(Constants.msg, "unable to update user");
            }
        });

    }


}
