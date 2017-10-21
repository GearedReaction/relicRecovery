package Opmodes.Competition.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import General.Utility.DataLogger;
import General.Utility.OpModeGeneral;

/**
 * Created by admin on 4/13/2017.
 */

@Autonomous (name = "Encoder Based Autonomous", group = "Competition")

public class EncoderBasedAutnomous extends OpMode {

    //Distances in feet
    public float distanceToBeacon = 0;
    public float distanceToSecondSensor = 0;
    public float distanceToCenterBall = 0;

    public void init()
    {
        OpModeGeneral.allInit(hardwareMap);
    }

    public void start()
    {

    }

    public void stop()
    {

    }


    public void loop() {

    }


}
