package Opmodes.OnTheFly;

import android.graphics.Path;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.io.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import General.DataType.MotionPoint;
import General.DataType.MotorPoint;
import General.DataType.MovingPart;
import General.DataType.Vector2;
import General.Utility.OpModeGeneral;
import General.DataType.MovingPart;

/**
 * Created by bryan perkins on 1/1/01.
 */
@TeleOp (name = "On The Fly Writer", group = "OnTheFly" )

public class OnTheFlyCreator extends OpMode {

    private File fileDir = Environment.getExternalStorageDirectory();
    private Thread inputThread = new Thread(new UpdateThread());
    private ConcurrentLinkedQueue<List<Float>> points = new ConcurrentLinkedQueue<>();
    private String fileName = "test.mtmp";
    private boolean saveStarted = false;
    private boolean frontORback = false;
    private boolean redORblue = false;
    private boolean saveIsReady;
    private boolean finished;
    private int column = 0;
    private int i = 0;
    int interval = 1;
    long nextTime = System.currentTimeMillis() + interval;

    public void init() {
        OpModeGeneral.motionInit(hardwareMap);
    }

    private class UpdateThread implements Runnable {

        @Override
        public void run(){
            while (i < 30000)
            {
                while (nextTime - System.currentTimeMillis() > 0)
                    ;
                write();
                nextTime += interval;
            }
            finished = true;
        }
    }

    private void write()
    {
            new Thread(new Runnable() {
                public void run() {
                    writePoint();
                }
            }).start();
            OpModeGeneral.MecanumControl(gamepad1, gamepad2);
            i++;
    }

    private void saveFile(String fileName) {
        try { inputThread.join(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        BufferedWriter buffered = null;
        FileWriter obj = null;
        saveStarted = true;
        File output;

        try {
            File dir = new File(fileDir + "/robotSaves/");
            if (!(dir.exists() && dir.isDirectory())) dir.mkdirs();
            String name = fileDir + "/robotSaves/" + fileName;

            output = new File(name);
            obj = new FileWriter(output);
            buffered = new BufferedWriter(obj);

            Iterator<List<Float>> itr = points.iterator();
            while (itr.hasNext()) {
                StringBuilder line = new StringBuilder("");
                for (Float value : itr.next()) {
                    line.append(value);
                    line.append(":");
                }
                buffered.write(line.toString());
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
                    buffered.close();
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

    public void start() {
        inputThread.start();
    }

    private void writePoint() {
        List<Float> values = new ArrayList<>();
        values.add((float) OpModeGeneral.leftBack.getPower());
        values.add((float) OpModeGeneral.leftFront.getPower());
        values.add((float) OpModeGeneral.rightBack.getPower());
        values.add((float) OpModeGeneral.rightFront.getPower());
        values.add((float) OpModeGeneral.lifter.getPower());
        values.add((float) OpModeGeneral.grabberL.getPosition());
        values.add((float) OpModeGeneral.grabberR.getPosition());
        values.add((float) OpModeGeneral.grabberLB.getPosition());
        values.add((float) OpModeGeneral.grabberRB.getPosition());
        points.offer(values);
    }

    public void loop() {
        if (!finished) {
            telemetry.addData("Tick", i);
            telemetry.addData("Count", points.size());
        }
        //Save File
        if (finished)
        {
            if (!saveIsReady)
            {
                telemetry.addData("Ready To Save", 0);
                telemetry.addData("File Name", fileName);
                telemetry.addData("Column", column);
                telemetry.addData("frontTrueBackFalse", frontORback);
                telemetry.addData("redTrueBackFalse", redORblue);

                if (gamepad1.a) fileName = "test.mtmp";
                if (gamepad1.dpad_down) frontORback = false;
                if (gamepad1.dpad_right) redORblue = false;
                if (gamepad1.dpad_left) redORblue = true;
                if (gamepad1.dpad_up) frontORback = true;
                if (gamepad1.x) column = 0;
                if (gamepad1.y) column = 1;
                if (gamepad1.b) column = 2;

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

                if (gamepad1.start) saveIsReady = true;
            }
            else if (!saveStarted) {
                new Thread(new Runnable() {
                    public void run() {
                        saveFile(fileName);
                    }
                });
            }

        }
    }










}