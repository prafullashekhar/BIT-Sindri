package com.bitsindri.bit.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.bitsindri.bit.R;
import com.bitsindri.bit.methods.Resource;
import com.bitsindri.bit.models.Club;
import com.bitsindri.bit.models.ClubData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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
    private static MutableLiveData<ClubData> clubDataMutable;
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

    public MutableLiveData<ClubData> getClubData(long clubId){
        if(clubDataMutable == null){
            clubDataMutable = new MutableLiveData<>();
        }
        new FetchClubDataAsyncTask().execute(clubId);
        return clubDataMutable;
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


    private static class FetchClubDataAsyncTask extends AsyncTask<Long, Void, Void>
    {
        private DocumentReference reference;

        @Override
        protected Void doInBackground(Long... longs) {
            ClubData clubData = new ClubData();
            Long clubId = longs[0];
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            reference = db.collection("Clubs").document(String.valueOf(clubId));

            reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        clubData.setClubName(documentSnapshot.getString("ClubName"));
                        clubData.setClubLogoUrl((documentSnapshot.getString("clubLogoUrl")));
                        clubData.setClubDescription(documentSnapshot.getString("clubDescription"));
                        clubData.setClubAchievements(documentSnapshot.getString("clubAchievements"));
                        clubData.setClubTags((List<String>)documentSnapshot.get("clubTags"));

                        clubData.setClubFacebook(documentSnapshot.getString("clubFacebook"));
                        clubData.setClubInstagram(documentSnapshot.getString("clubInstagram"));
                        clubData.setClubLinkedIn(documentSnapshot.getString("clubLinkedIn"));
                        clubData.setClubTwitter(documentSnapshot.getString("clubTwitter"));
                        clubData.setClubGmail(documentSnapshot.getString("clubGmail"));
                        clubData.setClubWebsite(documentSnapshot.getString("clubWebsite"));

                        clubDataMutable.setValue(clubData);
                    }
                }
            });

            return null;
        }

    }

}
