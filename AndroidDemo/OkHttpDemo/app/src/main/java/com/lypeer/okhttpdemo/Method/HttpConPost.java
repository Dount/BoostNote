package com.lypeer.okhttpdemo.Method;

import android.content.Context;
import android.util.Log;

import com.lypeer.okhttpdemo.Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class HttpConPost {
    private Context context;
    public HttpConPost(Context myContext) {
        this.context=myContext;
    }

    public void PostRequestHttpURLConnection(){
        try {
            //创建URL
            URL url =new URL("https://postman-echo.com/post");
            //调用URL对象的OpenConnection方法获取HttpURLConnection实例
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            //设置请求方法
            httpURLConnection.setRequestMethod("POST");
            //设置允许输入输出
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            //设置请求参数
            String params =new StringBuilder().append("strange="+ URLEncoder.encode("boom","UTF-8")).toString();
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(params.getBytes());
            outputStream.flush();
            outputStream.close();
            //连接
            httpURLConnection.connect();
            //检查是否请求成功
            if(httpURLConnection.getResponseCode() == 200){
                //调用HttpURLConnection 对象的getInputStream方法获取响应数据的输入流
                InputStream inputStream= httpURLConnection.getInputStream();
                //将输入流转换成字符串
                String data=Util.inputStream2String(inputStream);
                Log.i("zhouwei","data="+data);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
