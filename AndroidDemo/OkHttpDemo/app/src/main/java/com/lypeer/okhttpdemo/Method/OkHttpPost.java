package com.lypeer.okhttpdemo.Method;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class OkHttpPost {
    private Context context;
    private String URL="https://postman-echo.com/post";
    public OkHttpPost(Context context) {
        this.context = context;
    }

    public void Post(){
        OkHttpClient client =new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("strange","boom")
                .build();
        Request request = new Request.Builder().url(URL).post(formBody).build();
        Call call =client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    Log.i("zhouwei","syncPostRequestByOkHttp data-->"+response.body().string());
                }else {
                    throw new IOException("Unexpected code"+response);
                }
            }
        });
    }
}
