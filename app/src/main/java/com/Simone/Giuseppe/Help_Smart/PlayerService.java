package com.Simone.Giuseppe.Help_Smart;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;
import java.util.Calendar;
import java.util.Timer;


public class PlayerService extends Service {
    private MediaSessionCompat mediaSession;
    private Location lastLoc;
    private boolean firstLocation;
    private float speedSum;
    private double currmills, lastmills;
    private boolean first;
    private double diff,prevspeed=0;
    private Button launch,stop;
    private SharedPreferences Settings, mapTrack;
    private LocationManager lm;
    private boolean trackingActive;
    private boolean test;
    private double posLat ;
    private double posLng ;
    private LatLng position;
private String message;
    public static boolean SecondCall=false;
    public static boolean sms=false;
    public static boolean keyEmergency=false;
    public Context ctx;

    private final MediaSessionCompat.Callback mMediaSessionCallback
            = new MediaSessionCompat.Callback() {


        @Override
        public boolean onMediaButtonEvent(Intent mediaButtonEvent) {
            final String intentAction = mediaButtonEvent.getAction();
            if (Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
                final KeyEvent event = mediaButtonEvent.getParcelableExtra(
                        Intent.EXTRA_KEY_EVENT);
                if (event == null) {
                    return super.onMediaButtonEvent(mediaButtonEvent);
                }
                final int keycode = event.getKeyCode();
                final int action = event.getAction();
                if (event.getRepeatCount() == 0 && action == KeyEvent.ACTION_DOWN) {
                    switch (keycode) {



                        case KeyEvent.KEYCODE_MEDIA_PLAY:
                            if(test==false) {
                                SmsManager smsManager = SmsManager.getDefault();

                                smsManager.sendTextMessage(Settings.getString("NumeroEmergenza", ""), null, message + "http://maps.google.com/maps?f=q&q=" + mapTrack.getFloat("lat" + (mapTrack.getInt("cnt", 0) - 1), 0) + "," + mapTrack.getFloat("lon" + (mapTrack.getInt("cnt", 0) - 1), 0) + "&z=16", null, null);

                                if(Settings.getString("NumeroEmergenza2","")!="") {
                                    smsManager.sendTextMessage(Settings.getString("NumeroEmergenza2", ""), null, message + "http://maps.google.com/maps?f=q&q=" + mapTrack.getFloat("lat" + (mapTrack.getInt("cnt", 0) - 1), 0) + "," + mapTrack.getFloat("lon" + (mapTrack.getInt("cnt", 0) - 1), 0) + "&z=16", null, null);
                                }
                                final MediaPlayer mMediaPlayer;
                                mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.siren_noise);
                                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mediaPlayer) {
                                        mMediaPlayer.release();
                                    }
                                });
                                mMediaPlayer.start();


                                final Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:"+Settings.getString("NumeroEmergenza","")));
                                SecondCall=true;
                                sms=true;
                                keyEmergency=true;
                                MyPhoneStateListener.elapsedTime=0;
                                MyPhoneStateListener.startTime=0;
                                MyPhoneStateListener.endTime=0;
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(callIntent);
                                    }
                                }, 8000);


                            }else{
                                Log.e("test","playpause");

                            }
                            break;

                        case KeyEvent.KEYCODE_MEDIA_PAUSE:
                            if(test==false) {
                                SmsManager smsManager = SmsManager.getDefault();

                                smsManager.sendTextMessage(Settings.getString("NumeroEmergenza", ""), null, message + "http://maps.google.com/maps?f=q&q=" + mapTrack.getFloat("lat" + (mapTrack.getInt("cnt", 0) - 1), 0) + "," + mapTrack.getFloat("lon" + (mapTrack.getInt("cnt", 0) - 1), 0) + "&z=16", null, null);

                                if(Settings.getString("NumeroEmergenza2","")!="") {
                                    smsManager.sendTextMessage(Settings.getString("NumeroEmergenza2", ""), null, message + "http://maps.google.com/maps?f=q&q=" + mapTrack.getFloat("lat" + (mapTrack.getInt("cnt", 0) - 1), 0) + "," + mapTrack.getFloat("lon" + (mapTrack.getInt("cnt", 0) - 1), 0) + "&z=16", null, null);
                                }
                                final MediaPlayer mMediaPlayer;
                                mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.siren_noise);
                                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mediaPlayer) {
                                        mMediaPlayer.release();
                                    }
                                });
                                mMediaPlayer.start();


                                final Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:"+Settings.getString("NumeroEmergenza","")));
                                SecondCall=true;
                                sms=true;
                                keyEmergency=true;
                                MyPhoneStateListener.elapsedTime=0;
                                MyPhoneStateListener.startTime=0;
                                MyPhoneStateListener.endTime=0;
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(callIntent);
                                    }
                                }, 8000);


                            }else{
                                Log.e("test","playpause");

                            }
                            break;



                        case KeyEvent.KEYCODE_ENDCALL:
                            Log.e("endcall","next");
                            break;

                      /*  case KeyEvent.KEYCODE_MEDIA_PLAY:
                            Log.e("play","next");
                            break;*/

                        case KeyEvent.KEYCODE_MEDIA_NEXT:
                            Log.e("PRESSED",Boolean.toString(test));
                            if(test==false) {
                                SmsManager smsManager = SmsManager.getDefault();

                                Log.e("Player","SMS1");
                                if(Settings.getString("NumeroEmergenza2","")!="") {
                                    smsManager.sendTextMessage(Settings.getString("NumeroEmergenza2", ""), null, message + "http://maps.google.com/maps?f=q&q=" + mapTrack.getFloat("lat" + (mapTrack.getInt("cnt", 0) - 1), 0) + "," + mapTrack.getFloat("lon" + (mapTrack.getInt("cnt", 0) - 1), 0) + "&z=16", null, null);
                                }




                                final MediaPlayer mMediaPlayer;
                                mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.siren_noise);
                                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mediaPlayer) {
                                        mMediaPlayer.release();
                                    }
                                });
                                mMediaPlayer.start();


                                final Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:"+Settings.getString("NumeroEmergenza2","")));

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(callIntent);
                                    }
                                }, 8000);




                                Log.e("key",Settings.getString("NumeroEmergenza",""));
                            }else{
                                Log.e("test","next");
                                sendButtontoActivity();
                            }
                            break;




                            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                            if(test==false) {

                                SmsManager smsManager = SmsManager.getDefault();


                                Log.e("Player","SMS1");
                                if(Settings.getString("NumeroEmergenza","")!="") {
                                    smsManager.sendTextMessage(Settings.getString("NumeroEmergenza", ""), null, message + "http://maps.google.com/maps?f=q&q=" + mapTrack.getFloat("lat" + (mapTrack.getInt("cnt", 0) - 1), 0) + "," + mapTrack.getFloat("lon" + (mapTrack.getInt("cnt", 0) - 1), 0) + "&z=16", null, null);
                                }

                                final MediaPlayer mMediaPlayer;
                                mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.siren_noise);
                                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mediaPlayer) {
                                        mMediaPlayer.release();
                                    }
                                });
                                mMediaPlayer.start();


                                final Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:"+Settings.getString("NumeroEmergenza","")));

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(callIntent);
                                    }
                                }, 8000);


                            }else{
                                Log.e("test","previous");

                            }
                            break;
                        default:
                            Log.e("playpause",Integer.toString(keycode));
                            break;
                    }
                    startService(new Intent(getApplicationContext(), PlayerService.class).putExtra("test",test));
                    return true;
                }
            }
            return false;
        }
    };


    HandlerThread mLocationHandlerThread = null;
    Looper mLocationHandlerLooper = null;
    private Calendar calendar;
    private PowerManager.WakeLock screenLock;
    @Override
    public void onCreate() {
        super.onCreate();
        mLocationHandlerThread = new HandlerThread("locationHandlerThread");
        message="Messaggio automatico di emergenza: ho bisogno del tuo aiuto la mia posizione è: " +
                "";
        screenLock = ((PowerManager)getSystemService(POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG:road");
        screenLock.acquire();
        ctx=getApplicationContext();
        MainActivity.setRunning(true);


        PowerManager mgr = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "RoadSafety:mywake");
        wakeLock.acquire();
        first = true;

        trackingActive = false;
        firstLocation = true;
        calendar = Calendar.getInstance();


        Settings = getSharedPreferences("Settings", Context.MODE_PRIVATE);

            mapTrack = getSharedPreferences("track" + calendar.get(Calendar.DAY_OF_WEEK), Context.MODE_PRIVATE);
        editor = mapTrack.edit();
        if(mapTrack.getInt("mDay",0)!=calendar.get(Calendar.DATE) ) {
            Log.e("CLEAR","OK");
            editor.clear();
            editor.commit();
        }


           lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
           if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
               final Criteria criteria = new Criteria();

               criteria.setAccuracy(Criteria.ACCURACY_FINE);
               criteria.setSpeedRequired(true);
               criteria.setAltitudeRequired(false);
               criteria.setBearingRequired(false);
               criteria.setCostAllowed(true);
               criteria.setPowerRequirement(Criteria.POWER_LOW);
               startGpsTracking();

           } else {

           }

           Intent notificationIntent = new Intent(this, PlayerService.class);
           PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
           NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "HelpStrip")
                   .setSmallIcon(R.drawable.icona_app)
                   .setContentTitle("Help Smart")
                   .setContentText("Servizio di monitoraggio incidenti")
                   .setTicker("TICKER")
                   .setContentIntent(pendingIntent);
           Notification notification = builder.build();
           if (Build.VERSION.SDK_INT >= 26) {
               NotificationChannel channel = new NotificationChannel("HelpStrip", "HelpStrip", NotificationManager.IMPORTANCE_HIGH);
               channel.setDescription("Help Smart");
               NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
               notificationManager.createNotificationChannel(channel);
           }
           startForeground(101, notification);


        ComponentName receiver = new ComponentName(getPackageName(), RemoteReceiver.class.getName());
        mediaSession = new MediaSessionCompat(this, "PlayerService", receiver, null);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setPlaybackState(new PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PAUSED, 0, 0)
                .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE)
                .build());

        mediaSession.setCallback(mMediaSessionCallback);
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.requestAudioFocus(new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {


                final MediaPlayer mMediaPlayer;
                mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.silent_sound);
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mMediaPlayer.release();
                    }
                });
                mMediaPlayer.start();
            }
        }, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        mediaSession.setActive(true);

    }
    private PowerManager.WakeLock wakeLock;
   private  Timer timer=new Timer();
    @Override
    public int onStartCommand(Intent intent2, int flags, int startId) {




        test=intent2.getBooleanExtra("test",false);

        if (mediaSession.getController().getPlaybackState().getState() == PlaybackStateCompat.STATE_PLAYING) {

            mediaSession.setPlaybackState(new PlaybackStateCompat.Builder()
                    .setState(PlaybackStateCompat.STATE_PAUSED, 0, 0.0f)
                    .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE).build());
        } else {

            mediaSession.setPlaybackState(new PlaybackStateCompat.Builder()
                    .setState(PlaybackStateCompat.STATE_PLAYING, 0, 1.0f)
                    .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE).build());
        }
        return START_STICKY; // super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaSession.release();
        MainActivity.setRunning(false);
        timer.cancel();
        try
        {
            lm.removeUpdates(myLocListener);
            lm=null;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        if(screenLock.isHeld())
        screenLock.release();
        //wakeLock.release();
        stopForeground(true);
       // stopSelf();
    }




private SharedPreferences.Editor editor;

    private double start=0,end=0,elapsed=0;


    private LocationListener myLocListener = new LocationListener(){



        public void onLocationChanged(Location location) {
            Log.e("accuracy", Double.toString(location.getAccuracy()));
            if (test == false && location.getAccuracy()<10) {

                if (first == true) {
                    lastmills = 0;
                    first = false;
                }
                currmills = System.currentTimeMillis();
                if (lastLoc == null) {
                    lastLoc = location;
                }
                speedSum = 0;
                diff = ((currmills - lastmills) / 1000);
                speedSum = (location.distanceTo(lastLoc));
                speedSum /= diff;
                speedSum *= 3.6;
                //diff = (currmills - lastmills) / 1000;


               /* Log.e("currmillis", Double.toString(currmills));
                Log.e("lastmillis", Double.toString(lastmills));
                Log.e("difference", Double.toString(currmills-lastmills));
                Log.e("location",location.toString());
                Log.e("diff",Double.toString(diff));

                Log.e("locationdistance",Double.toString(location.distanceTo(lastLoc)));*/


                if (speedSum < 5)
                    speedSum=0;
                Log.e("Speed3", Double.toString(speedSum));
                Intent i = new Intent("LOCATION_UPDATED");
                i.putExtra("Speed", speedSum);
                    sendBroadcast(i);
                    if (speedSum == Double.POSITIVE_INFINITY)
                        speedSum = (float) prevspeed;
                    if (prevspeed > 45) {
                        if (prevspeed * 0.2 > speedSum) {
                            Log.e("Speed", Double.toString(speedSum));
                            Log.e("Speed", Double.toString(prevspeed));
                            Log.e("SpeedAccident", Settings.getString("NumeroEmergenza", ""));

                   /*AudioManager audioManager =
                                (AudioManager)ctx.getSystemService(Context.AUDIO_SERVICE);
                                audioManager.setStreamVolume (
                                        AudioManager.STREAM_MUSIC,
                                        audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                                        0);*/
                            final MediaPlayer mMediaPlayer;
                            mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.siren_noise);
                            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {
                                    mMediaPlayer.release();
                                }
                            });
                            mMediaPlayer.start();

                            //
                       /* try {
                            Thread.sleep(8000);
                        } catch (InterruptedException ie) {
                            Log.e("interrupt", ie.toString());
                        }*/

                            final Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + Settings.getString("NumeroEmergenza", "")));
                            SecondCall = true;
                            sms = true;
                            MyPhoneStateListener.elapsedTime = 0;
                            MyPhoneStateListener.startTime = 0;
                            MyPhoneStateListener.endTime = 0;
                            prevspeed = 0;
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    lastmills = System.currentTimeMillis();
                                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(callIntent);

                                }
                            }, 8000);

                        }
                    }

                        lastLoc = location;
                    start=System.currentTimeMillis();
                    elapsed=start-end;
                if(speedSum!=0 && elapsed>5000) {
                        posLat = location.getLatitude();
                        posLng = location.getLongitude();
                        position = new LatLng(posLat, posLng);

                        int cnt = mapTrack.getInt("cnt", 0);
                        editor = mapTrack.edit();
                        editor.putFloat("lat" + cnt, (float) posLat);
                        editor.putFloat("lon" + cnt, (float) posLng);
                        cnt++;
                        editor.putInt("cnt", cnt);
                        editor.putInt("mDay", calendar.get(Calendar.DATE));
                        editor.commit();
                        end=System.currentTimeMillis();
                    Log.e("Logging", Double.toString(elapsed));
                    }
                    prevspeed = speedSum;
                Log.e("in", Double.toString(diff));
                Log.e("curr", Double.toString(currmills));
                Log.e("last", Double.toString(lastmills));
                lastmills = System.currentTimeMillis();
                }

        }

            public void onProviderDisabled (String provider){
                Log.d("tag", "onProviderDisabled");
            }
            public void onProviderEnabled (String provider){
                Log.d("tag", "onProviderEnabled");
            }
            public void onStatusChanged (String provider,int status, Bundle extras){

                Log.d("tag", "onStatusChanged");
            }


    };



    private GpsStatus.Listener gpsListener= new GpsStatus.Listener() {
        public void onGpsStatusChanged(int status) {

            switch (status) {

                case GpsStatus.GPS_EVENT_FIRST_FIX:

                    Log.d("tag", "onGpsStatusChanged First Fix");

                    break;

                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:

                    Log.d("tag", "onGpsStatusChanged Satellite");

                    break;

                case GpsStatus.GPS_EVENT_STARTED:

                    Log.d("tag", "onGpsStatusChanged Started");

                    break;

                case GpsStatus.GPS_EVENT_STOPPED:

                    Log.d("tag", "onGpsStatusChanged Stopped");

                    break;
            }
        }
    };




    private void startGpsTracking() {
        mLocationHandlerThread.start();
        mLocationHandlerLooper = mLocationHandlerThread.getLooper();
        try {
            lm.addGpsStatusListener(gpsListener);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 50, 0, myLocListener, mLocationHandlerLooper);
        }catch(SecurityException se)
        {
            Log.e("Location:",se.toString());
        }
    }






    private void sendButtontoActivity(){
        Intent com=new Intent("buttons");
        sendButtonBroadcast(com);

}

private void sendButtonBroadcast(Intent com){
            com.putExtra("button1",true);

    LocalBroadcastManager.getInstance(this).sendBroadcast(com);

}




}