package General.Utility;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by admin on 4/21/2017.
 */

public class OpModeTimers {
    public static TimerTask shoot, stopAllLast, shootPause, shootAgain, stopAll, stopAllAgain, flip, park;
    public static Timer time;
    public static boolean stage1Complete = false;
    public static boolean isBlue = false;
    public static int millisecondDelay = 0;

    public static void timerInit() {
        time = new Timer();
        shoot = new TimerTask() {
            public void run() {
                OpModeGeneral.mecanumMove(0, 0, 0, false);
                OpModeGeneral.catapult.setPower(1);
            }
        };
        shootPause = new TimerTask() {
            @Override
            public void run() {
                OpModeGeneral.mecanumMove(0, 0, 0, false);
//                OpModeGeneral.combine.setPower(-1);
                OpModeGeneral.catapult.setPower(0);
            }
        };
        shootAgain = new TimerTask() {
            @Override
            public void run() {
//                OpModeGeneral.combine.setPower(0);
                OpModeGeneral.mecanumMove(0, 0, 0, false);
                OpModeGeneral.catapult.setPower(1);
                OpModeGeneral.combine.setPower(0);
            }
        };
        stopAll = new TimerTask() {
            @Override
            public void run() {
                OpModeGeneral.catapult.setPower(0);
                OpModeGeneral.mecanumMove(0, 0, 0, false);
            }
        };
        stopAllAgain = new TimerTask() {
            public void run() {
                OpModeGeneral.catapult.setPower(0);
                OpModeGeneral.mecanumMove(0, 0, 0, false);
                stage1Complete = true;
            }
        };
        stopAllLast = new TimerTask() {
            public void run() {
                OpModeGeneral.catapult.setPower(0);
                OpModeGeneral.mecanumMove(0, 0, 0, false);
                stage1Complete = true;
            }
        };
        flip = new TimerTask() {
            @Override
            public void run() {
                OpModeGeneral.combine.setPower(-1);
            }
        };


        park = new TimerTask() {
            @Override
            public void run() {
                if (isBlue) OpModeGeneral.mecanumMove(0, -1, 0, false);
                else OpModeGeneral.mecanumMove(-1,1,0, false);
            }
        };
    }
        public static void timerSchedule() {
            OpModeGeneral.mecanumMove(-1,0,0,false);
            time.schedule(stopAll, 1550 + millisecondDelay);
            time.schedule(shoot, 2000 + millisecondDelay);
            time.schedule(shootPause, 3000 + millisecondDelay);
            time.schedule(flip, 3500 + millisecondDelay);
            time.schedule(shootAgain,5000 + millisecondDelay);
            time.schedule(stopAllAgain,6000 + millisecondDelay);
             //time.schedule(park, 7000 + millisecondDelay);
       //     time.schedule(stopAllLast, 15500 + millisecondDelay);
        }
}

