package com.server.worker.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.server.worker.clientManager.ClientManager;
import com.server.worker.util.ConstantsI;

public class WorkerJFrame extends JFrame {

	private static final long serialVersionUID = -6509584379735226414L;
	
	private JPanel panel;  
	private JLabel msg; 
	private Dimension dimension;
	
	private Thread clientThread;
	private ClientManager clientManager;
	
	private DatagramSocket managerSocket;
	
	public WorkerJFrame() {
		
		super(ConstantsI.WORKER_WINDOW_TITLE);
		
		startAsWorker();
		
		initiateThreads();
		
		this.configureWindow();
	}
	
	private void startAsWorker() {
		
		System.out.println("Worker connecting at: "+ConstantsI.MANAGER_HOSTNAME+":"+ConstantsI.MANAGER_PORT);
		
		messageAsWorker(ConstantsI.NEW);
	}
	
	private void stopAsWorker() {
		
		System.out.println("Worker closed!");
		
		messageAsWorker(ConstantsI.CLOSED);
	}
	
	private void messageAsWorker(String message) {
		
		byte[] sendData;
		
		try {
			
			managerSocket = new DatagramSocket();

//			InetSocketAddress ipAddress = new InetSocketAddress(ConstantsI.MANAGER_HOSTNAME, ConstantsI.MANAGER_PORT);
			InetAddress ipAddress = InetAddress.getByName(ConstantsI.MANAGER_HOSTNAME);
			
			sendData = message.getBytes();
			
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, ConstantsI.MANAGER_PORT);
			
			managerSocket.send(sendPacket);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	private void initiateThreads() {
		
		clientManager = new ClientManager(managerSocket);
		clientThread = new Thread(clientManager);
		clientThread.start();
		
	}
	
	private void configureWindow() {
		
		panel = new JPanel();  
        msg = new JLabel("Worker Active");  
        dimension = new Dimension(ConstantsI.WORKER_WINDOW_MINIMUM_SIZE, ConstantsI.WORKER_WINDOW_MINIMUM_SIZE);
        
        panel.add(msg);  
        
        setMinimumSize(dimension);
        getContentPane().add(panel, BorderLayout.CENTER);  
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        
        addWindowListener(new WindowAdapter() {
        	
        	@Override
        	public void windowClosing(WindowEvent arg0) {
        		super.windowClosing(arg0);
        		
        		stopAsWorker();
        	}
		});
        
        pack();  
        setVisible(true);
	}
}
