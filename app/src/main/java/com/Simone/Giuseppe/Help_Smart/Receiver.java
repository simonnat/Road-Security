package com.Simone.Giuseppe.Help_Smart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

public class Receiver extends BroadcastReceiver {

    public Receiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
            KeyEvent event = (KeyEvent) intent .getParcelableExtra(Intent.EXTRA_KEY_EVENT);

            if (event == null) {
                return;
            }

            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                //context.sendBroadcast(new Intent(Intents.ACTION_PLAYER_PAUSE));
            }
        }

        }



    }



