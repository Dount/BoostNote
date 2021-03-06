package com.lypeer.okhttpdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lypeer.okhttpdemo.Method.HttpConGet;
import com.lypeer.okhttpdemo.Method.HttpConPost;
import com.lypeer.okhttpdemo.Method.OkHttpAsycGet;
import com.lypeer.okhttpdemo.Method.OkHttpCancel;
import com.lypeer.okhttpdemo.Method.OkHttpGet;
import com.lypeer.okhttpdemo.Method.OkHttpMultipart;
import com.lypeer.okhttpdemo.Method.OkHttpPost;
import com.lypeer.okhttpdemo.Method.OkHttpPostFile;
import com.lypeer.okhttpdemo.Method.OkHttpSetTime;

public class MainActivity extends AppCompatActivity {
    private static int WRITE_EXTERNAL_STORAGE_REQUEST_CODE=0x11;
    private HttpConGet httpConGet;
    private HttpConPost httpConPost;
    private OkHttpGet okHttpGet;
    private OkHttpAsycGet okHttpAsycGet;
    private OkHttpPost okHttpPost;
    private OkHttpPostFile okHttpPostFile;
    private OkHttpMultipart okHttpMultipart;
    private OkHttpSetTime okHttpSetTime;
    private OkHttpCancel okHttpCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //android 6.0 权限申请
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //android 6.0 API 必须申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }
        init(MainActivity.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void init(Context context){
        httpConGet = new HttpConGet(context);
        httpConPost = new HttpConPost(context);
        okHttpGet = new OkHttpGet(context);
        okHttpAsycGet = new OkHttpAsycGet(context);
        okHttpPost = new OkHttpPost(context);
        okHttpPostFile = new OkHttpPostFile(context);
        okHttpMultipart = new OkHttpMultipart(context);
        okHttpSetTime = new OkHttpSetTime(context);
        okHttpCancel = new OkHttpCancel(context);
        Button button1 = findViewById(R.id.HttpURLConnectionGet);
        Button button2 = findViewById(R.id.HttpURLConnectionPost);
        Button button3 = findViewById(R.id.OkHttpget);
        Button button4 = findViewById(R.id.OKHttpAsyacGet);
        Button button5 = findViewById(R.id.OkHttpPost);
        Button button6 = findViewById(R.id.OkHttpPostFile);
        Button button7 = findViewById(R.id.OkHttpPostCancelRequest);
        Button button8 = findViewById(R.id.OkHttpPostmultipart);
        Button button9 = findViewById(R.id.OkHttpPostSetTime);
        button1.setOnClickListener(new onClick());
        button2.setOnClickListener(new onClick());
        button3.setOnClickListener(new onClick());
        button4.setOnClickListener(new onClick());
        button5.setOnClickListener(new onClick());
        button6.setOnClickListener(new onClick());
        button7.setOnClickListener(new onClick());
        button8.setOnClickListener(new onClick());
        button9.setOnClickListener(new onClick());
    }
    public class onClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.HttpURLConnectionGet:
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            httpConGet.getRequestHttpURLConnection();
                        }
                    });
                    thread.start();
                    break;
                case R.id.HttpURLConnectionPost:
                    Thread thread1=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            httpConPost.PostRequestHttpURLConnection();
                        }
                    });
                    thread1.start();
                    break;
                case R.id.OkHttpget:
                    Thread thread2=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                okHttpGet.get();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread2.start();
                    break;
                case R.id.OKHttpAsyacGet:
                    okHttpAsycGet.Get();
                    break;
                case R.id.OkHttpPost:
                    okHttpPost.Post();
                    break;
                case R.id.OkHttpPostFile:
                    okHttpPostFile.post();
                    break;
                case R.id.OkHttpPostmultipart:
                    okHttpMultipart.post();
                    break;
                case R.id.OkHttpPostSetTime:
                    okHttpSetTime.setTime();
                    break;
                case R.id.OkHttpPostCancelRequest:
                    okHttpCancel.Cancel();
                    break;
            }
        }
    }
}
