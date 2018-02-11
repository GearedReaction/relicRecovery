package Opmodes.Competition.Autonomous;

import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
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

@Autonomous (name = "Autonomous", group = "Competition" )
public class AutoTimer extends OpMode {
    private File dir = Environment.getExternalStorageDirectory();
    private boolean ready = false;
    private boolean front = true;
    private boolean red = true;
    private int column = 1;
    private double speed = 0.4;

    private static TimerTask lift, stopLift, getTheColor, dropDown, moveBack, raise, raisePartial;
    private static TimerTask driveForward, driveBackward, strafeLeft, turn, turnNegative, fullStop, stop, stop2;
    private static TimerTask finalStrafe, strafeStop, finalForward, fForwardStop, dropBlock, finalBack, fBackStop, finalClose, finalPush, finalPushStop, finalBack2, fBack2Stop;
    private static Timer time;



    // Manage TimerTasks
    private void timerScheduleJewel() {
        time.schedule(lift, 500);
        time.schedule(stopLift, 1250);
        time.schedule(dropDown, 1500);
        time.schedule(getTheColor, 3000);
        time.schedule(raisePartial, 4000);
        time.schedule(moveBack, 4500);
        time.schedule(raise, 5000);
    }

    private void timerScheduleDrive() {
        // Starting from 6000

        int startTimeDrive = 6000;

        // Timing Front

        int stopFrontDrive = 7500;
        int turnFront = 8000;
        int stopTurnFront = 9050;

        // Timing Back
        int stopBackDrive = 7000;
        int strafeBack = 7500;
        int stopStrafeBack = 8650;
        int turnBack = 8700;
        int stopTurnBack = 11000;


        // Front Red
        if (front && red) {
            driveForward(startTimeDrive);
            fullStop(stopFrontDrive);
            turnNegative(turnFront);
            stop(stopTurnFront);
        }
        // Back Red
        else if (!front && red) {
            driveForward(startTimeDrive);
            fullStop(stopBackDrive);
            strafeLeft(strafeBack);
            stop(stopStrafeBack);
        }
        // Front Blue
        else if (front && !red)
        {
            driveBackward(startTimeDrive);
            fullStop(stopFrontDrive);
            turnNegative(turnFront);
            stop(stopTurnFront);
        }
        // Back Blue
        else if (!front && !red) {
            driveBackward(startTimeDrive);
            fullStop(stopBackDrive);
            strafeLeft(strafeBack);
            stop(stopStrafeBack);
            turn(turnBack);
            stop2(stopTurnBack);
        }

    }

    private void timerScheduleFinal() {
        time.schedule(finalStrafe, 11500);
        time.schedule(strafeStop, 12450);
        time.schedule(finalForward, 12500);
        time.schedule(fForwardStop, 13000);
        time.schedule(dropBlock, 13500);
        time.schedule(finalBack, 14000);
        time.schedule(fBackStop, 14500);
        time.schedule(finalClose, 15000);
        time.schedule(finalPush, 15500);
        time.schedule(finalPushStop, 17500);
        time.schedule(finalBack2, 18000);
        time.schedule(fBack2Stop, 18150);
    }

    private void jewelInit() {
        lift = new TimerTask() {
            public void run() {
                OpModeGeneral.lifter.setPower(-speed);
            }
        };

        stopLift = new TimerTask() {
            public void run() {
                OpModeGeneral.lifter.setPower(0);
            }
        };

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
                OpModeGeneral.mecanumMove(0,speed,0, false);
            }
        };
        driveBackward = new TimerTask() {
            public void run() {
                OpModeGeneral.mecanumMove(0,-speed,0, false);
            }
        };
        strafeLeft = new TimerTask() {
            public void run() {
                OpModeGeneral.mecanumMove(speed,0,0, false);
            }
        };
        turn = new TimerTask() {
            public void run() {
                OpModeGeneral.mecanumMove(0,0,-speed, false);
            }
        };
        turnNegative = new TimerTask() {
            public void run() {
                OpModeGeneral.mecanumMove(0,0,speed, false);
            }
        };

        fullStop = new TimerTask() {
            public void run() {
                OpModeGeneral.stopAllMotors();
            }
        };
        stop = new TimerTask() {
            public void run() {
                OpModeGeneral.stopAllMotors();
            }
        };
        stop2 = new TimerTask() {
            public void run() {
                OpModeGeneral.stopAllMotors();
            }
        };
    }

    private void finalInit() {
        finalStrafe = new TimerTask() {
            public void run() {
                if (column == 0)
                    OpModeGeneral.mecanumMove(speed,0,0, false);
                if (column == 2)
                    OpModeGeneral.mecanumMove(-speed,0,0, false);
            }
        };
        strafeStop = new TimerTask() {
            public void run() {
                OpModeGeneral.stopAllMotors();
            }
        };
        finalForward = new TimerTask() {
            public void run() {
                OpModeGeneral.mecanumMove(0,speed,0, false);
            }
        };
        fForwardStop = new TimerTask() {
            public void run() {
                OpModeGeneral.stopAllMotors();
            }
        };
        dropBlock = new TimerTask() {
            public void run() {
                OpModeGeneral.grabberL.setPosition(0.3);
                OpModeGeneral.grabberR.setPosition(0.7);
                OpModeGeneral.grabberRB.setPosition(0.3);
                OpModeGeneral.grabberLB.setPosition(0.7);
            }
        };
        finalBack = new TimerTask() {
            public void run() {
                OpModeGeneral.mecanumMove(0,-speed,0, false);
            }
        };
        fBackStop = new TimerTask() {
            public void run() {
                OpModeGeneral.stopAllMotors();
            }
        };
        finalClose = new TimerTask() {
            public void run() {
                OpModeGeneral.grabberL.setPosition(1);
                OpModeGeneral.grabberR.setPosition(0);
                OpModeGeneral.grabberRB.setPosition(1);
                OpModeGeneral.grabberLB.setPosition(0);
                OpModeGeneral.lifter.setPower(speed);
            }
        };
        finalPush = new TimerTask() {
            public void run() {
                OpModeGeneral.lifter.setPower(0);
                OpModeGeneral.mecanumMove(0,speed,0, false);
            }
        };
        finalPushStop = new TimerTask() {
            public void run() {
                OpModeGeneral.stopAllMotors();
            }
        };
        finalBack2 = new TimerTask() {
            public void run() {
                OpModeGeneral.mecanumMove(0,-speed,0, false);
            }
        };
        fBack2Stop = new TimerTask() {
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
        finalInit();
    }

    public void start() {
        int c = OpModeGeneral.camera.getVuMark();
        if (c != -1) {
            column = c;
        }
        OpModeGeneral.grabberL.setPosition(1);
        OpModeGeneral.grabberR.setPosition(0);
        OpModeGeneral.grabberRB.setPosition(1);
        OpModeGeneral.grabberLB.setPosition(0);
        timerScheduleJewel();
        timerScheduleDrive();
        timerScheduleFinal();
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
    private void stop(int timer) {
        time.schedule(stop, timer);
    }
    private void stop2(int timer) {
        time.schedule(stop2, timer);
    }
}
