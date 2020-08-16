package com.example.cmakeandndkdemo.jni;

public class JNIWait_notify {
    static {
        System.loadLibrary("dynamic-lib");
    }

    public native void waitNativeThread();

    public native void notifyNativeThread();

    public native void startProductAndConsumerThread();


}
