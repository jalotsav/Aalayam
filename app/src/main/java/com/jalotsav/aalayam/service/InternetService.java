package com.jalotsav.aalayam.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.jalotsav.aalayam.common.General_Fnctns;
import com.jalotsav.aalayam.common.NoInternet_Connection;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by JALOTSAV Dev. on 19/11/15.
 */
public class InternetService extends Service {

    ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // This schedule a runnable task every 500 MiliSeconds
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {

                    @SuppressWarnings("deprecation")
                    public void run() {

                        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                        // get the info from the currently running task
                        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);

                        ComponentName componentInfo = taskInfo.get(0).topActivity;
                        if(componentInfo.getPackageName().equals("com.jalotsav.aalayam"))
                        {
                            if(!General_Fnctns.isNetConnected(getApplicationContext())){

                                if(!componentInfo.getClassName().equals("com.jalotsav.aalayam.common.NoInternet_Connection")){

                                    Intent intntNoIntrntCnnctn = new Intent(getApplicationContext(), NoInternet_Connection.class);
                                    intntNoIntrntCnnctn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intntNoIntrntCnnctn);
                                }
                            }else{

                                sendBroadcast(new Intent("intntfltrNoIntrntActvt"));
                            }
                        }else
                            stopSelf();
                            // Log.i(TAG, "Application current NOT running on screen");
                    }
                });
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
        //return super.onStartCommand(intent, flags, startId);
        return android.app.Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
