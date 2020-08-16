package com.example.cmakeandndkdemo.jni;

import com.example.cmakeandndkdemo.model.Animal;

public class JNIAccessField {
    static {
        System.loadLibrary("native-lib");
    }

    public static int num;

    public native void accessInstanceField(Animal animal);

    public native void accessStaticField(Animal animal);

    public static native void staticAccessInstanceField();



}
