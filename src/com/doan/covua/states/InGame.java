package com.doan.covua.states;



import android.app.Activity;
import android.os.Bundle;
import android.util.Log;


public class InGame extends Activity{
	
	public static final String KEY_DIFFICULTY = "com.doan.covua.states.difficulty";
	public static final int DIFFICULTY_EASY = 0;
	public static final int DIFFICULTY_MEDIUM = 1;
	public static final int DIFFICULTY_HARD = 2;
	
	private ChessView chessview;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		chessview = new ChessView(this);
		setContentView(chessview);
		chessview.requestFocus();
	}
	
	
	
}