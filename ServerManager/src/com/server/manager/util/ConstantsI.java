package com.server.manager.util;

public interface ConstantsI {

	public static final String NEW = "NEW";
	public static final String CLOSED = "CLOSED";
	public static final String GAMES = "GAMES";
	public static final String GAME_ESCOLHIDO = "GAME_ESCOLHIDO";
	public static final String PLAY_GAME = "PLAY_GAME";
	public static final String ERROR = "ERROR";
	
	public static final Integer ACTUAL_CLIENT_MANAGER_PORT = 9988;
	public static final Integer CLOSE_ACTUAL_CLIENT_MANAGER_PORT = 9989;
	
	public static final Integer ACTUAL_WORKER_MANAGER_PORT = 9977;
	
//	public static final Integer ACTUAL_WORKER_MANAGER_PORT_TEST = 9966;
	public static final Integer CLOSE_ACTUAL_WORKER_MANAGER_PORT = 9978;
	
	public static final String MANAGER_WINDOW_TITLE = "Manager Server";
	public static final Integer MANAGER_WINDOW_MINIMUM_SIZE = 300;
	
	public static final Integer DATA_SIZE = 1024;
	
	public static final String XML_COMMUNICATION_PATH = "/config/eduCloudGaming-config.xml";
}
