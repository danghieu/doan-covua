package com.doan.covua.gamelogic;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import com.doan.covua.GUIInterface;
import com.doan.covua.GameMode;
import com.doan.covua.gamelogic.Position;

public class ChessController {

	PgnToken.PgnTokenReceiver gameTextListener;
	GUIInterface gui;
	GameTree tree;
	GameMode gameMode;
	
	public ChessController(GUIInterface gui, PgnToken.PgnTokenReceiver gamTextListener){
		this.gui = gui;
		this.gameTextListener = gamTextListener;
	}
	public final void newGame(GameMode gameMode){
		this.gameMode = gameMode;
		tree = new GameTree(gameTextListener);
	}
	public final void startGame() {
		setSelection();
		updateGUI();
	}
	private final void updateGUI(){
		
		//updateMoveList();
		//StringBuilder sb = new StringBuilder();
		if(tree.currentNode != tree.rootNode){
			//tree.goBack();
			
			//Position pos = currPos();
			/*List<Move> prevVarList = tree.variations();
			for (int i = 0; i < prevVarList.size(); i++) {
				if (i > 0) sb.append(' ');
				if (i == tree.currentNode.defaultChild)
        			sb.append("<b>");
        		sb.append(TextIO.moveToString(pos, prevVarList.get(i), false));
        		if (i == tree.currentNode.defaultChild)
        			sb.append("</b>");
			}*/
			//tree.goForward(-1);
		}
		gui.setPosition(currPos(), null, tree.variations());
	}
	/** get current position */
	final Position currPos(){
		return tree.currentPos;
	}
	/** get the last move */
	public final Move getLastMove(){
		return tree.currentNode.move;
	}
	final private void setSelection() {
        Move m = getLastMove();
        int sq = (m != null) ? m.to : -1;
        gui.setSelection(sq);
    }
	/** True if human's turn to make a move. (True in analysis mode.) */
	public final boolean humansTurn(){
		return gameMode.humansTurn(currPos().whiteMove);
	}
	 private void setAnimMove(Position sourcePos, Move move, boolean forward) {
	    	gui.setAnimMove(sourcePos, move, forward);
		}
	public final void makeHumanMove(Move m){
		for(int i=0; i<64; i++){
         	Log.v("make human move0000000000000","" + currPos().squares[i]);
         }
		Position oldPos = new Position(currPos());
		if (doMove(m)) {
			for(int i=0; i<64; i++)
				Log.v("domove....","" + currPos().squares[i]);
			 setAnimMove(oldPos, m, true);
			 updateGUI();
        } else {
           gui.setSelection(-1);
        }
	}
	 /**
     * Move a piece from one square to another.
     * @return True if the move was legal, false otherwise.
     */
	
    final private boolean doMove(Move move) {
    	//generate the moves 
    	
    	/*Position pos = currPos();
    	ArrayList<Move> moves = new Move().pseudoLegalMoves(pos);
        moves = Move.removeIllegal(pos, moves);
        int promoteTo = move.promoteTo;
        for (Move m : moves) {
            if ((m.from == move.from) && (m.to == move.to)) {
                if ((m.promoteTo != Piece.EMPTY) && (promoteTo == Piece.EMPTY)) {
                	//promoteMove = m;
                	//gui.requestPromotePiece();
                	
                	return false;
                }
                if (m.promoteTo == promoteTo) {
                	
                    //String strMove = TextIO.moveToString(pos, m, false);
                   
                    //processString(strMove);
                    //addToGameTree(m,"");
                	resetPos(move);
                    return true;
                }
            }
        }*/
    	
        resetPos(move);
        return true;
        //return false;
    }
    /** reset squares position */
    /** temp method */
    final private Position resetPos(Move move){
    	Position pos = currPos();
    	int pos_temp = pos.squares[move.from];
    	pos.squares[move.from] = 0;
    	
    	Log.v("square to","" + move.to);
    	
    	pos.squares[move.to] = pos_temp;
    	Position pos1 = new Position(pos);
    	return pos1;
    }
   
}

