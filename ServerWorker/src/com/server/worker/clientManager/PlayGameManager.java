package com.server.worker.clientManager;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.imageio.ImageIO;

import com.edu.cloud.gaming.communication.EduCloudGamingCommunication;
import com.server.worker.util.ConstantsI;
import com.server.worker.util.SocketConnectionUtil;

public class PlayGameManager extends Thread {

	private ClientDataDTO clientDataDTO;
	private DatagramSocket datagramSocket;
	private ServerSocket serverSocket;
	private Socket socket;
	
	private EduCloudGamingCommunication com;
	
	public PlayGameManager(ClientDataDTO clientDataDTO, EduCloudGamingCommunication com) {
		
		this.clientDataDTO = clientDataDTO;
		this.com = com;
	}
	
	@Override
	public void run() {
		
		try {
			
			try {
				serverSocket = SocketConnectionUtil.initServerSocket(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (serverSocket != null) {
				
				//--Conexao serversocket
				String playMessage = ConstantsI.PLAY_MANAGER+"#"+serverSocket.getLocalPort();
				
				byte[] sendData = playMessage.getBytes();
				
				datagramSocket = new DatagramSocket();
				
				InetAddress inetAddress = InetAddress.getByName(clientDataDTO.getIp());
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, inetAddress, clientDataDTO.getPort()); 
				datagramSocket.send(sendPacket); 
				//--
				
//				int i = 0;
//				String path = null;
				
				while (true) {
					
					try {
						socket = SocketConnectionUtil.initSocketServer(serverSocket);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
//					i++;
//					
//					if (i % 2 == 0) {
//						
//						path = "C:\\Users\\Alberto Vianna\\Desktop\\Beto\\CC\\Android Development\\workspaces\\wsExemplos\\DeliveryTracking\\res\\drawable-ldpi\\map_icon.png";
//					} else {
//						
//						path = "C:\\Users\\Alberto Vianna\\Desktop\\Beto\\CC\\Android Development\\workspaces\\wsExemplos\\DeliveryTracking\\res\\drawable-ldpi\\driver.png";
//					}
					
//					BufferedImage bufferedImage = ImageIO.read(new File(path));
					BufferedImage bufferedImage = com.sendImage();
					
					if (bufferedImage != null) {
						
						ImageIO.write(bufferedImage, "png", socket.getOutputStream());
						
						socket.getOutputStream().flush();
						
//						System.out.println("Imagem enviada");
						
						socket.getOutputStream().close();
						socket.close();
					}
					
					
					
					/*
					BufferedImage bufferedImage = ImageIO.read(new File("C:\\Users\\Alberto Vianna\\Desktop\\Beto\\CC\\Android Development\\workspaces\\wsExemplos\\DeliveryTracking\\res\\drawable-ldpi\\map_icon.png"));
//					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(bufferedImage, "png", socket.getOutputStream());
//					byte byteTeste[] = baos.toByteArray();
					
					
					socket.getOutputStream().flush();
					
					/*
					
//					this.sleep(10000);
					
					bufferedImage = ImageIO.read(new File("C:\\Users\\Alberto Vianna\\Desktop\\Beto\\CC\\Android Development\\workspaces\\wsExemplos\\DeliveryTracking\\res\\drawable-ldpi\\driver.png"));
//					baos = new ByteArrayOutputStream();
					ImageIO.write(bufferedImage, "png", socket.getOutputStream());
//					byteTeste = baos.toByteArray();
					
//					inetAddress = InetAddress.getByName(clientDataDTO.getIp());
//					sendPacket = new DatagramPacket(byteTeste, byteTeste.length, inetAddress, clientDataDTO.getPort()); 
//					socket.send(sendPacket);
					socket.getOutputStream().flush();
					*/
				}
			}
			
		} catch (SocketException e) {
			
			System.err.println("####################################");
			System.err.println("UDP Port "+socket.getPort()+" is occupied.");
			System.err.println("####################################");
			
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
