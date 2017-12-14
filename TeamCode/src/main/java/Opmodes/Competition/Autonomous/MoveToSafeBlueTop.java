package Opmodes.Competition.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.Timer;
import java.util.TimerTask;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import General.Utility.OpModeGeneral;

/**
 * Created by union on 17年11月3日.
 */
@Autonomous(name = "DriveForwardDropCube", group = "Competition")

// Face away from the relic cap zone

public class MoveToSafeBlueTop extends OpMode {
    public static TimerTask move, stopAll, turnLeft, turnRight, openServo;
    public static Timer time;
    public static boolean stage1Complete = false;
    public static boolean isBlue = false;
    public static int millisecondDelay = 0;

    public void init()
    {
        timerInit();
    }

    public void start()
    {
        timerSchedule();
    }
    public void loop() {}

    public static void timerInit() {
        time = new Timer();
        move = new TimerTask() {
            public void run() {
                OpModeGeneral.mecanumMove(0, 1, 0, false);
            }
        };
        turnLeft = new TimerTask() {
            public void run() {
                OpModeGeneral.mecanumMove(0, 0, 1, false);
            }
        };
        turnRight = new TimerTask() {
            public void run() {
                OpModeGeneral.mecanumMove(0, 0, -1, false);
            }
        };
        openServo = new TimerTask() {
            public void run() {
                OpModeGeneral.grabber.setPosition(1);
            }
        };


        stopAll = new TimerTask(){
            public void run() {
                OpModeGeneral.tankMove(0, 0, false);
                OpModeGeneral.grabber.setPosition(0.5);
                stage1Complete = true;
            }
        };
    }
    public static void timerSchedule(){
        time.schedule(move, 1000 + millisecondDelay);
        time.schedule(stopAll, 1700 + millisecondDelay);
        time.schedule(turnLeft, 2500 + millisecondDelay);
        time.schedule(move, 3000 + millisecondDelay);
        time.schedule(stopAll, 3100 + millisecondDelay);
        time.schedule(openServo, 3200 + millisecondDelay);
        time.schedule(stopAll, 3500 + millisecondDelay);
    }
}
