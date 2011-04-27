package com.doan.covua.states;


import java.nio.channels.AlreadyConnectedException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.doan.covua.R;


public class MainMenu extends Activity implements OnClickListener{

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);
        
        //set action listener
        View newBtn = findViewById(R.id.btn_newgame);
        newBtn.setOnClickListener(this);
        
        View optionBtn = findViewById(R.id.btn_option);
        optionBtn.setOnClickListener(this);
        
        View aboutBtn = findViewById(R.id.btn_about);
        aboutBtn.setOnClickListener(this);
        
        View exitBtn = findViewById(R.id.btn_exit);
        exitBtn.setOnClickListener(this);
    }
    
    // do something when click on menu
    public void onClick(View v){
    	
    	switch (v.getId()) {
    	case R.id.btn_newgame:
    		//openNewGameDialog();
    		Intent imode = new Intent (this, ChooseGameMode.class);
    		startActivity(imode);
    		break;
    	case R.id.btn_about:
    		Intent i = new Intent (this, About.class);
    		startActivity(i);
    		break;
		case R.id.btn_exit:
			finish();
			break;

		default:
			break;
		}
    }
   
}
