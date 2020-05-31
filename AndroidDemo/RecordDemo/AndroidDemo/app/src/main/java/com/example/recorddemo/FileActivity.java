package com.example.recorddemo;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FileActivity extends AppCompatActivity {


    @BindView(R.id.file_text)
    TextView fileText;
    @BindView(R.id.press_speak)
    TextView pressSpeak;
    @BindView(R.id.mBtnPlay)
    Button mBtnPlay;

    private ExecutorService mExecutorService;
    private MediaRecorder mediaRecorder;
    private File audioFile;
    private long startRecordTime, stopRecordTime;
    private Handler mainThreadHandler;

    //主线程和后台播放线程数据同步
    private volatile boolean mIsPlaying;
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        ButterKnife.bind(this);

        //录音jni函数不具备线程安全性，所以要用单线程。
        mExecutorService = Executors.newSingleThreadExecutor();

        mainThreadHandler = new Handler(Looper.getMainLooper());

        pressSpeak.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startRecord();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        stopRecord();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    @OnClick(R.id.mBtnPlay)
    public void play(){
        if(audioFile!=null&& !mIsPlaying){
            //设置当前播放状态
            mIsPlaying=true;
            //提交后台任务，开始播放
            mExecutorService.submit(new Runnable() {
                @Override
                public void run() {
                    doPlay(audioFile);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //activity销毁时,停止后台任务，避免内存泄漏
        mExecutorService.shutdownNow();
        releaseRecord();
        stopplay();
    }

    public void startRecord() {
        pressSpeak.setText("正在说话");

        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                //释放之前录音的 meidaRecord
                releaseRecord();

                //执行录音逻辑，如果失败提示用户
                if (!dostart()) {
                    recordFail();
                }
            }
        });

    }

    public void stopRecord() {
        pressSpeak.setText("按住说话");

        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                if (!doStop()) {
                    recordFail();
                }

                releaseRecord();
            }
        });

    }

    //启动录音
    public boolean dostart() {
        try {
            //创建MediaRecorder

            mediaRecorder = new MediaRecorder();
            //创建录音文件
            audioFile = new File(this.getExternalFilesDir(null).getAbsolutePath() + "/iRecorderDemo/" +
                    System.currentTimeMillis() + ".m4a");
            Log.i("zhouwei","filepath="+audioFile.getAbsolutePath());
            audioFile.getParentFile().mkdir();
            audioFile.createNewFile();
            //配置MediaRecorder

            //从麦克风采集
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //保存文件为MP4格式
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            //所有安卓系统都支持的采样频率
            mediaRecorder.setAudioSamplingRate(44100);
            //通过ACC编码格式
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            //音质比较好的频率
            mediaRecorder.setAudioEncodingBitRate(96000);

            //设置录音文件的位置
            mediaRecorder.setOutputFile(audioFile.getAbsolutePath());

            //开始录音
            mediaRecorder.prepare();
            mediaRecorder.start();

            //记录开始录音的时间，用于统计时长
            startRecordTime = System.currentTimeMillis();
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            //录音失败,避免Crash.
            return false;
        }
        Log.i("FileActivity", "msg=" + "录音成功");
        //录音成功

        return true;
    }


    //释放mediaRecorder
    public void releaseRecord() {
        //检查MediaReocrd不为null
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }

    }


    //错误处理
    public void recordFail() {
        audioFile = null;
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(FileActivity.this, "录音失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean doStop() {
        try {
            //停止录音
            mediaRecorder.stop();
            //记录停止时间，统计时长
            stopRecordTime = System.currentTimeMillis();

            //只接受超过3秒的录音

            final int second = (int) ((stopRecordTime - startRecordTime) / 1000);

            if (second > 3) {
                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        fileText.setText(fileText.getText() + "\n录音成功" + second + "秒");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    //实际播放逻辑
    private void doPlay(File audioFile){
        //配置播放器MediaPlayer
        mMediaPlayer=new MediaPlayer();
        try{
            //设置声音文件
            mMediaPlayer.setDataSource(audioFile.getAbsolutePath());


            //设置监听回调
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopplay();
                }
            });
            //设置错误处理
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    //提示用户
                    playFail();

                    //释放播放器
                    stopplay();

                    //错误已经处理，返回true
                    return true;
                }
            });

            //配置音量
            mMediaPlayer.setVolume(1,1);
            //是否重复播放
            mMediaPlayer.setLooping(false);
            //准备，开始
            mMediaPlayer.prepare();
            mMediaPlayer.start();

        }catch (RuntimeException |IOException e){
            //异常处理，防止闪退
            e.printStackTrace();

            //提醒用户
            playFail();

            //释放播放器;
            stopplay();
        }
    }

    //停止的逻辑
    private void stopplay(){
        //重置播放状态
        mIsPlaying=false;

        //释放播放器
        if(mMediaPlayer!=null){
            //重置监听器，防止内存泄漏
            mMediaPlayer.setOnCompletionListener(null);
            mMediaPlayer.setOnErrorListener(null);
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.stop();
            mMediaPlayer=null;
        }
    }

    //提醒用户播放失败
    private void playFail(){
        //在主线程Toast提示
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(FileActivity.this,"播放失败",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
