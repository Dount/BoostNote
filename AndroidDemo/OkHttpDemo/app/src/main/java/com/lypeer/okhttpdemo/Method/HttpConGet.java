package com.lypeer.okhttpdemo.Method;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.lypeer.okhttpdemo.Util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConGet {
    private Context context;
    public HttpConGet(Context myContext) {
        this.context=myContext;
    }
    public void getRequestHttpURLConnection(){
        try {
            //创建URL对象
            URL url = new URL("https://www.baidu.com");
            //调用URL对象的openConnection方法获取HTTPURLConnection对象
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            //设置请求方法
            httpURLConnection.setRequestMethod("GET");
            //开始连接
            httpURLConnection.connect();
            //检查是否请求成功
            if(httpURLConnection.getResponseCode() == 200){
                //调用HttpURLConnection对象的getInputStream方法获取响应数据的输入流。
                InputStream inputStream = httpURLConnection.getInputStream();
                //将输入流转换成字符串
                String data = Util.inputStream2String(inputStream);
                Log.i("zhouwei","data="+data);
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
