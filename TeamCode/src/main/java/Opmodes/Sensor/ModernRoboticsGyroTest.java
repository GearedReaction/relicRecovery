package Opmodes.Sensor;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import Devices.Drivers.ModernRoboticsGyro;

/**
 * Created by admin on 4/14/2017.
 */

@TeleOp (name = "Modern Robotics Gyro", group = "Sensors")

public class ModernRoboticsGyroTest extends OpMode {

    ModernRoboticsGyro gyro;

    public void init()
    {
        gyro = new ModernRoboticsGyro(hardwareMap, "gyro", 0x20);

    }

    public void loop()
    {
        telemetry.addData("Rotation", gyro.getHeadingData());
    }
}