package General.Utility;

/**
 * Created by union on 17年10月28日.
 */

public class Utilities {

    public static double squareWithNegative(double num)
    {
        return num > 0 ? Math.pow(num, 2) : -Math.pow(num,2);
    }

}
