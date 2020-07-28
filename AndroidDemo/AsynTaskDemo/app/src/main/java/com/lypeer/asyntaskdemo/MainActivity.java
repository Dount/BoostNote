package com.lypeer.asyntaskdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.concurrent.FutureTask;

public class MainActivity extends AppCompatActivity implements DownLoadAsyncTask.UpdateUI {
    private static int WRITE_EXTERNAL_STORAGE_REQUEST_CODE=0x11;
    private  DownLoadAsyncTask downLoadAsyncTask;
    private static String DOWNLOAD_FILE_JPG_URL="http://img2.3lian.com/2014/f6/173/d/51.jpg";
    private ProgressBar progressBar;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            progressBar.setProgress(msg.what);
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressbar);
        Button downloadBtn = findViewById(R.id.downloadBtn);
        downLoadAsyncTask=new DownLoadAsyncTask(MainActivity.this);
        downLoadAsyncTask.setUpdateUIInterface(this);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downLoadAsyncTask.execute(DOWNLOAD_FILE_JPG_URL);
            }
        });
        //android 6.0 权限申请
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //android 6.0 API 必须申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode,grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                Log.i("zhouwei","Permission Granted");
            } else {
                // Permission Denied
                Log.e("zhouwei","Permission Denied");
            }
        }
    }
    @Override
    public void UpdateProgressBar(Integer value) {
        Message message=new Message();
        message.what=value;
        handler.sendMessage(message);
    }
}
