package com.Simone.Giuseppe.Help_Smart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Calendar;

public class MyPhoneStateListener extends PhoneStateListener {
    public Context ctx;
    public static Boolean phoneRinging = false;
    private SharedPreferences Settings, mapTrack;
    private String message;
    Calendar calendar;


    public MyPhoneStateListener(Context context){
        ctx=context;
        calendar = Calendar.getInstance();

        Settings = ctx.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        mapTrack = ctx.getSharedPreferences("track" + calendar.get(Calendar.DAY_OF_WEEK), Context.MODE_PRIVATE);
        message="Messaggio automatico di emergenza: ho bisogno del tuo aiuto la mia posizione è:\n " ;
    }

    public static double startTime=0,elapsedTime=0, endTime=0;
    private boolean autorizeSms=false;

    public void onCallStateChanged(int state, String incomingNumber) {


        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                Log.e("elapsedIDLE",Double.toString(elapsedTime/1000));

                if(PlayerService.SecondCall) {
                    if(endTime==0)
                    endTime=System.currentTimeMillis();
                    elapsedTime=endTime-startTime;
                    Log.e("Start",Double.toString(startTime));
                    Log.e("End",Double.toString(endTime));
                    Log.e("elapsed",Double.toString(elapsedTime/1000));
                    if(elapsedTime/1000>=10) {
                        Log.d("DEBUG", "IDLE");
                        if (Settings.getString("NumeroEmergenza2", "") != "") {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + Settings.getString("NumeroEmergenza2", "")));
                            PlayerService.SecondCall = false;
                            autorizeSms=true;
                            endTime=0;
                            startTime=0;
                            elapsedTime=0;
                            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            ctx.startActivity(callIntent);
                        }else {
                            Log.e("else","1");
                            autorizeSms=false;
                            endTime = 0;
                            startTime=0;
                            elapsedTime=0;
                            PlayerService.sms=false;
                            PlayerService.SecondCall = false;
                            if(!PlayerService.keyEmergency) {
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(Settings.getString("NumeroEmergenza", ""), null, message +"\n"+ "http://maps.google.com/maps?f=q&q=" + mapTrack.getFloat("lat" + (mapTrack.getInt("cnt", 0) - 1), 0) + "," + mapTrack.getFloat("lon" + (mapTrack.getInt("cnt", 0) - 1), 0) + "&z=16", null, null);
                                Log.e("Send", "SMS1");
                                PlayerService.keyEmergency=false;

                            }
                        }
                    }else{
                        Log.e("else","2");
                        PlayerService.sms=false;
                        PlayerService.SecondCall = false;
                        startTime=0;
                        elapsedTime=0;
                        endTime=0;
                        Log.e("StartIDLE",Double.toString(startTime));
                    }
                }
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                Log.e("StartOFFHOOK",Double.toString(startTime));
                Log.e("endOFFHOOK",Double.toString(endTime));
                Log.e("elapsedOFFHOOK",Double.toString(elapsedTime/1000));

                if(PlayerService.sms) {
                    Log.e("offhook","offhooksms");
                    if(startTime==0)
                    startTime=System.currentTimeMillis();
                   /* try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                    }*/
                    Log.e("autorizeSms",Boolean.toString(autorizeSms));
                    Log.e("keyemergency",Boolean.toString(PlayerService.keyEmergency));
                    if(autorizeSms && !PlayerService.keyEmergency) {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(Settings.getString("NumeroEmergenza", ""), null, message +"\n"+ "http://maps.google.com/maps?f=q&q=" + mapTrack.getFloat("lat" + (mapTrack.getInt("cnt", 0) - 1), 0) + "," + mapTrack.getFloat("lon" + (mapTrack.getInt("cnt", 0) - 1), 0) + "&z=16", null, null);
                        Log.e("Send","SMS1");
                        try {
                        Thread.sleep(500);
                    } catch (InterruptedException ie) {
                    }
                        if (Settings.getString("NumeroEmergenza2", "") != "")
                        smsManager.sendTextMessage(Settings.getString("NumeroEmergenza2", ""), null, message +"\n"+ "http://maps.google.com/maps?f=q&q=" + mapTrack.getFloat("lat" + (mapTrack.getInt("cnt", 0) - 1), 0) + "," + mapTrack.getFloat("lon" + (mapTrack.getInt("cnt", 0) - 1), 0) + "&z=16", null, null);
                        Log.d("Send","SMS2");
                            PlayerService.sms = false;
                       autorizeSms=false;
                       startTime=0;
                       endTime=0;
                        elapsedTime=0;
                        PlayerService.keyEmergency=false;
                        PlayerService.SecondCall = false;
                        Log.d("DEBUG", "SMSEND");
                    }
                }
                Log.d("DEBUG", "OFFHOOK");

                break;
            case TelephonyManager.CALL_STATE_RINGING:
                Log.d("DEBUG", "RINGING");

                break;
        }
    }

}