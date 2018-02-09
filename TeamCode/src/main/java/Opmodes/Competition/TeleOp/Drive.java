package Opmodes.Competition.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import General.Utility.OpModeGeneral;

@TeleOp(name = "Drive", group = "Competition")
public class Drive extends OpMode {

    public void init() {
        OpModeGeneral.motionInit(hardwareMap);
        OpModeGeneral.sensorInit(hardwareMap);
        OpModeGeneral.jewelHitter.setPosition(0.5);
        OpModeGeneral.jewelExtender.setPosition(0.1);
    }

    public void loop() {
        OpModeGeneral.MecanumControl(gamepad1, gamepad2, false);
    }
}