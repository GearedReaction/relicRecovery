package Opmodes.OnTheFly;


import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import General.Utility.OpModeGeneral;
import General.DataType.MotionPoint;
import General.DataType.MotorPoint;
import General.DataType.MovingPart;

/**
 * Created by Bryan Perkins                       on 2/7/17.
 */

@Autonomous (name = "On The Fly Reader", group = "OnTheFly" )

public class OnTheFlyReader extends OpMode {

    private File dir = Environment.getExternalStorageDirectory();
    private Thread fileThread = new Thread(new LoadThread());
    private List<MotionPoint> motionPoints;
    private boolean frontORback = true;
    private boolean redORblue = true;
    private boolean ready = false;
    private String filename = "";
    int k = 0;

    private ConcurrentLinkedQueue<List<Double>> mpoints = new ConcurrentLinkedQueue<>();

    private void loadFile(String fileName) {
        int j = 0;
        for (String ln : getFile(fileName))
        {
            List<String> devices = Arrays.asList(ln.split(":"));
            List<Double> vals = new ArrayList<>();
            if (devices.size() >= OpModeGeneral.DEVICECOUNT) {
                for (int i = 0; i < 9; i++) {
                    if (devices.get(i).equals("-")) vals.add(null);
                    else vals.add(Double.parseDouble(devices.get(i)));
                }
            }
            mpoints.offer(vals);
            if (j == 5) ready = true;
            j++;
        }
    }

    private void move (List<Double> devices) {
        OpModeGeneral.leftBack.setPower(devices.get(0));
        OpModeGeneral.leftFront.setPower(devices.get(1));
        OpModeGeneral.rightBack.setPower(devices.get(2));
        OpModeGeneral.rightFront.setPower(devices.get(3));
        OpModeGeneral.lifter.setPower(devices.get(4));
        OpModeGeneral.grabberL.setPosition(devices.get(5));
        OpModeGeneral.grabberR.setPosition(devices.get(6));
        OpModeGeneral.grabberLB.setPosition(devices.get(7));
        OpModeGeneral.grabberRB.setPosition(devices.get(8));
    }

    private void moveEncoder (List<Double> devices) {
        if (devices.get(0) != null) OpModeGeneral.leftBack.setTargetPosition((int)(double) devices.get(0));
        if (devices.get(1) != null) OpModeGeneral.leftFront.setTargetPosition((int)(double) devices.get(1));
        if (devices.get(2) != null) OpModeGeneral.rightBack.setTargetPosition((int)(double) devices.get(2));
        if (devices.get(3) != null) OpModeGeneral.rightFront.setTargetPosition((int)(double) devices.get(3));
        OpModeGeneral.lifter.setPower(devices.get(4));

        OpModeGeneral.leftBack.setPower(1);
        OpModeGeneral.leftFront.setPower(1);
        OpModeGeneral.rightBack.setPower(1);
        OpModeGeneral.rightFront.setPower(1);

        OpModeGeneral.grabberL.setPosition(devices.get(5));
        OpModeGeneral.grabberR.setPosition(devices.get(6));
        OpModeGeneral.grabberLB.setPosition(devices.get(7));
        OpModeGeneral.grabberRB.setPosition(devices.get(8));
    }

    private List<String> getFile (String fileName) {
        List<String> lines = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader reader = new BufferedReader(fileReader);
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println(e.getStackTrace());
            telemetry.addData("FILE NOT FOUND", 0);
            return null;
        }
        catch (IOException e)
        {
            System.out.println(e.getStackTrace());
            telemetry.addData("IO EXCEPTION", 0);
            return null;
        }
        return lines;
    }

    private class LoadThread implements Runnable {
        @Override
        public void run(){
            telemetry.addData("Entered loading thread",0);
            //loadConfig();
            int pictograph = OpModeGeneral.camera.getVuMark();
            if (pictograph == -1) filename = "test.mtmp";
            else {
                if (pictograph == 0) filename += "Left";
                else if (pictograph == 1) filename += "Center";
                else if (pictograph == 2) filename += "Right";
                filename += redORblue ? "Red" : "Blue";
                filename += frontORback ? "F" : "B";
                filename += ".mtmp";
            }
            loadFile(dir + "/robotSaves/" + filename);
        }
    }

    private void loadConfig () {
        try {
            FileReader fileReader = new FileReader(dir + "/robotSaves/config.cfg");
            BufferedReader reader = new BufferedReader(fileReader);
            String line;
            int i = 0;
            while((line = reader.readLine()) != null) {
                if (i == 0) frontORback = (line.equals("0")) ? false : true;
                else redORblue = (line.equals("0")) ? false : true;
                i++;
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println(e.getStackTrace());
            telemetry.addData("FILE NOT FOUND", 0);
        }
        catch (IOException e)
        {
            System.out.println(e.getStackTrace());
            telemetry.addData("IO EXCEPTION", 0);
        }
    }

    public void init() {
        OpModeGeneral.motionInit(hardwareMap);
        OpModeGeneral.cameraInit(hardwareMap);
        OpModeGeneral.encoderMode();
    }

    public void start() {
        fileThread.start();
    }

    public void loop() {
        if (ready) {
            telemetry.addData("Tick", k);
            if (!mpoints.isEmpty()) {
                moveEncoder(mpoints.poll());
            }
            else {
                OpModeGeneral.stopAllMotors();
            }
        }
    }


}
