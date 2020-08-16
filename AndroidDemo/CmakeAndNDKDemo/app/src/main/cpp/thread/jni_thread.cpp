//
// Created by zhouwei on 2020/8/9.
//
//JNI线程的创建

#include <jvm.h>
#include <queue>
//包括所有的线程处理函数
#include <pthread.h>
#include <unistd.h>



struct ThreadRunArgs{
    int id;
    int result;
};


void *printThreadHello(void *arg){
    //这里可以处理线程的耗时操作

    return nullptr;
}

void *printThreadArgs(void *arg){
    ThreadRunArgs *args= static_cast<ThreadRunArgs *>(arg);
    LOGD("thread id is %d", args->id);
    LOGD("thread result is %d",args->result);
    //显示的退出线程方法，相当于return 0
    pthread_exit(0);
    //return nullptr;
}

void *printThreadJoin(void *arg){
    ThreadRunArgs *args = static_cast<ThreadRunArgs *>(arg);

    //获取调用开始时间
    struct timeval begin;
    gettimeofday(&begin, nullptr);
    //模拟耗时过程
    LOGD("start time is %lld",begin.tv_sec);
    sleep(3);

    //获取调用结束时间
    struct timeval end;
    gettimeofday(&end, nullptr);
    LOGD("end time is %lld",end.tv_sec);

    LOGD("Time used is %d",end.tv_sec - begin.tv_sec);

    //返回值
    return reinterpret_cast<void *>(args->result);
}


extern "C"
JNIEXPORT void JNICALL
Java_com_example_cmakeandndkdemo_jni_JNIThread_createNativeThread(JNIEnv *env,jobject instance){

//创建一个线程的句柄
pthread_t handles;

//参数1：线程的句柄
//参数2：线程调度的信息
//参数3：线程具体执行的函数
//参数4：传递给线程的参数
int result =pthread_create(&handles, nullptr,printThreadHello, nullptr);
if(result==0){
LOGD("Create thread success");
} else {
LOGD("Create thread fail");
}
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_cmakeandndkdemo_jni_JNIThread_createNativeThreadWithArgs(JNIEnv *env,jobject instance){
//创建一个线程的句柄
pthread_t handles;

//参数1：线程的句柄
//参数2：线程调度的信息
//参数3：线程具体执行的函数
//参数4：传递给线程的参数
ThreadRunArgs *args =new ThreadRunArgs;
args->id =2;
args->result =100;
int result =pthread_create(&handles, nullptr,printThreadArgs, args);
if(result==0){
LOGD("Create thread success");
} else {
LOGD("Create thread fail");
}
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_cmakeandndkdemo_jni_JNIThread_joinNativeThread(JNIEnv *env,jobject instance){
pthread_t handles;
ThreadRunArgs *args =new ThreadRunArgs;
args->id =2;
args->result =100;
int result =pthread_create(&handles, nullptr,printThreadJoin, args);

void *ret =nullptr;

//等待异步线程去获取返回的结果。
pthread_join(handles,&ret);
LOGD("result is %d",ret);
}




