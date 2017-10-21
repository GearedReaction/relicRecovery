package General.DataType;

import java.io.*;

import General.DataType.Vector2;

/**
 * Created by bryanperkins on 1/1/01.
 */
public class MotionPoint implements Serializable{


    public Vector2 vec;
    public int order;


    public MotionPoint (Vector2 _vec, int _order)
    {
        vec = _vec;
        order = _order;
    }


}