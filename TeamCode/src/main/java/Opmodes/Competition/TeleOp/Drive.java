package Opmodes.Competition.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import General.Utility.OpModeGeneral;


@TeleOp(name = "Drive", group = "Competition")
public class Drive extends OpMode {

    //Values used to calculate power
    int mode = 0;
    private boolean _lastAButton = false;
    private boolean _lastXButton = false;
    private boolean reverse = false;

    public void init()
    {
        OpModeGeneral.motorInit(hardwareMap);
        OpModeGeneral.servoInit(hardwareMap);
    }

    public void loop() {
        // DRIVE WITH SLOW MODE NOT TESTEDS

        if (gamepad1.x & !_lastXButton) {
            OpModeGeneral.mecanumMove(-gamepad1.left_stick_x/2, -gamepad1.left_stick_y/2, -gamepad1.right_stick_x, !reverse);
            _lastXButton = true;
        } else if (!gamepad1.x & _lastXButton){
            OpModeGeneral.mecanumMove(-gamepad1.left_stick_x/2, -gamepad1.left_stick_y/2, -gamepad1.right_stick_x, !reverse);
        } else if (gamepad1.x & _lastXButton) {
            _lastXButton = false;
        } else {
            OpModeGeneral.mecanumMove(-gamepad1.left_stick_x, -gamepad1.left_stick_y, -gamepad1.right_stick_x, !reverse);
        }

        //Trigger
        if (gamepad1.a & !_lastAButton) { triggerReverse(); }
        if (gamepad1.a) { _lastAButton = true; }
        else { _lastAButton = false; }

        OpModeGeneral.grabber.setPosition(1-(gamepad2.left_stick_x+1)/2);


        if (gamepad2.dpad_up)
        {
            OpModeGeneral.lifter.setPower(-0.5);
            OpModeGeneral.lifter2.setPower(0.5);
        }
        else if (gamepad2.dpad_down) {
            OpModeGeneral.lifter.setPower(0.5);
            OpModeGeneral.lifter2.setPower(-0.5);
        }
        else {
            OpModeGeneral.lifter.setPower(0);
            OpModeGeneral.lifter2.setPower(0);
        }



        if (gamepad2.right_bumper) {
            OpModeGeneral.extender.setPower(0.5);
        }
        else if (gamepad2.left_bumper){
            OpModeGeneral.extender.setPower(-0.5);
        }
        else {
            OpModeGeneral.extender.setPower(0);
        }
    }


    public void triggerReverse ()
    {
        reverse = !reverse;
    }
}