package com.example.servicedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Intent startServiceIntent;
    private Intent binServiceIntent;
    private ServiceConnection conn;
    private ServiceConnection messagerConn;
    private SampleBindService mBindService;
    private Intent messagerServiceIntent;
    private Messenger messenger;
    private boolean mMessagerServiceConnect;
    private Messenger mReceiverMessageHandler =new Messenger(new receiverMessage());
    private Intent forcegroundServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.i("zhouwei","Binder Service Success");
                SampleBindService.LocalServiice binder = (SampleBindService.LocalServiice) service;
                mBindService =binder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mBindService=null;
            }
        };

        messagerConn =new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                messenger=new Messenger(service);
                mMessagerServiceConnect=true;
                Log.i("zhouwei","Binder Service Success");
                Log.i("zhouwei","messenger:"+messenger);

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                messenger=null;
                mMessagerServiceConnect=false;
            }
        };
    }

    private void init() {
        Button start = findViewById(R.id.startService);
        Button stop = findViewById(R.id.stopService);
        start.setOnClickListener(new click());
        stop.setOnClickListener(new click());
        Button bindService = findViewById(R.id.bindService);
        Button unBindService = findViewById(R.id.unbindService);
        Button getServiceData = findViewById(R.id.getbindServiceData);
        bindService.setOnClickListener(new click());
        unBindService.setOnClickListener(new click());
        getServiceData.setOnClickListener(new click());
        Button bindMessageService = findViewById(R.id.MessageerService);
        Button unBindMessageService = findViewById(R.id.unbindMessageerService);
        Button getBindMessageServiceData = findViewById(R.id.getMessageerServiceData);
        bindMessageService.setOnClickListener(new click());
        unBindMessageService.setOnClickListener(new click());
        getBindMessageServiceData.setOnClickListener(new click());
        Button startForeGround = findViewById(R.id.ForegroudnService);
        Button stopForeGround = findViewById(R.id.unForegroudnService);
        startForeGround.setOnClickListener(new click());
        stopForeGround.setOnClickListener(new click());

        startServiceIntent = new Intent(this,SampleStartService.class);
        binServiceIntent = new Intent(this,SampleBindService.class);
        messagerServiceIntent = new Intent(this,SampleMessageService.class);
        forcegroundServiceIntent = new Intent(this,SampleForegroundService.class);
    }

    public class click implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.startService:
                    startService(startServiceIntent);
                    break;
                case R.id.stopService:
                    stopService(startServiceIntent);
                    break;
                case R.id.bindService:
                    bindService(binServiceIntent,conn,BIND_AUTO_CREATE);
                    break;
                case R.id.unbindService:
                    if(mBindService!=null){
                        mBindService=null;
                        unbindService(conn);
                    }
                    break;
                case R.id.getbindServiceData:
                    if(mBindService!=null){
                        Toast.makeText(MainActivity.this,"获取服务数据："+mBindService.getCount(),Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(MainActivity.this,"还没绑定服务",Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.MessageerService:
                    bindService(messagerServiceIntent,messagerConn,BIND_AUTO_CREATE);
                    Log.i("zhouwei","开发绑定服务");
                    break;
                case R.id.unbindMessageerService:
                    if(mMessagerServiceConnect){
                        Log.i("zhouwei","去掉绑定服务");
                        unbindService(messagerConn);
                        mMessagerServiceConnect=false;
                    }
                    break;
                case R.id.getMessageerServiceData:
                    if(mMessagerServiceConnect){
                        sendMessage();
                        Toast.makeText(MainActivity.this,"发送Message到服务端",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(MainActivity.this,"还没绑定服务",Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.ForegroudnService:
                    forcegroundServiceIntent.putExtra("flag",0);
                    startService(forcegroundServiceIntent);
                    break;

                case R.id.unForegroudnService:
                    forcegroundServiceIntent.putExtra("flag",1);
                    stopService(forcegroundServiceIntent);
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void sendMessage(){
        Message msg = Message.obtain();
        msg.what=1;
        //创建服务端与客户端之间的桥梁
        msg.replyTo=mReceiverMessageHandler;
        try {
            messenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public class  receiverMessage extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    Log.i("zhouwei","来自Service的消息 "+msg.getData().getString("replay"));
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

}
