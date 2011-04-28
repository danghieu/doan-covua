package com.doan.covua.gamelogic;

import com.doan.covua.GUIInterface;
import com.doan.covua.gamelogic.Position;;
public class ChessController {

	PgnToken.PgnTokenReceiver gameTextListener;
	Game game;
	GUIInterface gui;
	GameTree tree;
	
	public ChessController(GUIInterface gui, PgnToken.PgnTokenReceiver gamTextListener){
		this.gui = gui;
		this.gameTextListener = gamTextListener;
	}
	public final void newGame(){
		tree = new GameTree(gameTextListener);
	}
	public final void startGame() {
		updateUI();
	}
	private final void updateUI(){
		
		gui.setPosition(currPos(), null, null);
	}
	/** get current position */
	final Position currPos(){
		return tree.currentPos;
	}
}
