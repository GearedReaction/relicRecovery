package General.DataType;

import java.io.*;

import General.DataType.Vector2;

/**
 * Created by brianperkins on 6/6/06.
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