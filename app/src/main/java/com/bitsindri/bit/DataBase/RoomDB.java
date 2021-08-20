package com.bitsindri.bit.DataBase;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.bitsindri.bit.Dao.ImgUrlDao;
import com.bitsindri.bit.models.SlidingImgUrl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {SlidingImgUrl.class}, version = 1, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    private static final String DATABASE_NAME = "room_database";

    // returns dao
    public abstract ImgUrlDao getImgUrlDao();

    private static volatile RoomDB INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static RoomDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDB.class,
                            DATABASE_NAME)
                            .addCallback(callback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Callback to clear table when database is created for the first time
    private static final Callback callback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateAsyncTask(INSTANCE);
        }
    };

    private static class PopulateAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private final ImgUrlDao imgUrlDao;

        PopulateAsyncTask(RoomDB roomDB){
            imgUrlDao = roomDB.getImgUrlDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            imgUrlDao.deleteAll();
            return null;
        }
    }

}
