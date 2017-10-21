package General.DataType;

import java.io.Serializable;

/**
 * Created by bryanperkins on 1/1/01.
 */
public class Vector2 implements Serializable {
    public float x;
    public float y;
    public float rot;

    public Vector2 (float _x, float _y, float _rot)
    {
        x = _x; y = _y; rot = _rot;
    }

}