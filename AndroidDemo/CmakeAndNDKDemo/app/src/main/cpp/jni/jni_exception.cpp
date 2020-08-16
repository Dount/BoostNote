//
// Created by zhouwei on 2020/8/16.
//
#include <base.h>

extern "C"
JNIEXPORT void JNICALL
Java_com_example_cmakeandndkdemo_jni_JNIException_nativeInvokeJavaException (JNIEnv *env,jobject intstance){
    jclass cls =env->FindClass("com/example/cmakeandndkdemo/jni/JNIException");
    //获取operation方法
    jmethodID  mid1=env->GetMethodID(cls,"operation","()I");
    //获取构造方法
    jmethodID  mid2=env->GetMethodID(cls,"<init>","()V");
    //new 一个Exception方法。
    jobject obj=env->NewObject(cls,mid2);
    //通过Exception对象调用Operation.
    env->CallIntMethod(obj,mid1);
    //ExceptionOccurred()检查调用有没有发生异常。
    jthrowable exc =env->ExceptionOccurred();
    if(exc){
        //打印崩溃的日志
        env->ExceptionDescribe();
        //清除掉日常
        env->ExceptionClear();
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_cmakeandndkdemo_jni_JNIException_nativeThrowException(JNIEnv *env,jobject instance){

    jclass cls =env->FindClass("java/lang/IllegalArgumentException");

    env->ThrowNew(cls,"native throw exception");

    //Thrownew 向JAVA层抛出异常。
}