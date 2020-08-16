package com.example.cmakeandndkdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cmakeandndkdemo.jni.JNIAccessField;
import com.example.cmakeandndkdemo.jni.JNIAccessMethod;
import com.example.cmakeandndkdemo.jni.JNIBasicType;
import com.example.cmakeandndkdemo.jni.JNIBitmap;
import com.example.cmakeandndkdemo.jni.JNIConstructorClass;
import com.example.cmakeandndkdemo.jni.JNIException;
import com.example.cmakeandndkdemo.jni.JNIInvokeMethod;
import com.example.cmakeandndkdemo.jni.JNIReferenceType;
import com.example.cmakeandndkdemo.jni.JNIThread;
import com.example.cmakeandndkdemo.jni.JNIWait_notify;
import com.example.cmakeandndkdemo.load.JNIDynamicLoad;
import com.example.cmakeandndkdemo.model.Animal;
import com.example.cmakeandndkdemo.model.ICallbackMethod;
import com.example.cmakeandndkdemo.model.IThreadCallback;

public class MainActivity extends AppCompatActivity {
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public String[] strings={"apple","pear","bannana"};
    private Animal animal;
    private JNIThread jniThread;
    private JNIWait_notify jniWait_notify;
    private Bitmap bitmap;
    private JNIBitmap jniBitmap;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
        Button button1=findViewById(R.id.thread_button1);
        Button button2=findViewById(R.id.thread_button2);
        Button button3=findViewById(R.id.thread_button3);
        Button button4=findViewById(R.id.lockButton);
        Button button5=findViewById(R.id.unlockButton);
        Button button6=findViewById(R.id.product_resumer);
        button1.setOnClickListener(new onclick());
        button2.setOnClickListener(new onclick());
        button3.setOnClickListener(new onclick());
        button4.setOnClickListener(new onclick());
        button5.setOnClickListener(new onclick());
        button6.setOnClickListener(new onclick());
        imageView = findViewById(R.id.bitmap_image);
        imageView.setOnClickListener(new onclick());
        animal = new Animal("animal");
        final JNIDynamicLoad jniDynamicLoad=new JNIDynamicLoad();
        final TextView registTv = findViewById(R.id.registText);
        registTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registTv.setText(jniDynamicLoad.getNativeString());
            }
        });

        JNIBasicType jniBasicType=new JNIBasicType();
        int result=jniBasicType.callNativeInt(32);
        byte mybyte=jniBasicType.callNativeByte((byte) 64);
        char mychar=jniBasicType.callNativeChar('1');
        short myshort=jniBasicType.callNativeShort((short) 100);
        long mylong=jniBasicType.callNativeLong(400);
        float myfloat=jniBasicType.callNativeFloat((float) 1.0);
        double mydouble=jniBasicType.callNativeDouble(100.0);
        boolean myboolean =jniBasicType.callNativeBoolean(true);
        //JNI基础类型转换
        Log.i("zhouwei","JNI返回的int="+result);
        Log.i("zhouwei","JNI返回的Byte="+mybyte);
        Log.i("zhouwei","JNI返回的Char="+mychar);
        Log.i("zhouwei","JNI返回的Short="+myshort);
        Log.i("zhouwei","JNI返回的Long="+mylong);
        Log.i("zhouwei","JNI返回的Float="+myfloat);
        Log.i("zhouwei","JNI返回的Double="+mydouble);
        Log.i("zhouwei","JNI返回的Boolean="+myboolean);
        //JNI字符串转换
        jniBasicType.callNativeString("zhouwei");
        jniBasicType.stringMethod("zhouwei");
        //JNI引用类型转换
        JNIReferenceType jniReferenceType=new JNIReferenceType();
        String str=jniReferenceType.callNativeStringArray(strings);
        Log.i("zhouwei","JNI返回的字符串="+str);
        //局部引用
        String cacheStr=jniReferenceType.errorCacheLocalReference();
        Log.i("zhouwei","局部引用="+cacheStr);
        //全局引用
        String globalStr=jniReferenceType.cacheWithGlobalReferenece();
        Log.i("zhouwei","全局引用="+globalStr);
        //弱引用
        jniReferenceType.useWeakGlobalReference();
        //JNI访问java类字段
        JNIAccessField jniAccessField =new JNIAccessField();
        jniAccessField.accessStaticField(animal);
        jniAccessField.accessInstanceField(animal);
        Log.i("zhouwei","name is "+ animal.getName());
        Log.i("zhouwei","num is "+Animal.getNum());
        JNIAccessField.staticAccessInstanceField();
        Log.i("zhouwei","num is "+JNIAccessField.num);

        //JNI访问java类方法
        JNIAccessMethod jniAccessMethod=new JNIAccessMethod();
        jniAccessMethod.accessInstanceMethod(animal);
        jniAccessMethod.accessStaticMethod(animal);

        //JNI子线程访问java方法
        JNIInvokeMethod jniInvokeMethod=new JNIInvokeMethod();
        jniInvokeMethod.nativeCallBack(new ICallbackMethod() {
            @Override
            public void callback() {
                    Log.i("zhouwei","thread name is "+Thread.currentThread().getName());
            }
        });

        jniInvokeMethod.nativeThreadCallback(new IThreadCallback() {
            @Override
            public void callback() {
                Log.i("zhouwei","thread name is "+Thread.currentThread().getName());
            }
        });

        //JNI访问Java构造方法
        JNIConstructorClass jniConstructorClass=new JNIConstructorClass();
        String str1=jniConstructorClass.invokeAnimalConstructors().getName();
        String str2=jniConstructorClass.allocObjectConstructor().getName();
        Log.i("zhouwei","Message ="+str1);
        Log.i("zhouwei","Message ="+str2);
        //JNI异常处理
        JNIException jniException=new JNIException();
        try {
            jniException.nativeThrowException();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Log.i("zhouwei","从Native抛出异常"+e.getMessage());
        }
        jniException.nativeInvokeJavaException();

        //JNI线程的创建
        jniThread = new JNIThread();

        //JNI线程同步
        jniWait_notify = new JNIWait_notify();

        //JNI中Bitmap操作
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.timg);
        jniBitmap = new JNIBitmap();
    }

    public class onclick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.thread_button1:
                    //无参数启动线程
                   jniThread.createNativeThread();
                    break;
                case R.id.thread_button2:
                    //有参启动线程
                    jniThread.createNativeThreadWithArgs();
                    break;
                case R.id.thread_button3:
                    jniThread.joinNativeThread();
                    //线程join操作
                    break;
                case R.id.lockButton:
                    jniWait_notify.waitNativeThread();
                    break;
                case R.id.unlockButton:
                    jniWait_notify.notifyNativeThread();
                    break;

                case R.id.product_resumer:
                    jniWait_notify.startProductAndConsumerThread();
                    break;
                case R.id.bitmap_image:
                    Bitmap result=jniBitmap.callNativeMirrorBitmap(bitmap);
                    imageView.setImageBitmap(result);
                    break;
            }
        }
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public native int intFromJNI();

}
