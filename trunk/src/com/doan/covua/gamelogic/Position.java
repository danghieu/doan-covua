package com.doan.covua.gamelogic;
import android.util.Log;

public class Position {

	public int[] squares;

    public boolean whiteMove;

    /** Bit definitions for the castleMask bit mask. */
    public static final int A1_CASTLE = 0; /** White long castle. */
    public static final int H1_CASTLE = 1; /** White short castle. */
    public static final int A8_CASTLE = 2; /** Black long castle. */
    public static final int H8_CASTLE = 3; /** Black short castle. */
    
    private int castleMask;

    private int epSquare;
    
    /** Number of half-moves since last 50-move reset. */
    public int halfMoveClock;
    
    /** Game move number, starting from 1. */
    public int fullMoveCounter;

    private long hashKey;           // Cached Zobrist hash key
    private int wKingSq, bKingSq;   // Cached king positions
    
    public static int[] s_square = new int[64];
    public Position(){
    	squares = new int[64];
    	for(int i=0; i< 64; i++){
    		squares[i] = Piece.EMPTY;
    		
    	}
    	whiteMove = true;
        castleMask = 0;
        epSquare = -1;
        halfMoveClock = 0;
        fullMoveCounter = 1;
        wKingSq = bKingSq = -1;
        
          
    }
    public Position(Position other) {
        squares = new int[64];
        System.arraycopy(other.squares, 0, squares, 0, 64);
        whiteMove = other.whiteMove;
        castleMask = other.castleMask;
        epSquare = other.epSquare;
        halfMoveClock = other.halfMoveClock;
        fullMoveCounter = other.fullMoveCounter;
        hashKey = other.hashKey;
        wKingSq = other.wKingSq;
        bKingSq = other.bKingSq;
    }
   
    /** Return index in squares[] vector corresponding to (x,y). */
    public final static int getSquare(int x, int y) {
        return y * 8 + x;
    }
    public final void setWhiteMove(boolean whiteMove) {
        if (whiteMove != this.whiteMove) {
           // hashKey ^= whiteHashKey;
            this.whiteMove = whiteMove;
        }
    }
    /** Return piece occuping a square. */
    public final int getPiece(int square) {

    	return squares[square];
    }
    /** Set a square to a piece value. */
    public final void setPiece(int square, int piece) {
    	/*
    	// Update hash key
    	int oldPiece = squares[square];
        hashKey ^= psHashKeys[oldPiece][square];
        hashKey ^= psHashKeys[piece][square];
        */
    	// Update board
        squares[square] = piece;
        // Update king position 
        if (piece == Piece.WKING) {
            wKingSq = square;
        } else if (piece == Piece.BKING) {
            bKingSq = square;
        }
        
    }
    /** Return true if (x,y) is a dark square. */
    public final static boolean darkSquare(int x, int y) {
        return (x & 1) == (y & 1);
    }
    /** Return x position (file) corresponding to a square. */
    public final static int getX(int square) {
        return square & 7;
    }
    /** Return y position (rank) corresponding to a square. */
    public final static int getY(int square) {
        return square >> 3;
    }
    public final void setEpSquare(int epSquare) {
    	this.epSquare = epSquare;
    }
    /** En passant square, or -1 if no ep possible. */
    public final int getEpSquare() {
        return epSquare;
    }
    /** Bitmask describing castling rights. */
    public final int getCastleMask() {
        return castleMask;
    }
    public final void setCastleMask(int castleMask) {
       // hashKey ^= castleHashKeys[this.castleMask];
       // hashKey ^= castleHashKeys[castleMask];
        this.castleMask = castleMask;
    }
    /**
     * Decide if two positions are equal in the sense of the draw by repetition rule.
     * @return True if positions are equal, false otherwise.
     */
    final public boolean drawRuleEquals(Position other) {
        for (int i = 0; i < 64; i++) {
            if (squares[i] != other.squares[i])
                return false;
        }
        if (whiteMove != other.whiteMove)
            return false;
        if (castleMask != other.castleMask)
            return false;
        if (epSquare != other.epSquare)
            return false;
        return true;
    }
    
    @Override
    public boolean equals(Object o) {
    	if ((o == null) || (o.getClass() != this.getClass()))
            return false;
        Position other = (Position)o;
        if (!drawRuleEquals(other))
            return false;
        if (halfMoveClock != other.halfMoveClock)
            return false;
        if (fullMoveCounter != other.fullMoveCounter)
            return false;
        if (hashKey != other.hashKey)
            return false;
        return true;
    }
    
}
