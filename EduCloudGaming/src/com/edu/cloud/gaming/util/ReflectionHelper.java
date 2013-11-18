package com.edu.cloud.gaming.util;

import com.edu.cloud.gaming.communication.EduCloudGamingCommunication;

public class ReflectionHelper {

	public EduCloudGamingCommunication returnEduCloudGamingCommunicationFromStringFilePath(String classPath) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		EduCloudGamingCommunication com = (EduCloudGamingCommunication) Class.forName(classPath).newInstance();
		
		return com;
	}
	
//	public static void main(String[] args) {
//		
//		String path = "com.cloudgaming.communication.Communication";
//		
//		ReflectionHelper reflectionHelper = new ReflectionHelper();
//		
//		try {
//			
//			reflectionHelper.returnEduCloudGamingCommunicationFromStringFilePath(path);
//			
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//	}
}
