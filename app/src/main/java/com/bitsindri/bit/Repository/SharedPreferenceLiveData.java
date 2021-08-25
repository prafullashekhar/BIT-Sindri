package com.bitsindri.bit.Repository;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;

public abstract class SharedPreferenceLiveData<T> extends LiveData<T> {

    SharedPreferences sharedPreferences;
    String key;
    T defaultValue;

    public SharedPreferenceLiveData(SharedPreferences prefs, String key, T defaultValue){
        this.sharedPreferences = prefs;
        this.key = key;
        this.defaultValue = defaultValue;
    }

    private final SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    if(SharedPreferenceLiveData.this.key.equals(key)){
                        setValue(getValueFromPreference(key, defaultValue));
                    }
                }
            };

    protected abstract T getValueFromPreference(String key, T defaultValue);

    @Override
    protected void onActive() {
        super.onActive();
        setValue(getValueFromPreference(key, defaultValue));
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    @Override
    protected void onInactive() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
        super.onInactive();
    }
}
