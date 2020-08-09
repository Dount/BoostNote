package com.example.cmakeandndkdemo.load;


//JNI 动态注册的方法
public class JNIDynamicLoad {
    static {
        System.loadLibrary("dynamic-lib");
    }

    public native int sum(int x, int y);

    public native String getNativeString();
}
