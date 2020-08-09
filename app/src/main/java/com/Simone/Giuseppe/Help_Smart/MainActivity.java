package com.Simone.Giuseppe.Help_Smart;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    TextToSpeech myTts;

    private static MainActivity mCurrActivity;
    private static Button launch,test,numConf,showNumber;
    private Intent myService=null;
    private static boolean isRunning=false;
    private SharedPreferences Settings,mapTrack,date;
    private EditText number,number1;
    private String telNumber,telnumber2;
    private Calendar calendar;
    private SpeedReceiver myBroadCastReceiver;

    Intent mIntent;

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission))
                return false;
        }
        return true;
    }






    public void updateUI(final String s) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView textView = (TextView) findViewById(R.id.speed);
                            textView.setText(s);
            }
        });
    }




    public static MainActivity getInstace(){
        return ins;
    }

    private static MainActivity ins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIntent=getIntent();
        calendar=Calendar.getInstance();
        ins = this;
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        date=getSharedPreferences("date",Context.MODE_PRIVATE);
        if(date.getBoolean("first",true)) {
            SharedPreferences.Editor editor = date.edit();
            editor.putBoolean("first",false);
            editor.putInt("first_date",calendar.get(Calendar.DATE));
            editor.putInt("first_month",calendar.get(Calendar.MONTH));
            editor.commit();
        }


        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("android.permission.ACCESS_FINE_LOCATION");
        if (!addPermission(permissionsList, Manifest.permission.CALL_PHONE))
            permissionsNeeded.add("android.permission.CALL_PHONE");
        if (!addPermission(permissionsList, Manifest.permission.SEND_SMS))
            permissionsNeeded.add("android.permission.SEND_SMS");
        if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE))
            permissionsNeeded.add("android.permission.READ_PHONE_STATE");
        if(permissionsList.size()>0) {
            ActivityCompat.requestPermissions(this,
                    permissionsList.toArray(new String[permissionsList.size()]),
                    0);
        }


        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            AlertDialog alert;
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("GPS")
                    .setMessage("Bisogna attivare il gps per poter utilizzare l'app")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent=new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }



        calendar=Calendar.getInstance();
        Settings = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        test=findViewById(R.id.test);
        launch=findViewById(R.id.launch);
        numConf=findViewById(R.id.confirm);
        number=findViewById(R.id.num);
        number1=findViewById(R.id.num2);
        showNumber=findViewById(R.id.showNumber);
        Spinner giorno=findViewById(R.id.spinner);
        giorno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                day = getResources().getStringArray(R.array.giorni_values)[position];
                Log.e("Day",day);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                day="0";
            }
        });
        launch.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          myService = new Intent(getApplicationContext(), PlayerService.class);
                                          if(isRunning==false) {

                                              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                  MainActivity.this.startForegroundService(myService);
                                                  launch.setText("Stop");
                                                  isRunning=true;


                                              } else {
                                                  startService(myService);

                                                  launch.setText("Stop");
                                                  isRunning=true;
                                              }
                                          }else {
                                              stopService(myService);
                                              launch.setText("Avvia");
                                              isRunning=false;
                                          }

                                      }
                                  }


        );

        numConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telNumber=number.getText().toString();
                telnumber2=number1.getText().toString();
                if(!telNumber.isEmpty()) {
                    SharedPreferences.Editor editor = Settings.edit();
                    editor.clear();
                    editor.putString("NumeroEmergenza", telNumber);
                    if(!telnumber2.isEmpty())
                        editor.putString("NumeroEmergenza2", telnumber2);
                    editor.commit();
                    Toast.makeText(MainActivity.this, "Numeri:\n" +telNumber+"\n" + telnumber2+"\n"+" salvati", Toast.LENGTH_LONG).show();
                }
            }
        });
       final Context ctx=getApplicationContext();
    showNumber.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(ctx,"Numero 1: "+Settings.getString("NumeroEmergenza","")+"\n"+"Numero 2: "+Settings.getString("NumeroEmergenza2",""),Toast.LENGTH_LONG).show();
        }
    });




        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRunning==true)
                    stopService(myService);

                Intent testingActivity = new Intent(getApplicationContext(), Test.class);
                testingActivity.putExtra("runnning",isRunning);

                startActivity(testingActivity);

            }
        });


        mCurrActivity = this;
        myTts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    myTts.setLanguage(Locale.ITALIAN);
                }
        }
        });


        final MediaPlayer mMediaPlayer;
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.silent_sound);
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mMediaPlayer.release();
            }
        });
        mMediaPlayer.start();
        LocalBroadcastManager.getInstance(this).registerReceiver(

                mMessageReceiver, new IntentFilter("buttonEn"));

        myBroadCastReceiver = new SpeedReceiver();
        try
        {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("LOCATION_UPDATED");
            registerReceiver(myBroadCastReceiver, intentFilter);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        @Override

        public void onReceive(Context context, Intent intent) {
            Log.e("BROADCAST","test");
           // launch.setEnabled(true);
        }

    };






    @Override
    protected void onPause(){
        super.onPause();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroy() {
        stopService(myService);
        mCurrActivity = null;
        myService=null;
        if(myBroadCastReceiver!=null) {
            unregisterReceiver(myBroadCastReceiver);
        }
        if(mMessageReceiver!=null) {
            unregisterReceiver(mMessageReceiver);
        }
        super.onDestroy();
    }
    private String day="2";
    public void onClickTts(View v) {

            final MediaPlayer mMediaPlayer;
            mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.silent_sound);
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mMediaPlayer.release();
                }
            });
            mMediaPlayer.start();
        mapTrack= getSharedPreferences("track"+ day, Context.MODE_PRIVATE);
        Intent map= new Intent(this,MapsActivity.class);
        Log.e("Day",day);

        int cnt=mapTrack.getInt("cnt",0);
        if(day!="0"&& cnt!=0) {
            map.putExtra("day", day);
            startActivity(map);
        }else{
            Toast.makeText(this, "Selezionare giorno in cui è presente un track da visualizzare", Toast.LENGTH_SHORT).show();
        }


    }





   public static void setRunning(boolean check){
       if(check==false) {
           isRunning = false;
           launch.setText("Avvia");
       }
       if(check==true){
           isRunning = true;
           launch.setText("Stop");
       }
   }
}