package General.Utility;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Hardware;

import Devices.Drivers.AdafruitRGB;
import Devices.Drivers.ModernRoboticsGyro;
import Devices.Drivers.ModernRoboticsRGB;

/**
 * Created by onion on 11/8/16.
 */

public class OpModeGeneral {

    //Motors
    public static DcMotor leftFront;
    public static DcMotor rightFront;
    public static DcMotor leftBack;
    public static DcMotor rightBack;
    public static DcMotor catapult;
    public static DcMotor lifter;
    public static DcMotor lifter2;
    public static DcMotor extender;
    //Servos
    public static Servo grabber;



    public static void allInit (HardwareMap hardwareMap) {
        motorInit(hardwareMap);
        sensorInit(hardwareMap);
        servoInit(hardwareMap);
    }

    public static void motorInit (HardwareMap hardwareMap) {
        leftFront = hardwareMap.dcMotor.get("leftF");
        leftBack = hardwareMap.dcMotor.get("leftB");
        rightFront = hardwareMap.dcMotor.get("rightF");
        rightBack = hardwareMap.dcMotor.get("rightB");
        lifter = hardwareMap.dcMotor.get("lifter");
        lifter2 = hardwareMap.dcMotor.get("lifter2");
        extender = hardwareMap.dcMotor.get("extender");
    }

    public static void servoInit(HardwareMap hardwareMap) {
        grabber = hardwareMap.servo.get("grabber");
    }

    public static void sensorInit (HardwareMap hardwareMap) {
        //Color Sensors

    }


    public static void rawMove (double rightF, double rightB, double leftF, double leftB, boolean reverse)
    {
        int multiplier = reverse ? 1 : -1;

        leftFront.setPower(leftF * multiplier);
        leftBack.setPower(leftB * multiplier);
        rightFront.setPower(rightF * multiplier);
        rightBack.setPower(rightB * multiplier);
    }

    public static void tankMove (double leftY, double rightY, boolean reverse)
    {
        int multiplier = reverse ? 1 : -1;

        leftFront.setPower(leftY * multiplier);
        leftBack.setPower(leftY * multiplier);
        rightFront.setPower(rightY * multiplier);
        rightBack.setPower(rightY * multiplier);
    }

    public static void divisionDrive (double leftY, double rightX, boolean reverse)
    {
        double rightVector = leftY - rightX;
        double leftVector = rightX + leftY;
        double larger;
        if (Math.abs(rightVector) > 1 || Math.abs(leftVector) > 1)
        {
            larger = Math.max(rightVector, leftVector);
            rightVector /= larger;
            leftVector /= larger;
        }
        rightVector = Utilities.squareWithNegative(rightVector);
        leftVector = Utilities.squareWithNegative(leftVector);

        leftFront.setPower(leftVector);
        leftBack.setPower(leftVector);
        rightFront.setPower(rightVector);
        rightBack.setPower(rightVector);
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
        leftFront.setPower(_topLeft/_maxVector);
        leftBack.setPower(_topRight/_maxVector);
        rightFront.setPower(_bottomLeft/_maxVector);
        rightBack.setPower(_bottomRight/_maxVector);
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
            leftFront.setPower(speed * (_topLeft / _maxVector));
            leftBack.setPower(speed * (_topRight / _maxVector));
            rightFront.setPower(speed * (_bottomLeft / _maxVector));
            rightBack.setPower(speed * (_bottomRight / _maxVector));
        }
        else
        {
            leftFront.setPower(_topLeft / _maxVector);
            leftBack.setPower(_topRight / _maxVector);
            rightFront.setPower(_bottomLeft / _maxVector);
            rightBack.setPower(_bottomRight / _maxVector);
        }
    }

    public static void mecanumEncoderMove(float power, float[] distanceInTicks, float angle) {
        resetDriveEncoders(true);

        mecanumDriveAngle(power,angle);

    }

    public static void mecanumTurn(double turnSpeed){
        mecanumMove(0,0,turnSpeed,false);
    }

    public static void mecanumDriveAngle(float angle, float power) {
        float b = (float) Math.sin(90-angle);
        float a = (float) (Math.pow(power,2) - Math.pow(b,2));
        mecanumMove(a, b, 0,false);
    }

    public static void resetDriveEncoders(boolean useEncoders) {
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