package com.example.servicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;

public class SampleMessageService extends Service {

    public static final int MESSAGE = 1;

    final Messenger messenger = new Messenger(new IncomingHandler());

    class IncomingHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case MESSAGE:
                    Log.i("zhouwei","服务端收到来自客户端的消息");

                    Messenger client=msg.replyTo;
                    Message replayMsg=Message.obtain();
                    replayMsg.what=1;
                    Bundle bundle =new Bundle();
                    bundle.putString("replay","收到来自服务端的回复");
                    replayMsg.setData(bundle);
                    try {
                        client.send(replayMsg);
                        Log.i("zhouwei","开发发送回复");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("zhouwei","Message Service onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("zhouwei","Message Service onBind");
        return messenger.getBinder();
    }

    @Override
    public void onDestroy() {
        Log.i("zhouwei","Message Service onDestroy");
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        Log.i("zhouwei","Message Service onCreate");
        super.onCreate();
    }
}
