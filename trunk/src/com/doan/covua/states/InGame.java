package com.doan.covua.states;



import java.util.List;
import com.doan.covua.ChessBoard;
import com.doan.covua.GUIInterface;
import com.doan.covua.GameMode;
import com.doan.covua.R;
import com.doan.covua.gamelogic.ChessController;
import com.doan.covua.gamelogic.Move;
import com.doan.covua.gamelogic.Position;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;


public class InGame extends Activity implements GUIInterface{
	
	public static final String KEY_DIFFICULTY = "com.doan.covua.states.difficulty";
	public static final int DIFFICULTY_EASY = 0;
	public static final int DIFFICULTY_MEDIUM = 1;
	public static final int DIFFICULTY_HARD = 2;
	
	
	private ChessBoard cb;
	private ChessController ctrl;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initUI(true);
		ctrl = new ChessController(this,null);
		ctrl.newGame(new GameMode(GameMode.TWO_PLAYERS));
		ctrl.startGame();
	}
	
	public final void initUI(boolean initTitle){
		if(initTitle)
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main);
		
		cb = (ChessBoard)findViewById(R.id.chessboard);
		cb.setFocusable(true);
		cb.requestFocus();
		cb.setClickable(true);
		
		final GestureDetector gd = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
			private float scrollX = 0;
            private float scrollY = 0;
            public boolean onDown(MotionEvent e) {
                scrollX = 0;
                scrollY = 0;
                return false;
            }
			public boolean onSingleTapUp(MotionEvent e) {
                cb.cancelLongPress();
                handleClick(e);
                return true;
            }
            public boolean onDoubleTapEvent(MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_UP)
                    handleClick(e);
                return true;
            }
            private final void handleClick(MotionEvent e) {
            	if(ctrl.humansTurn()){
            		int sq = cb.eventToSquare(e);
            		Move m = cb.mousePressed(sq);
            		if(m != null){
            			ctrl.makeHumanMove(m);
            		}
            	}
            }
		});
		cb.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				return gd.onTouchEvent(event);
			}
		});
	}
	
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ChessBoard oldCB = cb;
        initUI(false);
        cb.setPosition(oldCB.pos);
        cb.cursorX = oldCB.cursorX;
        cb.cursorY = oldCB.cursorY;
        setSelection(oldCB.selectedSquare);
    }
    @Override
    public void setPosition(Position pos, String variantInfo, List<Move> variantMoves) {
      //  variantStr = variantInfo;
      //  this.variantMoves = variantMoves;
        cb.setPosition(pos);
        //setBoardFlip();
        //updateThinkingInfo();
    }
  
    @Override
    public void setSelection(int sq){
    	cb.setSelection(sq);
    }
    /** Report a move made that is a candidate for GUI animation. */
    public void setAnimMove(Position sourcePos, Move move, boolean forward) {
        if (/*animateMoves && */(move != null))
            cb.setAnimMove(sourcePos, move, forward);
    }
}