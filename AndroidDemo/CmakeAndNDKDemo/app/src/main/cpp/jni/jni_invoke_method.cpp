//
// Created by zhouwei on 2020/8/9.
//

#include <jni.h>
#include <jvm.h>
#include <pthread.h>

void *threadCallback(void *);


static jclass threadClazz;
static jmethodID threadMethod;
static jobject threadObject;

extern "C"
JNIEXPORT void JNICALL
Java_com_example_cmakeandndkdemo_jni_JNIInvokeMethod_nativeCallBack(JNIEnv *env,
        jobject instance,jobject callback){
        jclass callbackClazz = env->GetObjectClass(callback);
        jmethodID  callbackMethod = env->GetMethodID(callbackClazz,"callback","()V");
        env->CallVoidMethod(callback,callbackMethod);
}

//关于线程的方法。知识点不够，需要回头来看。8


extern "C"
JNIEXPORT void JNICALL
Java_com_example_cmakeandndkdemo_jni_JNIInvokeMethod_nativeThreadCallback(JNIEnv *env,
        jobject instance,jobject callback){
        //NewGlobalRef 全局的引用
        threadObject =env->NewGlobalRef(callback);
        threadClazz  =env->GetObjectClass(callback);
        threadMethod =env->GetMethodID(threadClazz,"callback","()V");

        //开启线程
        pthread_t handle;
        pthread_create(&handle, nullptr,threadCallback, nullptr);
}

void *threadCallback(void *){
    //实现在子线程中拿到Env
    JavaVM *gVM=getJvm();
    JNIEnv *env = nullptr;
    if(gVM->AttachCurrentThread(&env, nullptr)==0){
        //通过env来调用方法。
        env->CallVoidMethod(threadObject,threadMethod);
        gVM->DetachCurrentThread();
        //AttachCurrentThread与DetachCurrentThread需要配套使用
    }
    return 0;
}
