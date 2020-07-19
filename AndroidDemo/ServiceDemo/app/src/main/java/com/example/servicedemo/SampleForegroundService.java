package com.example.servicedemo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class SampleForegroundService extends Service {

    private static final int NOTIFICATION_DOWNLOAD_PROGRESS_ID = 0x0001;
    private boolean isRemove=false;

    NotificationManager notificationManager;

    String notificationId = "channelId";

    String notificationName = "channelName";
    @Override
    public void onCreate() {
        Log.i("zhouwei","onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("zhouwei","onstartCommand");
        int flag=intent.getExtras().getInt("flag");
        if(flag==0){
            if(!isRemove){
                createNotification();
            }
            isRemove=true;
        }else {
            if(isRemove){
                stopForeground(true);
            }
            isRemove=false;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i("zhouwei","onDestroy");
        if(isRemove){
            stopForeground(true);
        }
        isRemove=false;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotification(){
//        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
//        builder.setSmallIcon(R.drawable.ic_launcher_round);
//        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher_round));
//        builder.setAutoCancel(false);
//        builder.setOngoing(true);
//        builder.setShowWhen(true);
//        builder.setContentTitle("ForceGroundService");
//        Notification notification =builder.build();
//        startForeground(NOTIFICATION_DOWNLOAD_PROGRESS_ID,notification);
        startForegroundService();
    }

    private void startForegroundService()
    {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //创建NotificationChannel

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(notificationId, notificationName, NotificationManager.IMPORTANCE_HIGH);

            notificationManager.createNotificationChannel(channel);
        }
        startForeground(1,getNotification());

    }

    private Notification getNotification() {

        Notification.Builder builder = new Notification.Builder(this)

                .setSmallIcon(R.drawable.ic_launcher_round)

                .setContentTitle("投屏服务")

                .setContentText("投屏服务正在运行...");

        //设置Notification的ChannelID,否则不能正常显示

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            builder.setChannelId(notificationId);

        }

        Notification notification = builder.build();

        return notification;

    }
}
