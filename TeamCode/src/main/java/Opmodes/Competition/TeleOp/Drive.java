package Opmodes.Competition.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.util.Timer;
import java.util.TimerTask;

import General.Utility.OpModeGeneral;


@TeleOp(name = "Drive", group = "Competition")
public class Drive extends OpMode {

    //Values used to calculate power
     int mode = 0;
    private boolean _lastAButton = false;
    private boolean reverse = false;

    public void init()
    {
        OpModeGeneral.motorInit(hardwareMap);
    }

    public void loop() {

        if (gamepad1.dpad_up) {
            mode = 0;
        } else if (gamepad1.dpad_left) {
            mode = 1;
        } else if (gamepad1.dpad_right) {
            mode = 2;
        }

        if(mode == 0) {
            OpModeGeneral.divisionDrive(-gamepad1.left_stick_y, -gamepad1.right_stick_x, reverse);
        } else if (mode == 1) {
            OpModeGeneral.rawMove(-gamepad1.right_stick_y, -gamepad1.right_trigger, -gamepad1.left_stick_y, -gamepad1.left_trigger, reverse);
        } else if (mode == 2) {
            OpModeGeneral.tankMove(-gamepad1.left_stick_y, -gamepad1.right_stick_y, reverse);
        }


       // public static void rawMove (double rightF, double rightB, double leftF, double leftB, boolean reverse)
       // public static void tankMove (double leftY, double rightY, boolean reverse)





        //Trigger
        if (gamepad1.a & !_lastAButton) { triggerReverse(); }
        if (gamepad1.a) { _lastAButton = true; }
        else { _lastAButton = false; }

        OpModeGeneral.conteb.setPower(gamepad2.right_stick_y);
        if (gamepad2.dpad_up)
        {
            OpModeGeneral.lifter.setPower(1);
        }
        else if (gamepad2.dpad_down) {
            OpModeGeneral.lifter.setPower(-1);
        }
        else { OpModeGeneral.lifter.setPower(0); }
    }


    public void triggerReverse ()
    {
        reverse = !reverse;
    }
}