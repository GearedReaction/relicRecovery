package Opmodes.OnTheFly;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.vuforia.Image;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import General.DataType.MotionPoint;
import General.DataType.MotorPoint;
import General.DataType.MovingPart;
import General.DataType.RGB;
import General.DataType.Vector2;
import General.Utility.OpModeGeneral;

/**
 * Created by onion on 2/7/17.
 */

@Autonomous (name = "On The Fly Reader", group = "OnTheFly" )

public class OnTheFlyReader extends OpMode {

    List<MotionPoint> motionPoints;
    private long milliseconds;
    private long startTimeSinceEpoch;
    private boolean redORblue = true;
    private boolean frontORback = true;
    private String filename = "";
    private File dir = Environment.getExternalStorageDirectory();
    private boolean fileNotFound = false;


    public void init() {
        OpModeGeneral.motorInit(hardwareMap);
        OpModeGeneral.servoInit(hardwareMap );
        OpModeGeneral.cameraInit(hardwareMap);
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
        startTimeSinceEpoch = System.currentTimeMillis();
        milliseconds = 0;
    }

    int i = 0;
    public void loop()
    {
        if (!fileNotFound) {
            if (i < OnTheFlyCreator.RES) {
                if (milliseconds >= i * (30000 / motionPoints.size() - 1)) {
                    i++;
                }
            }
            if (i >= 0 && i < motionPoints.size() - 1) {
                MotionPoint currentPoint = motionPoints.get(i);
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

            if (i >= motionPoints.size()) {
                OpModeGeneral.mecanumMove(0, 0, 0, false);
            }
        }
    }

    private List<MotionPoint> loadFile(String fileName)
    {
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

    private void loadConfig ()
    {
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


}
