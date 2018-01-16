package General.DataType;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by brianperkins on 6/6/06.
 */
public class MotionPoint{

    public List<MotorPoint> points = new ArrayList<>();

    public MotionPoint(List<MotorPoint> _points)
    {
        points = _points;
    }

    public MotionPoint() {}
}