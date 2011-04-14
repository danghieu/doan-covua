package com.doan.covua.states;



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
	
	//define the screen
	public static final int SCR_W = 320;
	public static final int SCR_H = 480;
	//define the chess table
	public static final int cellSize = 20;
	public static final int xOrg		= (SCR_W - (cellSize*8))/2;
	public static final int yOrg		= xOrg + 70;
	
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
		
		Paint paint = new Paint();
		
		//draw background of table
		for (int x = 0, xpos = xOrg; x < 8; x++, xpos += cellSize)
		{
			for (int y = 0, ypos = yOrg; y < 8; y++, ypos += cellSize)
			{
				if( (x%2 == 0) && (y%2 == 0))
					paint.setColor(Color.BLACK);
				else
					paint.setColor(Color.WHITE);

				paint.setStyle(Paint.Style.FILL);
				
				canvas.drawRect(xpos, ypos,cellSize,cellSize, paint);
				
			}
		}
	}
}
