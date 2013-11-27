package com.cloudgame.core;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.cloudgame.core.models.Piece;
import com.edu.cloud.gaming.util.ImageHelper;

public class GameCore {
	
	//game attributes
	private BufferedImage currentGameImage;
	private ImageHelper imageHelper = new ImageHelper();

	private int gameColumns;
	private int gameRows;
	private Piece[][] gameMatrix;
	private Piece lastVisiblePiece;
	//------------------------
	
	//pieces attributes
	
	private BufferedImage baseIMG;
	private int baseWidth;
	private int baseHeight;
	private BufferedImage backPiece;
	private int pieceWidth;
	private int pieceHeight;
	private int pieceMargin;
	private ArrayList<Piece> listIMG;
	//-------------------------
	private long delay;
	
	public GameCore() throws IOException, URISyntaxException {
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

	private void configPieces() throws URISyntaxException, IOException{
		//base image
		
		BufferedImage baseIMG = imageHelper.createBufferedImageFromFilePath("/images/teste.png", this.getClass());
		
		baseWidth 	= baseIMG.getWidth();
		baseHeight 	= baseIMG.getHeight();
		
		//backface piece image
		backPiece 	= imageHelper.createBufferedImageFromFilePath("/images/peca0.png", this.getClass());
		pieceWidth 	= backPiece.getWidth();
		pieceHeight = backPiece.getHeight();
		pieceMargin	= -3;
		
		//pieces list
		listIMG = new ArrayList<Piece>();
		listIMG.add(new Piece(1, imageHelper.createBufferedImageFromFilePath("/images/peca1.png", this.getClass())));
		listIMG.add(new Piece(2, imageHelper.createBufferedImageFromFilePath("/images/peca2.png", this.getClass())));
		listIMG.add(new Piece(3, imageHelper.createBufferedImageFromFilePath("/images/peca3.png", this.getClass())));
		listIMG.add(new Piece(4, imageHelper.createBufferedImageFromFilePath("/images/peca4.png", this.getClass())));
		listIMG.add(new Piece(5, imageHelper.createBufferedImageFromFilePath("/images/peca5.png", this.getClass())));
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

		//baseWidth, baseHeight
		
		BufferedImage combined = new BufferedImage(baseWidth, baseHeight, BufferedImage.TYPE_INT_ARGB);
        
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
        
        Graphics g = combined.getGraphics();
  		//loop for the pieces image
        for(int r=0;r<gameRows;r++){ //rows
        	for(int c=0;c<gameColumns;c++){ //columns
        		int left 		= (pieceWidth+pieceMargin)*c;
        		int top 		= (pieceHeight+pieceMargin)*r;
        		float right 	= left+pieceWidth;
        		float bottom 	= top+pieceHeight;
        		
        		Piece piece 		= gameMatrix[r][c];
        		BufferedImage drawBitmap 	= backPiece;
        		
        		if(piece.isVisible()||piece.isPaired()){
        			drawBitmap = piece.getBitmap();
        			visiblePieces++;
        		}
        		
        		g.drawImage(drawBitmap, left, top, null);
            }
        }
        
        if(touchedPiece!=null && !foundPair && visiblePieces%2==0){
        	touchedPiece.setVisible(false);
        	lastVisiblePiece.setVisible(false);

        	generateGameIMGWithTimer(3000);
        	
        }
        
        //if any pair was founded and the number of visible pieces is even, trigger generate new image, hiding unpaired pieces
        lastVisiblePiece = touchedPiece;
        
        currentGameImage = combined;

	}
	
	public void generateGameIMGWithTimer(final long delay) {
		new Thread( new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(delay);
					generateGameIMG();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
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

	public void nextMove(float touchX, float touchY, int w) {
		
		float scale = ((float)w)/((float) baseWidth);
		
		Piece piece = getPieceByXY(touchX, touchY, scale);

		generateGameIMG(piece);

	}

	public BufferedImage getCurrentGameImage() {
		return currentGameImage;
	}

	public void setCurrentGameImage(BufferedImage currentGameImage) {
		this.currentGameImage = currentGameImage;
	}
	
	
	public String getPathImage(String path){
		URL url = this.getClass().getResource(path);
		return url.getPath();
	}
}
