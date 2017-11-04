package Opmodes.Competition.Autonomous;

import java.util.Timer;
import java.util.TimerTask;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import General.Utility.OpModeGeneral;

/**
 * Created by union on 17年11月3日.
 */
@Autonomous(name = "MoveToSafeBlueTop", group = "Competition")

// Face away from the relic cap zone

public class MoveToSafeBlueTop {
    public static TimerTask move, stopAll, turnLeft, turnRight;
    public static Timer time;
    public static boolean stage1Complete = false;
    public static boolean isBlue = false;
    public static int millisecondDelay = 0;

    public static void timerInit() {
        time = new Timer();
        move = new TimerTask() {
            public void run() {
                OpModeGeneral.tankMove(1, 1, false);
            }
        };
        turnLeft = new TimerTask() {
            public void run() {
                OpModeGeneral.tankMove(-1, 1, false);
            }
        };
        turnRight = new TimerTask() {
            public void run() {
                OpModeGeneral.tankMove(1, -1, false);
            }
        };

        stopAll = new TimerTask(){
            public void run() {
                OpModeGeneral.tankMove(0, 0, false);
                stage1Complete = true;
            }
        };
    }
    public static void timerSchedule(){
        time.schedule(move, 1000 + millisecondDelay);
        time.schedule(stopAll, 2500 + millisecondDelay);
        time.schedule(turnLeft, 2500 + millisecondDelay);
        time.schedule(move, 3000 + millisecondDelay);
        time.schedule(stopAll, 3250 + millisecondDelay);
    }
}
