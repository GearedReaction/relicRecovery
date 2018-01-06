package Opmodes.Testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import General.Utility.OpModeGeneral;

/**
 * Created by union on 11/11/16.
 */



public class WriteEncoderValues extends OpMode {

    public void init()
    {
        OpModeGeneral.motorInit(hardwareMap);
        OpModeGeneral.resetDriveEncoders(true);
    }

    public void loop()
    {

        OpModeGeneral.mecanumMove(gamepad1.y ? 0.25 : 0,gamepad1.x ? 0.25 : 0 ,0, false);


        if (gamepad1.start)
        {
            OpModeGeneral.resetDriveEncoders(true);
        }

    }
}
