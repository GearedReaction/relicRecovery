package Opmodes.OnTheFly;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
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
                Vector2 vec = currentPoint.vec;
                OpModeGeneral.mecanumMove(vec.x, vec.y, vec.rot, false);
                OpModeGeneral.grabber.setPosition(currentPoint.grabber);
                milliseconds = System.currentTimeMillis() - startTimeSinceEpoch;
                telemetry.addData("MPoint", "x: " + currentPoint.vec.x + "y: " + currentPoint.vec.y + "rot: " + currentPoint.vec.rot);
                telemetry.addData("Grabber", currentPoint.grabber);
                telemetry.addData("order", currentPoint.order);
                telemetry.addData("Time:", milliseconds);
                telemetry.addData("i", i);
            }
            if (i >= motionPoints.size()) {
                OpModeGeneral.mecanumMove(0, 0, 0, false);
                OpModeGeneral.grabber.setPosition(0.5);
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
                //M:x,y,r,o
                if (line.startsWith("M:"))
                {
                    line = line.substring(2);
                    List<String> strings = Arrays.asList(line.split("\\s*,\\s*"));
                    if (strings.size() >= 5) {
                        float x = Float.parseFloat(strings.get(0));
                        float y = Float.parseFloat(strings.get(1));
                        float rot = Float.parseFloat(strings.get(2));
                        float grabber = Float.parseFloat(strings.get(3));
                        int order = Integer.parseInt(strings.get(4));
                        MotionPoint mp = new MotionPoint(new Vector2(x,y,rot),grabber,order);
                        movement.add(mp);
                    }
                }
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
