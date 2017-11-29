package Opmodes.OnTheFly;

import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
    public static final int RES = 10;
    private long milliseconds;
    private long startTimeSinceEpoch;

    public void init()
    {
        OpModeGeneral.allInit(hardwareMap);
    }

    public void start() {

        try {
            String directory = FtcRobotControllerActivity.context.getFilesDir() + "/robotSaves/" + "current.mtmp";
            FileInputStream stream = new FileInputStream(directory);
            ObjectInputStream iStream = new ObjectInputStream(stream);
            motionPoints = (List<MotionPoint>) iStream.readObject();
            iStream.close();
            stream.close();
        }

        catch (FileNotFoundException FNFE) {
            telemetry.addData("FILE IS NOT FOUND", 1);
            System.out.println(FNFE.getStackTrace());
        }
        catch (IOException IOE) {
            telemetry.addData("IO EXCEPTION", 2);
            System.out.println(IOE.getStackTrace());
        }
        catch (ClassNotFoundException CNFE)
        {
            telemetry.addData("CLASS NOT FOUND EXCEPTION", 3);
            System.out.println(CNFE.getStackTrace());
        }

    }
    int i = 0;
    public void loop()
    {
        if (i < RES) {
            if (milliseconds >= (i + 1) * (30000 / RES)) {
                i++;
            }
        }
        if (i >= 1) {
            MotionPoint currentPoint = motionPoints.get(i - 1);
            Vector2 vec = currentPoint.vec;
            OpModeGeneral.mecanumMove(vec.x, vec.y, vec.rot, false);
            milliseconds = System.currentTimeMillis() - startTimeSinceEpoch;
            telemetry.addData("Time:", milliseconds);
            telemetry.addData("i", i);
        }

    }
}
