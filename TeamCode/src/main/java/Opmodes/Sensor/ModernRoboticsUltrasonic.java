package Opmodes.Sensor;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by admin on 4/1/2017.
 */


public class ModernRoboticsUltrasonic extends OpMode {
    UltrasonicSensor left, right;

    public void init(){
        left = hardwareMap.ultrasonicSensor.get("leftUltra");
        right = hardwareMap.ultrasonicSensor.get("rightUltra");
    }
    public void loop(){
        telemetry.addData("Left Reading:", left.getUltrasonicLevel());
        telemetry.addData("Right Reading:", right.getUltrasonicLevel());
    }
}
