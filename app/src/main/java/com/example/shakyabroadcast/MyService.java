package com.example.shakyabroadcast;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

public class MyService extends Service {
    static boolean flag = false;

    private int hour = 0, minute = 0, second = 0;

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flag = intent.getBooleanExtra("flag", false);

        new Thread(() -> {
            while (flag) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                second++;
                if (second >= 60) {
                    second = 0;
                    minute++;
                    if (minute >= 60) {
                        minute = 0;
                        hour++;
                    }
                }

                Intent i = new Intent("MyMessage");
                Bundle bundle = new Bundle();
                bundle.putInt("hour", hour);
                bundle.putInt("minute", minute);
                bundle.putInt("second", second);
                i.putExtras(bundle);
                sendBroadcast(i);
            }
        }).start();

        return START_STICKY;
    }
}
