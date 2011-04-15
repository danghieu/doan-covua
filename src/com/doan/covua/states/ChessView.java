package com.doan.covua.states;



import com.doan.covua.CDef;
import com.doan.covua.Piece;
import com.doan.covua.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class ChessView extends View{

	public final InGame ingame;
	
	private float width; // width of one tile
	private float height; // height of one tile
	
	public static byte[] boardDraw	= new byte[Piece.LENGTH];
	
	/*public static byte init_board[] = 
	{
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 9, 9, 9, 9, 9, 9, 9, 9, 0, 0,
		0, 0,13,13,13,13,13,13,13, 5, 0, 0,
		0, 0,13,13,13,13,13,13,13,13, 0, 0,
		0, 0,13,13,13,13,13,9,10,13,  0, 0,
		0, 0,13,13,13,13,13,13,13,6, 0, 0,
		0, 0,13,13,13,13,13,13,13,13, 0, 0,
		0, 0,13,13,13,13,13,13,13,13, 0, 0,
		0, 0,10,10,10,10,10,10,10,10, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	};*/
	public static byte init_board[] = 
	{
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 7, 3, 11, 5, 9, 11, 3, 7, 0, 0,
		0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0,
		0, 0,13,13,13,13,13,13,13,13, 0, 0,
		0, 0,13,13,13,13,13,13,13,13,  0, 0,
		0, 0,13,13,13,13,13,13,13,13, 0, 0,
		0, 0,13,13,13,13,13,13,13,13, 0, 0,
		0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0,
		0, 0,8, 4, 12, 6, 10, 12, 4, 8, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	};

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
		canvas.drawRect(10, 20, CDef.SCR_W - 10, CDef.SCR_H - 120, bground);
		
		/*
		for(int i=0;i<8;i++){
			canvas.drawText( String.valueOf((char)('A' + i)),CDef.xOrg + i*CDef.cellSize + CDef.cellSize/2, CDef.yOrg, bground);
		}
		*/
		init_board();
		
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
		
		//draw piece
		for (int a = 1; a <= 8; a++)
		{
			for (int b = 0; b <= 11; b++)
			{
				int c = 120 - a * 12 + b;
				int piece = boardDraw[c];
				if(piece != 0 ){
					
					int x = file(c) - 1;
					int y = 7 - (rank(c) - 1);
					
					if(piece != Piece.npiece){
						drawPiece(canvas, piece, CDef.xOrg + x*CDef.cellSize, CDef.yOrg + y*CDef.cellSize );
					}
				}
			}
		}
		
		
	}
	
	public void drawPiece(Canvas canvas, int piece, int x, int y){
		
		if(piece == Piece.npiece)
			return;
		//draw piece
		Resources r = getResources();
		Bitmap re_piece = BitmapFactory.decodeResource(r, 0);
		/*switch (piece)
		{
		//	black		white
			case  1:	case  2:	piece = 0;	break;	// pawn
			case  3:	case  4:	piece = 2;	break;	// knight
			case  5:	case  6:	piece = 5;	break;	// king
			case  7:	case  8:	piece = 1;	break;	// rook
			case  9:	case 10:	piece = 4;	break;	// queen
			case 11:	case 12:	piece = 3;	break;	// bishop
			default: return;
		}*/
		switch(piece){
		case Piece.bpawn:
			re_piece = BitmapFactory.decodeResource(r, R.drawable.bp_alt);
			break;
		case Piece.wpawn:
			re_piece = BitmapFactory.decodeResource(r, R.drawable.wp_alt);
			break;
		case Piece.bknight:
			re_piece = BitmapFactory.decodeResource(r, R.drawable.bn_alt);
			break;
		case Piece.wknight:
			re_piece = BitmapFactory.decodeResource(r, R.drawable.wn_alt);
			break;
		case Piece.bbishop:
			re_piece = BitmapFactory.decodeResource(r, R.drawable.bb_alt);
			break;
		case Piece.wbishop:
			re_piece = BitmapFactory.decodeResource(r, R.drawable.wb_alt);
			break;
		case Piece.brook:
			re_piece = BitmapFactory.decodeResource(r, R.drawable.br_alt);
			break;
		case Piece.wrook:
			re_piece = BitmapFactory.decodeResource(r, R.drawable.wr_alt);
			break;
		case Piece.wqueen:
			re_piece = BitmapFactory.decodeResource(r, R.drawable.wq_alt);
			break;
		case Piece.bqueen:
			re_piece = BitmapFactory.decodeResource(r, R.drawable.bq_alt);
			break;
		case Piece.bking:
			re_piece = BitmapFactory.decodeResource(r, R.drawable.bk_alt);
			break;
		case Piece.wking:
			re_piece = BitmapFactory.decodeResource(r, R.drawable.wk_alt);
			break;
		}
		canvas.drawBitmap(re_piece, x, y, null);
	}
	
	static int file(int square)	{ return (((square - 26) % 12) + 1); }
	static int rank(int square)	{ return (((square - 26) / 12) + 1); }
	
	public static void init_board(){
		for(int i=0; i< 144; i++){
			boardDraw[i] = init_board[i];
		}
	}
}
