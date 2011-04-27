package com.doan.covua.gamelogic;

public class Game {

	GameTree tree;
	PgnToken.PgnTokenReceiver gameTextListener;
	
	public Game(PgnToken.PgnTokenReceiver gameTextListener){
		this.gameTextListener = gameTextListener;
		newGame();
	}
	public final void newGame(){
		tree = new GameTree(gameTextListener);
	}
	final Position currPos() {
		return tree.currentPos;
	}
}
