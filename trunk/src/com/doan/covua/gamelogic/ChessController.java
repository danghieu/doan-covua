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
	
	boolean pendingDrawOffer;
	private boolean addFirst; 
	
	public ChessController(GUIInterface gui, PgnToken.PgnTokenReceiver gamTextListener){
		this.gui = gui;
		this.gameTextListener = gamTextListener;
	}
	
	private final static class SearchStatus {
    	boolean searchResultWanted = true;
    }
    SearchStatus ss = new SearchStatus();
	    
	public final void newGame(GameMode gameMode){
		//for computer thinks
		ss.searchResultWanted = false;
		
		//end
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
		//if(humansTurn()){
			Position oldPos = new Position(currPos());
			if (doMove(m)) {
				ss.searchResultWanted = false;
				setAnimMove(oldPos, m, true);
				updateGUI();
				 
	        } else {
	           gui.setSelection(-1);
	        }
		//}
		
	}
	
	Move promoteMove;
	 /**
     * Move a piece from one square to another.
     * @return True if the move was legal, false otherwise.
     */
    final private boolean doMove(Move move) {
    	//generate the moves 
    	
    	Position pos = currPos();
    	ArrayList<Move> moves = new MoveGen().pseudoLegalMoves(pos);
        moves = MoveGen.removeIllegal(pos, moves);
        int promoteTo = move.promoteTo;
        for (Move m : moves) {
            if ((m.from == move.from) && (m.to == move.to)) {
                if ((m.promoteTo != Piece.EMPTY) && (promoteTo == Piece.EMPTY)) {
                	promoteMove = m;
                	//gui.requestPromotePiece();
                	
                	return false;
                }
                if (m.promoteTo == promoteTo) {
                	
                    String strMove = TextIO.moveToString(pos, m, false);   
                    processString(strMove);
                    //addToGameTree(m,"");
                	//resetPos(move);
                    return true;
                }
            }
        }
    	
       // resetPos(move);
        //return true;
        return false;
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
    
    //update for do move
   
    /**
     * Update the game state according to move/command string from a player.
     * @param str The move or command to process.
     * @return True if str was understood, false otherwise.
     */
    
    public final boolean processString(String str) {
    	//do something
    	//.....
    	/*if (str.startsWith("draw ")) {
			String drawCmd = str.substring(str.indexOf(" ") + 1);
			handleDrawCmd(drawCmd);
	    	return true;
		} else if (str.equals("resign")) {
			addToGameTree(new Move(0, 0, 0), "resign");
			return true;
		}*/
    	Move m = TextIO.UCIstringToMove(str);
    	if(m != null){
    		 ArrayList<Move> moves = new MoveGen().pseudoLegalMoves(currPos());
             moves = MoveGen.removeIllegal(currPos(), moves);
             
             boolean legal = false;
             for (int i = 0; i < moves.size(); i++) {
             	if (m.equals(moves.get(i))) {
             		legal = true;
             		break;
             	}
             }
             if(!legal)
            	 m = null;
    	}
    	if (m == null){
    		m = TextIO.stringToMove(currPos(), str);
    	}
    	if (m == null){
    		return false;
    	}
    	addToGameTree(m, pendingDrawOffer ? "draw offer" : "");
    	return true;
    }
    
    private final void addToGameTree(Move m, String playerAction) {
    	if (m.equals(new Move(0, 0, 0))) { // Don't create more than one null move at a node
    		List<Move> varMoves = tree.variations();
    		for (int i = varMoves.size() - 1; i >= 0; i--) {
            	if (varMoves.get(i).equals(m)) {
            		tree.deleteVariation(i);
            	}
    		}
    	}
    	
    	List<Move> varMoves = tree.variations();
    	boolean movePresent = false;
    	int varNo;
    	for(varNo = 0; varNo < varMoves.size(); varNo++){
    		if (varMoves.get(varNo).equals(m)){
    			movePresent = true;
    			break;
    		}
    	}
    	if (!movePresent){
    		String moveStr = TextIO.moveToUCIString(m);
    		varNo = tree.addMove(moveStr, playerAction, 0, "", "");
    	}
    	int newPos = addFirst ? 0 : varNo;
    	tree.reorderVariation(varNo, newPos);
    	tree.goForward(newPos);
    	//int remaining = timeController.moveMade(System.currentTimeMillis(), !gamePaused);
        //tree.setRemainingTime(remaining);
        //updateTimeControl(true);
    	pendingDrawOffer = false;
    }

   
    
}

