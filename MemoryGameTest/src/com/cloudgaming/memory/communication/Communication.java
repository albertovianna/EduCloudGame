package com.cloudgaming.memory.communication;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;

import android.graphics.Bitmap;

import com.cloudgame.core.GameCore;
import com.edu.cloud.gaming.communication.EduCloudGamingCommunication;

public class Communication implements EduCloudGamingCommunication {
	
	private static final String IMAGE_DRIVER = "0";
	private static final String IMAGE_MAP = "1";
	
	private static final int WIDTH_IMAGE_INDEX=0;
	private static final int HEIGHT_IMAGE_INDEX=1;
	private static final int X_POSITION_INDEX=2;
	private static final int Y_POSITION_INDEX=3;
	private static final int WIDE_SCREN_INDEX=4;
	
	
	private static final String PATH_DRIVER = "/images/driver.png";
	private static final String PATH_MAP = "/images/map_icon.png";
	
	private String flagImage = IMAGE_MAP;

	private GameCore game;
	
	public Communication() {
		try {
			game = new GameCore();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public BufferedImage sendImage() {
		
		return game.getCurrentGameImage();
		
	}

	@Override
	public void receiveCommand(String command) {
		
		String[] commands = command.split("\\|");
		
		int imgW		= Integer.valueOf(commands[WIDTH_IMAGE_INDEX]);
		int imgH		= Integer.valueOf(commands[HEIGHT_IMAGE_INDEX]);
		float touchX 	= Float.valueOf(commands[X_POSITION_INDEX]);
		float touchY 	= Float.valueOf(commands[Y_POSITION_INDEX]);

		game.nextMove(touchX,touchY,imgW);
		
	}

}
