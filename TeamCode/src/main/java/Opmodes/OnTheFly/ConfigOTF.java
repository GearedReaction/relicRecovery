package Opmodes.OnTheFly;

import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

import General.DataType.MotionPoint;

/**
 * Created by admin on 12/16/2017.
 */

@TeleOp (name="OnTheFly Configure", group = "OnTheFly")
public class ConfigOTF extends OpMode {

    private boolean redORblue = true;
    private boolean frontORback = false;
    private boolean saveReady = false;

    private File fileDir = Environment.getExternalStorageDirectory();


    public void init() {}
    public void loop() {
        if (!saveReady) {
            telemetry.addData("redORblue", redORblue);
            telemetry.addData("frontORback", frontORback);
            if (gamepad1.dpad_up) {
                frontORback = true;
            }
            if (gamepad1.dpad_down) {
                frontORback = false;
            }
            if (gamepad1.dpad_left) {
                redORblue = false;
            }
            if (gamepad1.dpad_right) {
                redORblue = true;
            }
            if (gamepad1.a) {
                saveReady = true;
            }
        }
        else
        {
            FileWriter obj = null;
            File output;
            BufferedWriter buffered;
            try {
                File dir = new File(fileDir + "/robotSaves/");
                if (!(dir.exists() && dir.isDirectory())) {
                    dir.mkdirs();
                }
                output = new File(fileDir + "/robotSaves/config.cfg");
                obj = new FileWriter(output);
                buffered = new BufferedWriter(obj);
                buffered.write(frontORback ? "1" : "0");
                buffered.newLine();
                buffered.write(redORblue ? "1" : "0");
                buffered.newLine();
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
