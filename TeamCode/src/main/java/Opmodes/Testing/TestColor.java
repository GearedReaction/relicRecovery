package Opmodes.Testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import General.Utility.OpModeGeneral;

/**
 * Created by union on 18年1月19日.
 */

@TeleOp (name="TestColor", group = "Test")
public class TestColor extends OpMode {

    public void init() {
        OpModeGeneral.sensorInit(hardwareMap);
    }

    public void loop()
    {
        if (OpModeGeneral.isRed(OpModeGeneral.jewelColor)) telemetry.addData("Color", "Red");
        else telemetry.addData("Color", "Blue");
    }
}
