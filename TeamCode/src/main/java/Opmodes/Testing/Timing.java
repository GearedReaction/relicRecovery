package Opmodes.Testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import Opmodes.OnTheFly.OnTheFlyCreator;

/**
 * Created by union on 18年2月3日.
 */


@TeleOp(name="TimingTest", group = "Test")
public class Timing extends OpMode {

    private int i = 0;

    private int interval = 1;
    private long nextTime = System.currentTimeMillis() + interval;
    private Thread inputThread = new Thread(new UpdateThread());

    public void start()
    {
        //inputThread.start();
    }

    private class UpdateThread implements Runnable {

        @Override
        public void run(){
            while (true)
            {
//                while (nextTime - System.currentTimeMillis() > 0)
//                    ;
                try {
                    inputThread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
                nextTime += interval;
            }
        }
    }


    public void loop()
    {
        telemetry.addData("Tick", i);
        i++;
    }

    public void init() {}
}
