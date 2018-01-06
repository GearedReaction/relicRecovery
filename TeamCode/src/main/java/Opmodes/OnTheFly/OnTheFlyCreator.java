package Opmodes.OnTheFly;

import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

import General.DataType.MotionPoint;
import General.DataType.Vector2;
import General.Utility.OpModeGeneral;

/**
 * Created by bryan perkins on 1/1/01.
 */
@TeleOp (name = "On The Fly Writer", group = "OnTheFly" )

public class OnTheFlyCreator extends OpMode {


    public static final int RES = 3000;
    private long milliseconds;
    private long startTimeSinceEpoch;
    private List<MotionPoint> points = new ArrayList<>();
    private File fileDir = Environment.getExternalStorageDirectory();
    private boolean saveIsReady;
    private int fileID = 0;
    private boolean saveStarted = false;
    private boolean slomo = false;
    private boolean lastXButton = false;
    String fileName = "test.mtmp";
    boolean frontORback = false;
    boolean redORblue = false;
    int column = 0;



    public void init()
    {
        OpModeGeneral.motorInit(hardwareMap);
        OpModeGeneral.servoInit(hardwareMap);
    }


    public void start()
    {
        startTimeSinceEpoch = System.currentTimeMillis();
        milliseconds = 0;
    }
    int i = 0;
    public void loop()
    {
        //Record Data
        if (milliseconds <= 30000) {

            //Trigger slow-mo
            if (gamepad1.x & !lastXButton) slomo = !slomo;
            if (gamepad1.x) lastXButton = true;
            else lastXButton = false;

            if (i < RES) {
                if (milliseconds >= i * (30000 / RES)) {
                    //Create MotionPoint
                    if (slomo) points.add(new MotionPoint(new Vector2(-gamepad1.left_stick_x / 2, -gamepad1.left_stick_y / 2, gamepad1.right_stick_x / 2), 1 - (gamepad2.left_stick_x + 1) / 2, i));
                    else points.add(new MotionPoint(new Vector2(-gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x), 1 - (gamepad2.left_stick_x + 1) / 2, i));
                    i++;
                }
            }

            //Move
            if (slomo) OpModeGeneral.mecanumMove(-gamepad1.left_stick_x/2, -gamepad1.left_stick_y/2, gamepad1.right_stick_x/2, false);
            else OpModeGeneral.mecanumMove(-gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x, false);
            OpModeGeneral.grabber.setPosition(1 - (gamepad2.left_stick_x + 1) / 2);

            //Display data on screen
            telemetry.addData("Time:", milliseconds);
            telemetry.addData("MPointCount:", points.size());
            telemetry.addData("MPoint:", -gamepad1.left_stick_x + " : " + -gamepad1.left_stick_y + " : " + -gamepad1.right_stick_x);

            //Update the current millisecond value
            milliseconds = System.currentTimeMillis() - startTimeSinceEpoch;
        }

        //Save File
        else
        {
            if (!saveIsReady)
            {
                telemetry.addData("Ready To Save", 0);
                telemetry.addData("File Name", fileName);
                telemetry.addData("Column", column);
                telemetry.addData("frontORback", frontORback);
                telemetry.addData("redORblue", redORblue);

                if (gamepad1.a) fileName = "test.mtmp";


                if (gamepad1.dpad_up) frontORback = true;
                else if (gamepad1.dpad_down) frontORback = false;
                if (gamepad1.dpad_left) redORblue = false;
                else if (gamepad1.dpad_right) redORblue = true;

                if (gamepad1.x) column = 0;
                else if (gamepad1.y) column = 1;
                else if (gamepad1.b) column = 2;

                if (gamepad1.left_bumper)
                {
                    fileName = "";
                    if (column == 0) fileName += "Left";
                    else if (column == 1) fileName += "Center";
                    else if (column == 2) fileName += "Right";
                    fileName += redORblue ? "Red" : "Blue";
                    fileName += frontORback ? "F" : "B";
                    fileName += ".mtmp";
                    telemetry.addData("FileTest", fileName);
                }

                if (gamepad1.start)
                {
                    saveIsReady = true;
                }
            }
            else if (!saveStarted) saveFile(fileName);

        }
    }

    private void saveFile(String fileName)
    {
        saveStarted = true;
        FileWriter obj = null;
        File output;
        BufferedWriter buffered;
        try {
            File dir = new File(fileDir + "/robotSaves/");
            if (!(dir.exists() && dir.isDirectory())) {
                dir.mkdirs();
            }
            output = new File(fileDir + "/robotSaves/" + fileName);
            obj = new FileWriter(output);
            buffered = new BufferedWriter(obj);
            for (MotionPoint mtmp : points) {
                DecimalFormat format = new DecimalFormat("0.000");
                float _x = mtmp.vec.x;
                float _y = mtmp.vec.y;
                float _rot = mtmp.vec.rot;
                float _grab = mtmp.grabber;
                int _order = mtmp.order;
                String line = "M:"
                        + format.format(_x) +","
                        + format.format(_y)+","
                        + format.format(_rot)+","
                        + format.format(_grab) +","
                        + _order;
                buffered.write(line);
                buffered.newLine();
            }
        }
        catch (FileNotFoundException fl)
        {
            telemetry.addData("FILE IS NOT FOUND", 1);
            System.out.println(fl.getStackTrace());
        }
        catch (IOException io)
        {
            telemetry.addData("IO EXCEPTION", 2);
            System.out.println(io.getStackTrace());
        }
        finally
        {
            if (obj != null) {
                try {
                    obj.close();
                    telemetry.addData("Finished Saving File", 1);
                }
                catch (IOException io)
                {
                    telemetry.addData("IO EXCEPTION", 2);
                    System.out.println(io.getStackTrace());
                }
            }
        }
    }
}