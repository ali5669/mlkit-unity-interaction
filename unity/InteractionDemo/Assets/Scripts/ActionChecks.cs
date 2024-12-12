using Mono.Cecil;
using System;
using System.Collections;
using System.Collections.Generic;
using Unity.VisualScripting;
using UnityEngine;

interface IActionCheck
{
    bool Ok();
    void Update(params float[] data);
    bool RangeCheck(ref int check, float data, float min, float max);
    bool PositionCheck(ref int check, float data1, float data2);
}

public class CelebrateCheck : IActionCheck
{
    private int leftShoulderCheck;
    private int rightShoulderCheck;
    private int leftElbowCheck;
    private int rightElbowCheck;

    private readonly int checkTime;

    public CelebrateCheck(int checkTime)
    {
        leftShoulderCheck = 0;
        rightShoulderCheck = 0;
        leftElbowCheck = 0;
        rightElbowCheck = 0;
        this.checkTime = checkTime;
    }


    public bool Ok()
    {
        if (leftShoulderCheck > checkTime && rightShoulderCheck > checkTime &&
            leftElbowCheck > checkTime && rightElbowCheck > checkTime)
        {
            leftShoulderCheck = 0;
            rightShoulderCheck = 0;
            leftElbowCheck = 0;
            rightElbowCheck = 0;
            return true;
        }
        return false;
    }

    public void Update(params float[] data)
    {
        RangeCheck(ref leftShoulderCheck, data[0], 90, 180);
        RangeCheck(ref rightShoulderCheck, data[1], 90, 180);
        PositionCheck(ref leftElbowCheck, data[2], data[3]);
        PositionCheck(ref rightElbowCheck, data[4], data[5]);
    }

    public bool RangeCheck(ref int check, float data, float min, float max)
    {

        if (data > min && data < max)
        {
            check++;
            return true;
        }

        if (check > 0)
        {
            check--;
        }
        return false;

    }
    public bool PositionCheck(ref int check, float data1, float data2)
    {
        if(data1 > data2)
        {
            check++;
            return true;
        }

        if(check > 0)
        {
            check--;
        }
        return false;
    }
}

public class HugCheck : IActionCheck
{
    private int leftArmCheck;
    private int rightArmCheck;
    private int leftElbowCheck;
    private int rightElbowCheck;

    private readonly int checkTime;
    public HugCheck(int checkTime)
    {
        leftArmCheck = 0;
        rightArmCheck = 0;
        leftElbowCheck = 0;
        rightElbowCheck = 0;
        this.checkTime = checkTime;
    }

    public bool Ok()
    {
        if (leftArmCheck > checkTime && rightArmCheck > checkTime &&
            leftElbowCheck > checkTime && rightElbowCheck > checkTime)
        {
            leftArmCheck = 0;
            rightArmCheck = 0;
            leftElbowCheck = 0;
            rightElbowCheck = 0;
            return true;
        }
        return false;
    }

    public void Update(params float[] data)
    {
        RangeCheck(ref leftArmCheck, data[0], 150, 180);
        RangeCheck(ref rightArmCheck, data[1], 150, 180);
        PositionCheck(ref leftElbowCheck, data[2], data[3]);
        PositionCheck(ref rightElbowCheck, data[4], data[5]);
    }

    public bool RangeCheck(ref int check, float data, float min, float max)
    {
        if (data > min && data < max)
        {
            check++;
            return true;
        }

        if (check > 0)
        {
            check--;
        }
        return false;
    }

    public bool PositionCheck(ref int check, float data1, float data2)
    {
        if (data1 > data2)
        {
            check++;
            return true;
        }

        if (check > 0)
        {
            check--;
        }
        return false;
    }
}

public class SadCheck : IActionCheck
{
    private int leftHipCheck;
    private int rightHipCheck;

    private int leftKneeCheck;
    private int rightKneeCheck;

    private readonly int checkTime;
    public SadCheck(int checkTime)
    {
        leftHipCheck = 0;
        rightHipCheck = 0;
        leftKneeCheck = 0;
        rightKneeCheck = 0;
        this.checkTime = checkTime;
    }

    public bool Ok()
    {
        if (leftHipCheck > checkTime && rightHipCheck > checkTime && leftKneeCheck > checkTime && rightKneeCheck > checkTime)
        {
            leftHipCheck = 0;
            rightHipCheck = 0;
            leftKneeCheck = 0;
            rightKneeCheck = 0;
            return true;
        }
        return false;
    }

    public void Update(params float[] data)
    {
        RangeCheck(ref leftHipCheck, data[0], 0, 80);
        RangeCheck(ref rightHipCheck, data[1], 0, 80);
        RangeCheck(ref leftKneeCheck, data[2], 0, 60);
        RangeCheck(ref rightKneeCheck, data[3], 0, 60);
    }

    public bool RangeCheck(ref int check, float data, float min, float max)
    {
        if (data > min && data < max)
        {
            check++;
            return true;
        }

        if (check > 0)
        {
            check--;
        }
        return false;
    }

    public bool PositionCheck(ref int check, float data1, float data2)
    {
        if (data1 > data2)
        {
            check++;
            return true;
        }

        if (check > 0)
        {
            check--;
        }
        return false;
    }
}

public class ClapCheck : IActionCheck
{
    private int leftShoulderCheck;
    private int rightShoulderCheck;
    private int handsCheck;
    private bool change;

    private readonly int checkTime;

    public ClapCheck(int checkTime)
    {
        leftShoulderCheck = 0;
        rightShoulderCheck = 0;
        handsCheck = 0;
        this.checkTime = checkTime;
        change = false; // false: open / true: close
    }

    public bool Ok()
    {
        if (leftShoulderCheck > checkTime && rightShoulderCheck > checkTime && handsCheck > checkTime)
        {
            leftShoulderCheck = 0;
            rightShoulderCheck = 0;
            handsCheck = 0;
            change = false;
            return true;
        }
        return false;
    }

    public void Update(params float[] data)
    {
        RangeCheck(ref leftShoulderCheck, data[0], 0, 20);
        RangeCheck(ref rightShoulderCheck, data[1], 0, 20);
        if (data[2] > 0 && data[2] < 10)
        {
            if (change)
            {
                handsCheck++;
                change = true;
            }
            else
            {
                if(handsCheck > 0)
                {
                    handsCheck--;
                }
            }
        }
        if (data[2] > 50 && data[2] < 1000)
        {
            if(!change)
            {
                handsCheck++;
                change = false;
            }
            else
            {
                if(handsCheck > 0)
                {
                    handsCheck--;
                }
            }
        }
    }

    public bool RangeCheck(ref int check, float data, float min, float max)
    {
        if (data > min && data < max)
        {
            check++;
            return true;
        }

        if (check > 0)
        {
            check--;
        }
        return false;
    }

    public bool PositionCheck(ref int check, float data1, float data2)
    {
        if (data1 > data2)
        {
            check++;
            return true;
        }

        if (check > 0)
        {
            check--;
        }
        return false;
    }
}

public class PunchCheck : IActionCheck
{
    private int leftShoulderCheck;
    private int rightShoulderCheck;
    private int leftElbowCheck;
    private int rightElbowCheck;
    private int leftPunchCheck;
    private int rightPunchCheck;

    private readonly int checkTime;

    public PunchCheck(int checkTime)
    {
        leftShoulderCheck = 0;
        rightShoulderCheck = 0;
        leftElbowCheck = 0;
        rightElbowCheck = 0;
        leftPunchCheck = 0;
        rightPunchCheck = 0;
        this.checkTime = checkTime;
    }

    public bool Ok()
    {
        bool leftPunch = false;
        bool rightPunch = false;
        if (leftShoulderCheck > checkTime && leftElbowCheck > checkTime && leftPunchCheck > checkTime)
        {
            leftPunch = true;
        }
        if (rightShoulderCheck > checkTime && rightElbowCheck > checkTime && rightPunchCheck > checkTime)
        {
            rightPunch = true;
        }

        if ((leftPunch && rightPunch) || (!leftPunch && !rightPunch))
        {
            return false;
        }
        else
        {
            leftShoulderCheck = 0;
            rightShoulderCheck = 0;
            leftElbowCheck = 0;
            rightElbowCheck = 0;
            return true;
        }
    }

    public void Update(params float[] data)
    {
        RangeCheck(ref leftShoulderCheck, data[0], 0, 100);
        RangeCheck(ref rightShoulderCheck, data[1], 0, 100);
        RangeCheck(ref leftElbowCheck, data[2], 150, 180);
        RangeCheck(ref rightElbowCheck, data[3], 150, 180);

        RangeCheck(ref leftPunchCheck, Math.Abs(data[4] - data[6]), 0, 40);
        RangeCheck(ref rightPunchCheck, Math.Abs(data[5] - data[7]), 0, 40);
    }

    public bool RangeCheck(ref int check, float data, float min, float max)
    {
        if (data > min && data < max)
        {
            check++;
            return true;
        }

        if (check > 0)
        {
            check--;
        }
        return false;
    }

    public bool PositionCheck(ref int check, float data1, float data2)
    {
        if (data1 > data2)
        {
            check++;
            return true;
        }

        if (check > 0)
        {
            check--;
        }
        return false;
    }
}

public class WavingArmCheck : IActionCheck
{
    private int leftShoulderCheck;
    private int rightShoulderCheck;
    private int leftArmCheck;
    private int rightArmCheck;
    private int leftElbowCheck;
    private int rightElbowCheck;

    private readonly int checkTime;

    public WavingArmCheck(int checkTime)
    {
        leftShoulderCheck = 0;
        rightShoulderCheck = 0;
        leftArmCheck = 0;
        rightArmCheck = 0;
        leftElbowCheck = 0;
        rightElbowCheck = 0;
        this.checkTime = checkTime;
    }

    public bool Ok()
    {
        bool leftWave = false;
        bool rightWave = false;
        if (leftShoulderCheck > checkTime && leftArmCheck > checkTime && leftElbowCheck > checkTime)
        {
            leftWave = true;
        }
        if (rightShoulderCheck > checkTime && rightArmCheck > checkTime && rightElbowCheck > checkTime)
        {
            rightWave = true;
        }

        if ((leftWave && rightWave) || (!leftWave && !rightWave))
        {
            return false;
        }
        else
        {
            leftShoulderCheck = 0;
            rightShoulderCheck = 0;
            leftArmCheck = 0;
            rightArmCheck = 0;
            leftElbowCheck = 0;
            rightElbowCheck = 0;
            return true;
        }
    }

    public void Update(params float[] data)
    {
        RangeCheck(ref leftShoulderCheck, data[0], 120, 240);
        RangeCheck(ref rightShoulderCheck, data[1], 120, 240);
        RangeCheck(ref leftArmCheck, data[2], 150, 180);
        RangeCheck(ref rightArmCheck, data[3], 150, 180);

        PositionCheck(ref leftElbowCheck, data[6], data[4]);
        PositionCheck(ref rightElbowCheck, data[7], data[5]);
    }

    public bool RangeCheck(ref int check, float data, float min, float max)
    {
        if (data > min && data < max)
        {
            check++;
            return true;
        }

        if (check > 0)
        {
            check--;
        }
        return false;
    }

    public bool PositionCheck(ref int check, float data1, float data2)
    {
        if (data1 > data2)
        {
            check++;
            return true;
        }

        if (check > 0)
        {
            check--;
        }
        return false;
    }
}

public class ActionChecks
{
    private CelebrateCheck celebrateCheck;
    private HugCheck hugCheck;
    private SadCheck sadCheck;
    private ClapCheck clapCheck;
    private PunchCheck punchCheck;
    private WavingArmCheck wavingArmCheck;

    private const string celebrate = "celebrate";
    private const string hug = "hug";
    private const string sad = "sad";
    private const string clap = "clap";
    private const string punch = "punch";
    private const string wavingArm = "waving arm";
    private const string none = "none";

    public ActionChecks()
    {
        celebrateCheck = new CelebrateCheck(20);
        hugCheck = new HugCheck(20);
        sadCheck = new SadCheck(20);
        clapCheck = new ClapCheck(20);
        punchCheck = new PunchCheck(20);
        wavingArmCheck = new WavingArmCheck(20);
    }

    public string GetAction()
    {
        if (celebrateCheck.Ok())
        {
            return celebrate;
        }

        if (hugCheck.Ok())
        {
            return hug;
        }

        if (sadCheck.Ok())
        {
            return sad;
        }

        if (clapCheck.Ok())
        {
            return clap;
        }

        if (punchCheck.Ok())
        {
            return punch;
        }

        if (wavingArmCheck.Ok())
        {
            return wavingArm;
        }

        return none;
    }

    public void ChecksUpdate(float[] pose, float[] angles)
    {
        // shoulder angle, elbow shoulder position
        celebrateCheck.Update(angles[0], angles[1], pose[4], pose[1], pose[5], pose[3]);
        // elbow angle, shoulder elbow position
        hugCheck.Update(angles[2], angles[3], pose[1], pose[4], pose[3], pose[5]);
        // hip angle, knee angle
        sadCheck.Update(angles[4], angles[5], angles[6], angles[7]);
        // shoulder angle, hands distance
        float distance = (float)Math.Sqrt((Math.Pow(pose[13] - pose[15], 2) + Math.Pow(pose[14] - pose[16], 2)));
        clapCheck.Update(angles[0], angles[1], distance);
        // shoudler angle, elbow angle, shoulder position, elbow position
        punchCheck.Update(angles[0], angles[1], angles[2], angles[3], pose[1], pose[3], pose[4], pose[5]);
        // shoulder angle, elbow angle, shoulder position, elbow position
        wavingArmCheck.Update(angles[0], angles[1], angles[2], angles[3], pose[1], pose[3], pose[4], pose[5]);
    }
}