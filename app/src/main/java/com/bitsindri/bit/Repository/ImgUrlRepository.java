package com.bitsindri.bit.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.bitsindri.bit.Dao.ImgUrlDao;
import com.bitsindri.bit.DataBase.RoomDB;
import com.bitsindri.bit.models.SlidingImgUrl;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ImgUrlRepository {
    private final RoomDB roomDB;
    private LiveData<List<SlidingImgUrl>> allImgUrl;

    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public ImgUrlRepository(@NonNull Application application){
        roomDB = RoomDB.getDatabase(application);
        ImgUrlDao imgUrlDao = roomDB.getImgUrlDao();
        allImgUrl = imgUrlDao.getHomeSlidingImageUrl();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<SlidingImgUrl>> getAllImgUrl() {
        if(allImgUrl==null)
            loadUrl();
        return allImgUrl;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(List<SlidingImgUrl> list) {
        new InsertAsyncTask(roomDB).execute(list);
    }
    public void deleteAll(){
        new DeleteAsyncTask(roomDB).execute();
    }


    // loads data from realtime database firebase
    private void loadUrl() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("SlidingImage").child("HomeTop");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<SlidingImgUrl> urlList = new ArrayList<>();
                for(DataSnapshot dss : snapshot.getChildren()){
                    String imgUrl = dss.getValue(String.class);
                    assert imgUrl != null;
                    urlList.add(new SlidingImgUrl(imgUrl));
                }
                deleteAll();
                insert(urlList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    // AsyncTask to insert
    private static class InsertAsyncTask extends AsyncTask<List<SlidingImgUrl>, Void, Void>
    {
        private final ImgUrlDao imgUrlDao;
        InsertAsyncTask(RoomDB roomDB){
            imgUrlDao = roomDB.getImgUrlDao();
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<SlidingImgUrl>... lists) {
            imgUrlDao.insert(lists[0]);
            return null;
        }
    }

    // AsyncTask to deleteAll
    private static class DeleteAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private final ImgUrlDao imgUrlDao;
        DeleteAsyncTask(RoomDB roomDB){
            imgUrlDao = roomDB.getImgUrlDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            imgUrlDao.deleteAll();
            return null;
        }
    }
}
