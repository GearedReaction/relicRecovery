package Opmodes.Testing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

import java.nio.ByteBuffer;

import General.DataType.RGB;
import General.Subclasses.VuforiaLocalizerImplSubclass;
import General.Utility.OpModeGeneral;


/**
 * Created by admin on 12/16/2017.
 */

public class BallTest extends OpMode {

    public void init()
    {
        OpModeGeneral.cameraInit(hardwareMap);
    }

    public void start() {




    }
    int i = 0;
    public void loop() {
        i++;
        if (i % 50 == 0) {
//            VuforiaLocalizerImplSubclass vuforia = OpModeGeneral.camera.localizer;
//            Bitmap bmp = null;
//            if (vuforia.rgb != null) {
//                bmp = Bitmap.createBitmap(vuforia.rgb.getWidth(), vuforia.rgb.getHeight(), Bitmap.Config.RGB_565);
//                bmp.copyPixelsFromBuffer(vuforia.rgb.getPixels());
//            }
//            Bitmap[][] bitmaps = OpModeGeneral.splitBitmap(bmp, 2, 1);
//            Bitmap left = bitmaps[0][0];
//            Bitmap right = bitmaps[1][0];
//            RGB lavg = OpModeGeneral.calculateAverageColor(left);
//            RGB ravg = OpModeGeneral.calculateAverageColor(right);
//
//            telemetry.addData("GScaleL",OpModeGeneral.getGreyscaleCount(left));
//            telemetry.addData("PixelL", left.getWidth()*left.getHeight());
//            telemetry.addData("GScaleR",OpModeGeneral.getGreyscaleCount(right));
//            telemetry.addData("PixelR", right.getWidth()*right.getHeight());
//            telemetry.addData("LR",lavg.R);
//            telemetry.addData("LG",lavg.G);
//            telemetry.addData("LB",lavg.B);
//            telemetry.addData("RR",ravg.R);
//            telemetry.addData("RG",ravg.G);
//            telemetry.addData("RB",ravg.B);
        }

    }
}
