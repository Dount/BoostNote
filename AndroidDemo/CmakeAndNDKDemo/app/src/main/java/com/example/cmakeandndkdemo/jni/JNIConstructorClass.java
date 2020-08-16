package com.example.cmakeandndkdemo.jni;

import com.example.cmakeandndkdemo.model.Animal;

public class JNIConstructorClass {
    static{
        System.loadLibrary("native-lib");
    }

    public native Animal invokeAnimalConstructors();

    public native Animal allocObjectConstructor();

}
