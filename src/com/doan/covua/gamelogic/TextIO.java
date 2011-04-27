package com.doan.covua.gamelogic;

import android.util.Log;

public class TextIO {

	static public final String startPosFEN = new String("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
	
	/** Parse a FEN string and return a chess Position object. */
    public static final Position readFEN(String fen) throws ChessParseError {
		
    	 Position pos = new Position();
         String[] words = fen.split(" ");
         if (words.length < 2) {
             throw new ChessParseError("Too few spaces");
         }
         for (int i = 0; i < words.length; i++) {
         	words[i] = words[i].trim();
         }
         // Piece placement
         int row = 7;
         int col = 0;
         for (int i = 0; i < words[0].length(); i++) {
             char c = words[0].charAt(i);
             switch (c) {
                 case '1': col += 1; break;
                 case '2': col += 2; break;
                 case '3': col += 3; break;
                 case '4': col += 4; break;
                 case '5': col += 5; break;
                 case '6': col += 6; break;
                 case '7': col += 7; break;
                 case '8': col += 8; break;
                 case '/': row--; col = 0; break;
                 case 'P': safeSetPiece(pos, col, row, Piece.WPAWN);   col++; break;
                 case 'N': safeSetPiece(pos, col, row, Piece.WKNIGHT); col++; break;
 				 case 'B': safeSetPiece(pos, col, row, Piece.WBISHOP); col++; break;
 				 case 'R': safeSetPiece(pos, col, row, Piece.WROOK);   col++; break;
 				 case 'Q': safeSetPiece(pos, col, row, Piece.WQUEEN);  col++; break;
 				 case 'K': safeSetPiece(pos, col, row, Piece.WKING);   col++; break;
 				 case 'p': safeSetPiece(pos, col, row, Piece.BPAWN);   col++; break;
 				 case 'n': safeSetPiece(pos, col, row, Piece.BKNIGHT); col++; break;
 				 case 'b': safeSetPiece(pos, col, row, Piece.BBISHOP); col++; break;
 				 case 'r': safeSetPiece(pos, col, row, Piece.BROOK);   col++; break;
 				 case 'q': safeSetPiece(pos, col, row, Piece.BQUEEN);  col++; break;
 				 case 'k': safeSetPiece(pos, col, row, Piece.BKING);   col++; break;
 		         default: throw new ChessParseError("Invalid piece", pos);
             }
         }
         if (words[1].length() == 0) {
             throw new ChessParseError("Invalid side", pos);
         }
         pos.setWhiteMove(words[1].charAt(0) == 'w');
         /*
         // Castling rights
         int castleMask = 0;
         if (words.length > 2) {
             for (int i = 0; i < words[2].length(); i++) {
                 char c = words[2].charAt(i);
                 switch (c) {
                     case 'K':
                         castleMask |= (1 << Position.H1_CASTLE);
                         break;
                     case 'Q':
                         castleMask |= (1 << Position.A1_CASTLE);
                         break;
                     case 'k':
                         castleMask |= (1 << Position.H8_CASTLE);
                         break;
                     case 'q':
                         castleMask |= (1 << Position.A8_CASTLE);
                         break;
                     case '-':
                         break;
                     default:
                         throw new ChessParseError("Invalid castling flags", pos);
                 }
             }
         }
         pos.setCastleMask(castleMask);
         removeBogusCastleFlags(pos);
         
         if (words.length > 3) {
             // En passant target square
             String epString = words[3];
             if (!epString.equals("-")) {
                 if (epString.length() < 2) {
                     throw new ChessParseError("Invalid en passant square", pos);
                 }
                 pos.setEpSquare(getSquare(epString));
             }
         }

         try {
             if (words.length > 4) {
                 pos.halfMoveClock = Integer.parseInt(words[4]);
             }
             if (words.length > 5) {
                 pos.fullMoveCounter = Integer.parseInt(words[5]);
             }
         } catch (NumberFormatException nfe) {
             // Ignore errors here, since the fields are optional
         }

         // Each side must have exactly one king
         int wKings = 0;
         int bKings = 0;
         for (int x = 0; x < 8; x++) {
             for (int y = 0; y < 8; y++) {
                 int p = pos.getPiece(Position.getSquare(x, y));
                 if (p == Piece.WKING) {
                     wKings++;
                 } else if (p == Piece.BKING) {
                     bKings++;
                 }
             }
         }
         if (wKings != 1) {
             throw new ChessParseError("White must have exactly one king", pos);
         }
         if (bKings != 1) {
             throw new ChessParseError("Black must have exactly one king", pos);
         }
         
         //fixupEPSquare(pos);
         */
         return pos;
	}
    private static final void safeSetPiece(Position pos, int col, int row, int p) throws ChessParseError {
        if (row < 0) throw new ChessParseError("Too many rows");
        if (col > 7) throw new ChessParseError("Too many columns");
        if ((p == Piece.WPAWN) || (p == Piece.BPAWN)) {
            if ((row == 0) || (row == 7))
                throw new ChessParseError("Pawn on first/last rank");
        }
        pos.setPiece(Position.getSquare(col, row), p);
    }
    /**
     * Convert a string, such as "e4" to a square number.
     * @return The square number, or -1 if not a legal square.
     */
    public static final int getSquare(String s) {
        int x = s.charAt(0) - 'a';
        int y = s.charAt(1) - '1';
        if ((x < 0) || (x > 7) || (y < 0) || (y > 7))
            return -1;
        int checkx = Position.getSquare(x, y);
        Log.v("getsquare.....", "checkk" + checkx);
        return Position.getSquare(x, y);
    }
    
    public static final void removeBogusCastleFlags(Position pos) {
    	int castleMask = pos.getCastleMask();
    	int validCastle = 0;
    	if (pos.getPiece(4) == Piece.WKING) {
    		if (pos.getPiece(0) == Piece.WROOK) validCastle |= (1 << Position.A1_CASTLE);
    		if (pos.getPiece(7) == Piece.WROOK) validCastle |= (1 << Position.H1_CASTLE);
    	}
    	if (pos.getPiece(60) == Piece.BKING) {
    		if (pos.getPiece(56) == Piece.BROOK) validCastle |= (1 << Position.A8_CASTLE);
    		if (pos.getPiece(63) == Piece.BROOK) validCastle |= (1 << Position.H8_CASTLE);
    	}
    	castleMask &= validCastle;
    	pos.setCastleMask(castleMask);
    }
}
