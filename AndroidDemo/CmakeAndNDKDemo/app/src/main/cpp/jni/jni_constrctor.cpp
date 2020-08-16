//
// Created by zhouwei on 2020/8/15.
//

#include <base.h>

extern "C"
JNIEXPORT jobject JNICALL
Java_com_example_cmakeandndkdemo_jni_JNIConstructorClass_invokeAnimalConstructors(JNIEnv *env,jobject instance){
    jclass cls=env->FindClass("com/example/cmakeandndkdemo/model/Animal");

    //构造方法的方法名用<init>表示
    jmethodID mid =env->GetMethodID(cls,"<init>","(Ljava/lang/String;)V");
    jstring str=env->NewStringUTF("JNI访问Java构造方法方式1");
    jobject animal = env->NewObject(cls,mid,str);
    return animal;
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_example_cmakeandndkdemo_jni_JNIConstructorClass_allocObjectConstructor(JNIEnv *env,jobject instance){
    jclass cls=env->FindClass("com/example/cmakeandndkdemo/model/Animal");
    jmethodID mid =env->GetMethodID(cls,"<init>","(Ljava/lang/String;)V");
    //产生一个object
    jobject animal =env->AllocObject(cls);
    jstring str=env->NewStringUTF("JNI访问Java构造方法方式2");
    env->CallNonvirtualVoidMethod(animal,cls,mid,str);
    return animal;
}