package com.Simone.Giuseppe.Help_Smart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SpeedReceiver extends BroadcastReceiver
{


    @Override
    public void onReceive(Context context, Intent intent) {
        try
        {
            Log.d("RECEIVER", "onReceive() called");





            // uncomment this line if you had sent some data
            String data = Float.toString(intent.getFloatExtra("Speed",0)); // data is a key specified to intent while sending broadcast

            MainActivity.getInstace().updateUI(data);
        }

        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


}
