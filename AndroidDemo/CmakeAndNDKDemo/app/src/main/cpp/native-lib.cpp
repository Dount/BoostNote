#include <jni.h>
#include <string>

#include <People.h>
#include "People.h"

#include <android/log.h>

#define LOG_TAG "NativeMethod"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,LOG_TAG,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_cmakeandndkdemo_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    //std::string hello = "Hello from C++";
    People people;
    return env->NewStringUTF(people.getString().c_str());
}

//如果不是静态方法，第二个参数标示具体是哪一个java对象在调用这个native方法
extern "C"
JNIEXPORT jint JNICALL
Java_com_example_cmakeandndkdemo_MainActivity_intFromJNI(JNIEnv *env, jobject instance) {
    // TODO
}


//操作字符串
extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_cmakeandndkdemo_jni_JNIBasicType_callNativeString(JNIEnv *env,jobject instance,jstring mystring){
    const char *str = env->GetStringUTFChars(mystring, 0);
    LOGD("java String is %s",str);
    env->ReleaseStringUTFChars(mystring,str);
    return env->NewStringUTF("this is C Style string");
}


extern "C"
JNIEXPORT void JNICALL
Java_com_example_cmakeandndkdemo_jni_JNIBasicType_stringMethod(JNIEnv *env,jobject instance,jstring myString){
    const char *str=env->GetStringUTFChars(myString, 0);
    char buf[128];
    //获取长度
    int len =env->GetStringLength(myString);
    LOGD("Java string length is %d",len);
    //获取字符串的局部内容
    env->GetStringUTFRegion(myString,0,len-1,buf);
    LOGD("jstring is %s",buf);
    env->ReleaseStringUTFChars(myString,str);
}



// 八种基本数据类型 从 Java 到 JNI 的类型转换操作

// JNI 的基础数据类型在 Java 的基础类型上加了一个 j
// 查看源码，JNI 的基础数据类型就是在 C/C++ 基础之上，通过 typedef 声明的别名

///* Primitive types that match up with Java equivalents. */
//typedef uint8_t  jboolean; /* unsigned 8 bits */
//typedef int8_t   jbyte;    /* signed 8 bits */
//typedef uint16_t jchar;    /* unsigned 16 bits */
//typedef int16_t  jshort;   /* signed 16 bits */
//typedef int32_t  jint;     /* signed 32 bits */
//typedef int64_t  jlong;    /* signed 64 bits */
//typedef float    jfloat;   /* 32-bit IEEE 754 */
//typedef double   jdouble;  /* 64-bit IEEE 754 */

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_cmakeandndkdemo_jni_JNIBasicType_callNativeInt(JNIEnv *env,jobject instance,jint num){
    LOGD("java int value is %d", num);
    int c_num=num*2;
    return c_num;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_cmakeandndkdemo_jni_JNIBasicType_callNativeBoolean(JNIEnv *env,jobject instance,jboolean result){
    LOGD("java int value is %d", result);
    jboolean c_bool=(jbyte)!result;
    return c_bool;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_cmakeandndkdemo_jni_JNIBasicType_callNativeByte(JNIEnv *env,jobject instance,jbyte myByte){
    LOGD("java byte value is %d", myByte);
    jbyte c_byte = myByte + (jbyte) 10;
    return c_byte;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_cmakeandndkdemo_jni_JNIBasicType_callNativeChar(JNIEnv *env,jobject instance,jchar mychar){
    LOGD("java char value is %c", mychar);
    jchar c_char = mychar + (jchar) 3;
    return c_char;
}

extern "C"
JNIEXPORT jdouble JNICALL
Java_com_example_cmakeandndkdemo_jni_JNIBasicType_callNativeDouble (JNIEnv *env,jobject instance,jdouble mydouble){
    LOGD("java double value is %f", mydouble);
    jdouble c_double = mydouble + 20.0;
    LOGD("java double value is %f", c_double);
    return c_double;
}

extern "C"
JNIEXPORT jfloat JNICALL
Java_com_example_cmakeandndkdemo_jni_JNIBasicType_callNativeFloat (JNIEnv *env,jobject instance,jfloat myfloat){
    LOGD("java float value is %f", myfloat);
    jfloat c_float = myfloat + (jfloat) 10.0;
    LOGD("java float value is %f", c_float);
    return c_float;
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_example_cmakeandndkdemo_jni_JNIBasicType_callNativeLong (JNIEnv *env,jobject instance,jlong mylong){
    LOGD("java long value is %d", mylong);
    jlong c_long = mylong + 100;
    return c_long;
}

extern "C"
JNIEXPORT jshort JNICALL
Java_com_example_cmakeandndkdemo_jni_JNIBasicType_callNativeShort(JNIEnv *env,jobject instance,jshort myshort){
    LOGD("java char value is %d", myshort);
    jshort c_short = myshort + (jshort) 10;
    return c_short;
}



