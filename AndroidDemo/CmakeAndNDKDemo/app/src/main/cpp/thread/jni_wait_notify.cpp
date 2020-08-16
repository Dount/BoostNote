//
// Created by zhouwei on 2020/8/16.
//

#include <base.h>
#include <pthread.h>
#include <stdio.h>
#include <jvm.h>
#include <unistd.h>
#include <queue>
#include <ostream>

//互斥锁-线程等待
pthread_mutex_t mutex;

//条件变量-线程之间的唤醒和释放
pthread_cond_t cond;

pthread_t waitHandel;
pthread_t notifyHandle;

int flag=0;

void *waitThread(void *){
    LOGI("wait thread lock");
    //加锁
    pthread_mutex_lock(&mutex);

    while (flag==0){
        //线程等待挂起
        LOGI("waiting");
        //通过pthread_cond_wait 释放掉锁后，下面pthread_mutex_lock 才可以拿到锁
        pthread_cond_wait(&cond,&mutex);
    }
    LOGI("wait thread unlock");
    //释放锁
    pthread_mutex_unlock(&mutex);
    pthread_exit(0);

}

void *notifyThread(void *){
    LOGD("notify thread lock");

    pthread_mutex_lock(&mutex);
    flag=1;
    pthread_mutex_unlock(&mutex);

    //去唤醒上面wait
    pthread_cond_signal(&cond);
    LOGD("signal...");
    LOGD("notify thread unlock");
    pthread_exit(0);
}


extern "C"
JNIEXPORT void JNICALL
Java_com_example_cmakeandndkdemo_jni_JNIWait_1notify_waitNativeThread(JNIEnv *env,jobject instance){
    //对mutex进行初始化
    pthread_mutex_init(&mutex, nullptr);
    //对cond进行初始化
    pthread_cond_init(&cond, nullptr);

    pthread_create(&waitHandel, nullptr,waitThread, nullptr);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_cmakeandndkdemo_jni_JNIWait_1notify_notifyNativeThread(JNIEnv *env,jobject instance){
    pthread_create(&notifyHandle, nullptr,notifyThread, nullptr);
}