package Opmodes.Competition.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.util.Timer;
import java.util.TimerTask;

import General.Utility.OpModeGeneral;


@TeleOp(name = "Drive", group = "Competition")
public class Drive extends OpMode {

    //Values used to calculate power

    private boolean _lastAButton = false;
    private boolean launchReady = true;

    TimerTask launch = new TimerTask() {
        @Override
        public void run() {
            OpModeGeneral.catapult.setPower(0);
            launchReady = true;
            return;
        }
    };


    public void init()
    {
        OpModeGeneral.motorInit(hardwareMap);
    }

    public void loop()
    {

        OpModeGeneral.mecanumMove(-gamepad1.left_stick_x, -gamepad1.left_stick_y, -gamepad1.right_stick_x, negated);
        //Trigger
        if (gamepad1.a & !_lastAButton) { triggerReverse(); }
        if (gamepad1.a) { _lastAButton = true; }
        else { _lastAButton = false; }
        OpModeGeneral.combine.setPower(gamepad2.right_stick_y);
        OpModeGeneral.catapult.setPower(-gamepad2.left_stick_y);

        if (gamepad2.dpad_up)
        {
            OpModeGeneral.lifter.setPower(1);
        }
        else if (gamepad2.dpad_down) {
            OpModeGeneral.lifter.setPower(-1);
        }
        else { OpModeGeneral.lifter.setPower(0); }


    }
    Timer timer = new Timer();
    public void shoot()
    {
        if (launchReady) {
            timer = new Timer();
            launch.cancel();
            launchReady = false;
            OpModeGeneral.catapult.setPower(1);
            timer.schedule(launch, 320);
        }

    }

    private boolean negated;
    public void triggerReverse ()
    {
        negated = !negated;
    }
}