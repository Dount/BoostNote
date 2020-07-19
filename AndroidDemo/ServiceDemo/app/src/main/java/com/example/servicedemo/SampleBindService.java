package com.example.servicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class SampleBindService extends Service {

    private LocalServiice binder=new LocalServiice();
    private int count;
    private boolean quiet;
    public class LocalServiice extends Binder{
            SampleBindService getService(){
                return SampleBindService.this;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.i("zhouwei","Binder Service onBind");
        return binder;
    }

    @Override
    public void onCreate() {
        Log.i("zhouwei","Binder Service onCreate");
        super.onCreate();
        final Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                while (!quiet){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count++;
                }
            }
        });
        thread.start();
    }

    public int getCount(){
        return count;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("zhouwei","Binder Service onUnbind");
        return super.onUnbind(intent);

    }

    @Override
    public void onDestroy() {
        Log.i("zhouwei","Binder Service onDestroy");
        this.quiet =true;
        super.onDestroy();
    }
}
