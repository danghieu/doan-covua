package com.doan.covua;


import com.doan.covua.gamelogic.ChessParseError;
import com.doan.covua.gamelogic.Piece;
import com.doan.covua.gamelogic.Position;
import com.doan.covua.gamelogic.TextIO;

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
import android.view.View;

public class ChessBoard extends View{

	public static final String TAG = "ChessBoard";
	
	public Position pos;
	
	protected Paint darkPaint;
    protected Paint brightPaint;
    protected int x0, y0, sqSize;
    int pieceXDelta, pieceYDelta;
    public boolean flipped;
    
    
    public ChessBoard(Context context, AttributeSet attr){
    	super(context, attr);
    	
    	pos = new Position();
    	/*try {
			pos = new Position(TextIO.readFEN(TextIO.startPosFEN));
		} catch (ChessParseError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    	x0 = y0 = sqSize = 0;
	    pieceXDelta = pieceYDelta = -1;
    	flipped = false;
    	 
    	darkPaint = new Paint();
 	    darkPaint.setColor(Color.GRAY);
        brightPaint = new Paint();
        brightPaint.setColor(Color.WHITE);
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
	
	@Override
	protected void onDraw(Canvas canvas){
		final int width = getWidth();
		final int height = getHeight();
        sqSize = Math.min(getSqSizeW(width), getSqSizeH(height));
        computeOrigin(width, height);
       
        //debug
       // for(int i=0; i< 64; i++){
       // 	Log.v("debug the board...","" + pos.squares[i]);
       // }
        for(int x = 0; x < 8; x++)
        	for(int y = 0; y < 8; y++){
        		 final int xCrd = getXCrd(x);
                 final int yCrd = getYCrd(y);
                 Paint paint = Position.darkSquare(x, y) ? darkPaint : brightPaint;
                 canvas.drawRect(xCrd, yCrd, xCrd+sqSize, yCrd+sqSize, paint);
                 
                 int sq = Position.getSquare(x, y);

                 int p = pos.getPiece(sq);
                 
                 drawPiece(canvas, xCrd, yCrd, p);
        	}
       
	}
	protected int getXCrd(int x) { return x0 + sqSize * (flipped ? 7 - x : x); }
    protected int getYCrd(int y) { return y0 + sqSize * (flipped ? y : 7 - y); }
    
    protected final void drawPiece(Canvas canvas, int xCrd, int yCrd, int piece) {
    	if(piece == 0)
    		return;
		//draw piece
		Resources r = getResources();
		Bitmap re_piece = BitmapFactory.decodeResource(r, 0);
		Log.v("piec on draw piece...","" + piece);
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
    	if (!this.pos.equals(pos)) {
    		this.pos = new Position(pos);
    		doInvalidate = true;
    	}
    	if (doInvalidate)
    		invalidate();
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
			//if (paused || (startTime < 0) || (now >= stopTime) || (posHash != pos.zobristHash()))
			//	return false;
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
}
