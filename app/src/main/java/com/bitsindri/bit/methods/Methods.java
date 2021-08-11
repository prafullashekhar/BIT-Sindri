package com.bitsindri.bit.methods;

import android.app.ProgressDialog;
import android.content.Context;

import com.bitsindri.bit.R;

public class Methods {

    /*
     call to launch progress dialog
     ------how to use ------
     private ProgressDialog progressDialog;
     progressDialog = Methods.launchProgressDialog(progressDialog, LoginActivity.this);
     */
    public static ProgressDialog launchProgressDialog(ProgressDialog progressDialog, Context context){
        // launching progress bar
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        return progressDialog;
    }

}
