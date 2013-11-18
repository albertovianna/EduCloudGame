package com.server.worker.clientManager;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import com.edu.cloud.gaming.communication.EduCloudGamingCommunication;
import com.edu.cloud.gaming.dto.EduCloudGamingDTO;
import com.edu.cloud.gaming.dto.GameDTO;
import com.edu.cloud.gaming.util.ReflectionHelper;
import com.edu.cloud.gaming.util.XMLManager;
import com.server.worker.util.ConstantsI;

public class ClientManager implements Runnable {

	private DatagramSocket managerSocket;
	
	public ClientManager(DatagramSocket managerSocket) {
		
		this.managerSocket = managerSocket; 
	}
	
	@Override
	public void run() {
		
		try {
			
			//TODO por enquanto somente um cliente por vez. 
			
			//qualquer coisa colocar um while true aqui
			
			byte[] receiveData = new byte[ConstantsI.DATA_SIZE]; 
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			
			managerSocket.receive(receivePacket);
			
			String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
			
			if (message.contains(ConstantsI.PLAY_GAME)) {
				
				String[] firstStringArray = message.split("#");
				
//				String type = firstStringArray[0];
				
				String gameClient = firstStringArray[1];
				
				ClientDataDTO clientDataDTO = new ClientDataDTO();
				
				String[] secondStringArray = gameClient.split("\\|");
				
				String[] thirdStringArray = secondStringArray[1].split(":");  
				
				clientDataDTO.setGameName(secondStringArray[0]);
				clientDataDTO.setIp(thirdStringArray[0]);
				clientDataDTO.setPort(Integer.parseInt(thirdStringArray[1]));
				
				//--Obtendo a classe do jogo
				XMLManager xmlManager = new XMLManager();
				
				EduCloudGamingDTO eduCloudGamingDTO = xmlManager.convertXMLtoEduCloudGamingDTO(ConstantsI.XML_COMMUNICATION_PATH);
				GameDTO gameDTO = null;
				
				for (GameDTO gameAux : eduCloudGamingDTO.getGame()) {
					
					if (clientDataDTO.getGameName().equals(gameAux.getName())) {
						gameDTO = gameAux;
						break;
					}
				}
				
				if (gameDTO == null) {
					throw new Exception("Jogo não encontrado!");
				}
				
				ReflectionHelper reflectionHelper = new ReflectionHelper();
				
				EduCloudGamingCommunication com = reflectionHelper.returnEduCloudGamingCommunicationFromStringFilePath(gameDTO.getCommunicationClass());
				//--
				
				PlayGameManager playGameManager = new PlayGameManager(clientDataDTO, com);
				Thread thread = new Thread(playGameManager);
				thread.start();
				
				CommandGameManager commandGameManager = new CommandGameManager(clientDataDTO, com);
				thread = new Thread(commandGameManager);
				thread.start();
			}
			
		} catch (SocketException e) {
			
			System.err.println("####################################");
			System.err.println("UDP Port "+managerSocket.getPort()+" is occupied.");
			System.err.println("####################################");
			
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
}
