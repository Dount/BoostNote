//
// Created by zhouwei on 2020/8/15.
//

#include <jni.h>

//方法中(参数)返回值
//|java                   |JNI
//|String fun();          |()Ljava/lang/String;
//|long f(int i,Class s); |(ILjava/lang/Class;)J
//|String(byte[]bytes);   |([B)V  解释:由于是String 的构造函数，因此返回的是void

extern "C"
JNIEXPORT void JNICALL
Java_com_example_cmakeandndkdemo_jni_JNIAccessMethod_accessInstanceMethod(JNIEnv *env,
        jobject instance,jobject animal){
        jclass cls=env->GetObjectClass(animal);
        jmethodID mid =env->GetMethodID(cls,"callInstanceMethod","(I)V");
        //参数1：类名，参数2：方法名,参数3：返回值....参数X：返回值
        env->CallVoidMethod(animal,mid,2);
}

//通过非静态方法调用类中的静态方法。
extern "C"
JNIEXPORT void JNICALL
Java_com_example_cmakeandndkdemo_jni_JNIAccessMethod_accessStaticMethod(JNIEnv *env,
        jobject instance,jobject animal){
        jclass cls=env->GetObjectClass(animal);
        jmethodID mid =env->GetStaticMethodID(cls,"callStaticMethod","(Ljava/lang/String;)Ljava/lang/String;");
        jstring str=env->NewStringUTF("my jstring");
        //参数1：类名，参数2：方法名,参数3：返回值....参数X：返回值
        env->CallStaticObjectMethod(cls,mid,str);
        //实现callStaticMethod重载方法
        mid =env->GetStaticMethodID(cls,"callStaticMethod","([Ljava/lang/String;I)Ljava/lang/String;");


        //开始构建一个字符串数组
        jclass strClass = env->FindClass("java/lang/String");
        int size=2;
        jstring strImte;
        jobjectArray  strArray = env->NewObjectArray(size,strClass, nullptr);
        //往数组里添加字符串
        for(int i=0;i<size;i++){
            strImte = env->NewStringUTF("zhouwei in native");
            env->SetObjectArrayElement(strArray,i,strImte);
            //释放jstring
            env->DeleteLocalRef(strImte);

        }
        env->CallStaticObjectMethod(cls,mid,strArray,size);
        //释放Class
        env->DeleteLocalRef(strClass);
        //释放数组
        env->DeleteLocalRef(strArray);

}