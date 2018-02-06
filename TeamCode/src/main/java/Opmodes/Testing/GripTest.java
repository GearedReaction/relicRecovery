package Opmodes.Testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import General.Utility.OpModeGeneral;

/**
 * Created by union on 18年1月9日.
 */

public class GripTest extends OpMode {

    Servo rightB;
    Servo leftB;

    public void init()
    {
        OpModeGeneral.servoInit(hardwareMap);
        OpModeGeneral.lifter = hardwareMap.dcMotor.get("lifter");
        rightB = hardwareMap.servo.get("grabberRB");
        leftB = hardwareMap.servo.get("grabberLB");
    }

    public void loop()
    {
        OpModeGeneral.grabberL.setPosition(Range.clip(((gamepad2.left_stick_x + 1) / 2), 0, 1));
        OpModeGeneral.grabberR.setPosition(Range.clip(1 - ((gamepad2.left_stick_x + 1) / 2), 0, 1));
        rightB.setPosition(Range.clip(((gamepad2.left_stick_x + 1) / 2), 0, 1));
        leftB.setPosition(Range.clip(1 - ((gamepad2.left_stick_x + 1) / 2), 0, 1));
        OpModeGeneral.lifter.setPower(-gamepad1.right_stick_y);
    }
}
