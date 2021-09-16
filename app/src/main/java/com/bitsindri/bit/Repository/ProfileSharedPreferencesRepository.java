package com.bitsindri.bit.Repository;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.bitsindri.bit.methods.Constants;
import com.bitsindri.bit.methods.Methods;
import com.bitsindri.bit.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfileSharedPreferencesRepository {

    private final SharedPreferences sharedPreference;
    private static MutableLiveData<User> mutableUser;

    @SuppressLint("StaticFieldLeak")
    private static volatile ProfileSharedPreferencesRepository INSTANCE;
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static ProfileSharedPreferencesRepository getInstance(Application application){
        if(INSTANCE == null){
            INSTANCE = new ProfileSharedPreferencesRepository(application);
        }
        return INSTANCE;
    }

    public ProfileSharedPreferencesRepository(Application application){
        context = application;
        sharedPreference = application.getSharedPreferences(Constants.SHARED_PREF_FILE, Context.MODE_PRIVATE);
    }

    public MutableLiveData<User> getUser(){
        if(mutableUser == null){
            mutableUser = new MutableLiveData<>();
        }
        if(sharedPreference.getString(Constants.NAME, "").equals(""))
            new getUserAsyncTask().execute();

        User user = new User(
                sharedPreference.getString(Constants.NAME, ""),
                sharedPreference.getString(Constants.EMAIL, ""),
                sharedPreference.getString(Constants.BATCH, ""),
                sharedPreference.getString(Constants.BRANCH, ""),
                sharedPreference.getString(Constants.ROLL, ""),
                sharedPreference.getString(Constants.REG, ""),
                sharedPreference.getString(Constants.ABOUT, ""),
                sharedPreference.getString(Constants.CODECHEF, ""),
                sharedPreference.getString(Constants.LINKEDIN, ""),
                sharedPreference.getString(Constants.FACEBOOK, ""),
                sharedPreference.getString(Constants.INSTAGRAM, ""),
                sharedPreference.getString(Constants.GITHUB, ""),
                sharedPreference.getString(Constants.CODEFORCES, ""),
                sharedPreference.getString(Constants.PROFILE_PIC, ""),
                sharedPreference.getString(Constants.DOB, ""),
                sharedPreference.getString(Constants.CLUB, "")
        );

        mutableUser.setValue(user);
        return mutableUser;
    }

    // updates the user date in shared preferences and updates it also in online database
    public void updateUser(User updatedUser){
        new uploadUserAsyncTask().execute(updatedUser);
    }

    // clears the shared preference data
    public void clearLoginInfo() {
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.clear();
        editor.apply();
    }

    @SuppressLint("StaticFieldLeak")
    static ImageView image;
    @SuppressLint("StaticFieldLeak")
    static ProgressBar progress;
    public void uploadProfilePicInStorage(Uri imageToBeUpload , ImageView img , ProgressBar progressBar){
        image=img;
        progress=progressBar;
        img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        progressBar.setVisibility(View.VISIBLE);

        new uploadProfilePicInFirebaseAndPreferences().execute(imageToBeUpload);

    }


    /*---------------------------------------ALL TASKS ARE DONE IN BACKGROUND THREAD ------------------------------------------------------------------------------------------------------------*/
    /* ------------------------------------- async task to ger user data ---------------------------------------------------*/
    private static class getUserAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private DocumentReference reference;
        private SharedPreferences sharedPreferences;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = auth.getCurrentUser();
            assert currentUser != null;

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            reference = db.collection("Users").document(currentUser.getUid());

            sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREF_FILE, Context.MODE_PRIVATE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            User user = new User();
            reference.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()){
                                user.setName(documentSnapshot.getString(Constants.NAME));
                                user.setBatch(documentSnapshot.getString(Constants.BATCH));
                                user.setEmail(documentSnapshot.getString(Constants.EMAIL));
                                user.setBranch(documentSnapshot.getString(Constants.BRANCH));
                                user.setDob(documentSnapshot.getString(Constants.DOB));
                                user.setRollNo(documentSnapshot.getString(Constants.ROLL));
                                user.setRegNo(documentSnapshot.getString(Constants.REG));
                                user.setProfilePic(documentSnapshot.getString(Constants.PROFILE_PIC));
                                user.setClub(documentSnapshot.getString(Constants.CLUB));
                                user.setCodechefUrl(documentSnapshot.getString(Constants.CODECHEF));
                                user.setCodefrocesUrl(documentSnapshot.getString(Constants.CODEFORCES));
                                user.setGithubUrl(documentSnapshot.getString(Constants.GITHUB));
                                user.setLinkedInUrl(documentSnapshot.getString(Constants.LINKEDIN));
                                user.setFacebookUrl(documentSnapshot.getString(Constants.FACEBOOK));
                                user.setInstaUrl(documentSnapshot.getString(Constants.INSTAGRAM));
                                user.setAbout(documentSnapshot.getString(Constants.ABOUT));

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(Constants.NAME, user.getName());
                                editor.putString(Constants.BATCH, user.getBatch());
                                editor.putString(Constants.EMAIL, user.getEmail());
                                editor.putString(Constants.BRANCH, user.getBranch());
                                editor.putString(Constants.DOB, user.getDob());
                                editor.putString(Constants.ROLL, user.getRollNo());
                                editor.putString(Constants.REG, user.getRegNo());
                                editor.putString(Constants.PROFILE_PIC, user.getProfilePic());
                                editor.putString(Constants.CLUB, user.getClub());
                                editor.putString(Constants.CODECHEF, user.getCodechefUrl());
                                editor.putString(Constants.CODEFORCES, user.getCodefrocesUrl());
                                editor.putString(Constants.GITHUB, user.getGithubUrl());
                                editor.putString(Constants.LINKEDIN, user.getLinkedInUrl());
                                editor.putString(Constants.FACEBOOK, user.getFacebookUrl());
                                editor.putString(Constants.INSTAGRAM, user.getInstaUrl());
                                editor.putString(Constants.ABOUT, user.getAbout());
                                editor.apply();
                                mutableUser.setValue(user);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                          @Override
                          public void onFailure(@NonNull Exception e) {
                                Log.e(Constants.msg, "Unable to get user data "+e.toString());
                          }
                    });

            return null;
        }
    }

    /* --------------------------------- async task to update user data in fire store ---------------------------------------------*/
    private static class uploadUserAsyncTask extends AsyncTask<User, Void, Void>
    {
        private DocumentReference reference;
        private SharedPreferences sharedPreferences;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = auth.getCurrentUser();
            assert currentUser != null;

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            reference = db.collection("Users").document(currentUser.getUid());
            sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREF_FILE, Context.MODE_PRIVATE);
        }

        @Override
        protected Void doInBackground(User... users) {
            User updatedUser = users[0];

            SharedPreferences.Editor editor = sharedPreferences.edit();
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

            Map<String, Object> map = new HashMap<>();
            map.put(Constants.NAME, updatedUser.getName());
            map.put(Constants.EMAIL, updatedUser.getEmail());
            map.put(Constants.BATCH, updatedUser.getBatch());
            map.put(Constants.BRANCH, updatedUser.getBranch());
            map.put(Constants.ROLL, updatedUser.getRollNo());
            map.put(Constants.REG, updatedUser.getRegNo());
            map.put(Constants.DOB, updatedUser.getDob());
            map.put(Constants.PROFILE_PIC, updatedUser.getProfilePic());
            map.put(Constants.CLUB, updatedUser.getClub());
            map.put(Constants.CODECHEF, updatedUser.getCodechefUrl());
            map.put(Constants.CODEFORCES, updatedUser.getCodefrocesUrl());
            map.put(Constants.GITHUB, updatedUser.getGithubUrl());
            map.put(Constants.LINKEDIN, updatedUser.getLinkedInUrl());
            map.put(Constants.FACEBOOK, updatedUser.getFacebookUrl());
            map.put(Constants.INSTAGRAM, updatedUser.getInstaUrl());
            map.put(Constants.ABOUT, updatedUser.getAbout());

            reference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    TastyToast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT, TastyToast.SUCCESS);
                    mutableUser.setValue(updatedUser);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(Constants.msg, "unable to update user");
                }
            });

            return null;
        }
    }

    private static class uploadProfilePicInFirebaseAndPreferences extends AsyncTask<Uri, Void, Void>
    {
        private FirebaseAuth auth;
        private StorageReference reference;
        private String profileDownloadUrl;
        private SharedPreferences sharedPreferences;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            auth = FirebaseAuth.getInstance();
            FirebaseStorage store = FirebaseStorage.getInstance();
            reference = store.getReference().child("profile pictures").child(Objects.requireNonNull(auth.getUid()));
            sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREF_FILE, Context.MODE_PRIVATE);
        }

        @Override
        protected Void doInBackground(Uri... uris) {
            Uri imageToBeUpload = uris[0];
            reference.putFile(imageToBeUpload).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            profileDownloadUrl = uri.toString();
                            FirebaseFirestore mStore = FirebaseFirestore.getInstance();
                            DocumentReference documentReference = mStore.collection("Users").document(auth.getUid());
                            Map<String, Object> user = new HashMap<>();
                            user.put("ProfilePic",profileDownloadUrl);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Constants.PROFILE_PIC, profileDownloadUrl);
                            editor.commit();
                            documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    progress.setVisibility(View.INVISIBLE);
                                    image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    TastyToast.makeText(context, "Profile Pic updated", Toast.LENGTH_SHORT, TastyToast.SUCCESS);
                                }
                            });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progress.setVisibility(View.INVISIBLE);
                    TastyToast.makeText(context, "Please try again", Toast.LENGTH_SHORT, TastyToast.ERROR);
                }
            });
            return null;
        }

    }

}
