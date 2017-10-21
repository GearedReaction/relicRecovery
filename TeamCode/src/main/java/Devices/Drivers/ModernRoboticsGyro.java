package Devices.Drivers;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

/**
 * Created by admin on 4/14/2017.
 */

public class ModernRoboticsGyro {

    I2cDevice gyro;
    I2cDeviceSynch gyroReader;

    byte[] gyroCache;


    public ModernRoboticsGyro (HardwareMap hardwareMap, String name, int I2CAddress) {

        gyro = hardwareMap.i2cDevice.get(name);
        gyroReader = new I2cDeviceSynchImpl(gyro,  I2cAddr.create8bit(I2CAddress), false);
        gyroReader.engage();
        resetHeading();
    }

    public int getHeadingData ()
    {
        byte[] lsb = gyroReader.read(0x04, 1);
        byte[] msb = gyroReader.read(0x05, 1);

        return (lsb[0] & 0xFF) + ((msb[0] & 0xFF) << 8);
    }
    public void resetHeading ()
    {
        gyroReader.write8(0x03, 0x52);
    }
}
