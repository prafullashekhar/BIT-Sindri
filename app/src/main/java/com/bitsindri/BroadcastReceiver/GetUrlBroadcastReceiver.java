package com.bitsindri.BroadcastReceiver;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.bitsindri.bit.MainActivity;
import com.bitsindri.bit.Repository.ProfileSharedPreferencesRepository;
import com.bitsindri.bit.methods.Constants;
import com.bitsindri.bit.models.User;

public class GetUrlBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String url= intent.getDataString();
        Toast.makeText(context, url, Toast.LENGTH_SHORT).show();

    }
}
