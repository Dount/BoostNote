package com.example.cmakeandndkdemo.jni;

import android.graphics.Bitmap;

public class JNIBitmap {
    static {
        System.loadLibrary("native-lib");
    }

    public native Bitmap callNativeMirrorBitmap(Bitmap bitmap);
}
