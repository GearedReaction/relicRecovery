package Opmodes.Testing;

/**
 * Created by JORDAN ACCOMANDO!!!!!! on 18年2月2日.
 */

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import General.Utility.OpModeGeneral;


@TeleOp(name = "JewelTest", group = "Test")
public class JewelTest extends OpMode{

    public void init(){
        OpModeGeneral.sensorInit(hardwareMap);
    }

    public void loop(){
        telemetry.addData("IsRed", OpModeGeneral.isRed(OpModeGeneral.jewelColor));
    }

}
