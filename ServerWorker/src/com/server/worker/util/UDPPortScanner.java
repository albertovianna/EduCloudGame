package com.server.worker.util;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;


public class UDPPortScanner {
	
	public UDPPortScanner() {
		
	}
	
	private List<Integer> findFreePorts(Integer startPort, Integer stopPort) {
		
		
		System.out.println ("Checking UDP ports " + startPort +" to " + stopPort );

		List<Integer> portsList = new ArrayList<Integer>();
		
		for (int port = startPort; port <= stopPort; port++) {
			
			try {
				
				DatagramSocket ds = new DatagramSocket(port);
				ds.close();
				
				portsList.add(port);
				
			} catch (IOException ex) {
				
				System.out.println("UDP Port "+port+" is occupied.");
			}
		}
		
		return portsList;
	}
	
	public Integer findFreePort() {
		
		List<Integer> freePortsList = findFreePorts(ConstantsI.START_FREE_PORT_SCANNING, ConstantsI.STOP_FREE_PORT_SCANNING);
		
		if (freePortsList != null && ! freePortsList.isEmpty()) {
			
			return freePortsList.get(1);
		} else {
			return null;
		}
	}
}
