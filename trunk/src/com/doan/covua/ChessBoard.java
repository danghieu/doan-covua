package com.doan.covua;


import com.doan.covua.gamelogic.ChessParseError;
import com.doan.covua.gamelogic.Move;
import com.doan.covua.gamelogic.Piece;
import com.doan.covua.gamelogic.Position;
import com.doan.covua.gamelogic.TextIO;
import com.doan.covua.gamelogic.UndoInfo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ChessBoard extends View{

	public static final String TAG = "ChessBoard";
	
	public Position pos;
	
	protected Paint darkPaint;
    protected Paint brightPaint;
    protected Paint selectedSquarePaint;
    
    protected int x0, y0, sqSize;
    int pieceXDelta, pieceYDelta;
    public boolean flipped;
    public int selectedSquare;
    public boolean oneTouchMoves;
    public float cursorX, cursorY;
    public boolean cursorVisible;
    
    public ChessBoard(Context context, AttributeSet attr){
    	super(context, attr);
    	
    	pos = new Position();
    	x0 = y0 = sqSize = 0;
	    pieceXDelta = pieceYDelta = -1;
    	flipped = false;
    	 
    	darkPaint = new Paint();
 	    darkPaint.setColor(Color.GRAY);
        brightPaint = new Paint();
        brightPaint.setColor(Color.WHITE);
        
        selectedSquarePaint = new Paint();
        selectedSquarePaint.setStyle(Paint.Style.STROKE);
        selectedSquarePaint.setAntiAlias(true);
        selectedSquarePaint.setColor(Color.GREEN);
        
    }
    
    protected int getWidth(int sqSize) { return sqSize * 8 + 4; }
	protected int getHeight(int sqSize) { return sqSize * 8 + 4; }
	protected int getSqSizeW(int width) { return (width - 4) / 8; }
	protected int getSqSizeH(int height) { return (height - 4) / 8; }

	protected int getMaxHeightPercentage() { return 75; }
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		int sqSizeW = getSqSizeW(width);
		int sqSizeH = getSqSizeH(height);
		int sqSize = Math.min(sqSizeW, sqSizeH);
		pieceXDelta = pieceYDelta = -1;
		//labelBounds = null;
		if (height > width) {
			int p = getMaxHeightPercentage();
			height = Math.min(getHeight(sqSize), height * p / 100);
		} else {
			width = Math.min(getWidth(sqSize), width * 65 / 100);
		}
		setMeasuredDimension(width, height);
	}
	protected void computeOrigin(int width, int height) {
        x0 = (width - sqSize * 8) / 2;
        y0 = (height - sqSize * 8) / 2;
	}
	
	protected int getXFromSq(int sq) { return Position.getX(sq); }
	protected int getYFromSq(int sq) { return Position.getY(sq); }

	
	@Override
	protected void onDraw(Canvas canvas){
		Log.v("in onDraw","rightnow");
		boolean animActive = anim.updateState();
		final int width = getWidth();
		final int height = getHeight();
        sqSize = Math.min(getSqSizeW(width), getSqSizeH(height));
        computeOrigin(width, height);
       
        for(int x = 0; x < 8; x++)
        	for(int y = 0; y < 8; y++){
        		 final int xCrd = getXCrd(x);
                 final int yCrd = getYCrd(y);
                 Paint paint = Position.darkSquare(x, y) ? darkPaint : brightPaint;
                 canvas.drawRect(xCrd, yCrd, xCrd+sqSize, yCrd+sqSize, paint);
                 
                 int sq = Position.getSquare(x, y);
                 if (!animActive || !anim.squareHidden(sq)) {
                	 int p = pos.getPiece(sq);
                	
                	 drawPiece(canvas, xCrd, yCrd, p);
                 }
        	}
        
        if(!animActive && (selectedSquare != -1)){
        	
        	int selX = getXFromSq(selectedSquare);
            int selY = getYFromSq(selectedSquare);
            selectedSquarePaint.setStrokeWidth(sqSize/(float)16);
            
            int x0 = getXCrd(selX);
            int y0 = getYCrd(selY);
          
            canvas.drawRect(x0, y0, x0 + sqSize, y0 + sqSize, selectedSquarePaint);
        }
       anim.draw(canvas);
	}
	protected int getXCrd(int x) { return x0 + sqSize * (flipped ? 7 - x : x); }
    protected int getYCrd(int y) { return y0 + sqSize * (flipped ? y : 7 - y); }
    protected int getXSq(int xCrd) { int t = (xCrd - x0) / sqSize; return flipped ? 7 - t : t; }
    protected int getYSq(int yCrd) { int t = (yCrd - y0) / sqSize; return flipped ? t : 7 - t; }

    protected final void drawPiece(Canvas canvas, int xCrd, int yCrd, int piece) {
    	if(piece == Piece.EMPTY)
    		return;
		//draw piece
		Resources r = getResources();
		Bitmap re_piece = BitmapFactory.decodeResource(r, 0);
		switch(piece){
		case Piece.EMPTY:
			re_piece = null;
		case Piece.BPAWN:
			re_piece = BitmapFactory.decodeResource(r, R.drawable.bp_alt);
			break;
		case Piece.WPAWN:
			re_piece = BitmapFactory.decodeResource(r, R.drawable.wp_alt);
			break;
		case Piece.BKNIGHT:
			re_piece = BitmapFactory.decodeResource(r, R.drawable.bn_alt);
			break;
		case Piece.WKNIGHT:
			re_piece = BitmapFactory.decodeResource(r, R.drawable.wn_alt);
			break;
		case Piece.BBISHOP:
			re_piece = BitmapFactory.decodeResource(r, R.drawable.bb_alt);
			break;
		case Piece.WBISHOP:
			re_piece = BitmapFactory.decodeResource(r, R.drawable.wb_alt);
			break;
		case Piece.BROOK:
			re_piece = BitmapFactory.decodeResource(r, R.drawable.br_alt);
			break;
		case Piece.WROOK:
			re_piece = BitmapFactory.decodeResource(r, R.drawable.wr_alt);
			break;
		case Piece.WQUEEN:
			re_piece = BitmapFactory.decodeResource(r, R.drawable.wq_alt);
			break;
		case Piece.BQUEEN:
			re_piece = BitmapFactory.decodeResource(r, R.drawable.bq_alt);
			break;
		case Piece.BKING:
			re_piece = BitmapFactory.decodeResource(r, R.drawable.bk_alt);
			break;
		case Piece.WKING:
			re_piece = BitmapFactory.decodeResource(r, R.drawable.wk_alt);
			break;
		}
		if(re_piece != null)
		canvas.drawBitmap(re_piece, xCrd, yCrd, null);
    }
    /**
     * Set the board to a given state.
     * @param pos
     */
    final public void setPosition(Position pos) {
    	boolean doInvalidate = false;
    	if(anim.paused == true){
    		anim.paused = false;
    		doInvalidate = true;
    	}
    	if (!this.pos.equals(pos)) {
    		this.pos = new Position(pos);
    		doInvalidate = true;
    	}
    	if (doInvalidate)
    		invalidate();
    }
    /**
     * Set/clear the selected square.
     * @param square The square to select, or -1 to clear selection.
     */
    final public void setSelection(int square) {
        if (square != selectedSquare) {
            selectedSquare = square;
            invalidate();
        }
    }
    private Handler handlerTimer = new Handler();
	
	final class AnimInfo {
		AnimInfo() { startTime = -1; }
		boolean paused;
		long posHash;   // Position the animation is valid for
		long startTime; // Time in milliseconds when animation was started
		long stopTime;  // Time in milliseconds when animation should stop
		long now;       // Current time in milliseconds
		int piece1, from1, to1, hide1;
		int piece2, from2, to2, hide2;

		public final boolean updateState() {
			now = System.currentTimeMillis();
			return animActive();
		}
		private final boolean animActive() {
			if (paused || (startTime < 0) || (now >= stopTime))
				return false;
			return true;
		}
		public final boolean squareHidden(int sq) {
			if (!animActive())
				return false;
			return (sq == hide1) || (sq == hide2);
		}
		public final void draw(Canvas canvas) {
			if (!animActive())
				return;
			double animState = (now - startTime) / (double)(stopTime - startTime);
			drawAnimPiece(canvas, piece2, from2, to2, animState);
			//drawPiece(canvas, getXCrd(Position.getX(from1)), getYCrd(Position.getY(from1)), piece2);
			//drawPiece(canvas, getXCrd(to1), getYCrd(to1), piece1);
			//drawPiece(canvas, getXCrd(Position.getX(to1)), getYCrd(Position.getY(to1)), piece1);
			drawAnimPiece(canvas, piece1, from1, to1, animState);
			long now2 = System.currentTimeMillis();
			long delay = 20 - (now2 - now);
//			System.out.printf("delay:%d\n", delay);
			if (delay < 1) delay = 1;
	        handlerTimer.postDelayed(new Runnable() {
				@Override
				public void run() {
					invalidate();
				}
	        }, delay);
			invalidate();
		}
		private void drawAnimPiece(Canvas canvas, int piece, int from, int to, double animState) {
			if (piece == Piece.EMPTY)
				return;
		
            final int xCrd1 = getXCrd(Position.getX(from));
            final int yCrd1 = getYCrd(Position.getY(from));
            final int xCrd2 = getXCrd(Position.getX(to));
            final int yCrd2 = getYCrd(Position.getY(to));
        	
        	
            final int xCrd = xCrd1 + (int)Math.round((xCrd2 - xCrd1) * animState);
            final int yCrd = yCrd1 + (int)Math.round((yCrd2 - yCrd1) * animState);
            
        	drawPiece(canvas, xCrd, yCrd, piece);
		}
	}
	AnimInfo anim = new AnimInfo();
	
	final private boolean myColor(int piece){
		return (piece != Piece.EMPTY) && (Piece.isWhite(piece) == pos.whiteMove);
	}
	/**
     * Compute the square corresponding to the coordinates of a mouse event.
     * @param evt Details about the mouse event.
     * @return The square corresponding to the mouse event, or -1 if outside board.
     */
    public int eventToSquare(MotionEvent evt) {
    	int xCrd = (int)(evt.getX());
        int yCrd = (int)(evt.getY());

        int sq = -1;
        if (sqSize > 0) {
            int x = getXSq(xCrd);
            int y = getYSq(yCrd);
            if ((x >= 0) && (x < 8) && (y >= 0) && (y < 8)) {
                sq = Position.getSquare(x, y);
            }
        }
        return sq;
    }
	public Move mousePressed(int sq){
		if(sq < 0)
			return null;
		if(selectedSquare != -1){
			int p = pos.getPiece(selectedSquare);
			if(!myColor(p))
				setSelection(-1);
		}
		int p = pos.getPiece(sq);
		if(selectedSquare != -1){
			if(sq != selectedSquare){
				if (!myColor(p)) {
                    Move m = new Move(selectedSquare, sq, Piece.EMPTY);
                    setSelection(sq);
                    return m;
                }
			}
			 setSelection(-1);
		}else{
			if(oneTouchMoves){
				//do something
				//..........
			}
        	if (myColor(p)) {
        		setSelection(sq);
        	}
		}
		return null;
	}
	/**
	 * Set up move animation. The animation will start the next time setPosition is called.
	 * @param sourcePos The source position for the animation.
	 * @param move      The move leading to the target position.
	 * @param forward   True if forward direction, false for undo move.
	 */
	public void setAnimMove(Position sourcePos, Move move, boolean forward) {
		anim.startTime = -1;
		if(forward){
			// The animation will be played when pos == targetPos
			Position targetPos = new Position(sourcePos);
			UndoInfo ui = new UndoInfo();
			targetPos.makeMove(move, ui);
		}
		int animTime; // Animation duration in milliseconds.
		{
			int dx = Position.getX(move.to) - Position.getX(move.from);
			int dy = Position.getY(move.to) - Position.getY(move.from);
			double dist = Math.sqrt(dx * dx + dy * dy);
			double t = Math.sqrt(dist) * 100;
			animTime = (int)Math.round(t);
		}
		if (animTime > 0) {
			anim.startTime = System.currentTimeMillis();
			anim.stopTime = anim.startTime + animTime;
			anim.piece2 = Piece.EMPTY;
			anim.from2 = -1;
			anim.to2 = -1;
			anim.hide2 = -1;
			if (forward) {
				int p = sourcePos.getPiece(move.from);
				anim.piece1 = p;
				anim.from1 = move.from;
				anim.to1 = move.to;
				anim.hide1 = anim.to1;
				int p2 = sourcePos.getPiece(move.to);
				if (p2 != Piece.EMPTY) { // capture
					anim.piece2 = p2;
					anim.from2 = move.to;
					anim.to2 = move.to;
				} else if ((p == Piece.WKING) || (p == Piece.BKING)) {
					boolean wtm = Piece.isWhite(p);
					if (move.to == move.from + 2) { // O-O
						anim.piece2 = wtm ? Piece.WROOK : Piece.BROOK;
						anim.from2 = move.to + 1;
						anim.to2 = move.to - 1;
						anim.hide2 = anim.to2;
					} else if (move.to == move.from - 2) { // O-O-O
						anim.piece2 = wtm ? Piece.WROOK : Piece.BROOK;
						anim.from2 = move.to - 2;
						anim.to2 = move.to + 1;
						anim.hide2 = anim.to2;
					}
				}
			} else {
				int p = sourcePos.getPiece(move.from);
				anim.piece1 = p;
				if (move.promoteTo != Piece.EMPTY)
					anim.piece1 = Piece.isWhite(anim.piece1) ? Piece.WPAWN : Piece.BPAWN;
				anim.from1 = move.to;
				anim.to1 = move.from;
				anim.hide1 = anim.to1;
				if ((p == Piece.WKING) || (p == Piece.BKING)) {
					boolean wtm = Piece.isWhite(p);
					if (move.to == move.from + 2) { // O-O
						anim.piece2 = wtm ? Piece.WROOK : Piece.BROOK;
						anim.from2 = move.to - 1;
						anim.to2 = move.to + 1;
						anim.hide2 = anim.to2;
					} else if (move.to == move.from - 2) { // O-O-O
						anim.piece2 = wtm ? Piece.WROOK : Piece.BROOK;
						anim.from2 = move.to + 1;
						anim.to2 = move.to - 2;
						anim.hide2 = anim.to2;
					}
				}
			}
		}
	}
}
