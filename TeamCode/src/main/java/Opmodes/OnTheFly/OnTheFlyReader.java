package Opmodes.OnTheFly;


import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private Thread inputThread = new Thread(new UpdateThread());
    private List<MotionPoint> motionPoints;
    private boolean fileNotFound = false;
    private boolean frontORback = true;
    private boolean redORblue = true;
    private String filename = "";
    private int i = 0;


    private List<MotionPoint> loadFile(String fileName) {
        List<MotionPoint> movement = new ArrayList<MotionPoint>();
        String line;
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader reader = new BufferedReader(fileReader);

            while((line = reader.readLine()) != null) {
                List<MotorPoint> outputs = new ArrayList<>();
                if (line.startsWith("M:"))
                {
                    List<String> strings = Arrays.asList(line.split("\\s*|\\s*"));
                    for (String s : strings)
                    {
                        MotorPoint mpt = new MotorPoint();
                        List<String> stringyboi = Arrays.asList(s.split("\\s*:\\s*"));
                        if (stringyboi.size() == 3)
                        {
                            if (stringyboi.get(0).equals("M")) mpt.part = MovingPart.MOTOR;
                            else mpt.part = MovingPart.SERVO;

                            mpt.name = stringyboi.get(1);
                            mpt.value = Float.parseFloat(stringyboi.get(2));
                        }
                        outputs.add(mpt);
                    }
                }
                 movement.add(new MotionPoint(outputs));
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

        return movement;
    }

    private class UpdateThread implements Runnable {

        @Override
        public void run(){
            while (true)
            {
                try {
                    if (i < motionPoints.size()) {
                        drivePoint(i);
                        inputThread.sleep(30000 / motionPoints.size());
                        i++;
                    }
                    else {
                        OpModeGeneral.stopAllMotors();
                        break;
                    }
                }
                catch (InterruptedException e) {
                    telemetry.addData("ERROR", e.getStackTrace());
                }
            }
        }
    }

    private void drivePoint(int val) {
        MotionPoint currentPoint = motionPoints.get(val);
        for (MotorPoint m : currentPoint.points)
        {
            switch (m.part)
            {
                case MOTOR:
                    DcMotor mtr = hardwareMap.dcMotor.get(m.part.name());
                    mtr.setPower(m.value);
                    break;
                case SERVO:
                    Servo srv = hardwareMap.servo.get(m.part.name());
                    srv.setPosition(m.value);
                    break;
            }
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

    public void start() {
        //Determine which column to use
        loadConfig();
        int pictograph = OpModeGeneral.camera.getVuMark();
        if (pictograph == -1) filename = "test.mtmp";
        else
        {
            if (pictograph == 0) filename += "Left";
            else if (pictograph == 1) filename += "Center";
            else if (pictograph == 2) filename += "Right";
            filename += redORblue ? "Red" : "Blue";
            filename += frontORback ? "F" : "B";
            filename += ".mtmp";
        }
        telemetry.addData("File",filename);
        motionPoints = loadFile(dir + "/robotSaves/" + filename);
        if (motionPoints == null) fileNotFound = true;

    }

    public void init() {
        OpModeGeneral.motorInit(hardwareMap);
        OpModeGeneral.servoInit(hardwareMap );
        OpModeGeneral.cameraInit(hardwareMap);
    }

    public void loop() {}


}
