package Opmodes.Competition.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import General.Utility.OpModeGeneral;

@TeleOp(name = "Drive", group = "Competition")
public class Drive extends OpMode {

    private boolean lastAButton = false;
    private boolean lastXButton = false;
    private boolean reverse = false;
    private boolean slomo = false;

    public void init()
    {
        OpModeGeneral.motorInit(hardwareMap);
        OpModeGeneral.servoInit(hardwareMap);
    }

    public void loop() {
        //Trigger reverse
        if (gamepad1.a & !lastAButton) reverse = !reverse;
        if (gamepad1.a) lastAButton = true;
        else lastAButton = false;

        //Trigger slow-mo
        if (gamepad1.x & !lastXButton) slomo = !slomo;
        if (gamepad1.x) lastXButton = true;
        else lastXButton = false;

        //Move robot
        if (slomo) OpModeGeneral.mecanumMove(-gamepad1.left_stick_x/2, -gamepad1.left_stick_y/2, gamepad1.right_stick_x/2, !reverse);
        else OpModeGeneral.mecanumMove(-gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x, !reverse);

        //Compress or expand the grabber
        OpModeGeneral.grabber.setPosition(1-(gamepad2.right_stick_x+1)/2);

        //Expand the lifter
        OpModeGeneral.lifter.setPower(-gamepad2.left_stick_y);
    }
}