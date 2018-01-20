package Devices.Drivers;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;
import java.lang.String;

/**
 * Created by admin on 4/13/2017.
 */

public class ModernRoboticsRGB {

    byte[] colorAcache;
    byte[] colorCcache;

    I2cDevice colorA;
    static I2cDeviceSynch colorAreader;
    I2cDeviceSynch colorCreader;

    boolean touchState = false;  //Tracks the last known state of the touch sensor
    boolean LEDState = true;


    public ModernRoboticsRGB (HardwareMap hardwareMap, String name, int I2CAddress)
    {
        colorA = hardwareMap.i2cDevice.get(name);
        colorAreader = new I2cDeviceSynchImpl(colorA, I2cAddr.create8bit(I2CAddress), false);
        colorAreader.engage();
    }

    public int getColorNum()
    {
        return read(0x09);
    }
    public FieldColor getColorEnum()
    {
        int a = getColorNum();
        if (a == 58) return FieldColor.TILEGRAY;
        if (a == 62) return FieldColor.WHITETAPE;
        if (a == 7) return FieldColor.BLUEPARTICLE;
        if (a == 48) return FieldColor.REDCENTERVORTEX;
        if (a == 56) return FieldColor.YELLOWCENTERVORTEX;
        if (a == 3) return FieldColor.BEACONBLUE;
        if (a == 10) return FieldColor.BEACONRED;

        int b = getColorValue();
        if (b == 16 || b == 15) return FieldColor.WHITETAPE;
        return FieldColor.CLEAR;
    }

    public int getColorValue()
    {
        return read(0x04);
    }
    public int red()
    {
        return read(0x05);
    }
    public int green()
    {
        return read(0x06);
    }
    public int blue()
    {
        return read(0x07);
    }

    public void toggleActive()
    {
        command(0x00);
    }
    public void togglePassive()
    {
        command(0x01);
    }
    public void whiteBalance()
    {
        command(0x43);
    }
    public void blackBalance()
    {
        command(0x43);
    }

    private void command(int cmdValue)
    {
        colorAreader.write8(0x03, cmdValue);
    }
    public int read(int location) { return colorAreader.read(location, 1)[0] & 0xFF; }


}
