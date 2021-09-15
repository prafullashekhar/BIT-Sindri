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

public class ProfileSharedPreferencesRepository {

    private final SharedPreferences sharedPreference;
    private String profileDownloadUrl;

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

        new uploadUserAsyncTask().execute(updatedUser);
    }

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

    // functions to load and upload data from fire store database
//    private void loadData() {
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = auth.getCurrentUser();
//        assert currentUser != null;
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        DocumentReference reference = db.collection("Users").document(currentUser.getUid());
//
//        reference.get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        if(documentSnapshot.exists()){
//                            SharedPreferences.Editor editor = sharedPreference.edit();
//                            editor.putString(Constants.NAME, documentSnapshot.getString(Constants.NAME));
//                            editor.putString(Constants.BATCH, documentSnapshot.getString(Constants.BATCH));
//                            editor.putString(Constants.EMAIL, documentSnapshot.getString(Constants.EMAIL));
//                            editor.putString(Constants.BRANCH, documentSnapshot.getString(Constants.BRANCH));
//                            editor.putString(Constants.DOB, documentSnapshot.getString(Constants.DOB));
//                            editor.putString(Constants.ROLL, documentSnapshot.getString(Constants.ROLL));
//                            editor.putString(Constants.REG, documentSnapshot.getString(Constants.REG));
//                            editor.putString(Constants.PROFILE_PIC, documentSnapshot.getString(Constants.PROFILE_PIC));
//                            editor.putString(Constants.CLUB, documentSnapshot.getString(Constants.CLUB));
//                            editor.putString(Constants.CODECHEF, documentSnapshot.getString(Constants.CODECHEF));
//                            editor.putString(Constants.CODEFORCES, documentSnapshot.getString(Constants.CODEFORCES));
//                            editor.putString(Constants.GITHUB, documentSnapshot.getString(Constants.GITHUB));
//                            editor.putString(Constants.LINKEDIN, documentSnapshot.getString(Constants.LINKEDIN));
//                            editor.putString(Constants.FACEBOOK, documentSnapshot.getString(Constants.FACEBOOK));
//                            editor.putString(Constants.INSTAGRAM, documentSnapshot.getString(Constants.INSTAGRAM));
//                            editor.putString(Constants.ABOUT, documentSnapshot.getString(Constants.ABOUT));
//
//                            editor.apply();
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.e(Constants.msg, "Unable to get user data "+e.toString());
//            }
//        });
//
//    }

//    private void uploadData(User updatedUser) {
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = auth.getCurrentUser();
//        assert currentUser != null;
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        DocumentReference reference = db.collection("Users").document(currentUser.getUid());
//
//        Map<String, Object> map = new HashMap<>();
//        map.put(Constants.NAME, updatedUser.getName());
//        map.put(Constants.EMAIL, updatedUser.getEmail());
//        map.put(Constants.BATCH, updatedUser.getBatch());
//        map.put(Constants.BRANCH, updatedUser.getBranch());
//        map.put(Constants.ROLL, updatedUser.getRollNo());
//        map.put(Constants.REG, updatedUser.getRegNo());
//        map.put(Constants.DOB, updatedUser.getDob());
//        map.put(Constants.PROFILE_PIC, updatedUser.getProfilePic());
//        map.put(Constants.CLUB, updatedUser.getClub());
//        map.put(Constants.CODECHEF, updatedUser.getCodechefUrl());
//        map.put(Constants.CODEFORCES, updatedUser.getCodefrocesUrl());
//        map.put(Constants.GITHUB, updatedUser.getGithubUrl());
//        map.put(Constants.LINKEDIN, updatedUser.getLinkedInUrl());
//        map.put(Constants.FACEBOOK, updatedUser.getFacebookUrl());
//        map.put(Constants.INSTAGRAM, updatedUser.getInstaUrl());
//        map.put(Constants.ABOUT, updatedUser.getAbout());
//
//        reference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                TastyToast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT, TastyToast.SUCCESS);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.e(Constants.msg, "unable to update user");
//            }
//        });
//
//    }


//    public void uploadProfilePicInStorage(Uri imageToBeUpload , ImageView img , ProgressBar progressBar){
//        img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//        progressBar.setVisibility(View.VISIBLE);
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        FirebaseStorage store = FirebaseStorage.getInstance();
//        StorageReference reference = store.getReference().child("profile pictures").child(auth.getUid());
//
//        reference.putFile(imageToBeUpload).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        profileDownloadUrl = uri.toString();
//                        FirebaseFirestore mStore = FirebaseFirestore.getInstance();
//                        DocumentReference documentReference = mStore.collection("Users").document(auth.getUid());
//                        Map<String, Object> user = new HashMap<>();
//                        user.put("ProfilePic",profileDownloadUrl);
//                        SharedPreferences.Editor editor = sharedPreference.edit();
//                        editor.putString(Constants.PROFILE_PIC, profileDownloadUrl);
//                        editor.commit();
//                        documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                img.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                                progressBar.setVisibility(View.INVISIBLE);
//                                TastyToast.makeText(context, "Profile Pic updated", Toast.LENGTH_SHORT, TastyToast.SUCCESS);
//                            }
//                        });
//                    }
//                });
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                progressBar.setVisibility(View.INVISIBLE);
//                Log.e(Constants.msg, "Cannot upload image "+e.toString());
//            }
//        });
//    }

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
            reference.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()){
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(Constants.NAME, documentSnapshot.getString(Constants.NAME));
                                editor.putString(Constants.BATCH, documentSnapshot.getString(Constants.BATCH));
                                editor.putString(Constants.EMAIL, documentSnapshot.getString(Constants.EMAIL));
                                editor.putString(Constants.BRANCH, documentSnapshot.getString(Constants.BRANCH));
                                editor.putString(Constants.DOB, documentSnapshot.getString(Constants.DOB));
                                editor.putString(Constants.ROLL, documentSnapshot.getString(Constants.ROLL));
                                editor.putString(Constants.REG, documentSnapshot.getString(Constants.REG));
                                editor.putString(Constants.PROFILE_PIC, documentSnapshot.getString(Constants.PROFILE_PIC));
                                editor.putString(Constants.CLUB, documentSnapshot.getString(Constants.CLUB));
                                editor.putString(Constants.CODECHEF, documentSnapshot.getString(Constants.CODECHEF));
                                editor.putString(Constants.CODEFORCES, documentSnapshot.getString(Constants.CODEFORCES));
                                editor.putString(Constants.GITHUB, documentSnapshot.getString(Constants.GITHUB));
                                editor.putString(Constants.LINKEDIN, documentSnapshot.getString(Constants.LINKEDIN));
                                editor.putString(Constants.FACEBOOK, documentSnapshot.getString(Constants.FACEBOOK));
                                editor.putString(Constants.INSTAGRAM, documentSnapshot.getString(Constants.INSTAGRAM));
                                editor.putString(Constants.ABOUT, documentSnapshot.getString(Constants.ABOUT));

                                editor.apply();
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
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = auth.getCurrentUser();
            assert currentUser != null;

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            reference = db.collection("Users").document(currentUser.getUid());
        }

        @Override
        protected Void doInBackground(User... users) {
            User updatedUser = users[0];
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

    private static class uploadProfilePicInFirebaseAndPreferences extends AsyncTask<Uri, Void, Boolean>
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
            reference = store.getReference().child("profile pictures").child(auth.getUid());
            sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREF_FILE, Context.MODE_PRIVATE);
        }

        @Override
        protected Boolean doInBackground(Uri... uris) {
            Uri imageToBeUpload = uris[0];
            boolean[] success = {false};
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
                                    success[0] =true;
                                }
                            });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    success[0]=false;
                }
            });
            return success[0];
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progress.setVisibility(View.INVISIBLE);
//            if(aBoolean){
                image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                TastyToast.makeText(context, "Profile Pic updated", Toast.LENGTH_SHORT, TastyToast.SUCCESS);
//            }else{
//                TastyToast.makeText(context, "Please try again", Toast.LENGTH_SHORT, TastyToast.ERROR);
//            }
        }

    }

}
