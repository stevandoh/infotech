package com.iinfotechsystemsonline.app.utilities;


import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.iinfotechsystemsonline.app.R;

import java.net.ConnectException;
import java.util.concurrent.TimeoutException;

import androidx.appcompat.app.AlertDialog;

public class ServerUtils {

    public static void checkConnectivity(Context context, Throwable t){
        if (t instanceof ConnectException || t instanceof TimeoutException){
            setMsg(context);

        }
    }

    private  static void setMsg(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Slow or No Internet Connection.\n Please check your connectivity")
                .setTitle("Network Error");
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
