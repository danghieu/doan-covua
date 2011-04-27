package com.doan.covua.gamelogic;

import android.util.Log;

public class GameTree {

	Position startPos;
	Position currentPos;
	
	PgnToken.PgnTokenReceiver gameStateListener;
	
	public GameTree(PgnToken.PgnTokenReceiver gameStateListener){
		this.gameStateListener = gameStateListener;
		try{
			
			setStartPos(TextIO.readFEN(TextIO.startPosFEN));
		} catch(ChessParseError e){
			
		}
	}
	/** set start position **/
	final void setStartPos(Position pos){
		startPos = pos;
		currentPos = new Position(startPos);
	}
}
