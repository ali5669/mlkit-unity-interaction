
#include <jni.h>
#include "main.h"

struct RGB {
    unsigned char r, g, b;
};

extern "C"
JNIEXPORT void JNICALL
Java_com_unity3d_player_imageHandler_ImageHandler_handleImage(
        JNIEnv *env,
        jclass type,
        jlong ptr, jint width, jint height, jbyteArray image_) {
    unsigned char *srcRgb24 = reinterpret_cast<unsigned char *>(ptr);
    jbyte *image = env->GetByteArrayElements(image_, 0);

    int frameSize = width * height;
    int i, j;
    int index = 0;
    unsigned char r, g, b;
    int y, u, v;

    for (j = 0; j < height; j++) {
        int size = frameSize + (j >> 1) * width;
        for (i = 0; i < width; i++, index += 3) {
            b = srcRgb24[index];
            g = srcRgb24[index + 1];
            r = srcRgb24[index + 2];

            // rgb to yuv
            y = ((66 * r + 129 * g + 25 * b + 128) >> 8) + 16;
            u = ((-38 * r - 74 * g + 112 * b + 128) >> 8) + 128;
            v = ((112 * r - 94 * g - 18 * b + 128) >> 8) + 128;

            //NV21 has a plane of Y and interleaved planes of VU each sampled by a factor of 2
            // meaning for every 4 Y pixels there are 1 V and 1 U. Note the sampling is every other
            // pixel AND every other scanline.
            image[j * width + i] = (y < 0) ? 0 : ((y > 255) ? 255 : y);
            if (j % 2 == 0 && i % 2 == 0) {
                image[frameSize + j * width / 2 + i] = (v < 0) ? 0 : ((v > 255) ? 255 : v);
            } else if (j % 2 == 0 && i % 2 == 1) {
                image[frameSize + j * width / 2 + i - 1] = (u < 0) ? 0 : ((u > 255) ? 255 : u);
            }
        }
    }



    // 释放
    env->ReleaseByteArrayElements(image_, image, 0);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_unity3d_player_imageHandler_ImageHandler_RGB2YUV420P(
        JNIEnv *env,
        jclass type,
        jlong ptr, jint width, jint height, jbyteArray image_) {
    unsigned char *srcRgb24 = reinterpret_cast<unsigned char*>(ptr);
    jbyte *ptrY, *ptrU, *ptrV;
    jbyte *image = env->GetByteArrayElements(image_, 0);

    ptrY = image;
    ptrV = image + width * height;
    ptrU = ptrV + (width * height * 1 / 4);

    unsigned char y, u, v, r, g, b;
    int index = 0;
    for (int j = 0; j < height; j++) {
        for (int i = 0; i < width; i++) {
            index = width * j * 3 + i * 3;
            r = srcRgb24[index];
            g = srcRgb24[index + 1];
            b = srcRgb24[index + 2];
            y = (unsigned char) ((66 * r + 129 * g + 25 * b + 128) >> 8) + 16;
            u = (unsigned char) ((-38 * r - 74 * g + 112 * b + 128) >> 8) + 128;
            v = (unsigned char) ((112 * r - 94 * g - 18 * b + 128) >> 8) + 128;
            *(ptrY++) = ClipValue(y, 0, 255);
            if (j % 2 == 0 && i % 2 == 0) {
                *(ptrU++) = ClipValue(u, 0, 255);
            } else if (i % 2 == 0) {
                *(ptrV++) = ClipValue(v, 0, 255);
            }

        }
    }

    env->ReleaseByteArrayElements(image_, image, 0);

}

unsigned char ClipValue(unsigned char x, unsigned char min_val, unsigned char max_val) {
    if (x > max_val) {
        return max_val;
    } else if (x < min_val) {
        return min_val;
    } else {
        return x;
    }
}