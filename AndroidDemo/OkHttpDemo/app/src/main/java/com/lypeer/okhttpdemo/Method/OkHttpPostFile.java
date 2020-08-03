package com.lypeer.okhttpdemo.Method;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpPostFile {
    private Context context;

    public OkHttpPostFile(Context context) {
        this.context = context;
    }

    public void post(){
        final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
        String path=context.getExternalFilesDir(null)+"/test.txt";
        File file=new File(path);
        if(file.exists()){
            OkHttpClient okHttpClient=new OkHttpClient();
            RequestBody requestBody =RequestBody.create(MEDIA_TYPE_MARKDOWN,file);
            Request request = new Request.Builder().url("https://api.github.com/markdown/raw").post(requestBody).build();
            Call call = okHttpClient.newCall(request);
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
}
