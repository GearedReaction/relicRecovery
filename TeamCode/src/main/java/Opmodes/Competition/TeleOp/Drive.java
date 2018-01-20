package Opmodes.Competition.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import General.Utility.OpModeGeneral;

@TeleOp(name = "Drive", group = "Competition")
public class Drive extends OpMode {

    public void init() {
        OpModeGeneral.motorInit(hardwareMap);
        OpModeGeneral.servoInit(hardwareMap);
    }

    public void loop() {
        OpModeGeneral.MecanumControl(gamepad1, gamepad2);
    }
}