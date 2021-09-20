package com.bitsindri.bit.Repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.bitsindri.bit.R;
import com.bitsindri.bit.methods.Resource;
import com.bitsindri.bit.models.Club;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class ClubsDataRepository {

    private static volatile ClubsDataRepository INSTANCE;
    private FirebaseRemoteConfig remoteConfig;
    private MutableLiveData<Resource<ArrayList<Club>>> allClubsMutable;
    private String JsonClubsData;

    public ClubsDataRepository(Application application) {
    }

    public static ClubsDataRepository getInstance(Application application){
        if(INSTANCE == null){
            INSTANCE = new ClubsDataRepository(application);
        }
        return INSTANCE;
    }

    public MutableLiveData<Resource<ArrayList<Club>>> getClubs(){
        if(allClubsMutable == null){
            allClubsMutable = new MutableLiveData<>(Resource.loading(null));
        }
        fetchClubsFromRemote();
        return allClubsMutable;
    }

    /*-----------------------------------------------------------------------------------------------*/
    private void fetchClubsFromRemote()
    {
        remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();
        remoteConfig.setConfigSettingsAsync(configSettings);
        remoteConfig.setDefaultsAsync(R.xml.clubs_list_default);
        remoteConfig.activate();
        remoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if(task.isSuccessful()){
                    JsonClubsData = remoteConfig.getString("Clubs");
                    Gson gson = new Gson();
                    List<Club> allClubList = new ArrayList<>();
                    allClubList = gson.fromJson(JsonClubsData,new TypeToken<ArrayList<Club>>(){}.getType());
                    allClubsMutable.setValue(Resource.success((ArrayList<Club>) allClubList));
                }
            }
        });
    }

}
