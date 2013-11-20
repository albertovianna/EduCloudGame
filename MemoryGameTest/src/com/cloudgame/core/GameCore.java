package com.cloudgame.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;

import com.cloudgame.core.models.Piece;

public class GameCore {
	
	//game attributes
	private Bitmap currentGameImage;

	private int gameColumns;
	private int gameRows;
	private Piece[][] gameMatrix;
	private Piece lastVisiblePiece;
	//------------------------
	
	//pieces attributes
	
	private Bitmap baseIMG;
	private int baseWidth;
	private int baseHeight;
	private Bitmap backPiece;
	private int pieceWidth;
	private int pieceHeight;
	private int pieceMargin;
	private ArrayList<Piece> listIMG;
	//-------------------------
	
	public GameCore() {
		configGameAttrs();
		configPieces();
		setupRandomPieces();
		generateGameIMG();
	}
	
	private void configGameAttrs(){
		gameColumns = 5;
		gameRows 	= 2;
		gameMatrix	= new Piece[gameRows][gameColumns];
	}

	private void configPieces(){
		//base image
		baseIMG 	= BitmapFactory.decodeFile("images/teste.png");
		baseWidth 	= baseIMG.getWidth();
		baseHeight 	= baseIMG.getHeight();
		
		//backface piece image
		backPiece 	= BitmapFactory.decodeFile("images/peca0.png");
		pieceWidth 	= backPiece.getWidth();
		pieceHeight = backPiece.getHeight();
		pieceMargin	= -3;
		
		//pieces list
		listIMG = new ArrayList<Piece>();
		listIMG.add(new Piece(1, BitmapFactory.decodeFile("/images/peca1.png")));
		listIMG.add(new Piece(2, BitmapFactory.decodeFile("/images/peca2.png")));
		listIMG.add(new Piece(3, BitmapFactory.decodeFile("/images/peca3.png")));
		listIMG.add(new Piece(4, BitmapFactory.decodeFile("/images/peca4.png")));
		listIMG.add(new Piece(5, BitmapFactory.decodeFile("/images/peca5.png")));
	}
	
	private Piece getPieceByXY(float touchX, float touchY, float scaleContainer){
		int row 	= (int)Math.floor(touchY/((pieceHeight+pieceMargin)*scaleContainer));
		int column 	= (int)Math.floor(touchX/((pieceWidth+pieceMargin)*scaleContainer));
		
		return gameMatrix[row][column];
	}
	
	private void generateGameIMG(){
		generateGameIMG(null);
	}
	
	private void generateGameIMG(Piece touchedPiece){

		Bitmap generated = Bitmap.createBitmap(baseWidth, baseHeight, baseIMG.getConfig());
		
        Canvas canvas = new Canvas(generated);
        //canvas.drawBitmap(baseIMG, null, new RectF(0,0,baseWidth,baseHeight), null);
        
        int visiblePieces = 0;
        boolean foundPair = false;
        
        //if touchedPiece exists
        if(touchedPiece!=null){
        	
        	//sets visible true for the touched piece
        	touchedPiece.setVisible(true);
      		
	        //piece is visible and has the same index - is an pair
	  		if(lastVisiblePiece!=null && touchedPiece.getIndex()==lastVisiblePiece.getIndex()){
	  			touchedPiece.setPaired(true);
	  			lastVisiblePiece.setPaired(true);
	  			foundPair = true;
	  		}
	  		
        }
        //----------------------
        
  		//loop for the pieces image
        for(int r=0;r<gameRows;r++){ //rows
        	for(int c=0;c<gameColumns;c++){ //columns
        		//canvas.drawBitmap(backPiece, (pieceWidth+pieceMargin)*c, (pieceHeight+pieceMargin)*r, null);
        		float left 		= (pieceWidth+pieceMargin)*c;
        		float top 		= (pieceHeight+pieceMargin)*r;
        		float right 	= left+pieceWidth;
        		float bottom 	= top+pieceHeight;
        		
        		Piece piece 		= gameMatrix[r][c];
        		Bitmap drawBitmap 	= backPiece;
        		
        		if(piece.isVisible()||piece.isPaired()){
        			drawBitmap = piece.getBitmap();
        			visiblePieces++;
        		}
        		
        		canvas.drawBitmap(drawBitmap, null, new RectF(left,top,right,bottom), null);
            }
        }
        
        
        //if any pair was founded and the number of visible pieces is even, trigger generate new image, hiding unpaired pieces
        if(touchedPiece!=null && !foundPair && visiblePieces%2==0){
        	touchedPiece.setVisible(false);
        	lastVisiblePiece.setVisible(false);

        	generateGameIMGWithTimer(1000);
        	
        }
        
        lastVisiblePiece = touchedPiece;
        
        currentGameImage = generated;

	}
	
	private void generateGameIMGWithTimer(long delay) {
        Timer autoUpdate = new Timer();
        autoUpdate.schedule(new TimerTask() {
			@Override
			public void run() {
				new Runnable() {
				     public void run() {
				    	 generateGameIMG();
				    }
				};
			}
		},delay);
	}
	
	private void setupRandomPieces(){
		
		ArrayList<Integer> ex = new ArrayList<Integer>();
		int[] randomizedCount = new int[listIMG.size()];
		Random random = new Random();
		
		for(int r=0;r<gameRows;r++){ //rows
        	for(int c=0;c<gameColumns;c++){ //columns
        		int randomInt = getRandomWithExclusion(random,0,listIMG.size(),ex.toArray(new Integer[ex.size()]));
        		try {
					gameMatrix[r][c] = (Piece)listIMG.get(randomInt).clone();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
        		randomizedCount[randomInt]++;
        		if(randomizedCount[randomInt]>=2){
        			ex.add(randomInt);
        		}
            }
        }
	}
	
	private int getRandomWithExclusion(Random rnd, int start, int end, Integer[] exclude) {
		Arrays.sort(exclude);
		int rand = 0;
		do {
			rand = (int) rnd.nextInt(end) + start;
		} while (Arrays.binarySearch(exclude, rand) >= 0);
		return rand;
	}

	public void nextMove(float touchX, float touchY, float scale) {
		Piece piece = getPieceByXY(touchX, touchY, scale);

		generateGameIMG(piece);

	}

	public Bitmap getCurrentGameImage() {
		return currentGameImage;
	}

	public void setCurrentGameImage(Bitmap currentGameImage) {
		this.currentGameImage = currentGameImage;
	}
}
