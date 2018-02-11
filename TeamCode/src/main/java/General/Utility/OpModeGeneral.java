package General.Utility;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.qualcomm.hardware.lynx.LynxI2cColorRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import Devices.Drivers.VuforiaCamera;
import General.DataType.RGB;

/**
 * Created by Bryan Perkins on 11/8/16.
 */

public class OpModeGeneral {

    // Data
    public static final int DEVICECOUNT = 7;

    // Local
    private static double _topLeft, _topRight, _bottomLeft, _bottomRight, _maxVector;
    private static boolean lastAButton = false;
    private static boolean lastXButton = false;
    private static boolean lastBButton = false;
    private static boolean manualMode = false;
    private static boolean reverse = false;
    private static boolean slomo = false;
    private static int flipState = 0;
    private static boolean grabState;



    // Sensors
    public static LynxI2cColorRangeSensor blockProximity;
    public static ColorSensor jewelColor;
    public static VuforiaCamera camera;

    // Motors
    public static DcMotor rightFront;
    public static DcMotor rightBack;
    public static DcMotor leftFront;
    public static DcMotor leftBack;
    public static DcMotor extender;
    public static DcMotor lifter;


    // Servos
    public static Servo jewelExtender;
    public static Servo jewelHitter;
    public static Servo grabberLB;
    public static Servo grabberRB;
    public static Servo grabberL;
    public static Servo grabberR;
    public static Servo flipper;
    public static Servo relic;






    // Initialization
    public static void sensorInit (HardwareMap hardwareMap) {
        jewelColor = hardwareMap.colorSensor.get("jewelColor");
        blockProximity = hardwareMap.get(LynxI2cColorRangeSensor.class, "blockProximity");
    }

    public static void cameraInit (HardwareMap hardwareMap) {
        //Camera Init
        camera = new VuforiaCamera(hardwareMap);
        camera.initTracker();
    }

    public static void motionInit (HardwareMap hardwareMap) {
        reverse = false;
        slomo = false;
        manualMode = false;
        lastAButton = false;
        lastXButton = false;
        lastBButton = false;
        motorInit(hardwareMap);
        servoInit(hardwareMap);
    }

    public static void motorInit (HardwareMap hardwareMap) {
        leftFront = hardwareMap.dcMotor.get("leftF");
        leftBack = hardwareMap.dcMotor.get("leftB");
        rightFront = hardwareMap.dcMotor.get("rightF");
        rightBack = hardwareMap.dcMotor.get("rightB");
        lifter = hardwareMap.dcMotor.get("lifter");
        extender = hardwareMap.dcMotor.get("extender");
    }

    public static void servoInit(HardwareMap hardwareMap) {
        grabberL = hardwareMap.servo.get("grabberL");
        grabberR = hardwareMap.servo.get("grabberR");
        grabberLB = hardwareMap.servo.get("grabberLB");
        grabberRB = hardwareMap.servo.get("grabberRB");
        jewelExtender = hardwareMap.servo.get("jewelExtender");
        jewelHitter = hardwareMap.servo.get("jewelHitter");
        flipper = hardwareMap.servo.get("relic");
        relic = hardwareMap.servo.get("flipper");
    }

    public static void allInit (HardwareMap hardwareMap) {
        motorInit(hardwareMap);
        sensorInit(hardwareMap);
        servoInit(hardwareMap);
        cameraInit(hardwareMap);
    }

    public static void encoderMode (boolean justReset) {
        // Ensure that encoders are fully reset
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        if (!justReset) {
            // Set drive mode
            leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }






    // Movement
    public static void grab (double topServos, double bottomServos, double lifterPower, boolean open, boolean openBottom) {
        // Top
        if (open) {
            grabberL.setPosition(Range.clip(((-0.4 + 1) / 2), 0, 1));
            grabberR.setPosition(Range.clip(1 - ((-0.4 + 1) / 2), 0, 1));
        } else {
            grabberL.setPosition(Range.clip(((topServos + 1) / 2), 0, 1));
            grabberR.setPosition(Range.clip(1 - ((topServos + 1) / 2), 0, 1));
        }

        // Bottom
        if (openBottom) {
            grabberRB.setPosition(Range.clip(((-0.4 + 1) / 2), 0, 1));
            grabberLB.setPosition(Range.clip(1 - ((-0.4 + 1) / 2), 0, 1));
        } else {
            grabberRB.setPosition(Range.clip(((bottomServos + 1) / 2), 0, 1));
            grabberLB.setPosition(Range.clip(1 - ((bottomServos + 1) / 2), 0, 1));
        }

        OpModeGeneral.lifter.setPower(lifterPower);
    }

    public static void mecanumMove (double leftX, double leftY, double rightX, boolean negated, float speed) {
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

    public static void rawMove (double rightF, double rightB, double leftF, double leftB, boolean reverse) {
        int multiplier = reverse ? 1 : -1;

        leftFront.setPower(leftF * multiplier);
        leftBack.setPower(leftB * multiplier);
        rightFront.setPower(rightF * multiplier);
        rightBack.setPower(rightB * multiplier);
    }

    public static void mecanumMove (double leftX, double leftY, double rightX, boolean negated) {
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

    public static void singleGrab (boolean openTop, boolean openBottom, double lifterPower) {
        if (openTop) {
            grabberL.setPosition(0.3);
            grabberR.setPosition(0.7);
        }
        else {
            grabberL.setPosition(1);
            grabberR.setPosition(0);
        }
        if (openBottom) {
            grabberRB.setPosition(0.3);
            grabberLB.setPosition(0.7);
        }
        else {
            grabberLB.setPosition(0);
            grabberRB.setPosition(1);
        }

        lifter.setPower(lifterPower);
    }

    public static void divisionDrive (double leftY, double rightX, boolean reverse) {
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

    public static void tankMove (double leftY, double rightY, boolean reverse) {
        int multiplier = reverse ? 1 : -1;

        leftFront.setPower(leftY * multiplier);
        leftBack.setPower(leftY * multiplier);
        rightFront.setPower(rightY * multiplier);
        rightBack.setPower(rightY * multiplier);
    }

    public static void resetDriveEncoders (boolean useEncoders) {
        //Stop and reset encoder
        if (!useEncoders) {
            //Run without encoder
        }
        else
        {
            //Run using encoder
        }
    }

    public static void stopAllMotors() {
        rightFront.setPower(0);
        rightBack.setPower(0);
        leftFront.setPower(0);
        leftBack.setPower(0);
        lifter.setPower(0);
    }




    // Process sensor data
    public static Bitmap[][] splitBitmap(Bitmap bitmap, int xCount, int yCount) {
        Bitmap[][] bitmaps = new Bitmap[xCount][yCount];
        int width, height;
        width = bitmap.getWidth() / xCount;
        height = bitmap.getHeight() / yCount;
        for(int x = 0; x < xCount; ++x) {
            for(int y = 0; y < yCount; ++y) {
                bitmaps[x][y] = Bitmap.createBitmap(bitmap, x * width, y * height, width, height);
            }
        }
        return bitmaps;
    }

    public static RGB calculateAverageColor(Bitmap bitmap) {
        int R = 0; int G = 0; int B = 0;
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int n = 0;
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < pixels.length; i ++) {
            int color = pixels[i];
            R += Color.red(color);
            G += Color.green(color);
            B += Color.blue(color);
            n++;
        }
        RGB rgb = new RGB(R/n,G/n,B/n);
        return rgb;
    }

    public static int bluePeakCount(Bitmap bitmap)
    {
        return 0;
    }

    public static int redPeakCount(Bitmap bitmap)
    {
        return 0;
    }

    public static int getGreyscaleCount(Bitmap bitmap) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int n = 0;
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < pixels.length; i ++) {
            int color = pixels[i];
            if (Color.red(color) == Color.green(color) && Color.green(color) == Color.blue(color))
                n++;
        }
        return n;
    }

    public static boolean isRed (ColorSensor rgb) {
        if (rgb.red() > rgb.blue()) return true;
        return false;
    }


    // Movement Schemes
    public static void MecanumControl (Gamepad gamepad1, Gamepad gamepad2, boolean forceHalfSpeed) {
        //Trigger reverse
        if (gamepad1.a & !lastAButton) reverse = !reverse;
        if (gamepad1.a) lastAButton = true;
        else lastAButton = false;


        //Trigger slow-mo
        if (gamepad1.x & !lastXButton) slomo = !slomo;
        if (gamepad1.x) lastXButton = true;
        else lastXButton = false;

        //Trigger manual grabber control
        if (gamepad1.b & !lastBButton) manualMode = !manualMode;
        if (gamepad1.b) lastBButton = true;
        else lastBButton = false;

        //Control Extender
        if (gamepad2.dpad_left) flipState = 0;
        if (gamepad2.dpad_down) flipState = 1;
        if (gamepad2.dpad_right) flipState = 2;

        switch (flipState) {
            case 0:
                flipper.setPosition(1);
                break;
            case 1:
                flipper.setPosition(0.65);
                break;
            case 2:
                flipper.setPosition(0);
        }
        if (gamepad2.x) grabState = true;
        if (gamepad2.b) grabState = false;
        if (-gamepad2.left_stick_y > 0) extender.setPower(-gamepad2.left_stick_y);
        else extender.setPower(-gamepad2.left_stick_y / 3);
        if (grabState) extender.setPower(1);
        if (gamepad2.a) relic.setPosition(0.5);
        else if (gamepad2.y) relic.setPosition(0);
        else relic.setPosition(0.8);


        //Move robot
        if (forceHalfSpeed) mecanumMove(-gamepad1.left_stick_x/2, -gamepad1.left_stick_y/2, gamepad1.right_stick_x/2, !reverse);
        else if (slomo) mecanumMove(-gamepad1.left_stick_x/2, -gamepad1.left_stick_y/2, gamepad1.right_stick_x/2, !reverse);
        else mecanumMove(-gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x, !reverse);


        //Control Grabber (and lifter)
        double distance = blockProximity.getDistance(DistanceUnit.CM);
        if (!manualMode) {
            if (distance < 6 && !(gamepad1.right_trigger > 0.5))
                OpModeGeneral.singleGrab(false, false, 0);
            else
                OpModeGeneral.singleGrab(true, true, -gamepad2.right_stick_y);
        }
        else
        {
            OpModeGeneral.singleGrab((gamepad1.left_trigger > 0.5), (gamepad1.right_trigger > 0.5), -gamepad2.right_stick_y);
        }
    }

}