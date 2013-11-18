package com.cloudgaming.communication;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;

import com.edu.cloud.gaming.communication.EduCloudGamingCommunication;
import com.edu.cloud.gaming.util.ImageHelper;

public class Communication implements EduCloudGamingCommunication {
	
	private static final String IMAGE_DRIVER = "0";
	private static final String IMAGE_MAP = "1";
	
	private static final String PATH_DRIVER = "/images/driver.png";
	private static final String PATH_MAP = "/images/map_icon.png";
	
	private String flagImage = IMAGE_MAP;
	
	@Override
	public BufferedImage sendImage() {
		
		ImageHelper imageHelper = new ImageHelper();
		
		String path = null;
		
		if (flagImage != null && ! flagImage.isEmpty()) {
		
			if (flagImage.equals(IMAGE_DRIVER)) {
				path = PATH_DRIVER;
			} else if (flagImage.equals(IMAGE_MAP)) {
				path = PATH_MAP;
			}
		}
		
		BufferedImage bufferedImage = null;
		
		if (path != null && ! path.isEmpty()) {
			try {
				bufferedImage = imageHelper.createBufferedImageFromFilePath(path, this.getClass());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		
		
		return bufferedImage;
	}

	@Override
	public void receiveCommand(String command) {
		
		if (command != null && ! command.isEmpty()) {
		
			if (flagImage.equals(IMAGE_DRIVER)) {
				flagImage = IMAGE_MAP;
			} else if (flagImage.equals(IMAGE_MAP)) {
				flagImage = IMAGE_DRIVER;
			}
		}		
	}

}
