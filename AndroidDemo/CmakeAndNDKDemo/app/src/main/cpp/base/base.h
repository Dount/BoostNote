//
// Created by zhouwei on 2020/8/9.
//

#include <android/log.h>

#ifndef CMAKEANDNDKDEMO_BASE_H
#define CMAKEANDNDKDEMO_BASE_H

#define LOG_TAG "NativeMethod"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,LOG_TAG,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)


#endif //CMAKEANDNDKDEMO_BASE_H
