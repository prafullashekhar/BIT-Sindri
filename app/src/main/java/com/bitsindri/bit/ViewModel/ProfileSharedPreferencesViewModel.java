package com.bitsindri.bit.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bitsindri.bit.Repository.ProfileSharedPreferencesRepository;
import com.bitsindri.bit.models.User;

public class ProfileSharedPreferencesViewModel extends AndroidViewModel {

    private final ProfileSharedPreferencesRepository repository;
    private MutableLiveData<User> user;

    public ProfileSharedPreferencesViewModel(@NonNull Application application) {
        super(application);
        repository = new ProfileSharedPreferencesRepository(application);
        user = repository.getUser();
    }

    // call to get user data
    public LiveData<User> getUser(){
        return user;
    }

    // call to update user data
    public void updateUser(User updatedUser){
        repository.updateUser(updatedUser);
    }
}
