package com.example.cmakeandndkdemo.jni;

import com.example.cmakeandndkdemo.model.Animal;

public class JNIAccessMethod {

    static {
        System.loadLibrary("native-lib");
    }

    public native void accessInstanceMethod(Animal animal);

    public native void accessStaticMethod(Animal animal);

}
