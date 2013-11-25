package com.cloudgaming.memorygame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.cloudgaming.vo.PieceVO;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	ImageView gameIMG;
	TextView txtDegub;
	
	//game attributes
	int gameColumns;
	int gameRows;
	PieceVO[][] gameMatrix;
	PieceVO lastVisiblePiece;
	//------------------------
	
	//pieces attributes
	Bitmap baseIMG;
	int baseWidth;
	int baseHeight;
	Bitmap backPiece;
	int pieceWidth;
	int pieceHeight;
	int pieceMargin;
	ArrayList<PieceVO> listIMG;
	//-------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		configGameAttrs();
		configPieces();
		setupRandomPieces();
		configGameIMG();
		generateGameIMG();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	private void configGameAttrs(){
		gameColumns = 5;
		gameRows 	= 2;
		gameMatrix	= new PieceVO[gameRows][gameColumns];
	}
	
	private void configGameIMG(){
		
		txtDegub = (TextView) findViewById(R.id.txtDegub);
		gameIMG = (ImageView) findViewById(R.id.gameIMG);
		
		gameIMG.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				int imgW		= gameIMG.getWidth();
				int imgH		= gameIMG.getHeight();
				float touchX 	= event.getX();
				float touchY 	= event.getY();
				float scale		= ((float)imgW)/((float)MainActivity.this.baseWidth);

				txtDegub.setText("W: "+Integer.toString(imgW)+" H: "+Integer.toString(imgH)+" X: "+Float.toString(touchX)+" Y: "+Float.toString(touchY));
				
				PieceVO piece = getPieceByXY(touchX,touchY,scale);
				
				generateGameIMG(piece);
				
				return false;
			}
		});
		
	}
	
	private PieceVO getPieceByXY(float touchX, float touchY, float scaleContainer){
		int row 	= (int)Math.floor(touchY/((pieceHeight+pieceMargin)*scaleContainer));
		int column 	= (int)Math.floor(touchX/((pieceWidth+pieceMargin)*scaleContainer));
		
		return gameMatrix[row][column];
	}
	
	private void generateGameIMG(){
		generateGameIMG(null);
	}
	
	private void generateGameIMG(PieceVO touchedPiece){

		Bitmap generated 	= Bitmap.createBitmap(baseWidth, baseHeight, baseIMG.getConfig());
		
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
        		
        		PieceVO piece 		= gameMatrix[r][c];
        		Bitmap drawBitmap 	= backPiece;
        		
        		if(piece.isVisible()||piece.isPaired()){
        			drawBitmap = piece.getBitmap();
        			visiblePieces++;
        		}
        		
        		canvas.drawBitmap(drawBitmap, null, new RectF(left,top,right,bottom), null);
            }
        }
        
        //sets generated image on imageView
        gameIMG.setImageBitmap(generated);
        
        //if any pair was founded and the number of visible pieces is even, trigger generate new image, hiding unpaired pieces
        if(touchedPiece!=null && !foundPair && visiblePieces%2==0){
        	touchedPiece.setVisible(false);
        	lastVisiblePiece.setVisible(false);

        	generateGameIMGWithTimer(1000);
        	
        	/*//wait to regenerate image
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	
        	//regenerate image
        	generateGameIMG();*/
        	
        }
        
        lastVisiblePiece = touchedPiece;

	}
	
	private void generateGameIMGWithTimer(long delay) {
        Timer autoUpdate = new Timer();
        autoUpdate.schedule(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
				     public void run() {

				    	 generateGameIMG();

				    }
				});
			}
		},delay);
	}
	
	private void configPieces(){
		Options options = new BitmapFactory.Options();
		options.inScaled = false;
		
		//base image
		baseIMG 	= BitmapFactory.decodeResource(getResources(),R.drawable.teste,options);
		baseWidth 	= baseIMG.getWidth();
		baseHeight 	= baseIMG.getHeight();
		
		//backface piece image
		backPiece 	= BitmapFactory.decodeResource(getResources(),R.drawable.peca0,options);
		pieceWidth 	= backPiece.getWidth();
		pieceHeight = backPiece.getHeight();
		pieceMargin	= -3;
		
		//pieces list
		listIMG = new ArrayList<PieceVO>();
		listIMG.add(new PieceVO(1, BitmapFactory.decodeResource(getResources(),R.drawable.peca1,options)));
		listIMG.add(new PieceVO(2, BitmapFactory.decodeResource(getResources(),R.drawable.peca2,options)));
		listIMG.add(new PieceVO(3, BitmapFactory.decodeResource(getResources(),R.drawable.peca3,options)));
		listIMG.add(new PieceVO(4, BitmapFactory.decodeResource(getResources(),R.drawable.peca4,options)));
		listIMG.add(new PieceVO(5, BitmapFactory.decodeResource(getResources(),R.drawable.peca5,options)));
	}
	
	private void setupRandomPieces(){
		
		ArrayList<Integer> ex = new ArrayList<Integer>();
		int[] randomizedCount = new int[listIMG.size()];
		Random random = new Random();
		
		for(int r=0;r<gameRows;r++){ //rows
        	for(int c=0;c<gameColumns;c++){ //columns
        		int randomInt = getRandomWithExclusion(random,0,listIMG.size(),ex.toArray(new Integer[ex.size()]));
        		try {
					gameMatrix[r][c] = (PieceVO)listIMG.get(randomInt).clone();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
        		randomizedCount[randomInt]++;
        		if(randomizedCount[randomInt]>=2){
        			ex.add(randomInt);
        		}
            }
        }
		
		
		//gameMatrix = gameMatrix;
	}
	
	/* generate a random number in range, excluding specific */	
	private int getRandomWithExclusion(Random rnd, int start, int end, Integer[] exclude) {
	  Arrays.sort(exclude);
	  int rand = 0;
	  do {
	    rand = (int) rnd.nextInt(end)+start;
	  } 
	  while (Arrays.binarySearch(exclude, rand) >= 0);
	  return rand;
	}

}
