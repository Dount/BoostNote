package com.lypeer.okhttpdemo.Method;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpMultipart {
    private Context context;

    public OkHttpMultipart(Context context) {
        this.context = context;
    }

    public void post(){
        final String IMGUR_CLIENT_ID = "...";
        final MediaType MEDIA_TYPE_PNG=MediaType.parse("image/png");
        String path=context.getExternalFilesDir(null)+"/test.png";
        File file=new File(path);
        Log.i("zhouwei","FilePath="+path);
        if(file.exists()){
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody =new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("title","test")
                    .addFormDataPart("image","test.png",RequestBody.create(MEDIA_TYPE_PNG,file))
                    .build();
            Request request =new Request.Builder()
                    .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                    .url("https://api.imgur.com/3/image")
                    .post(requestBody)
                    .build();
            Call call =client.newCall(request);
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
