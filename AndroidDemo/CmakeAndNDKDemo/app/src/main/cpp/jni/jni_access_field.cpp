//
// Created by zhouwei on 2020/8/14.
//
//对应字段类型
//|Java   |JNI|
//|boolean|Z|
//|byte   |B|
//|char   |C|
//|short  |S|
//|int    |I|
//|long   |J|
//|float  |F|
//|double |D|
//|void   |V|

//完整字段类型
//|Java       |JNI|
//|String     |Ljava/lang/String|
//|Class      |Ljava/lang/Class|
//|Throwable  |Ljava/lang/Throwable|
//|int[]      |[I|
//|Object[]   |[Ljava/lang/Object;|
//|byte[]     |[B |


#include <jni.h>

extern "C"
JNIEXPORT void JNICALL
Java_com_example_cmakeandndkdemo_jni_JNIAccessField_accessInstanceField(
        JNIEnv *env,jobject instance,jobject animal){
        //获取类
        jclass cls=env->GetObjectClass(animal);
        //获取类中的字段
        jfieldID  fid=env->GetFieldID(cls,"name","Ljava/lang/String;");

        //自定义一个字符串
        jstring str=env->NewStringUTF("this is new name");

        env->SetObjectField(animal,fid,str);
}
//对静态字段进行修改
extern "C"
JNIEXPORT void JNICALL
Java_com_example_cmakeandndkdemo_jni_JNIAccessField_accessStaticField(
        JNIEnv *env,jobject instance,jobject animal){
    jclass cls=env->GetObjectClass(animal);
    jfieldID  fid=env->GetStaticFieldID(cls,"num","I");
    int num =env->GetStaticIntField(cls,fid);
    env->SetStaticIntField(cls,fid,++num);
}

//通过静态方法调用类中的静态字段。
extern "C"
JNIEXPORT void JNICALL
Java_com_example_cmakeandndkdemo_jni_JNIAccessField_staticAccessInstanceField(
        JNIEnv *env,jclass instance){
    jfieldID fid=env->GetStaticFieldID(instance,"num","I");
    int num =env->GetStaticIntField(instance,fid);
    env->SetStaticIntField(instance,fid,num+100);
}

