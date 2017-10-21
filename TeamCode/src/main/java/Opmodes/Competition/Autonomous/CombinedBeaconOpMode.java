package Opmodes.Competition.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.Timer;
import java.util.TimerTask;

import Devices.Drivers.FieldColor;
import General.Utility.OpModeGeneral;
import General.Utility.OpModeTimers;

/**
 * Created by admin on 4/19/2017.
 */
@Autonomous (name = "Beacon Autonomous (Work In Progress)", group = "Competition")

public class CombinedBeaconOpMode extends OpMode {

    public float currentTime = 0;
    public int stage = 0;

    public void init() {
        OpModeGeneral.allInit(hardwareMap);
        OpModeTimers.timerInit();
    }

    public void start()
    {
        OpModeGeneral.mecanumMove(-1,0,0,false);
        OpModeTimers.timerSchedule();
    }


    public void loop()
    {
        telemetry.addData("Current Stage", stage);
        telemetry.addData("Gyro",OpModeGeneral.gyro.getHeadingData());
        telemetry.addData("Enum", OpModeGeneral.colorMid.getColorEnum());
        if (OpModeTimers.stage1Complete) {
            switch (stage) {
                case 0:
                    if (OpModeTimers.stage1Complete) {
                        if (OpModeGeneral.colorMid.getColorEnum().equals(FieldColor.WHITETAPE)) {
                            stage++;
                            currentTime = System.currentTimeMillis();
                            OpModeGeneral.mecanumDriveAngle(-45,1);
                        } else {
                            OpModeGeneral.mecanumMove(1, 0, 0, false, 0.1f);
                        }
                    }
                case 1:
//                    int gyroData = OpModeGeneral.gyro.getHeadingData();
//                    if (gyroData < -47) {
//                        OpModeGeneral.mecanumMove(0, 0, -1, false, 0.2f);
//                    } else if (gyroData > -43) {
//                        OpModeGeneral.mecanumMove(0, 0, 1, false, 0.2f);
//                    } else {
//                        OpModeGeneral.mecanumMove(0, 0, 0, false);
//                        stage++;
//                    }
                    if (System.currentTimeMillis() > currentTime + 3000){
                        stage++;
                    }
                case 2:
                    OpModeGeneral.colorBeacon.togglePassive();
                    if (OpModeGeneral.colorBeacon.getColorEnum() != FieldColor.CLEAR) {
                        OpModeGeneral.mecanumMove(0, 0, 0, false);
                        stage++;
                    } else {
                        OpModeGeneral.mecanumMove(1, 0, 0, false, 0.1f);
                    }
                case 3:
                    //ColorBeacon is on the left
                    if (OpModeGeneral.colorBeacon.red() > OpModeGeneral.colorBeacon.blue()) {
                        telemetry.addData("ColorBeacon shows that red is on the left, triggering left", 0);
                        //Triggering Left Button
                    }
                    if (OpModeGeneral.colorBeacon.red() <= OpModeGeneral.colorBeacon.blue()) {
                        telemetry.addData("ColorBeacon shows that blue is on the left, triggering right", 0);
                        //Trigger Right Button
                    }
                case 4:
                    return;

            }
        }

    }

}
