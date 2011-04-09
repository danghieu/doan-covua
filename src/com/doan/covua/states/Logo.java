package com.doan.covua.states;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.doan.covua.R;


public class Logo extends Activity {
    // protected boolean _active = true;
    protected int _logoTime = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo);

        // Thread for display logo
        Thread logoThread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (waited < _logoTime) {
                        sleep(100);
                        waited += 100;
                    }
                } catch (InterruptedException e) {
                    // do no thing
                } finally {
                    // ket thuc state Logo va chuyen sang state moi.
                    Intent mainIntent = new Intent(Logo.this, SoundQuestion.class);
                    Logo.this.startActivity(mainIntent);
                    Logo.this.finish();
                    stop();
                }
            }

        };
        logoThread.start();
    }
}
