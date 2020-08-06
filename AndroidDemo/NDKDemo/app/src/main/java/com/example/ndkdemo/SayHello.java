package com.example.ndkdemo;

public class SayHello {
    static {
        System.loadLibrary("hello");
    }

    public static native String sayHello();
}
