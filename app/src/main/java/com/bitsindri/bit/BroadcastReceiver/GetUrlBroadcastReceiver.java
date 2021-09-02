package com.bitsindri.bit.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class GetUrlBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String url= intent.getDataString();
        Toast.makeText(context, url, Toast.LENGTH_SHORT).show();

    }

}
