package com.doan.covua.states;



import java.util.List;
import com.doan.covua.ChessBoard;
import com.doan.covua.GUIInterface;
import com.doan.covua.R;
import com.doan.covua.gamelogic.ChessController;
import com.doan.covua.gamelogic.Move;
import com.doan.covua.gamelogic.Position;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;


public class InGame extends Activity implements GUIInterface{
	
	public static final String KEY_DIFFICULTY = "com.doan.covua.states.difficulty";
	public static final int DIFFICULTY_EASY = 0;
	public static final int DIFFICULTY_MEDIUM = 1;
	public static final int DIFFICULTY_HARD = 2;
	
	
	private ChessBoard cb;
	private ChessController ctrl;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		unitUI(true);
		ctrl = new ChessController(this,null);
		ctrl.newGame();
		ctrl.startGame();
		//chessview = new ChessView(this);
		//setContentView(chessview);
		//chessview.requestFocus();
	}
	
	public final void unitUI(boolean initTitle){
		if(initTitle)
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main);
		
		cb = (ChessBoard)findViewById(R.id.chessboard);
		
		cb.setFocusable(true);
		cb.requestFocus();
		cb.setClickable(true);
	}
	
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ChessBoard oldCB = cb;
        unitUI(false);
        cb.setPosition(oldCB.pos);
    }
    @Override
    public void setPosition(Position pos, String variantInfo, List<Move> variantMoves) {
      //  variantStr = variantInfo;
      //  this.variantMoves = variantMoves;
        cb.setPosition(pos);
        //setBoardFlip();
        //updateThinkingInfo();
    }
}