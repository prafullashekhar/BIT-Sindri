package com.bitsindri.bit.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.bitsindri.bit.methods.Constants;
import com.bitsindri.bit.models.SlidingImgUrl;

import java.util.List;

@Dao
public interface ImgUrlDao {

    // not allowing the insert of the same URL multiple times by passing a
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<SlidingImgUrl> list);

    @Query("DELETE FROM " + Constants.IMG_URL_TABLE)
    void deleteAll();

    @Query("SELECT * FROM " + Constants.IMG_URL_TABLE)
    LiveData<List<SlidingImgUrl>> getHomeSlidingImageUrl();
}
