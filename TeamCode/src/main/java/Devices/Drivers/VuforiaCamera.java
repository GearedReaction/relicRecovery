package Devices.Drivers;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by union on 17年12月9日.
 */

public class VuforiaCamera {

    private static final  String LICENSE =
            "AdXxjOr/////AAAAGaTOECMge0kljpAvPDaE94QLITwBxL81LPY/QwXj0fEr6XU/EpDGIZFss7hqicgvQET9FURLVVHug2S2y0y2n45jCrrMgSUjJk++5R2O37U/tfsqpQiO1eoFVUOID17w9N2dhJUUxof38LLdXliLoFZqcdG+cIM7UFqZSz5slbB7KlywYnJTu6Akzi9aRKxlYDnfGI13XvTSv+aORr5/v+vE4Xyi00fMjv0/tRrmD9AwAVbIdvZM7xxs+yUi0dXG4TPiBELPJlPD0VO0dnJ/lt1BgzHLiFBVddtUqvxRcru4PZOOyurCPmG4QZmdwRFxMeLy3yrbN+MZuv2PZvqvHPOKhUzMCn7QytMrXKGGADxa";

    private VuforiaLocalizer localizer;
    private VuforiaTrackables trackable;



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
        return relicTrackables;
    }

    public VuforiaCamera(HardwareMap hardwareMap)
    {
        this(hardwareMap, VuforiaLocalizer.CameraDirection.BACK);
    }

    public VuforiaCamera(HardwareMap hardwareMap, VuforiaLocalizer.CameraDirection direction)
    {
        localizer = registerCamera(hardwareMap, direction);
        trackable = getTracker(localizer);
    }

    public void initTracker()
    {
        trackable.activate();
    }

    /**
     * Returns an integer value based on the VuMark
     * 0 = left
     * 1 = center
     * 2 = right
     * @return
     */
    public int getVuMark()
    {
        VuforiaTrackable relicTemplate = trackable.get(0);
        relicTemplate.setName("relicVuMarkTemplate");
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        if (vuMark.toString() == "LEFT") return 0;
        else if (vuMark.toString() == "CENTER") return 1;
        else if (vuMark.toString() == "RIGHT") return 2;
        else return -1;
    }


}
