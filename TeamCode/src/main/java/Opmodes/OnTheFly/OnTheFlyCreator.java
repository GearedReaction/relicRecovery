package Opmodes.OnTheFly;

import android.graphics.Path;
import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;


import General.DataType.CheckDirection;
import General.Utility.OpModeGeneral;

/**
 * Created by bryan perkins on 1/1/01.
 */
@TeleOp (name = "On The Fly Writer", group = "OnTheFly" )

public class OnTheFlyCreator extends OpMode {

    private List<CheckDirection> ckdirs = new ArrayList<>();

    private ConcurrentLinkedQueue<List<Double>> points = new ConcurrentLinkedQueue<>();
    private File fileDir = Environment.getExternalStorageDirectory();
    List<HashMap<Integer, Double>> mmap = new ArrayList<>();
    private String fileName = "test.mtmp";
    private boolean saveStarted = false;
    private boolean frontORback = false;
    private boolean redORblue = false;
    private boolean saveIsReady;
    private boolean finished;
    private int column = 0;
    private int i = 0;

    private int flipI = 0;

    private void writePoint() {
        //Write Point
        List < Double > values = new ArrayList<>();
        values.add(OpModeGeneral.leftBack.getPower());
        values.add(OpModeGeneral.leftFront.getPower());
        values.add(OpModeGeneral.rightBack.getPower());
        values.add(OpModeGeneral.rightFront.getPower());
        values.add(OpModeGeneral.lifter.getPower());
        values.add(OpModeGeneral.grabberL.getPosition());
        values.add(OpModeGeneral.grabberR.getPosition());
        values.add(OpModeGeneral.grabberLB.getPosition());
        values.add(OpModeGeneral.grabberRB.getPosition());
        points.offer(values);

        //Move Robot and iterate
        OpModeGeneral.MecanumControl(gamepad1, gamepad2);
        i++;
    }


    private void checkDirection(CheckDirection ckdir) {
        ckdir.tempUp = ckdir.up;
        if (ckdir.motor.getCurrentPosition() > ckdir.max) ckdir.up = true;
        else ckdir.up = false;
        int pos = ckdir.motor.getCurrentPosition();
        ckdir.max = pos < ckdir.max ? pos : ckdir.max;
        ckdir.min = pos < ckdir.min ? pos : ckdir.min;
        if (!(ckdir.previous == pos)) {
            if (ckdir.tempUp != ckdir.up) {
                // Minima
                if (ckdir.up) {
                    mmap.get(ckdir.index).put(flipI, (double) ckdir.min);
                    flipI = i;
                    ckdir.max = ckdir.min;
                }
                // Maxima
                else {
                    mmap.get(ckdir.index).put(flipI, (double) ckdir.max);
                    flipI = i;
                    ckdir.min = ckdir.max;
                }

            }
        }
    }

    private void writePointEncoder() {
        //Write Point
        for (CheckDirection c : ckdirs) checkDirection(c);

        mmap.get(4).put(i, OpModeGeneral.lifter.getPower());
        mmap.get(5).put(i, OpModeGeneral.grabberL.getPosition());
        mmap.get(6).put(i, OpModeGeneral.grabberR.getPosition());
        mmap.get(7).put(i, OpModeGeneral.grabberLB.getPosition());
        mmap.get(8).put(i, OpModeGeneral.grabberRB.getPosition());

        //Move Robot and iterate
        OpModeGeneral.MecanumControl(gamepad1, gamepad2);
        i++;
    }

    private void saveFile(String fileName) {

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

            StringBuilder line = new StringBuilder("");

            for (int zoink = 0; zoink < i; zoink++)
            {
                for (HashMap<Integer, Double> map : mmap)
                {
                    boolean hasValue = false;
                    for (Map.Entry<Integer, Double> entry : map.entrySet())
                    {
                        if (entry.getKey() == zoink);
                        {
                            line.append(entry.getValue());
                            line.append(":");
                            hasValue = true;
                            break;
                        }
                    }
                    if (!hasValue)
                    {
                        line.append("-");
                        line.append(":");
                    }
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

    public void init() {
        OpModeGeneral.motionInit(hardwareMap);
        ckdirs.add(new CheckDirection(0, OpModeGeneral.leftBack));
        ckdirs.add(new CheckDirection(1, OpModeGeneral.leftFront));
        ckdirs.add(new CheckDirection(2, OpModeGeneral.rightBack));
        ckdirs.add(new CheckDirection(3, OpModeGeneral.rightFront));
        for (int z = 0; z < 9; z++) mmap.add(new HashMap<Integer, Double>());
    }

    public void loop() {
        if (!finished) {
            telemetry.addData("Tick", i);
            telemetry.addData("Count", points.size());
            if (i < 30000) writePointEncoder();
            else finished = true;
            if (gamepad1.y) finished = true;
        }
        //Save File
        if (finished)
        {
            OpModeGeneral.stopAllMotors();
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
                saveFile(fileName);
            }

        }
    }










}