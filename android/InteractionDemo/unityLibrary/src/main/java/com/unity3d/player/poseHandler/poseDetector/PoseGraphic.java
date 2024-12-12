package com.unity3d.player.poseHandler.poseDetector;

import android.content.Context;
import android.graphics.PointF;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.modelHandler.ModelHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PoseGraphic {
    private static final String TAG = "PoseGraphic";
    private boolean showInFrameLikelihood;
    private float threshold = 0f;
    private static ModelHandler modelHandler;

    public PoseGraphic(Context context) throws IOException {
        modelHandler = ModelHandler.getInstance("st-gcn", 50,
                ModelHandler.assetFilePath(context, "stgcn_mobile.ptl"));
    }

    public void handlePose(@NonNull Pose pose) {
        List<PoseLandmark> landmarks = pose.getAllPoseLandmarks();
        if (landmarks.isEmpty()) {
//            Log.i(TAG, "handlePose: pose empty");
            return;
        }
        // Getting all the landmarks
        PoseLandmark nose = pose.getPoseLandmark(PoseLandmark.NOSE);
        PoseLandmark leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);
        PoseLandmark rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER);
        PoseLandmark leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW);
        PoseLandmark rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW);
        PoseLandmark leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST);
        PoseLandmark rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST);
        PoseLandmark leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP);
        PoseLandmark rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP);
        PoseLandmark leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE);
        PoseLandmark rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE);
        PoseLandmark leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE);
        PoseLandmark rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE);
        PoseLandmark leftPinky = pose.getPoseLandmark(PoseLandmark.LEFT_PINKY);
        PoseLandmark rightPinky = pose.getPoseLandmark(PoseLandmark.RIGHT_PINKY);
        PoseLandmark leftIndex = pose.getPoseLandmark(PoseLandmark.LEFT_INDEX);
        PoseLandmark rightIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_INDEX);
        PoseLandmark leftThumb = pose.getPoseLandmark(PoseLandmark.LEFT_THUMB);
        PoseLandmark rightThumb = pose.getPoseLandmark(PoseLandmark.RIGHT_THUMB);
        PoseLandmark leftHeel = pose.getPoseLandmark(PoseLandmark.LEFT_HEEL);
        PoseLandmark rightHeel = pose.getPoseLandmark(PoseLandmark.RIGHT_HEEL);
        PoseLandmark leftFootIndex = pose.getPoseLandmark(PoseLandmark.LEFT_FOOT_INDEX);
        PoseLandmark rightFootIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_FOOT_INDEX);

        // Frame Visibility Check
        showInFrameLikelihood = true;
        if (nose.getInFrameLikelihood() < threshold) {
            showInFrameLikelihood = false;
        }
        if (leftShoulder.getInFrameLikelihood() < threshold) {
            showInFrameLikelihood = false;
        }
        if (rightShoulder.getInFrameLikelihood() < threshold) {
            showInFrameLikelihood = false;
        }
        if (leftElbow.getInFrameLikelihood() < threshold) {
            showInFrameLikelihood = false;
        }
        if (rightElbow.getInFrameLikelihood() < threshold) {
            showInFrameLikelihood = false;
        }
        if (leftWrist.getInFrameLikelihood() < threshold) {
            showInFrameLikelihood = false;
        }
        if (rightWrist.getInFrameLikelihood() < threshold) {
            showInFrameLikelihood = false;
        }
        if (leftHip.getInFrameLikelihood() < threshold) {
            showInFrameLikelihood = false;
        }
        if (rightHip.getInFrameLikelihood() < threshold) {
            showInFrameLikelihood = false;
        }
        if (leftKnee.getInFrameLikelihood() < threshold) {
            showInFrameLikelihood = false;
        }
        if (rightKnee.getInFrameLikelihood() < threshold) {
            showInFrameLikelihood = false;
        }
        if (leftAnkle.getInFrameLikelihood() < threshold) {
            showInFrameLikelihood = false;
        }
        if (rightAnkle.getInFrameLikelihood() < threshold) {
            showInFrameLikelihood = false;
        }

        if (showInFrameLikelihood == true) {

            PoseAlignment poseAlignment = new PoseAlignment();
            Boolean shoulder_alignment = new Boolean(true);
            shoulder_alignment = poseAlignment.validatePoseAlignment(leftShoulder.getPosition().x, rightShoulder.getPosition().x, "shoulder");
            if (shoulder_alignment) {
                //Angles
                SmoothAngles smoothAngles = new SmoothAngles();

                Float leftShoulder_angle = smoothAngles.getAngle(leftElbow, leftShoulder, leftHip);
                Float rightShoulder_angle = smoothAngles.getAngle(rightElbow, rightShoulder, rightHip);
                Float leftElbow_angle = smoothAngles.getAngle(leftWrist, leftElbow, leftShoulder);
                Float rightElbow_angle = smoothAngles.getAngle(rightWrist, rightElbow, rightShoulder);
                Float leftHip_angle = smoothAngles.getAngle(leftKnee, leftHip, leftShoulder);
                Float rightHip_angle = smoothAngles.getAngle(rightKnee, rightHip, rightShoulder);
                Float leftKnee_angle = smoothAngles.getAngle(leftAnkle, leftKnee, leftHip);
                Float rightKnee_angle = smoothAngles.getAngle(rightAnkle, rightKnee, rightHip);
                Float leftAnkle_angle = smoothAngles.getAngle(leftFootIndex, leftAnkle, leftKnee);
                Float rightAnkle_angle = smoothAngles.getAngle(rightFootIndex, rightAnkle, rightKnee);

                float[] angleArray = new float[10];
                angleArray[0] = leftShoulder_angle;
                angleArray[1] = rightShoulder_angle;
                angleArray[2] = leftElbow_angle;
                angleArray[3] = rightElbow_angle;
                angleArray[4] = leftHip_angle;
                angleArray[5] = rightHip_angle;
                angleArray[6] = leftKnee_angle;
                angleArray[7] = rightKnee_angle;
                angleArray[8] = leftAnkle_angle;
                angleArray[9] = rightAnkle_angle;

                float[] angleErrors = new float[10];
                PoseComparison poseCompare = new PoseComparison();
                angleErrors = poseCompare.comparePose(angleArray);


                //Creating array for smoothing
                List<Float> inputArray = new ArrayList<Float>();
                inputArray.add((float) leftShoulder.getPosition().x);
                inputArray.add((float) leftShoulder.getPosition().y);
                inputArray.add((float) rightShoulder.getPosition().x);
                inputArray.add((float) rightShoulder.getPosition().y);
                inputArray.add((float) leftElbow.getPosition().x);
                inputArray.add((float) leftElbow.getPosition().y);
                inputArray.add((float) rightElbow.getPosition().x);
                inputArray.add((float) rightElbow.getPosition().y);
                inputArray.add((float) leftWrist.getPosition().x);
                inputArray.add((float) leftWrist.getPosition().y);
                inputArray.add((float) rightWrist.getPosition().x);
                inputArray.add((float) rightWrist.getPosition().y);
                inputArray.add((float) leftHip.getPosition().x);
                inputArray.add((float) leftHip.getPosition().y);
                inputArray.add((float) rightHip.getPosition().x);
                inputArray.add((float) rightHip.getPosition().y);
                inputArray.add((float) leftKnee.getPosition().x);
                inputArray.add((float) leftKnee.getPosition().y);
                inputArray.add((float) rightKnee.getPosition().x);
                inputArray.add((float) rightKnee.getPosition().y);
                inputArray.add((float) leftAnkle.getPosition().x);
                inputArray.add((float) leftAnkle.getPosition().y);
                inputArray.add((float) rightAnkle.getPosition().x);
                inputArray.add((float) rightAnkle.getPosition().y);
                inputArray.add((float) leftPinky.getPosition().x);
                inputArray.add((float) leftPinky.getPosition().y);
                inputArray.add((float) rightPinky.getPosition().x);
                inputArray.add((float) rightPinky.getPosition().y);
                inputArray.add((float) leftIndex.getPosition().x);
                inputArray.add((float) leftIndex.getPosition().y);
                inputArray.add((float) rightIndex.getPosition().x);
                inputArray.add((float) rightIndex.getPosition().y);
                inputArray.add((float) leftThumb.getPosition().x);
                inputArray.add((float) leftThumb.getPosition().y);
                inputArray.add((float) rightThumb.getPosition().x);
                inputArray.add((float) rightThumb.getPosition().y);
                inputArray.add((float) leftHeel.getPosition().x);
                inputArray.add((float) leftHeel.getPosition().y);
                inputArray.add((float) rightHeel.getPosition().x);
                inputArray.add((float) rightHeel.getPosition().y);
                inputArray.add((float) leftFootIndex.getPosition().x);
                inputArray.add((float) leftFootIndex.getPosition().y);
                inputArray.add((float) rightFootIndex.getPosition().x);
                inputArray.add((float) rightFootIndex.getPosition().y);
                inputArray.add((float) nose.getPosition().x);
                inputArray.add((float) nose.getPosition().y);

                //Calling the smoothing function
                CoordsArray.currentCoords = smoothAngles.smoothenCoords(inputArray);

                PointF new_leftShoulder = new PointF(CoordsArray.currentCoords.get(0), CoordsArray.currentCoords.get(1));
                PointF new_rightShoulder = new PointF(CoordsArray.currentCoords.get(2), CoordsArray.currentCoords.get(3));
                PointF new_shoulderMid = new PointF((CoordsArray.currentCoords.get(0) + CoordsArray.currentCoords.get(2)) / 2, (CoordsArray.currentCoords.get(1) + CoordsArray.currentCoords.get(3)) / 2);
                PointF new_leftElbow = new PointF(CoordsArray.currentCoords.get(4), CoordsArray.currentCoords.get(5));
                PointF new_rightElbow = new PointF(CoordsArray.currentCoords.get(6), CoordsArray.currentCoords.get(7));
                PointF new_leftWrist = new PointF(CoordsArray.currentCoords.get(8), CoordsArray.currentCoords.get(9));
                PointF new_rightWrist = new PointF(CoordsArray.currentCoords.get(10), CoordsArray.currentCoords.get(11));
                PointF new_leftHip = new PointF(CoordsArray.currentCoords.get(12), CoordsArray.currentCoords.get(13));
                PointF new_rightHip = new PointF(CoordsArray.currentCoords.get(14), CoordsArray.currentCoords.get(15));
                PointF new_leftKnee = new PointF(CoordsArray.currentCoords.get(16), CoordsArray.currentCoords.get(17));
                PointF new_rightKnee = new PointF(CoordsArray.currentCoords.get(18), CoordsArray.currentCoords.get(19));
                PointF new_leftAnkle = new PointF(CoordsArray.currentCoords.get(20), CoordsArray.currentCoords.get(21));
                PointF new_rightAnkle = new PointF(CoordsArray.currentCoords.get(22), CoordsArray.currentCoords.get(23));
                PointF new_leftPinky = new PointF(CoordsArray.currentCoords.get(24), CoordsArray.currentCoords.get(25));
                PointF new_rightPinky = new PointF(CoordsArray.currentCoords.get(26), CoordsArray.currentCoords.get(27));
                PointF new_leftIndex = new PointF(CoordsArray.currentCoords.get(28), CoordsArray.currentCoords.get(29));
                PointF new_rightIndex = new PointF(CoordsArray.currentCoords.get(30), CoordsArray.currentCoords.get(31));
                PointF new_leftThumb = new PointF(CoordsArray.currentCoords.get(32), CoordsArray.currentCoords.get(33));
                PointF new_rightThumb = new PointF(CoordsArray.currentCoords.get(34), CoordsArray.currentCoords.get(35));
                PointF new_leftHeel = new PointF(CoordsArray.currentCoords.get(36), CoordsArray.currentCoords.get(37));
                PointF new_rightHeel = new PointF(CoordsArray.currentCoords.get(38), CoordsArray.currentCoords.get(39));
                PointF new_leftFootIndex = new PointF(CoordsArray.currentCoords.get(40), CoordsArray.currentCoords.get(41));
                PointF new_rightFootIndex = new PointF(CoordsArray.currentCoords.get(42), CoordsArray.currentCoords.get(43));
                PointF new_nose = new PointF(CoordsArray.currentCoords.get(44), CoordsArray.currentCoords.get(45));


                getPoints(new_nose, new_leftShoulder, new_shoulderMid, new_rightShoulder, new_leftElbow, new_rightElbow, new_leftWrist, new_rightWrist, new_leftThumb, new_rightThumb, new_leftPinky, new_rightPinky, new_leftIndex, new_rightIndex, new_leftHip, new_rightHip, new_leftKnee, new_rightKnee, new_leftAnkle, new_rightAnkle, new_leftHeel, new_rightHeel, new_leftFootIndex, new_rightFootIndex);

                getAngles(leftShoulder_angle, rightShoulder_angle, leftElbow_angle, rightElbow_angle, leftHip_angle, rightHip_angle, leftKnee_angle, rightKnee_angle, leftAnkle_angle, rightAnkle_angle);

            } else if(!CoordsArray.currentCoords.isEmpty()){
                SmoothAngles smoothAngles = new SmoothAngles();
                Float leftShoulder_angle = smoothAngles.getAngle(leftElbow, leftShoulder, leftHip);
                Float rightShoulder_angle = smoothAngles.getAngle(rightElbow, rightShoulder, rightHip);
                Float leftElbow_angle = smoothAngles.getAngle(leftWrist, leftElbow, leftShoulder);
                Float rightElbow_angle = smoothAngles.getAngle(rightWrist, rightElbow, rightShoulder);
                Float leftHip_angle = smoothAngles.getAngle(leftKnee, leftHip, leftShoulder);
                Float rightHip_angle = smoothAngles.getAngle(rightKnee, rightHip, rightShoulder);
                Float leftKnee_angle = smoothAngles.getAngle(leftAnkle, leftKnee, leftHip);
                Float rightKnee_angle = smoothAngles.getAngle(rightAnkle, rightKnee, rightHip);
                Float leftAnkle_angle = smoothAngles.getAngle(leftFootIndex, leftAnkle, leftKnee);
                Float rightAnkle_angle = smoothAngles.getAngle(rightFootIndex, rightAnkle, rightKnee);

                float[] angleArray = new float[10];
                angleArray[0] = leftShoulder_angle;
                angleArray[1] = rightShoulder_angle;
                angleArray[2] = leftElbow_angle;
                angleArray[3] = rightElbow_angle;
                angleArray[4] = leftHip_angle;
                angleArray[5] = rightHip_angle;
                angleArray[6] = leftKnee_angle;
                angleArray[7] = rightKnee_angle;
                angleArray[8] = leftAnkle_angle;
                angleArray[9] = rightAnkle_angle;

                float[] angleErrors = new float[10];
                PoseComparison poseCompare = new PoseComparison();
                angleErrors = poseCompare.comparePose(angleArray);

                PointF new_leftShoulder = new PointF(CoordsArray.currentCoords.get(0), CoordsArray.currentCoords.get(1));
                PointF new_rightShoulder = new PointF(CoordsArray.currentCoords.get(2), CoordsArray.currentCoords.get(3));
                PointF new_shoulderMid = new PointF((CoordsArray.currentCoords.get(0) + CoordsArray.currentCoords.get(2)) / 2, (CoordsArray.currentCoords.get(1) + CoordsArray.currentCoords.get(3)) / 2);
                PointF new_leftElbow = new PointF(CoordsArray.currentCoords.get(4), CoordsArray.currentCoords.get(5));
                PointF new_rightElbow = new PointF(CoordsArray.currentCoords.get(6), CoordsArray.currentCoords.get(7));
                PointF new_leftWrist = new PointF(CoordsArray.currentCoords.get(8), CoordsArray.currentCoords.get(9));
                PointF new_rightWrist = new PointF(CoordsArray.currentCoords.get(10), CoordsArray.currentCoords.get(11));
                PointF new_leftHip = new PointF(CoordsArray.currentCoords.get(12), CoordsArray.currentCoords.get(13));
                PointF new_rightHip = new PointF(CoordsArray.currentCoords.get(14), CoordsArray.currentCoords.get(15));
                PointF new_leftKnee = new PointF(CoordsArray.currentCoords.get(16), CoordsArray.currentCoords.get(17));
                PointF new_rightKnee = new PointF(CoordsArray.currentCoords.get(18), CoordsArray.currentCoords.get(19));
                PointF new_leftAnkle = new PointF(CoordsArray.currentCoords.get(20), CoordsArray.currentCoords.get(21));
                PointF new_rightAnkle = new PointF(CoordsArray.currentCoords.get(22), CoordsArray.currentCoords.get(23));
                PointF new_leftPinky = new PointF(CoordsArray.currentCoords.get(24), CoordsArray.currentCoords.get(25));
                PointF new_rightPinky = new PointF(CoordsArray.currentCoords.get(26), CoordsArray.currentCoords.get(27));
                PointF new_leftIndex = new PointF(CoordsArray.currentCoords.get(28), CoordsArray.currentCoords.get(29));
                PointF new_rightIndex = new PointF(CoordsArray.currentCoords.get(30), CoordsArray.currentCoords.get(31));
                PointF new_leftThumb = new PointF(CoordsArray.currentCoords.get(32), CoordsArray.currentCoords.get(33));
                PointF new_rightThumb = new PointF(CoordsArray.currentCoords.get(34), CoordsArray.currentCoords.get(35));
                PointF new_leftHeel = new PointF(CoordsArray.currentCoords.get(36), CoordsArray.currentCoords.get(37));
                PointF new_rightHeel = new PointF(CoordsArray.currentCoords.get(38), CoordsArray.currentCoords.get(39));
                PointF new_leftFootIndex = new PointF(CoordsArray.currentCoords.get(40), CoordsArray.currentCoords.get(41));
                PointF new_rightFootIndex = new PointF(CoordsArray.currentCoords.get(42), CoordsArray.currentCoords.get(43));
                PointF new_nose = new PointF(CoordsArray.currentCoords.get(44), CoordsArray.currentCoords.get(45));

                getPoints(new_nose, new_leftShoulder, new_shoulderMid, new_rightShoulder, new_leftElbow, new_rightElbow, new_leftWrist, new_rightWrist, new_leftThumb, new_rightThumb, new_leftPinky, new_rightPinky, new_leftIndex, new_rightIndex, new_leftHip, new_rightHip, new_leftKnee, new_rightKnee, new_leftAnkle, new_rightAnkle, new_leftHeel, new_rightHeel, new_leftFootIndex, new_rightFootIndex);


                getAngles(leftShoulder_angle, rightShoulder_angle, leftElbow_angle, rightElbow_angle, leftHip_angle, rightHip_angle, leftKnee_angle, rightKnee_angle, leftAnkle_angle, rightAnkle_angle);

            }
        }
    }

    private static void getAngles(Float leftShoulder_angle, Float rightShoulder_angle, Float leftElbow_angle, Float rightElbow_angle, Float leftHip_angle, Float rightHip_angle, Float leftKnee_angle, Float rightKnee_angle, Float leftAnkle_angle, Float rightAnkle_angle) {
        String angles = "";
        angles += leftShoulder_angle + " ";
        angles += rightShoulder_angle + " ";
        angles += leftElbow_angle + " ";
        angles += rightElbow_angle + " ";
        angles += leftHip_angle + " ";
        angles += rightHip_angle + " ";
        angles += leftKnee_angle + " ";
        angles += rightKnee_angle + " ";
        angles += leftAnkle_angle + " ";
        angles += rightAnkle_angle;
    }

    private static void getPoints(PointF new_nose, PointF new_leftShoulder, PointF new_shoulderMid, PointF new_rightShoulder, PointF new_leftElbow, PointF new_rightElbow, PointF new_leftWrist, PointF new_rightWrist, PointF new_leftThumb, PointF new_rightThumb, PointF new_leftPinky, PointF new_rightPinky, PointF new_leftIndex, PointF new_rightIndex, PointF new_leftHip, PointF new_rightHip, PointF new_leftKnee, PointF new_rightKnee, PointF new_leftAnkle, PointF new_rightAnkle, PointF new_leftHeel, PointF new_rightHeel, PointF new_leftFootIndex, PointF new_rightFootIndex) {
        String points = "";
        points += new_nose.x + " " + -new_nose.y + " ";
        points += new_leftShoulder.x + " " + -new_leftShoulder.y + " ";
        points += new_shoulderMid.x + " " + -new_shoulderMid.y + " ";
        points += new_rightShoulder.x + " " + -new_rightShoulder.y + " ";
        points += new_leftElbow.x + " " + -new_leftElbow.y + " ";
        points += new_rightElbow.x + " " + -new_rightElbow.y + " ";
        points += new_leftWrist.x + " " + -new_leftWrist.y + " ";
        points += new_rightWrist.x + " " + -new_rightWrist.y + " ";
        points += new_leftThumb.x + " " + -new_leftThumb.y + " ";
        points += new_rightThumb.x + " " + -new_rightThumb.y + " ";
        points += new_leftPinky.x + " " + -new_leftPinky.y + " ";
        points += new_rightPinky.x + " " + -new_rightPinky.y + " ";
        points += new_leftIndex.x + " " + -new_leftIndex.y + " ";
        points += new_rightIndex.x + " " + -new_rightIndex.y + " ";
        points += new_leftHip.x + " " + -new_leftHip.y + " ";
        points += new_rightHip.x + " " + -new_rightHip.y + " ";
        points += new_leftKnee.x + " " + -new_leftKnee.y + " ";
        points += new_rightKnee.x + " " + -new_rightKnee.y + " ";
        points += new_leftAnkle.x + " " + -new_leftAnkle.y + " ";
        points += new_rightAnkle.x + " " + -new_rightAnkle.y + " ";
        points += new_leftHeel.x + " " + -new_leftHeel.y + " ";
        points += new_rightHeel.x + " " + -new_rightHeel.y + " ";
        points += new_leftFootIndex.x + " " + -new_leftFootIndex.y + " ";
        points += new_rightFootIndex.x + " " + -new_rightFootIndex.y;

//        Log.i(TAG, points);

        String modelPose = modelHandler.GetMessage(points);
//        if(!modelPose.equals("Null")){
//            Log.i(TAG, "pose:" + modelPose);
//        }

        UnityPlayer.UnitySendMessage("Texture Controller", "get_pose", modelPose);
    }
}
