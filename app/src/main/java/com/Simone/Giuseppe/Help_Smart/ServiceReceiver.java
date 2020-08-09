package com.Simone.Giuseppe.Help_Smart;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class ServiceReceiver extends BroadcastReceiver {
    TelephonyManager telephony;

    public void onReceive(Context context, Intent intent) {
        Log.e("receiver","in");
        MyPhoneStateListener phoneListener = new MyPhoneStateListener(context);
        telephony = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    public void onDestroy() {
        telephony.listen(null, PhoneStateListener.LISTEN_NONE);
    }

}

