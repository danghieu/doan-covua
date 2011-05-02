package com.doan.covua.gamelogic;

import java.util.ArrayList;
import java.util.List;

public class GameTree {

	Position startPos;
	Position currentPos;
	Node rootNode;
	Node currentNode;
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
		rootNode = new Node();
		currentNode = rootNode;
		currentPos = new Position(startPos);
	}
	private final void updateListener() {
		if (gameStateListener != null) {
			gameStateListener.clear();
		}
	}
	 /** Go backward in game tree. */
    public final void goBack() {
    	if (currentNode.parent != null) {
    		currentPos.unMakeMove(currentNode.move, currentNode.ui);
    		currentNode = currentNode.parent;
    	}
    }
    /** Go forward in game tree.
     * @param variation Which variation to follow. -1 to follow default variation.
     */
    public final void goForward(int variation) {
    	goForward(variation, true);
    }
    public final void goForward(int variation, boolean updateDefault) {
    	//if (currentNode.verifyChildren(currentPos))
    	//	updateListener();
    	if (variation < 0)
    		variation = currentNode.defaultChild;
    	int numChildren = currentNode.children.size();
    	if (variation >= numChildren)
    		variation = 0;
    	if (updateDefault)
    		currentNode.defaultChild = variation;
    	if (numChildren > 0) {
    		currentNode = currentNode.children.get(variation);
    		currentPos.makeMove(currentNode.move, currentNode.ui);
            TextIO.fixupEPSquare(currentPos);
    	}
    }
	
    /** List of possible continuation moves. */
    public final List<Move> variations() {
    	//if (currentNode.verifyChildren(currentPos))
    	//	updateListener();
    	List<Move> ret = new ArrayList<Move>();
    	for (Node child : currentNode.children)
    		ret.add(child.move);
    	return ret;
    }
    
    /** Add a move last in the list of variations.
     * @return Move number in variations list. -1 if moveStr is not a valid move
     */
    public final int addMove(String moveStr, String playerAction, int nag, String preComment, String postComment) {
    	//if (currentNode.verifyChildren(currentPos))
    	//	updateListener();
    	int idx = currentNode.children.size();
    	Node node = new Node(currentNode, moveStr, playerAction, Integer.MIN_VALUE, nag, preComment, postComment);
    	Move move = TextIO.UCIstringToMove(moveStr);
    	if (move == null)
    		move = TextIO.stringToMove(currentPos, moveStr);
    	if (move == null){
    		return -1;
    	}
    	node.moveStr = TextIO.moveToString(currentPos, move, false);
    	node.move = move;
    	node.ui = new UndoInfo();
    	currentNode.children.add(node);
    	updateListener();
    	return idx;
    }
    
	/** Move a variation in the ordered list of variations. */
    public final void reorderVariation(int varNo, int newPos) {
    	
    	//if (currentNode.verifyChildren(currentPos))
    	//	updateListener();
    	int nChild = currentNode.children.size();
    	if ((varNo < 0) || (varNo >= nChild) || (newPos < 0) || (newPos >= nChild))
    		return;
    	Node var = currentNode.children.get(varNo);
    	currentNode.children.remove(varNo);
    	
    	currentNode.children.add(newPos, var);
    	
    	int newDef = currentNode.defaultChild;
    	if (varNo == newDef) {
    		newDef = newPos;
    	} else {
        	if (varNo < newDef) newDef--;
        	if (newPos <= newDef) newDef++;
    	}
    	
		currentNode.defaultChild = newDef;
		
    	//updateListener();
    	
    }
    
	/** Delete a variation. */
    public final void deleteVariation(int varNo) {
    	//if (currentNode.verifyChildren(currentPos))
    	//	updateListener();
    	int nChild = currentNode.children.size();
    	if ((varNo < 0) || (varNo >= nChild))
    		return;
    	currentNode.children.remove(varNo);
    	if (varNo == currentNode.defaultChild) {
    		currentNode.defaultChild = 0;
    	} else if (varNo < currentNode.defaultChild) {
    		currentNode.defaultChild--;
    	}
    	//updateListener();
    }
    
    
    /** nodePos must represent the same position as this Node object. */
   /* private final boolean verifyChildren(Position nodePos) {
    	boolean anyToRemove = false;
    	for (Node child : children) {
    		if (child.move == null) {
    	    	Move move = TextIO.stringToMove(nodePos, child.moveStr);
    			if (move != null) {
    				child.moveStr = TextIO.moveToString(nodePos, move, false);
    				child.move = move;
    				child.ui = new UndoInfo();
    			} else {
    				anyToRemove = true;
    			}
    		}
    	}
    	if (anyToRemove) {
    		List<Node> validChildren = new ArrayList<Node>();
        	for (Node child : children)
        		if (child.move != null)
        			validChildren.add(child);
        	children = validChildren;
    	}
    	return anyToRemove;
    }*/

	/**
     *  A node object represents a position in the game tree.
     *  The position is defined by the move that leads to the position from the parent position.
     *  The root node is special in that it doesn't have a move.
     */
	public static class Node{
		String moveStr;	
    	Move move;					// Computed on demand for better PGN parsing performance.
    	
    	private UndoInfo ui;
    	String playerAction;
    	
    	int remainingTime;			// Remaining time in ms for side that played moveStr, or INT_MIN if unknown.
    	int nag;					// Numeric annotation glyph
    	String preComment;			// Comment before move
    	String postComment;			// Comment after move
    	
    	private Node parent;		// Null if root node
    	int defaultChild;
    	private List<Node> children;
    	
    	public Node(){
    		this.moveStr = "";
    		this.move = null;
    		this.ui = null;
    		this.parent = null;
    		this.playerAction = "";
    		this.children = new ArrayList<Node>();
    		this.defaultChild = 0;
    		this.nag = 0;
    		this.preComment = "";
    		this.postComment = "";
    	}
    	public Node(Node parent, String moveStr, String playerAction, int remainingTime, int nag,
				String preComment, String postComment) {
    		this.moveStr = moveStr;
    		this.move = null;
    		this.ui = null;
    		this.playerAction = playerAction;
    		this.remainingTime = remainingTime;
    		this.parent = parent;
    		this.children = new ArrayList<Node>();
    		this.defaultChild = 0;
    		this.nag = nag;
    		this.preComment = preComment;
    		this.postComment = postComment;
    	}
    	/** nodePos must represent the same position as this Node object. */
    	/*private final boolean verifyChildren(Position nodePos) {
    		boolean anyToRemove = false;
    		for(Node child : children){
    			if (child.move == null) {
    				
    			}
    		}
    	}*/
	}
}
