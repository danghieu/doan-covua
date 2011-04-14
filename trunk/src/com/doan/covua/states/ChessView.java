package com.doan.covua.states;



import com.doan.covua.CDef;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class ChessView extends View{

	public final InGame ingame;
	
	private float width; // width of one tile
	private float height; // height of one tile
	
	
	
	public ChessView(Context context) {
		super(context);
		this.ingame = (InGame) context;
		setFocusable(true);
		setFocusableInTouchMode(true);
	}
	
	private void getRect(int x, int y, Rect rect) {
		rect.set((int) (x * width), (int) (y * height), (int) (x
		* width + width), (int) (y * height + height));
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		Paint chessTable = new Paint();
		Paint bground	= new Paint();
		
		//draw background
		bground.setColor(0xFF333333);
		canvas.drawRect(10, 20, CDef.SCR_W - 10, CDef.SCR_H - 150, bground);
		
		/*
		for(int i=0;i<8;i++){
			canvas.drawText( String.valueOf((char)('A' + i)),CDef.xOrg + i*CDef.cellSize + CDef.cellSize/2, CDef.yOrg, bground);
		}
		*/
		
		//draw background of table
		for (int x = 0, xpos = CDef.xOrg; x < 8; x++, xpos += CDef.cellSize)
		{
			for (int y = 0, ypos = CDef.yOrg; y < 8; y++, ypos += CDef.cellSize)
			{
				if( ((x%2 == 0) && (y%2 == 0)) || ((x%2 == 1) && (y%2 == 1)))
					chessTable.setColor(Color.WHITE);
				else
					chessTable.setColor(Color.BLACK);

				chessTable.setStyle(Paint.Style.FILL);
				
				canvas.drawRect(xpos, ypos, xpos + CDef.cellSize, ypos + CDef.cellSize, chessTable);
				
			}
		}
	}
	
	public void drawPiece(Canvas canvas){
		
	}
}
