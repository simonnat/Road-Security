package com.Simone.Giuseppe.Help_Smart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class Test extends AppCompatActivity {
    private Drawable rosso ;
    private Drawable verde ;
    private View primo ;
    private Intent myService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        rosso = getDrawable(R.drawable.circler);
         verde = getDrawable(R.drawable.circleg);
        primo = findViewById(R.id.first);
        myService = new Intent(getApplicationContext(), PlayerService.class);
        myService.putExtra("test", true);
        boolean isRunning = getIntent().getBooleanExtra("running", false);

        if (isRunning == false) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(myService);
                isRunning = true;
            } else {
                startService(myService);
                isRunning = true;
            }
        } else {
            stopService(myService);
            isRunning = false;
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(

                mMessageReceiver, new IntentFilter("buttons"));
    }
    private boolean btn1=false,btn2=false;
    //private Button start=findViewById(R.id.launch);

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        @Override

        public void onReceive(Context context, Intent intent) {


            check();
        }

    };

    private void check(){
            Log.e("btn1",Boolean.toString(btn1));
            //start.setClickable(true);
            stopService(myService);
            btn1=false;
            sendButtontoMain();
            finish();

    }

    @Override
    public void onBackPressed(){
        stopService(myService);
        btn1=false;
        btn2=false;
        finish();
    }
    @Override
    public void onPause(){
        btn1=false;
        btn2=false;
        stopService(myService);
        finish();
        super.onPause();
    }

    @Override
    public void onDestroy(){
        btn1=false;
        btn2=false;
        stopService(myService);
        finish();
        super.onDestroy();

    }

    private void sendButtontoMain(){
        Intent com=new Intent("buttonEn");
        sendButtonBroadcast(com);

    }

    private void sendButtonBroadcast(Intent com) {
        com.putExtra("buttonEnable", true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(com);
    }




    }
