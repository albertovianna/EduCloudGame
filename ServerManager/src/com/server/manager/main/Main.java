package com.server.manager.main;

import com.server.manager.util.LogUtil;

public class Main {

	public static void main(String[] args) {
		
		try {
			
			@SuppressWarnings("unused")
			ManagerJFrame managerJFrame = new ManagerJFrame();
		} catch (Exception e) {
			LogUtil.exceptionToFile(e);
		}
	}
}
