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

using namespace std;
std::queue<int> data;
pthread_mutex_t dataMutex;

pthread_cond_t dataCond;

//生产者用来不断产生物资,然后会唤醒消费者函数去执行
void *productThread(void *) {
    while(data.size()<10){
        pthread_mutex_lock(&dataMutex);
        LOGD("生产物品");
        data.push(1);
        if(data.size()> 0){
            LOGD("等待消费");
            pthread_cond_signal(&dataCond);
        }
        pthread_mutex_unlock(&dataMutex);
        sleep(3);
    }

    pthread_exit(0);
}

//消费者函数回去消费物资,如果没有物资会继续等待

void *consumerThread(void *){
    while (1){
        pthread_mutex_lock(&dataMutex);
        if(data.size()>0){
            LOGI("消费物品");

        } else{
            LOGI("等待生产");
            pthread_cond_wait(&dataCond,&dataMutex);
        }
        pthread_mutex_unlock(&dataMutex);
        sleep(1);
    }
    pthread_exit(0);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_cmakeandndkdemo_jni_JNIWait_1notify_startProductAndConsumerThread(JNIEnv *env,jobject instance){
    pthread_mutex_init(&dataMutex, nullptr);

    pthread_cond_init(&dataCond, nullptr);

    pthread_t productHandle;

    pthread_t consumerHandler;

    pthread_create(&productHandle, nullptr,productThread, nullptr);
    pthread_create(&consumerHandler, nullptr,consumerThread, nullptr);
}