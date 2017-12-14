package Opmodes.OnTheFly;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import General.DataType.MotionPoint;
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
    private boolean redORblue = false;
    private boolean frontORback = true;
    private String filename = "";
    private File dir = FtcRobotControllerActivity.context.getFilesDir();


    public void init() { OpModeGeneral.allInit(hardwareMap); }

    public void start() {
        int pictograph = OpModeGeneral.pictoSensor.getVuMark();
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
        motionPoints = loadFile(dir + "/robotSaves/" + filename);

        startTimeSinceEpoch = System.currentTimeMillis();
        milliseconds = 0;
    }

    int i = 0;
    public void loop()
    {
        if (i < OnTheFlyCreator.RES) {
            if (milliseconds >= (i + 1) * (30000 / OnTheFlyCreator.RES)) {
                i++;
            }
        }
        if (i >= 1) {
            MotionPoint currentPoint = motionPoints.get(i - 1);
            Vector2 vec = currentPoint.vec;
            OpModeGeneral.mecanumMove(vec.x, vec.y, vec.rot, false);
            OpModeGeneral.grabber.setPosition(currentPoint.grabber);
            milliseconds = System.currentTimeMillis() - startTimeSinceEpoch;
            telemetry.addData("Time:", milliseconds);
            telemetry.addData("i", i);
        }
    }

    private List<MotionPoint> loadFile(String fileName)
    {
        List<MotionPoint> movement = new ArrayList<MotionPoint>();
        String line = null;
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader reader = new BufferedReader(fileReader);

            while((line = reader.readLine()) != null) {
                //M:x,y,r,o
                if (line.startsWith("M:"))
                {
                    line = line.substring(2);
                    float x = Float.parseFloat(line.substring(0,5));
                    float y = Float.parseFloat(line.substring(6,11));
                    float rot = Float.parseFloat(line.substring(12,17));
                    float grabber = Float.parseFloat(line.substring(18,23));
                    int order = Integer.parseInt(line.substring(24));
                    MotionPoint mp = new MotionPoint(new Vector2(x,y,rot),grabber,order);
                    movement.add(mp);
                }
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println(e.getStackTrace());
        }
        catch (IOException e)
        {
            System.out.println(e.getStackTrace());
        }

        return movement;
    }
}
