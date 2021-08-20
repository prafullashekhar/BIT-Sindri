package com.bitsindri.bit.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.bitsindri.bit.Dao.ImgUrlDao;
import com.bitsindri.bit.DataBase.RoomDB;
import com.bitsindri.bit.models.SlidingImgUrl;

import java.util.List;

public class ImgUrlRepository {
    private final RoomDB roomDB;
    private final LiveData<List<SlidingImgUrl>> allImgUrl;

    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public ImgUrlRepository(Application application){
        roomDB = RoomDB.getDatabase(application);
        ImgUrlDao imgUrlDao = roomDB.getImgUrlDao();
        allImgUrl = imgUrlDao.getHomeSlidingImageUrl();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<SlidingImgUrl>> getAllImgUrl() {
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
