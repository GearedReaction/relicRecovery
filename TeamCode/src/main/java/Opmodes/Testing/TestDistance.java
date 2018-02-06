package Opmodes.Testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import General.Utility.OpModeGeneral;

/**
 * Created by RemoteDesktop on 2/6/2018.
 */

@TeleOp(name="TestDistance", group = "Test")
public class TestDistance extends OpMode {

    private boolean lastBButton;
    private boolean manualMode;

    public void init() {
        OpModeGeneral.motionInit(hardwareMap);
        OpModeGeneral.sensorInit(hardwareMap);
    }


    public void loop() {
        if (gamepad1.b & !lastBButton) manualMode = !manualMode;
        if (gamepad1.b) lastBButton = true;
        else lastBButton = false;


        double distance = OpModeGeneral.blockProximity.getDistance(DistanceUnit.CM);
        telemetry.addData("Distance", distance);
        if (!manualMode) {
            if (distance < 6 && !gamepad1.right_bumper) OpModeGeneral.singleGrab(false, false, 0);
            else OpModeGeneral.singleGrab(true, true, -gamepad2.right_stick_y);
        }
        else
        {
            OpModeGeneral.singleGrab((gamepad1.left_trigger > 0.5), (gamepad1.right_trigger > 0.5), -gamepad2.right_stick_y);
        }
    }

}
