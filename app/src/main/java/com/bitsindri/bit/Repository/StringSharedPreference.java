package com.bitsindri.bit.Repository;

import android.content.SharedPreferences;

public class StringSharedPreference extends SharedPreferenceLiveData<String> {

    public StringSharedPreference(SharedPreferences prefs, String key, String defaultValue) {
        super(prefs, key, defaultValue);
    }

    @Override
    protected String getValueFromPreference(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public SharedPreferenceLiveData<String> getStringLiveData(String key, String defaultValue){
        return new SharedPreferenceLiveData<String>(sharedPreferences, key, defaultValue) {
            @Override
            protected String getValueFromPreference(String key, String defaultValue) {
                return defaultValue;
            }
        };
    }
}
