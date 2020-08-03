package com.lypeer.okhttpdemo.Method;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpSetTime {
    private Context context;

    public OkHttpSetTime(Context context) {
        this.context = context;
    }

    public void setTime(){
        OkHttpClient client =new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(1,TimeUnit.SECONDS)
                .build();
        Request request =new Request.Builder()
                .url("http://httpbin.org/delay/2")
                .build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("zhouwei", "onFailure IOException-->" + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.i("zhouwei", "onResponse data-->" + response.body().string());
                } else {
                    throw new IOException("Unexpected code " + response);
                }
            }
        });
    }
}
