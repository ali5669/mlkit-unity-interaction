using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PoseGraphic
{
    #region definition
    private float[] pose;

    private LineRenderer lineRenderBody;
    private LineRenderer lineRenderNeck;
    private LineRenderer lineRenderLeftArm;
    private LineRenderer lineRenderRightArm;
    private LineRenderer lineRenderLeftLeg;
    private LineRenderer lineRenderRightLeg;
    private LineRenderer lineRenderLeftHand;
    private LineRenderer lineRenderRightHand;

    private GameObject nose;
    private GameObject leftShoulder;
    private GameObject midShoudler;
    private GameObject rightShoulder;
    private GameObject leftElbow;
    private GameObject rightElbow;
    private GameObject leftWrist;
    private GameObject rightWrist;
    private GameObject leftThumb;
    private GameObject rightThumb;
    private GameObject leftPinky;
    private GameObject rightPinky;
    private GameObject leftIndex;
    private GameObject rightIndex;
    private GameObject leftHip;
    private GameObject rightHip;
    private GameObject leftKnee;
    private GameObject rightKnee;
    private GameObject leftAnkle;
    private GameObject rightAnkle;
    private GameObject leftHeel;
    private GameObject right_Heel;
    private GameObject leftFootIndex;
    private GameObject rightFootIndex;
    #endregion

    public PoseGraphic()
    {
        #region get points
        nose = GameObject.Find("Player/Nose");
        leftShoulder = GameObject.Find("Player/Left Shoulder");
        midShoudler = GameObject.Find("Player/Mid Shoulder");
        rightShoulder = GameObject.Find("Player/Right Shoulder");
        leftElbow = GameObject.Find("Player/Left Elbow");
        rightElbow = GameObject.Find("Player/Right Elbow");
        leftWrist = GameObject.Find("Player/Left Wrist");
        rightWrist = GameObject.Find("Player/Right Wrist");
        leftThumb = GameObject.Find("Player/Left Thumb");
        rightThumb = GameObject.Find("Player/Right Thumb");
        leftPinky = GameObject.Find("Player/Left Pinky");
        rightPinky = GameObject.Find("Player/Right Pinky");
        leftIndex = GameObject.Find("Player/Left Index");
        rightIndex = GameObject.Find("Player/Right Index");
        leftHip = GameObject.Find("Player/Left Hip");
        rightHip = GameObject.Find("Player/Right Hip");
        leftKnee = GameObject.Find("Player/Left Knee");
        rightKnee = GameObject.Find("Player/Right Knee");
        leftAnkle = GameObject.Find("Player/Left Ankle");
        rightAnkle = GameObject.Find("Player/Right Ankle");
        leftHeel = GameObject.Find("Player/Left Heel");
        right_Heel = GameObject.Find("Player/Right Heel");
        leftFootIndex = GameObject.Find("Player/Left Foot Index");
        rightFootIndex = GameObject.Find("Player/Right Foot Index");
        #endregion
        #region get lineRenders
        lineRenderBody = GameObject.Find("Player/Body").GetComponent<LineRenderer>();
        lineRenderNeck = GameObject.Find("Player/Neck").GetComponent<LineRenderer>();
        lineRenderLeftArm = GameObject.Find("Player/Left Arm").GetComponent<LineRenderer>();
        lineRenderRightArm = GameObject.Find("Player/Right Arm").GetComponent<LineRenderer>();
        lineRenderLeftLeg = GameObject.Find("Player/Left Leg").GetComponent<LineRenderer>();
        lineRenderRightLeg = GameObject.Find("Player/Right Leg").GetComponent<LineRenderer>();
        lineRenderLeftHand = GameObject.Find("Player/Left Hand").GetComponent<LineRenderer>();
        lineRenderRightHand = GameObject.Find("Player/Right Hand").GetComponent<LineRenderer>();
        #endregion

        lineRenderBody.positionCount = 5;
        lineRenderNeck.positionCount = 2;
        lineRenderLeftArm.positionCount = 4;
        lineRenderRightArm.positionCount = 4;
        lineRenderLeftLeg.positionCount = 6;
        lineRenderRightLeg.positionCount = 6;
        lineRenderLeftHand.positionCount = 3;
        lineRenderRightHand.positionCount = 3;
        Debug.Log("对象获取成功!");
    }

    public void Update(float[] newPose)
    {
        pose = (float[])newPose.Clone();
    }

    public void Draw(float step, float baseY, float scale)
    {
        DrawPoints(step, baseY, scale);
        DrawLines();
    }

    private void DrawPoints(float step, float baseY, float scale)
    {
        // TODO: 优化这坨屎
        nose.transform.localPosition = new Vector3(
            Mathf.Lerp(nose.transform.localPosition.x, scale * pose[0], step),
            Mathf.Lerp(nose.transform.localPosition.y, scale * pose[1] + baseY, step),
            0);
        leftShoulder.transform.localPosition = new Vector3(
            Mathf.Lerp(leftShoulder.transform.localPosition.x, scale * pose[2], step),
            Mathf.Lerp(leftShoulder.transform.localPosition.y, scale * pose[3] + baseY, step),
            0);
        midShoudler.transform.localPosition = new Vector3(
            Mathf.Lerp(midShoudler.transform.localPosition.x, scale * pose[4], step),
            Mathf.Lerp(midShoudler.transform.localPosition.y, scale * pose[5] + baseY, step),
            0);
        rightShoulder.transform.localPosition = new Vector3(
            Mathf.Lerp(rightShoulder.transform.localPosition.x, scale * pose[6], step),
            Mathf.Lerp(rightShoulder.transform.localPosition.y, scale * pose[7] + baseY, step),
            0);
        leftElbow.transform.localPosition = new Vector3(
            Mathf.Lerp(leftElbow.transform.localPosition.x, scale * pose[8], step),
            Mathf.Lerp(leftElbow.transform.localPosition.y, scale * pose[9] + baseY, step),
            0);
        rightElbow.transform.localPosition = new Vector3(
            Mathf.Lerp(rightElbow.transform.localPosition.x, scale * pose[10], step),
            Mathf.Lerp(rightElbow.transform.localPosition.y, scale * pose[11] + baseY, step),
            0);
        leftWrist.transform.localPosition = new Vector3(
            Mathf.Lerp(leftWrist.transform.localPosition.x, scale * pose[12], step),
            Mathf.Lerp(leftWrist.transform.localPosition.y, scale * pose[13] + baseY, step),
            0);
        rightWrist.transform.localPosition = new Vector3(
            Mathf.Lerp(rightWrist.transform.localPosition.x, scale * pose[14], step),
            Mathf.Lerp(rightWrist.transform.localPosition.y, scale * pose[15] + baseY, step),
            0);
        leftThumb.transform.localPosition = new Vector3(
            Mathf.Lerp(leftThumb.transform.localPosition.x, scale * pose[16], step),
            Mathf.Lerp(leftThumb.transform.localPosition.y, scale * pose[17] + baseY, step),
            0);
        rightThumb.transform.localPosition = new Vector3(
            Mathf.Lerp(rightThumb.transform.localPosition.x, scale * pose[18], step),
            Mathf.Lerp(rightThumb.transform.localPosition.y, scale * pose[19] + baseY, step),
            0);
        leftPinky.transform.localPosition = new Vector3(
            Mathf.Lerp(leftPinky.transform.localPosition.x, scale * pose[20], step),
            Mathf.Lerp(leftPinky.transform.localPosition.y, scale * pose[21] + baseY, step),
            0);
        rightPinky.transform.localPosition = new Vector3(
            Mathf.Lerp(rightPinky.transform.localPosition.x, scale * pose[22], step),
            Mathf.Lerp(rightPinky.transform.localPosition.y, scale * pose[23] + baseY, step),
            0);
        leftIndex.transform.localPosition = new Vector3(
            Mathf.Lerp(leftIndex.transform.localPosition.x, scale * pose[24], step),
            Mathf.Lerp(leftIndex.transform.localPosition.y, scale * pose[25] + baseY, step),
            0);
        rightIndex.transform.localPosition = new Vector3(
            Mathf.Lerp(rightIndex.transform.localPosition.x, scale * pose[26], step),
            Mathf.Lerp(rightIndex.transform.localPosition.y, scale * pose[27] + baseY, step),
            0);
        leftHip.transform.localPosition = new Vector3(
            Mathf.Lerp(leftHip.transform.localPosition.x, scale * pose[28], step),
            Mathf.Lerp(leftHip.transform.localPosition.y, scale * pose[29] + baseY, step),
            0);
        rightHip.transform.localPosition = new Vector3(
            Mathf.Lerp(rightHip.transform.localPosition.x, scale * pose[30], step),
            Mathf.Lerp(rightHip.transform.localPosition.y, scale * pose[31] + baseY, step),
            0);
        leftKnee.transform.localPosition = new Vector3(
            Mathf.Lerp(leftKnee.transform.localPosition.x, scale * pose[32], step),
            Mathf.Lerp(leftKnee.transform.localPosition.y, scale * pose[33] + baseY, step),
            0);
        rightKnee.transform.localPosition = new Vector3(
            Mathf.Lerp(rightKnee.transform.localPosition.x, scale * pose[34], step),
            Mathf.Lerp(rightKnee.transform.localPosition.y, scale * pose[35] + baseY, step),
            0);
        leftAnkle.transform.localPosition = new Vector3(
            Mathf.Lerp(leftAnkle.transform.localPosition.x, scale * pose[36], step),
            Mathf.Lerp(leftAnkle.transform.localPosition.y, scale * pose[37] + baseY, step),
            0);
        rightAnkle.transform.localPosition = new Vector3(
            Mathf.Lerp(rightAnkle.transform.localPosition.x, scale * pose[38], step),
            Mathf.Lerp(rightAnkle.transform.localPosition.y, scale * pose[39] + baseY, step),
            0);
        leftHeel.transform.localPosition = new Vector3(
            Mathf.Lerp(leftHeel.transform.localPosition.x, scale * pose[40], step),
            Mathf.Lerp(leftHeel.transform.localPosition.y, scale * pose[41] + baseY, step),
            0);
        right_Heel.transform.localPosition = new Vector3(
            Mathf.Lerp(right_Heel.transform.localPosition.x, scale * pose[42], step),
            Mathf.Lerp(right_Heel.transform.localPosition.y, scale * pose[43] + baseY, step),
            0);
        leftFootIndex.transform.localPosition = new Vector3(
            Mathf.Lerp(leftFootIndex.transform.localPosition.x, scale * pose[44], step),
            Mathf.Lerp(leftFootIndex.transform.localPosition.y, scale * pose[45] + baseY, step),
            0);
        rightFootIndex.transform.localPosition = new Vector3(
            Mathf.Lerp(rightFootIndex.transform.localPosition.x, scale * pose[46], step),
            Mathf.Lerp(rightFootIndex.transform.localPosition.y, scale * pose[47] + baseY, step),
            0);
    }

    private void DrawLines()
    {
        DrawLineBody();
        DrawLineNeck();
        DrawLineLeftArm();
        DrawLineRightArm();
        DrawLineLeftLeg();
        DrawLineRightLeg();
        DrawLineLeftHand();
        DrawLineRightHand();
    }

    private void DrawLineBody()
    {
        lineRenderBody.SetPosition(0, leftShoulder.transform.localPosition);
        lineRenderBody.SetPosition(1, rightShoulder.transform.localPosition);
        lineRenderBody.SetPosition(2, rightHip.transform.localPosition);
        lineRenderBody.SetPosition(3, leftHip.transform.localPosition);
        lineRenderBody.SetPosition(4, leftShoulder.transform.localPosition);
    }

    private void DrawLineNeck()
    {
        lineRenderNeck.SetPosition(0, nose.transform.localPosition);
        lineRenderNeck.SetPosition(1, midShoudler.transform.localPosition);
    }

    private void DrawLineLeftArm()
    {
        lineRenderLeftArm.SetPosition(0, leftShoulder.transform.localPosition);
        lineRenderLeftArm.SetPosition(1, leftElbow.transform.localPosition);
        lineRenderLeftArm.SetPosition(2, leftWrist.transform.localPosition);
        lineRenderLeftArm.SetPosition(3, leftIndex.transform.localPosition);
    }

    private void DrawLineRightArm()
    {
        lineRenderRightArm.SetPosition(0, rightShoulder.transform.localPosition);
        lineRenderRightArm.SetPosition(1, rightElbow.transform.localPosition);
        lineRenderRightArm.SetPosition(2, rightWrist.transform.localPosition);
        lineRenderRightArm.SetPosition(3, rightIndex.transform.localPosition);
    }

    private void DrawLineLeftLeg()
    {
        lineRenderLeftLeg.SetPosition(0, leftHip.transform.localPosition);
        lineRenderLeftLeg.SetPosition(1, leftKnee.transform.localPosition);
        lineRenderLeftLeg.SetPosition(2, leftAnkle.transform.localPosition);
        lineRenderLeftLeg.SetPosition(3, leftFootIndex.transform.localPosition);
        lineRenderLeftLeg.SetPosition(4, leftHeel.transform.localPosition);
        lineRenderLeftLeg.SetPosition(5, leftAnkle.transform.localPosition);
    }

    private void DrawLineRightLeg()
    {
        lineRenderRightLeg.SetPosition(0, rightHip.transform.localPosition);
        lineRenderRightLeg.SetPosition(1, rightKnee.transform.localPosition);
        lineRenderRightLeg.SetPosition(2, rightAnkle.transform.localPosition);
        lineRenderRightLeg.SetPosition(3, rightFootIndex.transform.localPosition);
        lineRenderRightLeg.SetPosition(4, right_Heel.transform.localPosition);
        lineRenderRightLeg.SetPosition(5, rightAnkle.transform.localPosition);
    }

    private void DrawLineLeftHand()
    {
        lineRenderLeftHand.SetPosition(0, leftPinky.transform.localPosition);
        lineRenderLeftHand.SetPosition(1, leftWrist.transform.localPosition);
        lineRenderLeftHand.SetPosition(2, leftThumb.transform.localPosition);
    }

    private void DrawLineRightHand()
    {
        lineRenderRightHand.SetPosition(0, rightPinky.transform.localPosition);
        lineRenderRightHand.SetPosition(1, rightWrist.transform.localPosition);
        lineRenderRightHand.SetPosition(2, rightThumb.transform.localPosition);
    }
}
