package General.DataType;

import java.io.*;

import General.DataType.Vector2;

/**
 * Created by brianperkins on 6/6/06.
 */
public class MotionPoint implements Serializable{


    public Vector2 vec;
    public float grabber;
    public int order;


    public MotionPoint (Vector2 _vec, float _grabber, int _order)
    {
        vec = _vec;
        order = _order;
        grabber = _grabber;
    }


}