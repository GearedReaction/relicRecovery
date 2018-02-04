package General.DataType;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by union on 18年2月3日.
 */

public class CheckDirection {
    public int index;
    public int max = 0;
    public int min = 0;
    public boolean up;
    public boolean tempUp;
    public DcMotor motor;
    public int previous;

    public CheckDirection (int index, DcMotor motor)
    {
        this.index = index;
        this.motor = motor;
    }

}
