package com.bitsindri.bit.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.bitsindri.bit.methods.Constants;

@Entity(tableName = Constants.IMG_URL_TABLE)
public class SlidingImgUrl {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "Url")
    private String imgUrl;

    public SlidingImgUrl(@NonNull String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @NonNull
    public String getImgUrl(){
        return imgUrl;
    }
    
}
