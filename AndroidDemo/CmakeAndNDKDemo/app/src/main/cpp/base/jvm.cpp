//
// Created by zhouwei on 2020/8/9.
//

#include <jvm.h>

static JavaVM *gVM =nullptr;

#ifdef  __cplusplus
extern "C"{
#endif
    void setJvm(JavaVM *jvm){
        gVM=jvm;
    }

    JavaVM * getJvm(){
        return gVM;
    }
#ifdef __cplusplus
}
#endif
