package com.lypeer.okhttpdemo.Method;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpGet {
    private Context context;
    private String URL="https://www.baidu.com";
    public OkHttpGet(Context context) {
        this.context = context;
    }

    public void get() throws Exception {
        OkHttpClient client =new OkHttpClient();
        Request request = new Request.Builder().url(URL).build();
        Call call = client.newCall(request);
        Response response =call.execute();
        if(response.isSuccessful()){
            Log.i("zhouwei","syncGetRequestByOkHttp data-->"+response.body().string());
        }else {
            throw new IOException("Unexpected code " + response);
        }
    }
}
