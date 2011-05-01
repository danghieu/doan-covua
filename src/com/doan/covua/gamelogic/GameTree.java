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
	public final List<Move> variations(){
		//if (currentNode.verifyChildren(currentPos)){
			//updateListener();
		//}
		List<Move> ret = new ArrayList<Move>();
    	for (Node child : currentNode.children)
    		ret.add(child.move);
    	return ret;
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
    
	/**
     *  A node object represents a position in the game tree.
     *  The position is defined by the move that leads to the position from the parent position.
     *  The root node is special in that it doesn't have a move.
     */
	public static class Node{
    	Move move;					// Computed on demand for better PGN parsing performance.
    	
    	private UndoInfo ui;
    	
    	private Node parent;		// Null if root node
    	int defaultChild;
    	private List<Node> children;
    	
    	public Node(){
    		this.move = null;
    		this.ui = null;
    		this.parent = null;
    		this.children = new ArrayList<Node>();
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
