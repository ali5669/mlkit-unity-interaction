using System;
using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;

public class Receiver : MonoBehaviour
{
    #region definition
    private const string celebrate = "celebrate";
    private const string hug = "hug";
    private const string sad = "sad";
    private const string clap = "clap";
    private const string punch = "punch";
    private const string wavingArm = "waving arm";
    private const string none = "none";

    // android communicate
    private AndroidJavaClass androidJavaClass;
    private AndroidJavaObject androidJavaObject;
    private float[] pose;
    private float[] angles;

    // human render
    private PoseGraphic poseGraphic;

    // character control
    private Controller controller;

    // action classify
    private ActionChecks actionChecks;

    // action show
    private TextMeshPro actionText;

    public string action;

    public float speed = 5f;
    public float scale = 0.1f;
    public float baseY = 50f;

    #endregion

    // Start is called before the first frame update
    void Start()
    {
        androidJavaClass = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        androidJavaObject = androidJavaClass.GetStatic<AndroidJavaObject>("currentActivity");
        Debug.Log("接收器创建成功！");
        poseGraphic = new PoseGraphic();
        controller = GameObject.Find("unitychan").GetComponent<Controller>();
        pose = new float[50];
        angles = new float[50];

        actionChecks = new ActionChecks();
        action = "none";

        actionText = GameObject.Find("ActionText").GetComponent<TextMeshPro>();
        actionText.text = "Action: " + action;
    }

    private void Update()
    {
        float step = speed * Time.deltaTime;
        poseGraphic.Update(pose);
        poseGraphic.Draw(step, baseY, scale);

        actionChecks.ChecksUpdate(pose, angles);
        //ActionClassify(actionChecks.GetAction());

        if (!controller.IsPlaying())
        {
            actionText.text = "Action: " + action;
        }
    }

    public void get_data(string str)
    {
        if (string.IsNullOrEmpty(str))
        {
            Debug.Log("数据为空");
        }
        else
        {
            string[] strArr = str.Split(' ');
            for (int i = 0; i < strArr.Length; i++)
            {
                pose[i] = Convert.ToSingle(strArr[i]);
            }
        }
    }

    public void get_angles(string str)
    {
        if (string.IsNullOrEmpty(str))
        {
            Debug.Log("数据为空");
        }
        else
        {
            string[] strArr = str.Split(' ');
            for (int i = 0; i < strArr.Length; i++)
            {
                angles[i] = Convert.ToSingle(strArr[i]);
            }
        }
    }

    public void get_pose(string str)
    {
        if (string.IsNullOrEmpty(str))
        {
            Debug.Log("数据为空");
        }
        else
        {
            action = str;
            ActionClassify(str);
        }
    }

    private void ActionClassify(String action)
    {
        switch (action)
        {
            case celebrate:
                controller.OnCelebrate(); break;
            case hug:
                controller.OnHug(); break;
            case clap: 
                controller.OnClap(); break;
            case punch:
                controller.OnPunch(); break;
            case sad:
                controller.OnSad(); break;
            case wavingArm:
                controller.ONWavingArm(); break;
            default:
                break;
        }
    }
}