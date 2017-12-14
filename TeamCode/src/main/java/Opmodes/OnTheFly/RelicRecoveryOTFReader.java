package Opmodes.OnTheFly;


import General.Utility.OpModeGeneral;

/**
 * Created by union on 17年12月11日.
 */

public class RelicRecoveryOTFReader extends OnTheFlyReader {

    @Override
    public void init()
    {
        int pictograph = OpModeGeneral.pictoSensor.getVuMark();
    }


}
