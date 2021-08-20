package com.bitsindri.bit.ViewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bitsindri.bit.Repository.ImgUrlRepository;
import com.bitsindri.bit.models.SlidingImgUrl;

import java.util.List;

public class ImgUrlViewModel extends AndroidViewModel
{
    private final ImgUrlRepository imgUrlRepository;
    private LiveData<List<SlidingImgUrl>> allImgUrl;

    public ImgUrlViewModel(@NonNull Application application) {
        super(application);
        imgUrlRepository = new ImgUrlRepository(application);
        allImgUrl = imgUrlRepository.getAllImgUrl();
    }

    public LiveData<List<SlidingImgUrl>> getAllImgUrl(){
        return allImgUrl;
    }

}
