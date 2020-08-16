package com.example.cmakeandndkdemo.jni;


import com.example.cmakeandndkdemo.model.ICallbackMethod;
import com.example.cmakeandndkdemo.model.IThreadCallback;

public class JNIInvokeMethod {
    static {
        System.loadLibrary("dynamic-lib");
    }

    public native void nativeCallBack(ICallbackMethod callback);

    public native void nativeThreadCallback(IThreadCallback callback);

}