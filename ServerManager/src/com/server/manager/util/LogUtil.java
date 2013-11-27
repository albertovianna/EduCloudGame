package com.server.manager.util;

//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.PrintWriter;

public class LogUtil {

//	private static final String FILE_PATH = "C:/Users/Alberto Vianna/Dropbox/exception.txt";
	
	public static void exceptionToFile(Exception e) {
		
//		try {
//			
//			FileWriter fstream = new FileWriter(FILE_PATH, true);
//			PrintWriter out = new PrintWriter(fstream);
//	        out.write(e.toString());
//	        e.printStackTrace(out);
//	        out.close();
//	        
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
        
		e.printStackTrace();
	}
}
