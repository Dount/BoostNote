package com.lypeer.okhttpdemo;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Util {

    /**
     * 将输入流转换成字符串
     *
     * @param inputStream 输入流
     * @return 字符串数据
     */
    public static String inputStream2String(InputStream inputStream) {
        String data = "";
        BufferedReader bufferedReader = null;
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            data = stringBuilder.toString();
            if (!TextUtils.isEmpty(data)) {
                //去除最后一个多余的换行符
                data = data.substring(0, data.length() - 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }
}
