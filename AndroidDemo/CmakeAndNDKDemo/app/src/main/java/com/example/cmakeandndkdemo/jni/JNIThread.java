package com.example.cmakeandndkdemo.jni;

public class JNIThread {

    static {
        System.loadLibrary("dynamic-lib");
    }

    public native void createNativeThread();

    public native void createNativeThreadWithArgs();

    public native void joinNativeThread();



}
