package com.example.usiuparking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.appcompat.app.AppCompatActivity;

public class CheckNetworkStatus  {

    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager cManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = cManager.getActiveNetworkInfo();
        return activeInfo != null &&activeInfo.isConnected();
    }

}
