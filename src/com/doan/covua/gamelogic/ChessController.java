package com.doan.covua.gamelogic;

import android.util.Log;

import com.doan.covua.GUIInterface;

public class ChessController {

	PgnToken.PgnTokenReceiver gameTextListener;
	Game game;
	GUIInterface gui;
	public ChessController(GUIInterface gui, PgnToken.PgnTokenReceiver gamTextListener){
		this.gui = gui;
		this.gameTextListener = gamTextListener;
	}
	public final void newGame(){
		game = new Game(gameTextListener);
	}
	public final void startGame() {
		updateUI();
	}
	private final void updateUI(){
		try{
		gui.setPosition(game.currPos(), null, null);
		}catch(Exception e){
			Log.v("update UI...","" + e);
		}
	}
}
