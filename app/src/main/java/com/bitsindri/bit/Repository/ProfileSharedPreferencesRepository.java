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
import com.bitsindri.bit.methods.Resource;
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
    private static MutableLiveData<Resource<User>> mutableUser;

    @SuppressLint("StaticFieldLeak")
    private static volatile ProfileSharedPreferencesRepository INSTANCE;
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static StorageUtil storageUtil;

    public static ProfileSharedPreferencesRepository getInstance(Application application){
        if(INSTANCE == null){
            INSTANCE = new ProfileSharedPreferencesRepository(application);
        }
        return INSTANCE;
    }

    public ProfileSharedPreferencesRepository(Application application){
        context = application;
        storageUtil = new StorageUtil(context);
    }

    public MutableLiveData<Resource<User>> getUser(){
        if(mutableUser == null){
            mutableUser = new MutableLiveData<>(Resource.loading(new User()));
        }
        if(storageUtil.getName().equals("")) {
            new getUserAsyncTask().execute();
        }
        else {
            mutableUser.setValue(Resource.success(storageUtil.getUser()));
        }
        return mutableUser;
    }

    // updates the user date in shared preferences and updates it also in online database
    public void updateUser(User updatedUser){
        mutableUser.setValue(Resource.loading(storageUtil.getUser()));
        new uploadUserAsyncTask().execute(updatedUser);
    }

    // clears the shared preference data
    public void clearLoginInfo() {
        mutableUser.setValue(Resource.loading(new User()));
        storageUtil.clearLoginInfo();
    }

    @SuppressLint("StaticFieldLeak")
    static ImageView image;
    public void uploadProfilePicInStorage(Uri imageToBeUpload , ImageView img){
        image=img;
        img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        new uploadProfilePicInFirebaseAndPreferences().execute(imageToBeUpload);

    }


    /*---------------------------------------ALL TASKS ARE DONE IN BACKGROUND THREAD ------------------------------------------------------------------------------------------------------------*/
    /* ------------------------------------- async task to ger user data ---------------------------------------------------*/
    private static class getUserAsyncTask extends AsyncTask<Void, Void, Void>
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

                                mutableUser.setValue(Resource.success(user));
                                storageUtil.storeUser(user);
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
                    storageUtil.storeUser(updatedUser);
                    TastyToast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT, TastyToast.SUCCESS);
                    mutableUser.setValue(Resource.success(updatedUser));
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
        private User currentUser;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            auth = FirebaseAuth.getInstance();
            FirebaseStorage store = FirebaseStorage.getInstance();
            reference = store.getReference().child("profile pictures").child(Objects.requireNonNull(auth.getUid()));
            sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREF_FILE, Context.MODE_PRIVATE);
            currentUser = storageUtil.getUser();
            mutableUser.setValue(Resource.loading(currentUser));
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
                            documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    currentUser.setProfilePic(profileDownloadUrl);
                                    storageUtil.storeUser(currentUser);
//                                    progress.setVisibility(View.INVISIBLE);
                                    image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    TastyToast.makeText(context, "Profile Pic updated", Toast.LENGTH_SHORT, TastyToast.SUCCESS);
                                    mutableUser.setValue(Resource.success(currentUser));
                                }
                            });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    progress.setVisibility(View.INVISIBLE);
                    TastyToast.makeText(context, "Please try again", Toast.LENGTH_SHORT, TastyToast.ERROR);
                }
            });
            return null;
        }

    }

}