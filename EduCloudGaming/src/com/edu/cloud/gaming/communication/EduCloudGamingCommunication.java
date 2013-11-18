package com.edu.cloud.gaming.communication;

import java.awt.image.BufferedImage;

public interface EduCloudGamingCommunication {

	public BufferedImage sendImage();
	
	public void receiveCommand(String commmand);
	
	
}
