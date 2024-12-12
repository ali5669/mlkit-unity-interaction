using easyar;
using System;
using System.Runtime.InteropServices;
using TMPro;
using Unity.VisualScripting;
using UnityEngine;
using UnityEngine.UI;

namespace InterActionSence
{
    public class Texture2ImgForAndroid : MonoBehaviour
    {
        public ARSession arSession;
        //public MeshRenderer CubeRenderer;

        private MotionTrackerFrameSource videoCamera;
        private CameraImageRenderer cameraRenderer;
        //private Texture cubeTexture;
        private RenderTexture cameraTexture;
        private Action<Camera, RenderTexture> targetTextureEventHandler;

        private AndroidJavaClass androidJavaClass;
        private AndroidJavaObject androidJavaObject;


        //action strings
        private const string celebrate = "celebrate";
        private const string hug = "hug";
        private const string sad = "sad";
        private const string clap = "clap";
        private const string punch = "punch";
        private const string wavingArm = "waving arm";
        private const string none = "none";

        //character controller
        private Controller controller;

        //canvas texts
        public TextMeshProUGUI poseText;
        public TextMeshProUGUI FPSText;

        private int count;
        private float deltaTime;

        private string action;
        
        private void Awake()
        {
            androidJavaClass = new AndroidJavaClass("com.unity3d.player.UnityPlayer");

            androidJavaObject = androidJavaClass.GetStatic<AndroidJavaObject>("currentActivity");

            if(androidJavaObject == null)
            {
                Debug.LogError("Failed to obtain Android Activity from Unity Player class.");
            }

            videoCamera = arSession.GetComponentInChildren<MotionTrackerFrameSource>();
            cameraRenderer = arSession.GetComponentInChildren<CameraImageRenderer>();
            //cubeTexture = CubeRenderer.material.mainTexture;
            targetTextureEventHandler = (camera, texture) =>
            {
                if (texture)
                {
                    cameraTexture = texture;
                }
                else
                {
                    cameraTexture = null;
                }
            };

            videoCamera.DeviceOpened += () =>
            {
                if (videoCamera.Device == null)
                {
                    return;
                }
            };


            controller = GameObject.Find("unitychan").GetComponent<Controller>();

        }

        private void Update()
        {
            if (cameraTexture != null)
            {
                Texture2D tex = new Texture2D(cameraTexture.width, cameraTexture.height, TextureFormat.RGB24, false);
                var old_rt = RenderTexture.active;
                RenderTexture.active = cameraTexture;

                tex.ReadPixels(new Rect(0, 0, cameraTexture.width, cameraTexture.height), 0, 0);
                tex.Apply();

                RenderTexture.active = old_rt;

                unsafe
                {
                    IntPtr p = Marshal.UnsafeAddrOfPinnedArrayElement(tex.GetRawTextureData(), 0);
                    long rawPtr = p.ToInt64();
                    ProcessImage(rawPtr, cameraTexture.width, cameraTexture.height);
                }

                Destroy(tex);
            }

            count++;
            deltaTime += Time.deltaTime;

            if(deltaTime >= 0.5f)
            {
                var fps = count / deltaTime;
                count = 0;
                deltaTime = 0;
                FPSText.text = $"FPS: {Mathf.Ceil(fps)}";
            }
            if (!controller.IsPlaying())
            {
                poseText.text = "pose:" + action;
            }
        }

        public void Capture(bool on)
        {
            if (!videoCamera || videoCamera.Device == null)
            {
                return;
            }

            if (on)
            {
                cameraRenderer.RequestTargetTexture(targetTextureEventHandler);
            }
            else
            {
                cameraRenderer.DropTargetTexture(targetTextureEventHandler);
            }
            return;
        }

        private void ProcessImage(long p, int width, int height)
        {
            androidJavaObject.Call("get_image", p, width, height);
        }

        public void get_pose(string str)
        {
            if (string.IsNullOrEmpty(str))
            {
                Debug.Log("Êý¾ÝÎª¿Õ");
            }
 
            action = str;
            ActionClassify(str);
            
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
}
