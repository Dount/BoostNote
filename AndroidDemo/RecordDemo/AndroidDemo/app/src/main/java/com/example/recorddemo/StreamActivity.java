package com.example.recorddemo;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StreamActivity extends AppCompatActivity {

    @BindView(R.id.mBtnStart)
    Button mBtnStart;
    @BindView(R.id.mTvLog)
    TextView mTvLog;
    @BindView(R.id.mBtnPlay)
    Button mBtnPlay;

    //录音状态，volatile保证多线程内存同步，避免出问题。
    private volatile boolean mIsRecording;
    private volatile boolean mIsPlaying;
    private ExecutorService mExecutorService;
    private Handler mMainThreadHandler;

    //buffer 不能太大，避免OOM
    private static final int BUFFER_SIZE = 2048;
    private byte[] mBuffer;
    private File mAudioFile;
    private FileOutputStream mfileoutputStream;
    private long mStartRecordTime, mStopRecordTime;
    private AudioRecord audioRecord;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);
        ButterKnife.bind(this);

        mBuffer = new byte[BUFFER_SIZE];

        mExecutorService = Executors.newSingleThreadExecutor();
        mMainThreadHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mExecutorService.shutdownNow();
    }

    @OnClick(R.id.mBtnStart)
    public void start() {
        if (mIsRecording) {
            mBtnStart.setText("开始录音");
            mIsRecording = false;
        } else {
            mBtnStart.setText("停止录音");
            //改变录音状态
            mIsRecording = true;
            //提交后台任务，执行录音逻辑
            mExecutorService.submit(new Runnable() {
                @Override
                public void run() {
                    if (!startRecord()) {
                        recordFail();
                    }
                }
            });
        }
    }

    @OnClick(R.id.mBtnPlay)
    public void play(){
        //检查播放状态，防止重复播放
        if(mAudioFile!=null && !mIsPlaying){
            //设置当前为播放状态
            mIsPlaying=true;
            //在后台线程提交播放任务，防止阻塞主线程
            mExecutorService.submit(new Runnable() {
                @Override
                public void run() {
                    doPlay(mAudioFile);
                }
            });
        }
    }



    //录音错误处理
    private void recordFail() {
        mMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(StreamActivity.this, "录音失败", Toast.LENGTH_SHORT).show();
                //重置录音状态,以及UI状态
                mIsRecording = false;
                mBtnStart.setText("开始");
            }
        });
    }

    //播放错误处理
    private void playFail(){
        mAudioFile=null;
        mMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(StreamActivity.this,"播放失败",Toast.LENGTH_SHORT).show();
            }
        });
    }



    private boolean startRecord() {
        //创建录音文件
        try {
            mAudioFile = new File(this.getExternalFilesDir(null).getAbsolutePath() + "/iRecorderDemo/" +
                    System.currentTimeMillis() + ".pcm");
            mAudioFile.getParentFile().mkdir();
            mAudioFile.createNewFile();
            Log.i("zhouwei","filepath="+mAudioFile.getAbsolutePath());
            //创建文件输出流
            mfileoutputStream = new FileOutputStream(mAudioFile);
            //配置AudioRecord
            //从麦克风采集
            int audioSource = MediaRecorder.AudioSource.MIC;
            //采样率
            int sampleRate = 44100;
            //设置单声道
            int channelConfig = AudioFormat.CHANNEL_IN_MONO;
            //比特率
            int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
            //计算AudioRecord 内部buffer最小的大小
            int buffersize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);

            //buffer不能小于最低要求，也不能小于我们每次读取的大小。
            try {
                audioRecord = new AudioRecord(audioSource, sampleRate, channelConfig, audioFormat, Math.max(buffersize, BUFFER_SIZE));
            } catch (Exception e) {
                e.printStackTrace();
            }
            //开始录音
            audioRecord.startRecording();

            //记录开始录音时间，用于统计时长
            mStartRecordTime = System.currentTimeMillis();

            //循环读取数据，写到输出流
            while (mIsRecording) {
                //只要还在录音状态，就一直读取数据
                int read = audioRecord.read(mBuffer, 0, BUFFER_SIZE);
                if (read > 0) {
                    //读取成功就写到文件中
                    mfileoutputStream.write(mBuffer, 0, read);
                } else {
                    //读取失败，返回false提示用户
                    return false;
                }
            }
            //退出循环，停止录音，释放资源
            return stopRecord();

        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            return false;
        } finally {
            //释放 AudioRecord
            if (audioRecord != null) {
                audioRecord.release();
            }
        }
    }

    //结束录音逻辑
    private boolean stopRecord() {
        try {
            //停止录音
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
            mfileoutputStream.close();
            //记录结束时间,统计时长
            mStopRecordTime = System.currentTimeMillis();
            //大于3秒才算成功，在主线程改变ui显示
            final int second = (int) (mStopRecordTime - mStartRecordTime / 1000);

            if (second > 3) {
                mMainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mTvLog.setText(mTvLog.getText() + "\n录音成功" + second + "秒");
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
            //捕获异常，避免闪退，返回false提示用户
            return false;
        }
        return true;
    }

    //实际播放逻辑
    private void doPlay(File audioFile){
        //配置播放器
        //音乐类型，扬声器播放
        Log.i("zhouwei","开始播放录音文件="+audioFile.getAbsolutePath());
        int streamType= AudioManager.STREAM_MUSIC;
        //录音时采用的采样频率，所以播放时候使用同样的采样频率。
        int sampleRate=44100;
        //MONO表示单声道，录音输入单声道,播放用输出单声道。
        int channelConfig=AudioFormat.CHANNEL_OUT_MONO;
        //录音时使用16bit,所以播放时使用同样的格式
        int audioFormat=AudioFormat.ENCODING_PCM_16BIT;
        //流模式
        int mode= AudioTrack.MODE_STREAM;
        //计算最小buffer大小
        int minBufferSize=AudioTrack.getMinBufferSize(sampleRate,channelConfig,audioFormat);
        //构造AudioTrack
        AudioTrack audioTrack=new AudioTrack(streamType,sampleRate,channelConfig,audioFormat,
                //不能小于AudioTrack的最低要求，也不能小于我们每次读的大小。
                Math.max(minBufferSize,BUFFER_SIZE),
                mode);
        Log.i("zhouwei","走到AudioTrack初始化");
        //从文件流读数据

        audioTrack.play();
        FileInputStream inputStream=null;
        try {
            inputStream=new FileInputStream(audioFile);
            int read;
            //只要没读完，循环写播放
            while ((read=inputStream.read(mBuffer))>0){
                int ret=audioTrack.write(mBuffer,0,read);
                Log.i("zhouwei","在写文件="+ret);
                //检查write返回值，错误处理
                switch (ret){
                    case AudioTrack.ERROR_INVALID_OPERATION:
                    case AudioTrack.ERROR_BAD_VALUE:
                    case AudioManager.ERROR_DEAD_OBJECT:
                        playFail();
                        return;
                    default:
                        break;
                }
            }

            //循环读数据，写到播放器去播放。
        }catch (RuntimeException |IOException e){
            e.printStackTrace();
            //错误处理，防止闪退。
            playFail();
        }finally {
            mIsPlaying=false;
            //关闭文件输入流
            if(inputStream!=null){
                closeQuietly(inputStream);
            }
            //播放器释放
            resetQuietly(audioTrack);
            Log.i("zhouwei","播放完成");
        }
    }

    private void resetQuietly(AudioTrack audioTrack){
        try {
            audioTrack.stop();
            audioTrack.release();
        }catch (RuntimeException e){
            e.printStackTrace();
        }
    }

    //静默关闭输入流
    private void closeQuietly(FileInputStream inputStream){
        try {
            inputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}