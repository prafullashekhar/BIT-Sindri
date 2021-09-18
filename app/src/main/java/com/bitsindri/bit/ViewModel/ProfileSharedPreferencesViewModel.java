package com.bitsindri.bit.ViewModel;

import android.app.Application;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bitsindri.bit.Repository.ClubsDataRepository;
import com.bitsindri.bit.Repository.ProfileSharedPreferencesRepository;
import com.bitsindri.bit.methods.Resource;
import com.bitsindri.bit.models.Club;
import com.bitsindri.bit.models.User;

import java.util.ArrayList;
import java.util.List;

public class ProfileSharedPreferencesViewModel extends AndroidViewModel {

    private final Application application;
    private final MutableLiveData<User> _user;
    public final LiveData<User> user;
    private MutableLiveData<Resource<ArrayList<Club>>> _clubs = new MutableLiveData<>();
    public LiveData<Resource<ArrayList<Club>>> clubs = _clubs;

    public ProfileSharedPreferencesViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        _user = ProfileSharedPreferencesRepository.getInstance(application).getUser();
        user = _user;
        _clubs.postValue(Resource.loading(null));
        _clubs.postValue(Resource.success(ClubsDataRepository.getInstance(application).getClubs()));
    }

    // call to update user data
    public void updateUser(User updatedUser){
        ProfileSharedPreferencesRepository.getInstance(application).updateUser(updatedUser);
    }

    // call to upload and update profile pic
    public void  uploadProfilePicInStorage(Uri uri, ImageView img, ProgressBar progressBar){
        ProfileSharedPreferencesRepository.getInstance(application).uploadProfilePicInStorage(uri,img,progressBar);
    }

    // call to clear loggedIn user data
    public void clearLoginInfo(){
        ProfileSharedPreferencesRepository.getInstance(application).clearLoginInfo();
    }

}
