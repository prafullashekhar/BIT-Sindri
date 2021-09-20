package com.bitsindri.bit.Repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.bitsindri.bit.methods.Constants;
import com.bitsindri.bit.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class StorageUtil {
    private Context context;
    private String storage_name = Constants.SHARED_PREF_FILE;
    private SharedPreferences sharedPreferences;

    public StorageUtil(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(storage_name, Context.MODE_PRIVATE);
    }

    public void storeUser(User user) {
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

    }

    public User getUser() {
        User user = new User(
                sharedPreferences.getString(Constants.NAME, ""),
                sharedPreferences.getString(Constants.EMAIL, ""),
                sharedPreferences.getString(Constants.BATCH, ""),
                sharedPreferences.getString(Constants.BRANCH, ""),
                sharedPreferences.getString(Constants.ROLL, ""),
                sharedPreferences.getString(Constants.REG, ""),
                sharedPreferences.getString(Constants.ABOUT, ""),
                sharedPreferences.getString(Constants.CODECHEF, ""),
                sharedPreferences.getString(Constants.LINKEDIN, ""),
                sharedPreferences.getString(Constants.FACEBOOK, ""),
                sharedPreferences.getString(Constants.INSTAGRAM, ""),
                sharedPreferences.getString(Constants.GITHUB, ""),
                sharedPreferences.getString(Constants.CODEFORCES, ""),
                sharedPreferences.getString(Constants.PROFILE_PIC, ""),
                sharedPreferences.getString(Constants.DOB, ""),
                sharedPreferences.getString(Constants.CLUB, "")
        );
        return user;
    }

    public void clearLoginInfo(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public String getName() {
        return sharedPreferences.getString(Constants.NAME, "");
    }
    public String getProfilePic(){
        return sharedPreferences.getString(Constants.PROFILE_PIC, "");
    }
}
