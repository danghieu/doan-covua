/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.doan.covua.gamelogic;

import java.util.ArrayList;

/**
 *
 * @author petero
 */
public class Move {
    /** From square, 0-63. */
    public int from;

    /** To square, 0-63. */
    public int to;

    /** Promotion piece. */
    public int promoteTo;
    
   
    /** undo Infor 
    int capturedPiece;
    int castleMask;
    int epSquare;
    int halfMoveClock;*/
    /** end */
    public Move(){}
    /** Create a move object. */
    public Move(int from, int to, int promoteTo) {
        this.from = from;
        this.to = to;
        this.promoteTo = promoteTo;
    }

    public Move(Move m) {
        this.from = m.from;
        this.to = m.to;
        this.promoteTo = m.promoteTo;
    }
    
    @Override
    public boolean equals(Object o) {
        if ((o == null) || (o.getClass() != this.getClass()))
            return false;
        Move other = (Move)o;
        if (from != other.from)
            return false;
        if (to != other.to)
            return false;
        if (promoteTo != other.promoteTo)
            return false;
        return true;
    }
    @Override
    public int hashCode() {
        return (from * 64 + to) * 16 + promoteTo;
    }
    
    
    /** Useful for debugging. */
    //public final String toString() {
   // 	return TextIO.moveToUCIString(this);
    //}
      

}


