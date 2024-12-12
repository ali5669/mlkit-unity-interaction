package com.unity3d.player.imageHandler;

// TODO: 看看这个类，说不定能用上
import cn.easyar.ImageHelper;

public class ImageHandler {
    static {
        System.loadLibrary("native-lib");
    }

    public static native void handleImage(long ptr, int width, int height, byte[] image_);
    public static native void RGB2YUV420P(long ptr, int width, int height, byte[] image_);
}
