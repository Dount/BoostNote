package com.lypeer.asyntaskdemo;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownLoadAsyncTask extends AsyncTask<String,Integer,String> {
    private Context context;
    private int valueProgress =100;
    private PowerManager.WakeLock mWakeLock;
    public DownLoadAsyncTask(Context context) {
        this.context=context;
    }

    @Override
    protected String doInBackground(String... strings) {
        InputStream input=null;
        OutputStream output = null;
        HttpURLConnection connection =null;
        try {
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if(connection.getResponseCode()!=HttpURLConnection.HTTP_OK){
                return "Server returned HTTP" + connection.getResponseCode()
                        +""+connection.getResponseMessage();
            }
            int fileLength = connection.getContentLength();
            input = connection.getInputStream();
            output = new FileOutputStream(getSDCardDir());
            byte data[]=new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data))!=-1){
                if(isCancelled()){
                    input.close();
                    return null;
                }
                total +=count;
                if(fileLength >0){
                    //与onProgressUpdate对应，更新进度条.
                    publishProgress((int) (total * 100 / fileLength));
                }
                Thread.sleep(100);
                output.write(data,0,count);
            }

        }catch (Exception e){
            return e.toString();
        }finally {
            try {
                if (output!=null)
                    output.close();
                if (input!=null)
                    input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(connection!=null){
                connection.disconnect();
            }
        }
        return null;
    }

    /**
     * onPreExecute是可以选择性覆写的方法
     * 在主线程中执行,在异步任务执行之前,该方法将会被调用
     * 一般用来在执行后台任务前对UI做一些标记和准备工作，
     * 如在界面上显示一个进度条。
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        PowerManager pm= (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock=pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,getClass().getName());
        mWakeLock.acquire();
    }

    /**
     * onPostExecute是可以选择性覆写的方法
     * 在主线程中执行,在异步任务执行完成后,此方法会被调用
     * 一般用于更新UI或其他必须在主线程执行的操作,传递参数bitmap为
     * doInBackground方法中的返回值
     * @param bitmap
     */
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        mWakeLock.release();
    }

    /**
     * onProgressUpdate是可以选择性覆写的方法
     * 在主线程中执行,当后台任务的执行进度发生改变时,
     * 当然我们必须在doInBackground方法中调用publishProgress()
     * 来设置进度变化的值
     * @param values
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if(updateUI!=null){
            updateUI.UpdateProgressBar(values[0]);
        }
    }

    public File getSDCardDir(){
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            // 创建一个文件夹对象，赋值为外部存储器的目录
            String dirName = context.getExternalFilesDir(null)+"/MyDownload/";
            File f = new File(dirName);
            if(!f.exists()){
                f.mkdir();
            }
            File downloadFile=new File(f,"new.jpg");
            return downloadFile;
        }
        else{

            return null;
        }
    }

    public UpdateUI updateUI;

    public interface UpdateUI{
        void UpdateProgressBar(Integer value);
    }

    public void setUpdateUIInterface(UpdateUI updateUI){
        this.updateUI=updateUI;
    }
}
