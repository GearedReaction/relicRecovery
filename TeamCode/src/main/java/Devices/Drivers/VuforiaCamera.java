package Devices.Drivers;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by union on 17年12月9日.
 */

public class VuforiaCamera {

    private static final  String LICENSE =
            "AdXxjOr/////AAAAGaTOECMge0kljpAvPDaE94QLITwBxL81LPY/QwXj0fEr6XU/EpDGIZFss7hqicgvQET9FURLVVHug2S2y0y2n45jCrrMgSUjJk++5R2O37U/tfsqpQiO1eoFVUOID17w9N2dhJUUxof38LLdXliLoFZqcdG+cIM7UFqZSz5slbB7KlywYnJTu6Akzi9aRKxlYDnfGI13XvTSv+aORr5/v+vE4Xyi00fMjv0/tRrmD9AwAVbIdvZM7xxs+yUi0dXG4TPiBELPJlPD0VO0dnJ/lt1BgzHLiFBVddtUqvxRcru4PZOOyurCPmG4QZmdwRFxMeLy3yrbN+MZuv2PZvqvHPOKhUzMCn7QytMrXKGGADxa";

    private VuforiaLocalizer registerCamera(HardwareMap hardwareMap, VuforiaLocalizer.CameraDirection direction, String licenseID)
    {
        VuforiaLocalizer vuforia;
        int cameraMonitorViewId = hardwareMap.appContext.getResources()
                .getIdentifier(
                        "cameraMonitorViewId",
                        "id",
                        hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = licenseID;
        parameters.cameraDirection = direction;
        vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        return vuforia;
    }

    private VuforiaLocalizer registerCamera(HardwareMap hardwareMap, VuforiaLocalizer.CameraDirection direction)
    {
        return registerCamera(hardwareMap, direction, LICENSE);
    }

    private VuforiaTrackables getTracker(VuforiaLocalizer localizer)
    {
        VuforiaTrackables relicTrackables = localizer.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");
        return relicTrackables;
    }


}
