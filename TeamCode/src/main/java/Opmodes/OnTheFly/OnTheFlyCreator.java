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
    private List<String> fileNames = new ArrayList<>();
    private File fileDir = Environment.getExternalStorageDirectory();
    private boolean saveIsReady;
    private int fileID = 0;
    private boolean saveStarted = false;

    private void initFileNames()
    {
        fileNames.add("test");
        fileNames.add("LeftBlueF");
        fileNames.add("CenterBlueF");
        fileNames.add("RightBlueF");
        fileNames.add("LeftBlueB");
        fileNames.add("CenterBlueB");
        fileNames.add("RightBlueB");
        fileNames.add("LeftRedF");
        fileNames.add("CenterRedF");
        fileNames.add("RightRedF");
        fileNames.add("LeftRedB");
        fileNames.add("CenterRedB");
        fileNames.add("RightRedB");
    }

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
            if (i < RES) {
                if (milliseconds >= i * (30000 / RES)) {
                    //Create MotionPoint
                    points.add(new MotionPoint(new Vector2(-gamepad1.left_stick_x, -gamepad1.left_stick_y, -gamepad1.right_stick_x), 1 - (gamepad2.left_stick_x + 1) / 2, i));
                    i++;
                }
            }
            //Move
            OpModeGeneral.mecanumMove(-gamepad1.left_stick_x, -gamepad1.left_stick_y, -gamepad1.right_stick_x, false);
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
            initFileNames();


            if (!saveIsReady)
            {
                telemetry.clear();
                telemetry.addData("Ready To Save", 0);
                telemetry.addData("File Name", fileNames.get(fileID));

                if (gamepad1.right_bumper)
                {
                    fileID++;
                    if (fileID >= fileNames.size())
                    {
                        fileID = 0;
                    }
                }
                if (gamepad1.left_bumper)
                {
                    fileID--;
                    if (fileID <= 0)
                    {
                        fileID = fileNames.size() - 1;
                    }
                }
                if (gamepad1.start)
                {
                    saveIsReady = true;
                }
            }
            else if (!saveStarted) saveFile();

        }
    }

    private void saveFile()
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
            output = new File(fileDir + "/robotSaves/" + fileNames.get(fileID) + ".mtmp");
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