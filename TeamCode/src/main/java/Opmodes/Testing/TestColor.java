package Opmodes.Testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import java.util.Timer;
import java.util.TimerTask;

import Devices.Drivers.ModernRoboticsRGB;
import General.Utility.OpModeGeneral;

/**
 * Created by union on 18年1月19日.
 */

@Autonomous (name="TestColor", group = "Test")
public class TestColor extends OpMode {

    public static ModernRoboticsRGB sensor;
    public static Servo jewelExtender;
    public static Servo jewelHitter;
    public static TimerTask getTheColor, dropDown;
    public static Timer time;
    public static boolean isRed;


    public void start()
    {
        timerSchedule();
    }

    public void init() {

        OpModeGeneral.servoInit(hardwareMap);
        OpModeGeneral.sensorInit(hardwareMap);
        jewelExtender = hardwareMap.servo.get("jewelExtender");
        jewelHitter = hardwareMap.servo.get("jewelHitter");
        sensor = new ModernRoboticsRGB(hardwareMap, "cc", 0x3c);
        sensor.togglePassive();
        if (OpModeGeneral.isRed(OpModeGeneral.jewelColor)) telemetry.addData("Color", "Red");
        else telemetry.addData("Color", "Blue");
        timerInit();
    }


    public void loop()
    {

    }


    private void timerSchedule(){
        time.schedule(dropDown, 1000);
        time.schedule(getTheColor, 2500);
    }

    private void timerInit() {
        time = new Timer();
        dropDown = new TimerTask() {
            public void run() {
                jewelExtender.setPosition(Range.clip(1, 0, 1));
            }
        };

        getTheColor = new TimerTask() {
            public void run() {

                if(isRed()){
                    jewelHitter.setPosition(Range.clip(1, 0, 1));
                } else {
                    jewelHitter.setPosition(Range.clip(0, 0, 1));
                }
            }
        };

    }

    private boolean isRed(){
        if(sensor.blue() > sensor.red()){
            return false;
        } else {
            return true;
        }

    }

}
