package com.doan.covua.states;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import com.doan.covua.R;


public class SplashScreen extends Activity {

    protected boolean _active = false;
    protected boolean isSound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        Bundle receiveIsSound = this.getIntent().getExtras();

        // nhan du lieu tu man hinh soundquestion
        isSound = receiveIsSound.getBoolean("isSound");

        // chuyen vao mainmenu
        Thread mainmenuThread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!_active) {
                        sleep(100);
                    }
                } catch (InterruptedException e) {
                } finally {
                    // finish();
                    Intent mainmenu = new Intent(SplashScreen.this, MainMenu.class);
                    SplashScreen.this.startActivity(mainmenu);
                    SplashScreen.this.finish();
                    stop();
                }
            }
        };
        mainmenuThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            _active = true;
        }
        return true;
    }

}
