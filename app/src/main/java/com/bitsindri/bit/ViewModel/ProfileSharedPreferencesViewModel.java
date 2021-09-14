package com.bitsindri.bit.ViewModel;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bitsindri.bit.Repository.ProfileSharedPreferencesRepository;
import com.bitsindri.bit.models.User;

import java.util.ArrayList;

public class ProfileSharedPreferencesViewModel extends AndroidViewModel {

    private final Application application;
    private final MutableLiveData<User> user;

    public ProfileSharedPreferencesViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        user = ProfileSharedPreferencesRepository.getInstance(application).getUser();
    }

    // call to get user data
    public LiveData<User> getUser(){
        return user;
    }

    // call to update user data
    public void updateUser(User updatedUser){
        ProfileSharedPreferencesRepository.getInstance(application).updateUser(updatedUser);
    }

    public void  uploadProfilePicInStorage(Uri uri, ImageView img, ProgressBar progressBar){
        ProfileSharedPreferencesRepository.getInstance(application).uploadProfilePicInStorage(uri,img,progressBar);
    }

    public ArrayList<User> getAllUsers(ProgressBar progressBar){
        return ProfileSharedPreferencesRepository.getInstance(application).getAllUsers(progressBar);
    }

    public void clearLoginInfo(){
        ProfileSharedPreferencesRepository.getInstance(application).clearLoginInfo();
    }


}
