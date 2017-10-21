package Opmodes.Testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import General.Utility.OpModeGeneral;

/**
 * Created by union on 11/11/16.
 */


@TeleOp(name = "Write Encoder Values", group = "TeleOp")

public class WriteEncoderValues extends OpMode {

    @Override
    public void init()
    {
        OpModeGeneral.motorInit(hardwareMap);
        OpModeGeneral.resetDriveEncoders(true);
    }

    public void loop()
    {

        OpModeGeneral.mecanumMove(gamepad1.y ? 0.25 : 0,gamepad1.x ? 0.25 : 0 ,0, false);
        telemetry.addData("topLeft: ",OpModeGeneral.left1.getCurrentPosition());
        telemetry.addData("bottomRight: ",OpModeGeneral.right2.getCurrentPosition());
        telemetry.addData("topRight: ",OpModeGeneral.right1.getCurrentPosition());
        telemetry.addData("bottomLeft: ",OpModeGeneral.left2.getCurrentPosition());

        if (gamepad1.start)
        {
            OpModeGeneral.resetDriveEncoders(true);
        }

    }
}
