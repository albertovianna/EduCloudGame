package com.server.manager.workManager;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import com.server.manager.clientManager.ClientConnectionManager;
import com.server.manager.util.ConstantsI;
import com.server.manager.util.LogUtil;

public class WorkerManager implements Runnable {

	private Integer actualPort = null;
	private DatagramSocket workerManagerSocket = null;
	
	private ClientConnectionManager clientConnectionManager;
	
	private List<WorkerDTO> workersList;
	
	public WorkerManager(ClientConnectionManager clientConnectionManager) {

		actualPort = ConstantsI.ACTUAL_WORKER_MANAGER_PORT;
		workersList = new ArrayList<WorkerDTO>();
		this.clientConnectionManager = clientConnectionManager;
	}

	@Override
	public void run() {
		
		try {
			
			workerManagerSocket = new DatagramSocket(actualPort);
			
			byte[] receiveData; 
			
			while (true) {
				
				try {
					
					receiveData = new byte[ConstantsI.DATA_SIZE]; 
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
					
					workerManagerSocket.receive(receivePacket);
					
					String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
					
					if (message.equalsIgnoreCase(ConstantsI.NEW)) {
						
						WorkerDTO workerDTO = new WorkerDTO();
						workerDTO.setIp(receivePacket.getAddress());
						workerDTO.setPort(receivePacket.getPort());
						
						System.out.println("Worker connected! "+workerDTO.getIp()+":"+workerDTO.getPort());
						
						this.workersList.add(workerDTO);
						clientConnectionManager.setWorkersList(workersList);
						
					} else if (message.equalsIgnoreCase(ConstantsI.CLOSED)) {
						
						for (int i = 0; i < workersList.size(); i++) {
							
							WorkerDTO workerDTO = workersList.get(i);
							
							if ( workerDTO.getIp().equals( receivePacket.getAddress() ) ) {
								
								System.out.println("Worker Closed! "+workerDTO.getIp()+":"+workerDTO.getPort());
								
								workersList.remove(workerDTO);
								clientConnectionManager.setWorkersList(workersList);
							}
						}
					}
					
				} catch (Exception e) {
					LogUtil.exceptionToFile(e);
				}
			}
		} catch (SocketException e) {
			
			System.err.println("####################################");
			System.err.println("UDP Port "+actualPort+" is occupied.");
			System.err.println("####################################");
			
			LogUtil.exceptionToFile(e);
		} catch (Exception e) {
			LogUtil.exceptionToFile(e);
		}
	}
	
	public List<WorkerDTO> getWorkersList() {
		return workersList;
	}
	public void setWorkersList(List<WorkerDTO> workersList) {
		this.workersList = workersList;
	}
	public ClientConnectionManager getClientConnectionManager() {
		return clientConnectionManager;
	}
	public void setClientConnectionManager(ClientConnectionManager clientConnectionManager) {
		this.clientConnectionManager = clientConnectionManager;
	}
	
}
