

#ifndef INTERACTIONDEMO_MAIN_H
#define INTERACTIONDEMO_MAIN_H

#include <jni.h>


extern "C"{
JNIEXPORT void JNICALL
    Java_com_unity3d_player_imageHandler_ImageHandler_handleImage(
            JNIEnv *env,
    jclass type,
            jlong ptr, jint width, jint height, jbyteArray image_);

JNIEXPORT void JNICALL
    Java_com_unity3d_player_imageHandler_ImageHandler_RGB2YUV420P(
        JNIEnv *env,
        jclass type,
        jlong ptr, jint width, jint height, jbyteArray image_);

unsigned char ClipValue(unsigned char x, unsigned char min_val, unsigned char max_val);
}

#endif //INTERACTIONDEMO_MAIN_H