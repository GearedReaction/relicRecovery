package Opmodes.Competition.Autonomous;

import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import General.Utility.OpModeGeneral;

/**
 * Created by union on 18年2月9日.
 */

public class AutoTimer extends OpMode {
    private File dir = Environment.getExternalStorageDirectory();
    private boolean ready = false;
    private boolean front = true;
    private boolean red = true;
    private int column = 1;

    private static TimerTask getTheColor, dropDown, moveBack, raise, raisePartial;
    private static TimerTask driveForward, driveBackward, strafeLeft, turn, turnNegative, fullStop;
    private static Timer time;



    // Manage TimerTasks
    private void timerScheduleJewel() {
        time.schedule(dropDown, 0);
        time.schedule(getTheColor, 1500);
        time.schedule(raisePartial, 2500);
        time.schedule(moveBack, 3000);
        time.schedule(raise, 3500);
        time.schedule(moveBack, 5000);
    }

    private void timerScheduleDrive() {
        // Starting from 6000

        int startTimeDrive = 6000;

        // Timing Front

        int stopFrontDrive = 7500;
        int turnFront = 8000;
        int stopTurnFront = 8500;

        // Timing Back
        int stopBackDrive = 7000;
        int strafeBack = 7500;
        int stopStrafeBack = 8000;
        int turnBack = 8500;
        int stopTurnBack = 9500;
        // Front Red
        if (front && red) {
            driveForward(startTimeDrive);
            fullStop(stopFrontDrive);
            turnNegative(turnFront);
            fullStop(stopTurnFront);
        }
        // Back Red
        else if (!front && red) {
            driveForward(startTimeDrive);
            fullStop(stopBackDrive);
            strafeLeft(strafeBack);
            fullStop(stopStrafeBack);
        }
        // Front Blue
        else if (front && !red)
        {
            driveBackward(startTimeDrive);
            fullStop(stopFrontDrive);
            turnNegative(turnFront);
            fullStop(stopTurnFront);
        }
        // Back Blue
        else if (!front && !red) {
            driveBackward(startTimeDrive);
            fullStop(stopBackDrive);
            strafeLeft(strafeBack);
            fullStop(stopStrafeBack);
            turn(turnBack);
            fullStop(stopTurnBack);
        }

    }

    private void jewelInit() {
        dropDown = new TimerTask() {
            public void run() {
                OpModeGeneral.jewelExtender.setPosition(1);
            }
        };

        getTheColor = new TimerTask() {
            public void run() {
                if (red) {
                    if (OpModeGeneral.isRed(OpModeGeneral.jewelColor))
                        OpModeGeneral.jewelHitter.setPosition(0.7);
                    else
                        OpModeGeneral.jewelHitter.setPosition(0.3);
                }
                else {
                    if (OpModeGeneral.isRed(OpModeGeneral.jewelColor))
                        OpModeGeneral.jewelHitter.setPosition(0.3);
                    else
                        OpModeGeneral.jewelHitter.setPosition(0.7);
                }
            }
        };

        moveBack = new TimerTask() {
            public void run() {
                OpModeGeneral.jewelHitter.setPosition(0.5);
            }
        };

        raise = new TimerTask() {
            public void run() {
                OpModeGeneral.jewelExtender.setPosition(0.35);
            }
        };

        raisePartial = new TimerTask() {
            public void run() {
                OpModeGeneral.jewelExtender.setPosition(0.5);
            }
        };

    }

    private void driveInit() {
        driveForward = new TimerTask() {
            public void run() {
                OpModeGeneral.mecanumMove(0,0.5,0, true);
            }
        };
        driveBackward = new TimerTask() {
            public void run() {
                OpModeGeneral.mecanumMove(0,-0.5,0, true);
            }
        };
        strafeLeft = new TimerTask() {
            public void run() {
                OpModeGeneral.mecanumMove(-0.5,0,0, true);
            }
        };
        turn = new TimerTask() {
            public void run() {
                OpModeGeneral.mecanumMove(0,0,0.5, true);
            }
        };
        turnNegative = new TimerTask() {
            public void run() {
                OpModeGeneral.mecanumMove(0,0,-0.5, true);
            }
        };

        fullStop = new TimerTask() {
            public void run() {
                OpModeGeneral.stopAllMotors();
            }
        };
    }

    private void loadConfig () {
        try {
            FileReader fileReader = new FileReader(dir + "/robotSaves/config.cfg");
            BufferedReader reader = new BufferedReader(fileReader);
            String line;
            int i = 0;
            while((line = reader.readLine()) != null) {
                if (i == 0) front = (line.equals("1"));
                else red = (line.equals("1"));
                i++;
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println(e.getStackTrace());
            telemetry.addData("FILE NOT FOUND", 0);
        }
        catch (IOException e)
        {
            System.out.println(e.getStackTrace());
            telemetry.addData("IO EXCEPTION", 0);
        }
    }


    // OpMode Control
    public void init() {
        loadConfig();
        time = new Timer();
        OpModeGeneral.allInit(hardwareMap);
        OpModeGeneral.jewelHitter.setPosition(0.5);
        OpModeGeneral.jewelExtender.setPosition(0.1);
        OpModeGeneral.grabberL.setPosition(0);
        OpModeGeneral.grabberR.setPosition(1);
        OpModeGeneral.grabberLB.setPosition(1);
        OpModeGeneral.grabberRB.setPosition(0);
        jewelInit();
        driveInit();
    }

    public void start() {
        int c = OpModeGeneral.camera.getVuMark();
        if (c != -1) {
            column = c;
        }
        timerScheduleJewel();
        timerScheduleDrive();
    }

    public void loop() {}

    private void driveForward(int timer) {
        time.schedule(driveForward, timer);
    }
    private void driveBackward(int timer) {
        time.schedule(driveBackward, timer);
    }
    private void strafeLeft(int timer) {
        time.schedule(strafeLeft, timer);
    }
    private void turn(int timer) {
        time.schedule(turn, timer);
    }
    private void turnNegative(int timer) {
        time.schedule(turnNegative, timer);
    }
    private void fullStop(int timer) {
        time.schedule(fullStop, timer);
    }
}
