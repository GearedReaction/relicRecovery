package General.DataType;

/**
 * Created by union on 18年1月9日.
 */

public class MotorPoint {

    public MovingPart part;
    public String name;
    public float value;

    public MotorPoint() {}

    public MotorPoint(MovingPart _part, String _name, float _value)
    {
        part = _part;
        name = _name;
        value = _value;
    }
}
