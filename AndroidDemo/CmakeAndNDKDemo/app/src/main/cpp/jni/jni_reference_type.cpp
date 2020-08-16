//
// Created by zhouwei on 2020/8/13.
//

#include <jni.h>
#include <string>
#include <android/log.h>

//|Java Reference     |Native         |JavaReference  |Native
//|All object         |jobject        |char[]         |jcharArrya
//|java.lang.Class    |jclass         |short[]        |jshortArray
//|java.lang.String   |jstring        |int[]          |jintArray
//|Object[]           |jobjectArray   |long[]         |jlongArray
//|boolean[]          |jbooleanArray  |float[]        |jfloatArray
//|byte[]             |jbyteArray     |double[]       |jdoubleArray
//|java.lang.Throwable|jthrowable     |


#define LOG_TAG "NativeMethod"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,LOG_TAG,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_cmakeandndkdemo_jni_JNIReferenceType_callNativeStringArray(
        JNIEnv *env ,jobject thiz,jobjectArray str_array){
    int len=env->GetArrayLength(str_array);
    LOGD("len is %d",len);
    //通过对象获取数组中的元素
    jstring firstStr= static_cast<jstring>(env->GetObjectArrayElement(str_array, 0));
    //转换成字符串
    const char*str=env->GetStringUTFChars(firstStr,0);
    LOGD("first str is %s",str);
    //释放创建的字符串
    env->ReleaseStringUTFChars(firstStr,str);
    return env->NewStringUTF(str);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_cmakeandndkdemo_jni_JNIReferenceType_errorCacheLocalReference(JNIEnv *env,jobject instance){
    jclass  localRefs = env->FindClass("java/lang/String");
    jmethodID mid =env->GetMethodID(localRefs,"<init>","(Ljava/lang/String;)V");
    jstring str=env->NewStringUTF("string");
    for(int i =0;i<1000;++i){
        jclass cls=env->FindClass("java/lang/String");
        //删除本地引用
        env->DeleteLocalRef(cls);
    }
    return static_cast<jstring>(env->NewObject(localRefs,mid,str));
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_cmakeandndkdemo_jni_JNIReferenceType_cacheWithGlobalReferenece(JNIEnv *env,jobject instance){
    static jclass  stringClass = nullptr;
    if(stringClass == nullptr){
        jclass cls =env->FindClass("java/lang/String");
        //转换为全局变量,因为局部变量不能缓存static.
        stringClass = static_cast<jclass>(env->NewLocalRef(cls));
        //将局部变量释放
        env->DeleteLocalRef(cls);
    } else{
        LOGD("use cached");
    }
    jmethodID mid =env->GetMethodID(stringClass,"<init>","(Ljava/lang/String;)V");
    jstring str=env->NewStringUTF("string");
    return static_cast<jstring>(env->NewObject(stringClass,mid,str));
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_cmakeandndkdemo_jni_JNIReferenceType_useWeakGlobalReference(JNIEnv *env,jobject instance){
    static jclass  stringClass = nullptr;
    if(stringClass == nullptr){
        jclass cls =env->FindClass("java/lang/String");
        //转换为全局变量,因为局部变量不能缓存static.
        stringClass = static_cast<jclass>(env->NewWeakGlobalRef(cls));
        //将局部变量释放
        env->DeleteLocalRef(cls);
    } else{
        LOGD("use cached");
    }
    jmethodID mid =env->GetMethodID(stringClass,"<init>","(Ljava/lang/String;)V");
    //判断引用是否被GC调用
    jboolean isGc=env->IsSameObject(stringClass, nullptr);
    //释放弱引用
    env->DeleteWeakGlobalRef(stringClass);

    LOGD("GC state =%d",isGc);
}