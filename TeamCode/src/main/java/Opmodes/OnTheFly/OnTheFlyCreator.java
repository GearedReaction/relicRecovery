package Opmodes.OnTheFly;

import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.*;
import java.util.concurrent.ConcurrentLinkedQueue;


import General.Utility.OpModeGeneral;

/**
 * Created by bryan perkins on 1/1/01.
 */
@TeleOp (name = "On The Fly Writer", group = "OnTheFly" )

public class OnTheFlyCreator extends OpMode {



    private ConcurrentLinkedQueue<List<Double>> points = new ConcurrentLinkedQueue<>();
    private File fileDir = Environment.getExternalStorageDirectory();
    List<HashMap<Integer, Double>> mmap = new ArrayList<>();
    private List<double[]> idirs = new ArrayList<>();
    private String fileName = "test.mtmp";
    private boolean saveStarted = false;
    private boolean front = false;
    private boolean red = false;
    private boolean saveIsReady;
    private boolean finished;
    private int column = 0;
    private int i = 0;


    // MotionPoint detection and writing
    private void checkDirection(DcMotor motor, int id) {
        int sign;
        if (motor.getPower() == 0) sign = 0;
        else if (motor.getPower() > 0) sign = 1;
        else sign = -1;

        int pSign;
        if (idirs.get(id)[0] == 0) pSign = 0;
        else if (idirs.get(id)[0] > 0) pSign = 1;
        else pSign = -1;

        if (sign != pSign) {
            mmap.get(id).put((int) idirs.get(id)[1], (double) motor.getCurrentPosition());
            idirs.get(id)[0] = motor.getPower();
            idirs.get(id)[1] = i;
        }

    }

    private void writePoint() {
        //Write Point
        checkDirection(OpModeGeneral.leftBack, 0);
        checkDirection(OpModeGeneral.leftFront, 1);
        checkDirection(OpModeGeneral.rightBack, 2);
        checkDirection(OpModeGeneral.rightFront, 3);

        mmap.get(4).put(i, OpModeGeneral.lifter.getPower());
        mmap.get(5).put(i, OpModeGeneral.grabberL.getPosition());
        mmap.get(6).put(i, OpModeGeneral.grabberR.getPosition());
        mmap.get(7).put(i, OpModeGeneral.grabberLB.getPosition());
        mmap.get(8).put(i, OpModeGeneral.grabberRB.getPosition());

        //Move Robot and iterate
        OpModeGeneral.MecanumControl(gamepad1, gamepad2, true);
        i++;
    }

    // File Management
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
            telemetry.addData("Final tick", i);
            for (int j = 0; j < i; j++)
            {
                for (HashMap<Integer, Double> hmap : mmap)
                {
                    if (hmap.containsKey(j))
                    {
                        line.append(hmap.get(j));
                        line.append(":");
                    }
                    else
                    {
                        line.append("-");
                        line.append(":");
                    }
                }
                buffered.write(line.toString());
                buffered.newLine();
                line = new StringBuilder("");
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

    // OpMode Control
    public void init() {
        OpModeGeneral.motionInit(hardwareMap);
        OpModeGeneral.sensorInit(hardwareMap);
        OpModeGeneral.jewelHitter.setPosition(0.5);
        OpModeGeneral.jewelExtender.setPosition(0.1);
        for (int i = 0; i < 4; i++) idirs.add(new double[]{0.0,0.0});
        for (int z = 0; z < 9; z++) mmap.add(new HashMap<Integer, Double>());

    }

    public void start() {
        OpModeGeneral.encoderMode(true);
    }

    public void loop() {
        if (!finished) {
            telemetry.addData("Tick", i);
            telemetry.addData("Count", points.size());
            writePoint();

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
                telemetry.addData("red", red ? "True" : "False");
                telemetry.addData("front", front ? "True" : "False");

                if (gamepad1.a) fileName = "test.mtmp";
                if (gamepad1.dpad_down) front = false;
                if (gamepad1.dpad_right) red = false;
                if (gamepad1.dpad_left) red = true;
                if (gamepad1.dpad_up) front = true;
                if (gamepad1.x) column = 0;
                if (gamepad1.y) column = 1;
                if (gamepad1.b) column = 2;

                if (gamepad1.left_bumper)
                {
                    fileName = "";
                    if (column == 0) fileName += "Left";
                    else if (column == 1) fileName += "Center";
                    else if (column == 2) fileName += "Right";
                    fileName += red ? "Red" : "Blue";
                    fileName += front ? "F" : "B";
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