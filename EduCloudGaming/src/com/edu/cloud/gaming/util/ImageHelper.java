package com.edu.cloud.gaming.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

public class ImageHelper {

	public BufferedImage createBufferedImageFromFilePath(String filePath, Class clazz) throws IOException, URISyntaxException {
		
		URL url = clazz.getResource(filePath);
		
		if (url != null) {
			
//			File file = new File(url.getPath());
			
//			InputStream inputStream = new FileInputStream(file);
			
//		    ImageInputStream imageInputStream = ImageIO.createImageInputStream(inputStream);
		    ImageInputStream imageInputStream = ImageIO.createImageInputStream(url.openStream());
		    
		    BufferedImage bufferedImage = ImageIO.read(imageInputStream);
		    
		    return bufferedImage;
		    
		} else {

			throw new IOException("Url vazia");
		}
	}
}
