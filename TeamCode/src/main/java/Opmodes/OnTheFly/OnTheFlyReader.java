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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import General.Utility.OpModeGeneral;

/**
 * Created by Bryan Perkins                       on 2/7/17.
 */

@Autonomous (name = "On The Fly Reader", group = "OnTheFly" )

public class OnTheFlyReader extends OpMode {

    private File dir = Environment.getExternalStorageDirectory();
    private Thread LoadThread = new Thread(new LoadThread());
    private boolean ready = false;
    private boolean front = true;
    private boolean red = true;
    private String filename = "";
    private int k = 0;

    private static TimerTask getTheColor, dropDown, moveBack, raise, raisePartial, run;
    private static Timer time;

    private ConcurrentLinkedQueue<List<Double>> mpoints = new ConcurrentLinkedQueue<>();

    // Move
    private void moveEncoder (List<Double> devices) {

        if (devices.get(0) != 111111.22334) OpModeGeneral.leftBack.setTargetPosition((int)(double) devices.get(0));
        if (devices.get(1) != 111111.22334) OpModeGeneral.leftFront.setTargetPosition((int)(double) devices.get(1));
        if (devices.get(2) != 111111.22334) OpModeGeneral.rightBack.setTargetPosition((int)(double) devices.get(2));
        if (devices.get(3) != 111111.22334) OpModeGeneral.rightFront.setTargetPosition((int)(double) devices.get(3));

        OpModeGeneral.leftBack.setPower(0.75);
        OpModeGeneral.leftFront.setPower(0.75);
        OpModeGeneral.rightBack.setPower(0.75);
        OpModeGeneral.rightFront.setPower(0.75);

        OpModeGeneral.lifter.setPower(devices.get(4));

        OpModeGeneral.grabberL.setPosition(devices.get(5));
        OpModeGeneral.grabberR.setPosition(devices.get(6));
        OpModeGeneral.grabberLB.setPosition(devices.get(7));
        OpModeGeneral.grabberRB.setPosition(devices.get(8));
    }


    // Parse File
    private void loadFile(String fileName) {
        for (String ln : getFile(fileName))
        {
            List<String> devices = Arrays.asList(ln.split(":"));
            List<Double> vals = new ArrayList<>();
            if (devices.size() >= OpModeGeneral.DEVICECOUNT) {
                for (int i = 0; i < 9; i++) {
                    if (devices.get(i).equals("-")) vals.add(111111.22334);
                    else vals.add(Double.parseDouble(devices.get(i)));
                }
            }
            mpoints.offer(vals);
        }
    }

    private class LoadThread implements Runnable {
        @Override
        public void run(){
            telemetry.addData("Entered loading thread",0);
            int pictograph = OpModeGeneral.camera.getVuMark();
            if (pictograph == -1) filename = "test.mtmp";
            else {
                if (pictograph == 0) filename += "Left";
                else if (pictograph == 1) filename += "Center";
                else if (pictograph == 2) filename += "Right";
                filename += red ? "Red" : "Blue";
                filename += front ? "F" : "B";
                filename += ".mtmp";
            }
            loadFile(dir + "/robotSaves/" + filename);
        }
    }


    // Manage TimerTasks
    private void timerSchedule() {
        time.schedule(dropDown, 0);
        time.schedule(getTheColor, 1500);
        time.schedule(raisePartial, 2500);
        time.schedule(moveBack, 3000);
        time.schedule(raise, 3500);
        time.schedule(run, 4000);
    }

    private void timerInit() {
        time = new Timer();
        dropDown = new TimerTask() {
            public void run() {
                OpModeGeneral.jewelExtender.setPosition(1);
            }
        };

        getTheColor = new TimerTask() {
            public void run() {
                if (red) {
                    if (OpModeGeneral.isRed(OpModeGeneral.jewelColor))
                        OpModeGeneral.jewelHitter.setPosition(0.7);
                    else
                        OpModeGeneral.jewelHitter.setPosition(0.3);
                }
                else {
                    if (OpModeGeneral.isRed(OpModeGeneral.jewelColor))
                        OpModeGeneral.jewelHitter.setPosition(0.3);
                    else
                        OpModeGeneral.jewelHitter.setPosition(0.7);
                }
            }
        };

        moveBack = new TimerTask() {
            public void run() {
                OpModeGeneral.jewelHitter.setPosition(0.5);
            }
        };

        raise = new TimerTask() {
            public void run() {
                OpModeGeneral.jewelExtender.setPosition(0.35);
            }
        };

        raisePartial = new TimerTask() {
            public void run() {
                OpModeGeneral.jewelExtender.setPosition(0.5);
            }
        };

        run = new TimerTask() {
            public void run() {
                ready = true;
            }
        };
    }


    // Load Files
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

    private void loadConfig () {
        try {
            FileReader fileReader = new FileReader(dir + "/robotSaves/config.cfg");
            BufferedReader reader = new BufferedReader(fileReader);
            String line;
            int i = 0;
            while((line = reader.readLine()) != null) {
                if (i == 0) front = (line.equals("1"));
                else red = (line.equals("1"));
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


    // OpMode Control
    public void init() {
        loadConfig();
        OpModeGeneral.allInit(hardwareMap);
        OpModeGeneral.encoderMode(false);
        OpModeGeneral.jewelHitter.setPosition(0.5);
        OpModeGeneral.jewelExtender.setPosition(0.1);
        OpModeGeneral.grabberL.setPosition(0);
        OpModeGeneral.grabberR.setPosition(1);
        OpModeGeneral.grabberLB.setPosition(1);
        OpModeGeneral.grabberRB.setPosition(0);
        timerInit();
    }

    public void start() {
        LoadThread.start();
        timerSchedule();
    }

    public void loop() {
        if (ready) {
            telemetry.addData("Tick", k);
            telemetry.addData("Power", OpModeGeneral.leftFront.getDirection());
            if (!mpoints.isEmpty()) {
                moveEncoder(mpoints.poll());
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                OpModeGeneral.stopAllMotors();
            }
            k++;
        }
    }


}
