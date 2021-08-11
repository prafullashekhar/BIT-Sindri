package com.bitsindri.bit;

import android.app.Application;

import com.onesignal.OneSignal;

public class MyApplication extends Application {

    private static final String ONESIGNAL_APP_ID = "4bb4d4de-1d1a-4298-bece-892666741ab9";

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
    }

}
