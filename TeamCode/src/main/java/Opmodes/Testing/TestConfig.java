package Opmodes.Testing;

import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import General.Utility.OpModeGeneral;

/**
 * Created by union on 18年2月9日.
 */

@TeleOp(name = "TestConfig", group = "Test")
public class TestConfig extends OpMode {

    private File dir = Environment.getExternalStorageDirectory();
    private boolean front;
    private boolean red;

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

    public void init(){
        loadConfig();
    }

    public void loop(){
        telemetry.addData("red", red ? "True" : "False");
        telemetry.addData("front", front ? "True" : "False");
    }
}
