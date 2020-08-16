//
// Created by zhouwei on 2020/8/9.
//

#include <jvm.h>
#include <base.h>
#include <pthread.h>

#define JAVA_CLASS "com/example/cmakeandndkdemo/load/JNIDynamicLoad"
jstring getMessage(JNIEnv *env,jobject jobj){
    return env->NewStringUTF("this is msg");
}

jint plus(JNIEnv *env,jobject jobj,jint x,jint y){
    return x+y;
}


static JNINativeMethod getMethods[]={
    //注册函数，
    //参数1:函数名
    //参数2:函数类型
    //参数3:函数指针
        {"getNativeString","()Ljava/lang/String;",(void *)getMessage},
        {"sum","(II)I",(void *)plus}
};

//注册JAVA类，
//参数1:JNIenv
//参数2:java包名
//参数3:注册的函数的集合
//参数4:函数的数量
int registerNativeMethods(JNIEnv *env, const char *name,
        JNINativeMethod *method,jint nMethods){
        jclass jcls;
        //找到声明native方法的类
        jcls =env->FindClass(name);
        if(jcls == nullptr){
            return JNI_FALSE;
        }
        //开始注册
        if(env->RegisterNatives(jcls,method,nMethods)<0){
            return JNI_FALSE;
        }

    return JNI_TRUE;
}


JNIEXPORT
int JNICALL JNI_OnLoad(JavaVM *vm, void *reserved){
    JNIEnv *env;
    if(vm->GetEnv(reinterpret_cast<void **>(&env),JNI_VERSION_1_6)!=JNI_OK){
        return JNI_FALSE;
    }

    setJvm(vm);
    registerNativeMethods(env,JAVA_CLASS,getMethods,2);

    return JNI_VERSION_1_6;
}