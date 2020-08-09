#include <jni.h>
#include <string>

#include <people.h>
#include "People/People.h"

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

}

    // TODO


//如果是静态方法，由于没有对应的java实例对象，
// 因此JNI层的函数第二个参数是jclass clazz，表
// 示调用哪一个Java 的Class的静态方法。
    extern "C"
    JNIEXPORT jint JNICALL
    Java_com_example_cmakeandndkdemo_MainActivity_intStaticFromJNI(JNIEnv *env, jclass type) {

        // TODO
}