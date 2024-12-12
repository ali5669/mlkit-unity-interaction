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

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.PoseDetectorOptionsBase;

import java.io.IOException;

/** A processor to run pose detector. */
public class PoseDetectorProcessor extends VisionProcessorBase<Pose> {

  private static final String TAG = "PoseDetectorProcessor";
  private final PoseDetector detector;
  private final PoseGraphic poseGraphic;

  public PoseDetectorProcessor(
      Context context, PoseDetectorOptionsBase options) throws IOException {
    super(context);
    detector = PoseDetection.getClient(options);
    poseGraphic = new PoseGraphic(context);
  }

  @Override
  public void stop() {
    super.stop();
    detector.close();
  }

  @Override
  protected Task<Pose> detectInImage(InputImage image) {
    return detector.process(image);
  }

  @Override
  protected void onSuccess(@NonNull Pose pose) {
    poseGraphic.handlePose(pose);
  }

  @Override
  protected void onFailure(@NonNull Exception e) {
    Log.e(TAG, "Pose detection failed!", e);
  }
}
