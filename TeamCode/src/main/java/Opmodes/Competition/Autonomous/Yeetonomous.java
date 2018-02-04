package Opmodes.Competition.Autonomous;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.configuration.Utility;

import Devices.Drivers.FieldColor;
import General.Utility.OpModeGeneral;

/**
 * Created by ironOwlKing on 2/4/2017.
 */
@Autonomous(name = "", group = "Competition")

public class Yeetonomous extends OpMode {

    int stage = 1;
    boolean isBlue = true;
    float currentTime;

    public void init()
    {
        OpModeGeneral.allInit(hardwareMap);
        currentTime = System.currentTimeMillis();
    }

    public void loop() {


        telemetry.addData("stage", stage);
        switch (stage) {
            case 1:
                if (System.currentTimeMillis() - currentTime < 1000){
                    OpModeGeneral.mecanumMove(0,1,0, false, 1);
                }
                else {
                    OpModeGeneral.mecanumMove(0, 1, 0, false, 0.1f);
                }
            case 2:

                }

//            case 3:
//                if (System.currentTimeMillis() - currentTime < 1000) {
//                    move(1, 0, 0, false);
//                } else if (colorMid.red() < 280) {
//                    stage++;
//                    currentTime = System.currentTimeMillis();
//                }
//            case 4:
//                if (colorBeacon.red() > 680) {
//                    if (isBlue) {
//                        //Right
//                        if (System.currentTimeMillis() - time < 100) {
//                            move(-1, 0, 0, false);
//                        } else if (System.currentTimeMillis() > 100 && System.currentTimeMillis() < 500) {
//                            move(0, 1, 0, false);
//                        } else if (System.currentTimeMillis() > 500 && System.currentTimeMillis() < 900) {
//                            move(0, -1, 0, false);
//                        } else {
//                            stage++;
//                        }
//                    } else {
//                        //Left
//                        if (System.currentTimeMillis() - time < 100) {
//                            move(1, 0, 0, false);
//                        } else if (System.currentTimeMillis() > 100 && System.currentTimeMillis() < 500) {
//                            move(0, 1, 0, false);
//                        } else if (System.currentTimeMillis() > 500 && System.currentTimeMillis() < 900) {
//                            move(0, -1, 0, false);
//                        } else {
//                            stage++;
//                        }
//                    }
//                }
//                if (colorBeacon.blue() > 2000) {
//                    if (isBlue) {
//                        //Left
//                        if (System.currentTimeMillis() - time < 100) {
//                            move(1, 0, 0, false);
//                        } else if (System.currentTimeMillis() > 100 && System.currentTimeMillis() < 500) {
//                            move(0, 1, 0, false);
//                        } else if (System.currentTimeMillis() > 500 && System.currentTimeMillis() < 900) {
//                            move(0, -1, 0, false);
//                        } else {
//                            stage++;
//                        }
//                    } else {
//                        //Right
//                        if (System.currentTimeMillis() - time < 100) {
//                            move(-1, 0, 0, false);
//                        } else if (System.currentTimeMillis() > 100 && System.currentTimeMillis() < 500) {
//                            move(0, 1, 0, false);
//                        } else if (System.currentTimeMillis() > 500 && System.currentTimeMillis() < 900) {
//                            move(0, -1, 0, false);
//                        } else {
//                            stage++;
//                        }
//                    }
//                }
//
//        }
        }


    }
