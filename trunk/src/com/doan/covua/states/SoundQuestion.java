package com.doan.covua.states;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.doan.covua.R;


public class SoundQuestion extends Activity {

    Button buttonNo;
    Button buttonYes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.soundquestion);

        buttonYes = (Button) findViewById(R.id.clickYes);
        buttonNo = (Button) findViewById(R.id.clickNo);
        buttonYes.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Bundle sendIsSound = new Bundle();
                sendIsSound.putBoolean("isSound", true);
                Intent nextActivity = new Intent(SoundQuestion.this, SplashScreen.class);
                nextActivity.putExtras(sendIsSound);
                startActivity(nextActivity);
                finish();
            }
        });
        buttonNo.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Bundle sendIsSound = new Bundle();
                sendIsSound.putBoolean("isSound", false);
                Intent nextActivity = new Intent(SoundQuestion.this, SplashScreen.class);
                nextActivity.putExtras(sendIsSound);
                startActivity(nextActivity);
                finish();
            }
        });
    }
}
