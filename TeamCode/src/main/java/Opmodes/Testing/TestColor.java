package Opmodes.Testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import java.util.Timer;
import java.util.TimerTask;

import General.Utility.OpModeGeneral;

/**
 * Created by union on 18年1月19日.
 */

@TeleOp (name="JewelTest", group = "Test")
public class TestColor extends OpMode {

    public static TimerTask getTheColor, dropDown;
    public static Timer time;


    public void start()
    {
        timerSchedule();
    }

    public void init() {

        OpModeGeneral.servoInit(hardwareMap);
        OpModeGeneral.sensorInit(hardwareMap);
        OpModeGeneral.jewelExtender.setPosition(0.5);
        OpModeGeneral.jewelHitter.setPosition(0.5);
        timerInit();
    }


    public void loop() {}


    private void timerSchedule(){
        time.schedule(dropDown, 1000);
        time.schedule(getTheColor, 2500);
    }

    private void timerInit() {
        time = new Timer();
        dropDown = new TimerTask() {
            public void run() {
                OpModeGeneral.jewelExtender.setPosition(1);
            }
        };

        getTheColor = new TimerTask() {
            public void run() {
                if (OpModeGeneral.isRed(OpModeGeneral.jewelColor))
                    OpModeGeneral.jewelHitter.setPosition(0.7);
                else
                    OpModeGeneral.jewelHitter.setPosition(0.3);


            }
        };

    }
}
