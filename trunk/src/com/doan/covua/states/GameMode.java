package com.doan.covua.states;

import com.doan.covua.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class GameMode extends Activity implements OnClickListener{
	
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamemode);
      
        View cwhBtn = findViewById(R.id.btn_cwh);
        cwhBtn.setOnClickListener(this);
        
        View hwhBtn = findViewById(R.id.btn_hwh);
        hwhBtn.setOnClickListener(this);
       
	}
	 
	 public void onClick(View v){
		 
		 switch (v.getId()) {
		 case R.id.btn_cwh:
			 openNewGameDialog();
			 break;
		 case R.id.btn_hwh:
			 break;
		 }
	 }
	 private void openNewGameDialog(){
	    	new AlertDialog.Builder(this).setTitle(R.string.difficulty_title).setItems(R.array.difficulty, 
	    			new DialogInterface.OnClickListener() {						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							startInGame(which);
						}
					})
					.show();
	 }
	 public void startInGame(int i){
		 Intent intent = new Intent(this, InGame.class);
		 intent.putExtra(InGame.KEY_DIFFICULTY, i);
		 startActivity(intent);
	 }
}
