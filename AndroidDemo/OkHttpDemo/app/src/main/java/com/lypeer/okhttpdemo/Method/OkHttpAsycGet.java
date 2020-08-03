package com.lypeer.okhttpdemo.Method;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpAsycGet {
    private Context context;
    private String URL="https://www.baidu.com";

    public OkHttpAsycGet(Context context) {
        this.context = context;
    }
    public void Get (){
        OkHttpClient client =new OkHttpClient();
        Request request =new Request.Builder().url(URL).build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                    if(response.isSuccessful()){
                        Log.i("zhouwei","syncGetRequestByOkHttp data-->"+response.body().string());
                    }else {
                        throw new IOException("Unexpected code"+response);
                    }
            }
        });
    }
}
