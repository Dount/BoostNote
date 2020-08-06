//
// Created by zhouwei on 2020/8/6.
//

#include <jni.h>
/* Header for class com_example_ndkdemo_SayHello */

#ifndef _Included_com_example_ndkdemo_SayHello
#define _Included_com_example_ndkdemo_SayHello
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_example_ndkdemo_SayHello
 * Method:    sayHello
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_example_ndkdemo_SayHello_sayHello
  (JNIEnv *env , jclass clasz){
  return env->NewStringUTF("Hello From JNI!");
}

#ifdef __cplusplus
}
#endif
#endif
