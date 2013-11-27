package android.client.playGame;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;

import android.app.Activity;
import android.app.AlertDialog;
import android.client.activity.R;
import android.client.util.SocketConnectionUtil;
import android.content.DialogInterface;
import android.os.Handler;

public class CommandThread extends Thread {

	private Socket socket;
	private Handler handler;
	private ServerWorkerDTO serverWorkerDTO;
	
	private AlertDialog alertDialog;
	private Activity activity;
	
	private boolean stopFlag;
	
	private String command;
	
	public CommandThread(DatagramSocket gameSocket, ServerWorkerDTO serverWorkerDTO, Activity activity, Handler handler) {
		
		this.activity = activity;
		this.handler = handler;
		this.serverWorkerDTO = serverWorkerDTO;
		
		stopFlag = false;
	}
	
	@Override
	public void run() {
		
		if (serverWorkerDTO.getCommandPort() != null) {
			
			if (serverWorkerDTO.getInetAddress() != null 
					&& serverWorkerDTO.getInetAddress().getHostAddress() != null) {
				
				while (true) {
					
					if (this.stopFlag) {
						return;
					}
					
					if (command != null) {
						
						try {
							socket = SocketConnectionUtil.initSocketClient(serverWorkerDTO.getInetAddress().getHostAddress(), serverWorkerDTO.getCommandPort());
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						
						try {
							
							socket.getOutputStream().write(command.getBytes());
							
							socket.getOutputStream().flush();
							
							socket.getOutputStream().close();
							
							socket.close();
							
						} catch (IOException e) {
							e.printStackTrace();
							
							showAlertDialog(activity.getString(R.string.error), e.getMessage());
							
						}
						
						command = null;
						
						System.out.println("Comando Enviado");
					}
				}
			}
		}
	}
	
	private void showAlertDialog(String title, String message) {
		
		alertDialog = new AlertDialog.Builder(activity).create();
		alertDialog.setButton(activity.getString(R.string.ok), 
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
				alertDialog.dismiss();
			}
		});
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
		alertDialog.show();
	}
	
	public void stopThread() {
		
		this.stopFlag = true;
	}
	
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	
}
