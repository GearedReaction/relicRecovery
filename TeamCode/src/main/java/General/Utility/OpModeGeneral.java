package General.Utility;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.Servo;

import Devices.Drivers.AdafruitRGB;
import Devices.Drivers.ModernRoboticsGyro;
import Devices.Drivers.ModernRoboticsRGB;

/**
 * Created by union on 11/8/16.
 */

public class OpModeGeneral {

    public static DcMotor left1;
    public static DcMotor right1;
    public static DcMotor combine;
    public static DcMotor left2;
    public static DcMotor right2;
    public static DcMotor catapult;
    public static DcMotor lifter;
    public static ModernRoboticsRGB colorMid;
    public static ModernRoboticsRGB colorBeacon;
    public static ModernRoboticsRGB colorBack;
    public static ModernRoboticsGyro gyro;
    public static Servo flipper;


    public static void allInit (HardwareMap hardwareMap)
    {
        motorInit(hardwareMap);
        sensorInit(hardwareMap);
        servoInit(hardwareMap);
    }

    public static void motorInit (HardwareMap hardwareMap)
    {

    }

    public static void servoInit(HardwareMap hardwareMap)
    {

    }

    public static void sensorInit (HardwareMap hardwareMap)
    {
        //Color Sensors

    }


    private static double _topLeft, _topRight, _bottomLeft, _bottomRight, _maxVector;

    public static void mecanumMove (double leftX, double leftY, double rightX, boolean negated){
        //Each joystick alone gives the wheel a unique set of instructions
        //These equations add them all together
        if (negated) {
            _topLeft = leftX - leftY - rightX;
            _topRight = -leftX - leftY - rightX;
            _bottomLeft = leftX + leftY - rightX;
            _bottomRight = -leftX + leftY - rightX;
        }
        else
        {
            _topRight = -(-leftX - leftY + rightX);
            _topLeft = -(leftX - leftY + rightX);
            _bottomRight = -(-leftX + leftY + rightX);
            _bottomLeft = -(leftX + leftY + rightX);
        }

        //Find the largest absolute value
        _maxVector = Math.max(Math.max(Math.abs(_topLeft), Math.abs(_topRight)),
                Math.max(Math.abs(_bottomLeft), Math.abs(_bottomRight)));

        //If the vector is being divided is less than 1, set it to just 1. Allow for micromanagement
        //Also makes sure we don't divide by zero
        _maxVector = _maxVector > 1 ? _maxVector : 1;

        //Set power to values divided by the largest so numbers are in range and proportional
        left1.setPower(_topLeft/_maxVector);
        left2.setPower(_topRight/_maxVector);
        right1.setPower(_bottomLeft/_maxVector);
        right2.setPower(_bottomRight/_maxVector);
    }
    public static void mecanumMove (double leftX, double leftY, double rightX, boolean negated, float speed){
        //Each joystick alone gives the wheel a unique set of instructions
        //These equations add them all together
        if (negated) {
            _topLeft = leftX - leftY - rightX;
            _topRight = -leftX - leftY - rightX;
            _bottomLeft = leftX + leftY - rightX;
            _bottomRight = -leftX + leftY - rightX;
        }
        else
        {
            _topRight = -(-leftX - leftY + rightX);
            _topLeft = -(leftX - leftY + rightX);
            _bottomRight = -(-leftX + leftY + rightX);
            _bottomLeft = -(leftX + leftY + rightX);
        }

        //Find the largest absolute value
        _maxVector = Math.max(Math.max(Math.abs(_topLeft), Math.abs(_topRight)),
                Math.max(Math.abs(_bottomLeft), Math.abs(_bottomRight)));

        //If the vector is being divided is less than 1, set it to just 1. Allow for micromanagement
        //Also makes sure we don't divide by zero
        _maxVector = _maxVector > speed ? _maxVector : speed;

        //Set power to values divided by the largest so numbers are in range and proportional
        if (_maxVector <= 1) {
            left1.setPower(speed * (_topLeft / _maxVector));
            left2.setPower(speed * (_topRight / _maxVector));
            right1.setPower(speed * (_bottomLeft / _maxVector));
            right2.setPower(speed * (_bottomRight / _maxVector));
        }
        else
        {
            left1.setPower(_topLeft / _maxVector);
            left2.setPower(_topRight / _maxVector);
            right1.setPower(_bottomLeft / _maxVector);
            right2.setPower(_bottomRight / _maxVector);
        }
    }



    public static void mecanumEncoderMove(float power, float[] distanceInTicks, float angle)
    {
        resetDriveEncoders(true);

        mecanumDriveAngle(power,angle);

    }

    public static void mecanumTurn(double turnSpeed){
        mecanumMove(0,0,turnSpeed,false);
    }

    public static void mecanumDriveAngle(float angle, float power)
    {
        float b = (float) Math.sin(90-angle);
        float a = (float) (Math.pow(power,2) - Math.pow(b,2));
        mecanumMove(a, b, 0,false);
    }

    public static void resetDriveEncoders(boolean useEncoders)
    {
        //Stop and reset encoder
        if (!useEncoders) {
            //Run without encoder
        }
        else
        {
            //Run using encoder
        }
    }
}