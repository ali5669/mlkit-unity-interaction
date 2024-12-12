package com.unity3d.player.poseHandler;

import static android.content.Context.CAMERA_SERVICE;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.PoseLandmark;
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions;

import com.unity3d.player.modelHandler.ModelHandler;
import com.unity3d.player.poseHandler.poseDetector.PoseDetectorProcessor;
import com.unity3d.player.poseHandler.poseDetector.PoseGraphic;
import com.unity3d.player.poseHandler.poseDetector.VisionImageProcessor;
import com.unity3d.player.poseHandler.poseDetector.VisionProcessorBase;
import com.unity3d.player.utils.FrameMetadata;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PoseHandler {
    private static final String TAG = "PoseHandler";
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private final Object processorLock = new Object();
    private VisionImageProcessor imageProcessor;
    private int rotation;


    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 0);
        ORIENTATIONS.append(Surface.ROTATION_90, 90);
        ORIENTATIONS.append(Surface.ROTATION_180, 180);
        ORIENTATIONS.append(Surface.ROTATION_270, 270);
    }


    public PoseHandler(Activity activity) throws CameraAccessException, IOException {
        PoseDetectorOptions options = new PoseDetectorOptions.Builder()
                .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
                .build();

        imageProcessor = new PoseDetectorProcessor(activity, options);

        rotation = getRotationCompensation("0", activity, false);
    }

    public void detectInImage(byte[] image, int width, int height) {

//        Log.i(TAG, "rotation: " + rotation);

        ByteBuffer buffer = ByteBuffer.allocate(image.length);
//        buffer.put(rotateYUV420Degree180(image, width, height));
        buffer.put(image);
        buffer.flip();

        try {
            synchronized (processorLock) {
                imageProcessor.processByteBuffer(
                        buffer,
                        new FrameMetadata.Builder()
                                .setWidth(width)
                                .setHeight(height)
                                .setRotation(rotation)
                                .build());
            }
        } catch (Exception e) {
            Log.e(TAG, "detectInImage: process image error", e);
        }

//        YuvImage yuv = new YuvImage(image, ImageFormat.YV12, width, height, null);
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        yuv.compressToJpeg(new Rect(0, 0, width, height), 100, out);
//        byte[] bytes = out.toByteArray();
//        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//        bitmap = null;
////
////
////
//        Matrix matrix = new Matrix();
//        matrix.postRotate(180); // 此处可以设置旋转角度
//        matrix.preScale(-1, 1); // 此处可以设置翻转方向
////
//        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
////
////        InputImage img = InputImage.fromBitmap(rotatedBitmap, 0);
//        rotatedBitmap = null;

    }


    /**
     * Get the angle by which an image must be rotated given the device's current
     * orientation.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private int getRotationCompensation(String cameraId, Activity activity, boolean isFrontFacing)
            throws CameraAccessException {
        // Get the device's current rotation relative to its "native" orientation.
        // Then, from the ORIENTATIONS table, look up the angle the image must be
        // rotated to compensate for the device's rotation.
        int deviceRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int rotationCompensation = ORIENTATIONS.get(deviceRotation);

        // Get the device's sensor orientation.
        CameraManager cameraManager = (CameraManager) activity.getSystemService(CAMERA_SERVICE);

        int sensorOrientation = cameraManager
                .getCameraCharacteristics(cameraId)
                .get(CameraCharacteristics.SENSOR_ORIENTATION);

        if (isFrontFacing) {
            rotationCompensation = (sensorOrientation + rotationCompensation) % 360;
        } else { // back-facing
            rotationCompensation = (sensorOrientation - rotationCompensation + 360) % 360;
        }
        return rotationCompensation;
    }

    private byte[] rotateYUV420Degree180(byte[] data, int imageWidth, int imageHeight) {
        byte[] yuv = new byte[imageWidth * imageHeight * 3 / 2];
        int i = 0;
        int count = 0;

        for (i = imageWidth * imageHeight - 1; i >= 0; i--) {
            yuv[count] = data[i];
            count++;
        }

        i = imageWidth * imageHeight * 3 / 2 - 1;
        for (i = imageWidth * imageHeight * 3 / 2 - 1; i >= imageWidth * imageHeight; i -= 2) {
            yuv[count++] = data[i - 1];
            yuv[count++] = data[i];
        }
        return yuv;
    }
}
