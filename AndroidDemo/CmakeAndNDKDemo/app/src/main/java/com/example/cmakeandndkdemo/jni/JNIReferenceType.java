package com.example.cmakeandndkdemo.jni;

public class JNIReferenceType {

    static {
        System.loadLibrary("native-lib");
    }

    public native String callNativeStringArray(String[] strArray);

    public native String errorCacheLocalReference();

    public native String cacheWithGlobalReferenece();

    public native void useWeakGlobalReference();


}
