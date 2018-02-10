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

    private static TimerTask getTheColor, dropDown, moveBack, raise, raisePartial, run, move, moveReverse;
    private static Timer time;


    // Manage TimerTasks
    private void timerSchedule() {
        time.schedule(dropDown, 0);
        time.schedule(getTheColor, 1500);
        time.schedule(raisePartial, 2500);
        time.schedule(moveBack, 3000);
        time.schedule(raise, 3500);
        time.schedule(moveBack, 5000);


        //time.schedule(run, 4000);
    }

    private void jewelInit() {
        time = new Timer();

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

        run = new TimerTask() {
            public void run() {
                ready = true;
            }
        };
    }

    private void driveInit() {
        move = new TimerTask() {
            public void run() {
                OpModeGeneral.mecanumMove(0,1,0,false);
            }
        };

        moveReverse = new TimerTask() {
            public void run() {
                OpModeGeneral.mecanumMove(0,-1,0,false);
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
        OpModeGeneral.allInit(hardwareMap);
        OpModeGeneral.jewelHitter.setPosition(0.5);
        OpModeGeneral.jewelExtender.setPosition(0.1);
        OpModeGeneral.grabberL.setPosition(0);
        OpModeGeneral.grabberR.setPosition(1);
        OpModeGeneral.grabberLB.setPosition(1);
        OpModeGeneral.grabberRB.setPosition(0);
        jewelInit();
    }

    public void start() {
        timerSchedule();
    }

    public void loop() {}
}
