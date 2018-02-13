package Opmodes.OnTheFly;

import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by admin on 12/16/2017.
 */

@TeleOp (name="OnTheFly Configure", group = "OnTheFly")
public class ConfigOTF extends OpMode {

    private File fileDir = Environment.getExternalStorageDirectory();

    private boolean saveReady = false;
    private boolean front = false;
    private boolean red = true;
    private double speed = 0.4;


    public void init() {}
    public void loop() {
        if (!saveReady) {
            telemetry.addData("red", red ? "True" : "False");
            telemetry.addData("front", front ? "True" : "False");
            telemetry.addData("speed", speed);
            if (gamepad1.dpad_up) front = true;
            if (gamepad1.dpad_down) front = false;
            if (gamepad1.dpad_left) red = false;
            if (gamepad1.dpad_right) red = true;
            if (gamepad1.y) speed += 0.0001;
            else if (gamepad1.a) speed -= 0.0001;
            try { Thread.sleep(5); }
            catch (InterruptedException e) {}
            if (gamepad1.start) saveReady = true;
        }
        else
        {
            FileWriter obj = null;
            File output;
            BufferedWriter buffered = null;
            try {
                File dir = new File(fileDir + "/robotSaves/");
                if (!(dir.exists() && dir.isDirectory())) {
                    dir.mkdirs();
                }
                output = new File(fileDir + "/robotSaves/config.cfg");
                obj = new FileWriter(output);
                buffered = new BufferedWriter(obj);
                buffered.write(front ? "1" : "0");
                buffered.newLine();
                buffered.write(red ? "1" : "0");
                buffered.newLine();
                buffered.write(""+speed);
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
                        buffered.close();
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
    }
}
