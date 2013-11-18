package com.server.manager.clientManager;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;

import com.edu.cloud.gaming.dto.EduCloudGamingDTO;
import com.edu.cloud.gaming.dto.GameDTO;
import com.edu.cloud.gaming.util.XMLManager;
import com.server.manager.util.ConstantsI;
import com.server.manager.workManager.WorkerDTO;

public class ClientConnectionManager implements Runnable {
	
	private Integer actualPort;
	private DatagramSocket clientManagerSocket = null;
	
	private List<GameDTO> gamesList;
	
	private List<WorkerDTO> workersList;
//	private List<WorkerDTO> workersUsedList;
	
	public ClientConnectionManager() {
		
		actualPort = ConstantsI.ACTUAL_CLIENT_MANAGER_PORT;
		
//		workersUsedList = new ArrayList<WorkerDTO>();
		
		this.checkActiveGames();
	}
	
	public void checkActiveGames() {
		
		XMLManager xmlManager = new XMLManager();
		
		EduCloudGamingDTO eduCloudGamingDTO = xmlManager.convertXMLtoEduCloudGamingDTO(ConstantsI.XML_COMMUNICATION_PATH);
		
		this.gamesList = eduCloudGamingDTO.getGame();
		
		//testes abaixo da arquitetura
		
//		this.gamesList = new ArrayList<GameDTO>();
		
//		GameDTO gameDTO;
//		gameDTO = new GameDTO();
//		gameDTO.setName("Labirinto");
//		gamesList.add(gameDTO);
//		gameDTO = new GameDTO();
//		gameDTO.setName("Memória");
//		gamesList.add(gameDTO);
//		gameDTO = new GameDTO();
//		gameDTO.setName("Teste");
//		gamesList.add(gameDTO);
	}
	
	/**
	 * GAMES # <Game name> | <Game name> | ...
	 * @return 
	 */
	private String createStringGamesList(List<GameDTO> gamesList) {
		
		String result = ConstantsI.GAMES+"#";
		
		for (int i = 0; i < gamesList.size(); i++) {
			
			GameDTO game = gamesList.get(i);
			
			if (i == 0) {
				
				result = result + game.getName();
				
			} else {

				result = result +"|"+ game.getName();
			}
		}
		
		return result;
	}
	
	@Override
	public void run() {
		
		try {
			
			clientManagerSocket = new DatagramSocket(actualPort);
			
			byte[] receiveData; 
			
			while (true) {
				
				try {
					
					receiveData = new byte[ConstantsI.DATA_SIZE]; 
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
					
					clientManagerSocket.receive(receivePacket);
					
					String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
					
					InetAddress clientIpAddress = receivePacket.getAddress(); 
					int clientPort = receivePacket.getPort(); 
					
					if (message.contains(ConstantsI.GAMES)) {

						String gamesMessage = createStringGamesList(gamesList);
						
						byte[] sendData = gamesMessage.getBytes();
						
						DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientIpAddress, clientPort); 

						clientManagerSocket.send(sendPacket); 
						
					} else if (message.contains(ConstantsI.GAME_ESCOLHIDO)) {
						
						String[] firstStringArray = message.split("#");
						
						String type = firstStringArray[0];
						
						if (type.equalsIgnoreCase(ConstantsI.GAME_ESCOLHIDO)) {
							
							//nenhum worker conectado
							if (workersList == null) {
								
								String sendMessage = ConstantsI.ERROR+"#"
										+"Nenhum worker conectado";
				
								byte[] sendData = sendMessage.getBytes();
								
								DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientIpAddress, clientPort); 
			
								clientManagerSocket.send(sendPacket); 
								
							} else {
								
								boolean workerAvailable = false;
								
								for (WorkerDTO workerDTO : workersList) {
									
									if ( ! workerDTO.isUsed()) {
										
										workerDTO.setUsed(true);
										
										workerAvailable = true;
										
										String gameName = firstStringArray[1];
										
										// PLAY_GAME # <Game game> | <ClientIP ip> : <ClientPort port>
										String sendMessage = ConstantsI.PLAY_GAME+"#"
																	+gameName+"|" 
																	+clientIpAddress.getHostAddress()+":"
																	+clientPort;
																	
										byte[] sendData = sendMessage.getBytes();
										
										DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, workerDTO.getIp(), workerDTO.getPort()); 
					
										clientManagerSocket.send(sendPacket); 
									}	
								}
								
								//todos os workers estão ocupados
								if ( ! workerAvailable) {
									
									String sendMessage = ConstantsI.ERROR+"#"
											+"Todos os workers estão ocupados";
					
									byte[] sendData = sendMessage.getBytes();
									
									DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientIpAddress, clientPort); 
				
									clientManagerSocket.send(sendPacket); 
								}
							}
						}
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (SocketException e) {
			
			System.err.println("####################################");
			System.err.println("UDP Port "+actualPort+" is occupied.");
			System.err.println("####################################");
			
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<WorkerDTO> getWorkersList() {
		return workersList;
	}
	public void setWorkersList(List<WorkerDTO> workersList) {
		this.workersList = workersList;
	}
}
