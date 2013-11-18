package com.server.manager.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.server.manager.clientManager.ClientConnectionManager;
import com.server.manager.util.ConstantsI;
import com.server.manager.workManager.WorkerManager;

public class ManagerJFrame extends JFrame {

	private static final long serialVersionUID = 3138716508175563511L;
	
//	private JPanel panel;  
//	private JLabel title; 
//	private Dimension dimension;
	
	private ClientConnectionManager clientConnectionManager;
	private Thread clientThread;
	
	private WorkerManager workerManager;
	private Thread workerManagerThread;
	
	public ManagerJFrame() {
		
		super(ConstantsI.MANAGER_WINDOW_TITLE);
		
		initiateThreads();
		
		this.configureWindow();
	}
	
	private void initiateThreads() {
		
		clientConnectionManager = new ClientConnectionManager();
		clientThread = new Thread(clientConnectionManager);
		clientThread.start();
		
		workerManager = new WorkerManager(clientConnectionManager);
		workerManagerThread = new Thread(workerManager);
		workerManagerThread.start();
		
	}
	
	private void configureWindow() {
		
		JPanel panel = new JPanel();  
		JLabel title = new JLabel("Manager Active");  
        Dimension dimension = new Dimension(ConstantsI.MANAGER_WINDOW_MINIMUM_SIZE, ConstantsI.MANAGER_WINDOW_MINIMUM_SIZE);
        
        panel.add(title);  
        
        setMinimumSize(dimension);
        getContentPane().add(panel, BorderLayout.CENTER);  
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        
        addWindowListener(new WindowAdapter() {
        	
        	@Override
        	public void windowClosing(WindowEvent arg0) {
        		super.windowClosing(arg0);
        		
        		clientThread.interrupt();
        		workerManagerThread.interrupt();
        	}
		});
        
        pack();  
        setVisible(true);
	}
}
