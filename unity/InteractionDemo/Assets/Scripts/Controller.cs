using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Rendering.Universal.Internal;

public class Controller : MonoBehaviour
{
    private Animator animator;
    // TODO: 去animator里面更改名称
    private const string baseLayer = "Base Layer.";
    private const string celebrate = "CELEBRATE";
    private const string hug = "KICK";
    private const string sad = "CONCERN";
    private const string clap = "CLAP";
    private const string punch = "DAMAGED";
    private const string wavingArm = "WAVING_ARM";
    private const string wait = "WAIT";

    // Start is called before the first frame update
    void Start()
    {
        animator = GetComponent<Animator>();
    }

    public void OnCelebrate()
    {
        if (!IsPlaying())
        {
            animator.Play(celebrate, -1, 0f);
        }
    }

    public void OnHug()
    {
        if (!IsPlaying())
        {
            animator.Play(hug, -1, 0f);
        }
    }

    public void OnSad()
    {
        if (!IsPlaying())
        {
            animator.Play(sad, -1, 0f);
        }
    }

    public void OnClap()
    {
        if(!IsPlaying())
        {
            animator.Play(clap, -1, 0f);
        }
    }

    public void OnPunch()
    {
        if(!IsPlaying())
        {
            animator.Play(punch, -1, 0f);
        }
    }

    public void ONWavingArm()
    {
        if(!IsPlaying())
        {
            animator.Play(wavingArm, -1, 0f);
        }
    }

    public bool IsPlaying()
    {
        AnimatorStateInfo info = animator.GetCurrentAnimatorStateInfo(0);
        return !info.IsName(baseLayer + wait);
        //if (info.IsName(baseLayer + celebrate) ||
        //    info.IsName(baseLayer + hug) ||
        //    info.IsName(baseLayer + sad) ||
        //    info.IsName(baseLayer + clap) ||
        //    info.IsName(baseLayer + punch) ||
        //    info.IsName(baseLayer + wavingArm))
        //{
        //    return true;
        //}
        //return false;
    }
}
    
