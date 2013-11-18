package com.server.worker.util;

public interface ConstantsI {

	public static final String NEW = "NEW";
	public static final String CLOSED = "CLOSED";
	
	public static final String PLAY_GAME = "PLAY_GAME";
	public static final String PLAY_MANAGER = "PLAY_MANAGER";
	public static final String COMMAND_MANAGER = "COMMAND_MANAGER";
	public static final String ERROR = "ERROR";
	
	public static final Integer START_FREE_PORT_SCANNING = 1024;
	public static final Integer STOP_FREE_PORT_SCANNING = 65535;
	
	public static final String WORKER_WINDOW_TITLE = "Worker Server";
	public static final Integer WORKER_WINDOW_MINIMUM_SIZE = 300;
	
	public static final String MANAGER_HOSTNAME = "127.0.0.1";
	public static final Integer MANAGER_PORT = 9977;
//	public static final Integer WORKER_PORT_PLAY = 9966;
	public static final Integer WORKER_PLAY_GAME_OFF_PORT = 9989;
	
	public static final Integer DATA_SIZE = 1024;
	
	public static final Integer SOCKET_TIMEOUT = 100000;
	
	public static final String XML_COMMUNICATION_PATH = "config/eduCloudGaming-config.xml";
}
