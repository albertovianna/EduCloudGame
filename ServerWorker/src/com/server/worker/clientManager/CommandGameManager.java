package com.server.worker.clientManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.edu.cloud.gaming.communication.EduCloudGamingCommunication;
import com.server.worker.util.ConstantsI;
import com.server.worker.util.SocketConnectionUtil;

public class CommandGameManager extends Thread {

	private ClientDataDTO clientDataDTO;
	private ServerSocket serverSocket;
	private DatagramSocket datagramSocket;
	private Socket socket;
	
	private EduCloudGamingCommunication com;
	
	
	public CommandGameManager(ClientDataDTO clientDataDTO, EduCloudGamingCommunication com) {
		
		this.clientDataDTO = clientDataDTO;
		this.com = com;
	}
	
	public static String fromStream(InputStream in) throws IOException
	{
	    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	    StringBuilder out = new StringBuilder();
	    String line;
	    while ((line = reader.readLine()) != null) {
	        out.append(line);
	    }
	    return out.toString();
	}
	
	@Override
	public void run() {
		
		try {
			serverSocket = SocketConnectionUtil.initServerSocket(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (serverSocket != null) {
			
			try {
				
				//--Conexao serversocket
				String playMessage = ConstantsI.COMMAND_MANAGER+"#"+serverSocket.getLocalPort();
				
				byte[] sendData = playMessage.getBytes();
				
				datagramSocket = new DatagramSocket();
				
				InetAddress inetAddress = InetAddress.getByName(clientDataDTO.getIp());
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, inetAddress, clientDataDTO.getPort()); 
				datagramSocket.send(sendPacket);
			
				//--
			
				while (true) {
					
					try {
						socket = SocketConnectionUtil.initSocketServer(serverSocket);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					InputStream is = socket.getInputStream();
					
					String command = fromStream(is);
					
					com.receiveCommand(command);
					
				}
				
			} catch (SocketException e) {
				
				System.err.println("####################################");
				System.err.println("UDP Port "+datagramSocket.getPort()+" is occupied.");
				System.err.println("####################################");
				
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
