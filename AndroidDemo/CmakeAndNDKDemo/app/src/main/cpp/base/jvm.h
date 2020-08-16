//
// Created by zhouwei on 2020/8/15.
//

#ifndef CMAKEANDNDKDEMO_JVM_H
#define CMAKEANDNDKDEMO_JVM_H

#include <base.h>
#ifdef __cplusplus
extern "C"{
#endif

void setJvm(JavaVM *jvm);

JavaVM *getJvm();

#ifdef __cplusplus
}
#endif

#endif //CMAKEANDNDKDEMO_JVM_H
