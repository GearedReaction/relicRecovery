package Opmodes.OnTheFly;

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
import java.util.List;
import java.io.*;

import General.DataType.MotionPoint;
import General.DataType.Vector2;
import General.Utility.OpModeGeneral;

/**
 * Created by bryan perkins on 1/1/01.
 */
@TeleOp (name = "On The Fly Writer", group = "OnTheFly" )

public class OnTheFlyCreator extends OpMode {

    public static final int RES = 3000;
    private long milliseconds;
    private long startTimeSinceEpoch;
    List<MotionPoint> points = new ArrayList<MotionPoint>();

    public void init()
    {
        OpModeGeneral.motorInit(hardwareMap);
        OpModeGeneral.servoInit(hardwareMap);
    }


    public void start()
    {
        startTimeSinceEpoch = System.currentTimeMillis();
        milliseconds = 0;
    }
    int i = 0;
    public void loop()
    {
        if (milliseconds <= 30000) {
            if (i < RES) {
                if (milliseconds >= (i + 1) * (30000 / RES)) {
                    points.add(new MotionPoint(new Vector2(-gamepad1.left_stick_x, -gamepad1.left_stick_y, -gamepad1.right_stick_x), 1 - (gamepad2.left_stick_x + 1) / 2, i));
                    i++;
                }
            }
            OpModeGeneral.mecanumMove(-gamepad1.left_stick_x, -gamepad1.left_stick_y, -gamepad1.right_stick_x, false);
            OpModeGeneral.grabber.setPosition(1 - (gamepad2.left_stick_x + 1) / 2);
            milliseconds = System.currentTimeMillis() - startTimeSinceEpoch;


            telemetry.addData("Time:", milliseconds);
            telemetry.addData("MPointCount:", points.size());
            telemetry.addData("MPoint:", -gamepad1.left_stick_x + " : " + -gamepad1.left_stick_y + " : " + -gamepad1.right_stick_x);
        }
        else
        {
            FileWriter obj = null;
            File output;
            BufferedWriter buffered;
            try {
                File dir = new File(FtcRobotControllerActivity.context.getFilesDir()+"/robotSaves");
                if (!(dir.exists() && dir.isDirectory()))
                {
                    dir.mkdirs();
                }
                output = new File(FtcRobotControllerActivity.context.getFilesDir() + "/robotSaves/" +"current.mtmp");
                obj =  new FileWriter(output);
                buffered = new BufferedWriter(obj);
                for (MotionPoint mtmp : points) {
                    DecimalFormat format = new DecimalFormat("#.000");
                    String line = String.format("M:"
                            +format.format(mtmp.vec.x)+","
                            +format.format(mtmp.vec.y)+","
                            +format.format(mtmp.vec.rot)+","
                            +format.format(mtmp.grabber)+","
                            +mtmp.order);
                    buffered.write(line);
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
                        obj.close();
                    }
                    catch (IOException io)
                    {
                        telemetry.addData("IO EXCEPTION", 2);
                        System.out.println(io.getStackTrace());
                    }
                }
            }
            stop();
        }
    }
    public void stop()
    {

    }
    public void save()
    {
        FileWriter obj = null;
        File output;
        BufferedWriter buffered;
        try {
            File dir = new File(FtcRobotControllerActivity.context.getFilesDir()+"/robotSaves");
            if (!(dir.exists() && dir.isDirectory()))
            {
                dir.mkdirs();
            }
            output = new File(FtcRobotControllerActivity.context.getFilesDir() + "/robotSaves/" +"current.mtmp");
            obj =  new FileWriter(output);
            buffered = new BufferedWriter(obj);
            for (MotionPoint mtmp : points) {
                DecimalFormat format = new DecimalFormat("#.000");
                String line = String.format("M:"
                        +format.format(mtmp.vec.x)+","
                        +format.format(mtmp.vec.y)+","
                        +format.format(mtmp.vec.rot)+","
                        +format.format(mtmp.grabber)+","
                        +mtmp.order);
                buffered.write(line);
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
                    obj.close();
                }
                catch (IOException io)
                {
                    telemetry.addData("IO EXCEPTION", 2);
                    System.out.println(io.getStackTrace());
                }
            }
        }
    }
}