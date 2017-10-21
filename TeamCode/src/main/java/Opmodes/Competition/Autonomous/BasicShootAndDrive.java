package Opmodes.Competition.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.Timer;
import java.util.TimerTask;

import Devices.Drivers.FieldColor;
import General.Utility.OpModeGeneral;
import General.Utility.OpModeTimers;

/**
 * Created by bryanperkins on 12/10/16.
 */
@Autonomous (name = "Basic Shoot and Drive", group = "Competition")
public class BasicShootAndDrive extends OpMode {

    public float currentTime = 0;
    public boolean isP2Enabled = false;

    public void init() {
        OpModeGeneral.allInit(hardwareMap);
        OpModeTimers.timerInit();
        }

    public void start()
    {
        OpModeGeneral.mecanumMove(-1,0,0,false);
        OpModeTimers.timerSchedule();
    }

    public int stage = 0;
    public void loop()
    {

    }

}