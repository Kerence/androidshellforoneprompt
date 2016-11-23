package demo.libsimterm.kimi.com.demo;

import android.app.Activity;
import android.os.Bundle;

import com.kimi.libsimterm.TermSessionCommandUtil;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init in main thread
        TermSessionCommandUtil.getInstance(MainActivity.this);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                //this demo takes snapshot every 10 seconds without always prompting toast that app has been authorized as root
                TermSessionCommandUtil.getInstance(MainActivity.this).exec("screencap -p /sdcard/a.png");
            }
        },1000,10000);
    }
}
