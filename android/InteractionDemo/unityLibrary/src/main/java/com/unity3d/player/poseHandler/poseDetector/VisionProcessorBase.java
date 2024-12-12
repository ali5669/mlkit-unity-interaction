/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.unity3d.player.poseHandler.poseDetector;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import androidx.annotation.GuardedBy;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.mlkit.vision.common.InputImage;
import com.unity3d.player.utils.FrameMetadata;
import com.unity3d.player.utils.ScopedExecutor;

import java.nio.ByteBuffer;

public abstract class VisionProcessorBase<T> implements VisionImageProcessor {
    private static final String TAG = "VisionProcessorBase";
    private final ActivityManager activityManager;
    private final ScopedExecutor executor;

    // Whether this processor is already shut down
    private boolean isShutdown;

    // To keep the latest images and its metadata.
    @GuardedBy("this")
    private ByteBuffer latestImage;

    @GuardedBy("this")
    private FrameMetadata latestImageMetaData;
    // To keep the images and metadata in process.
    @GuardedBy("this")
    private ByteBuffer processingImage;

    @GuardedBy("this")
    private FrameMetadata processingMetaData;

    protected VisionProcessorBase(Context context) {
        activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        executor = new ScopedExecutor(TaskExecutors.MAIN_THREAD);
    }


    // -----------------Code for processing live preview frame from Camera1 API-----------------------
    @Override
    public synchronized void processByteBuffer(
            ByteBuffer data, final FrameMetadata frameMetadata) {
        latestImage = data;
        latestImageMetaData = frameMetadata;
        if (processingImage == null && processingMetaData == null) {
            processLatestImage();
        }
    }

    private synchronized void processLatestImage() {
        processingImage = latestImage;
        processingMetaData = latestImageMetaData;
        latestImage = null;
        latestImageMetaData = null;
        if (processingImage != null && processingMetaData != null && !isShutdown) {
            processImage(processingImage, processingMetaData);
        }
    }

    private void processImage(
            ByteBuffer data, final FrameMetadata frameMetadata) {
        // If live viewport is on (that is the underneath surface view takes care of the camera preview
        // drawing), skip the unnecessary bitmap creation that used for the manual preview drawing.

        requestDetectInImage(
                InputImage.fromByteBuffer(
                        data,
                        frameMetadata.getWidth(),
                        frameMetadata.getHeight(),
                        frameMetadata.getRotation(),
                        InputImage.IMAGE_FORMAT_YV12))
                .addOnSuccessListener(executor, results -> processLatestImage());
    }


    // -----------------Common processing logic-------------------------------------------------------
    private Task<T> requestDetectInImage(final InputImage image) {
        return detectInImage(image)
                .addOnSuccessListener(
                        executor,
                        results -> {
                            VisionProcessorBase.this.onSuccess(results);
                        })
                .addOnFailureListener(
                        executor,
                        e -> {
                            String error = "Failed to process. Error: " + e.getLocalizedMessage();
                            Log.d(TAG, error);
                            e.printStackTrace();
                            VisionProcessorBase.this.onFailure(e);
                        });
    }

    @Override
    public void stop() {
        executor.shutdown();
        isShutdown = true;
    }

    protected abstract Task<T> detectInImage(InputImage image);

    protected abstract void onSuccess(@NonNull T results);

    protected abstract void onFailure(@NonNull Exception e);
}
