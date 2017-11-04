package Opmodes.Sensor;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import Devices.Drivers.ModernRoboticsRGB;

/**
 * Created by admin on 4/10/2017.
 */

public class ModernRoboticsRGBTest extends OpMode {

     ModernRoboticsRGB sensor;

    public void init()
    {
        sensor = new ModernRoboticsRGB(hardwareMap, "cc", 0x3c);
        sensor.togglePassive();

    }
    public void loop()
    {
        telemetry.addData("ColorValue", sensor.getColorValue());
    }
}
