package Opmodes.Testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import General.Utility.OpModeGeneral;

/**
 * Created by admin on 12/15/2017.
 */

@TeleOp (name="VuMarkTest", group = "Test")
public class VuMarkTest extends OpMode {

    public void init()
    {
        OpModeGeneral.cameraInit(hardwareMap);
    }

    public void loop()
    {
        telemetry.addData("Vuforia", OpModeGeneral.camera.getVuMark());
    }
}
