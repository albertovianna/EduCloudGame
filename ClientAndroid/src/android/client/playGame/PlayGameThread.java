package android.client.playGame;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.app.AlertDialog;
import android.client.activity.R;
import android.client.util.ConstantI;
import android.client.util.SocketConnectionUtil;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class PlayGameThread extends Thread {

//	private DatagramSocket gameSocket;
//	private ServerSocket serverSocket;
	private Socket socket;
	private Handler handler;
//	private DatagramPacket receivePacket;
//	private Integer playPort;
//	private String playHost;
	private ServerWorkerDTO serverWorkerDTO;
	
	private AlertDialog alertDialog;
	private Activity activity;
	
	private boolean stopFlag;
	
	public PlayGameThread(DatagramSocket gameSocket, ServerWorkerDTO serverWorkerDTO, Activity activity, Handler handler) {
//	public PlayGameThread(DatagramSocket gameSocket, DatagramPacket receivePacket, Activity activity, Handler handler) {
//	public PlayGameThread(DatagramSocket gameSocket, Activity activity, Handler handler) {
		
//		InetAddress inetAddress = gameSocket.getInetAddress();
//		Integer port = gameSocket.getLocalPort();
//		gameSocket.close();
		
//		try {
//			serverSocket = SocketConnectionUtil.initServerSocket(port);
//			socket = SocketConnectionUtil.initSocketServer(serverSocket);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
//		this.gameSocket = gameSocket;
//		this.playPort = playPort;
//		this.playHost = playHost;
		this.activity = activity;
		this.handler = handler;
		this.serverWorkerDTO = serverWorkerDTO;
		
		stopFlag = false;
	}
	
	public void run() {
		
//		byte[] receiveData = new byte[ConstantI.DATA_SIZE]; 
//		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//		
//		try {
//			gameSocket.receive(receivePacket);
//		} catch (IOException e2) {
//			e2.printStackTrace();
//		}
		
//		if (receivePacket != null 
//				&& receivePacket.getAddress() != null
//				&& receivePacket.getAddress().getHostName() != null) {
		
		if (serverWorkerDTO.getPort() != null) {
			
//			String host = receivePacket.getAddress().getHostName();
//			
//			String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
//			
//			String[] firstStringArray = message.split("#");
//			
//			String type = firstStringArray[0];
//			
//			String[] secondStringArray = firstStringArray[1];
//			
//			String host = secondStringArray[0];
//			Integer port = Integer.parseInt(firstStringArray[1]);
			
			if (serverWorkerDTO.getInetAddress() != null 
					&& serverWorkerDTO.getInetAddress().getHostAddress() != null) {
				
				
				
				while (true) {
					
					if (this.stopFlag) {
						return;
					}
//					byte[] bytea = new byte[1];
//					bytea[0] = 0;
//					retunMessage(bytea);
//					
//					try {
//						this.sleep(5000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					
//					bytea[0] = 1;
//					retunMessage(bytea);
					
					try {
						socket = SocketConnectionUtil.initSocketClient(serverWorkerDTO.getInetAddress().getHostAddress(), serverWorkerDTO.getPort());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					
					try {
						
						InputStream is = socket.getInputStream();
						ByteArrayOutputStream buffer = new ByteArrayOutputStream();

						int nRead;
						byte[] data = new byte[4096];

						while ( ( nRead = is.read(data, 0, data.length) ) != -1) {
						  buffer.write(data, 0, nRead);
						}

						buffer.flush();
						
//						byte[] receiveData = socket.getInputStream().re;
						
//						receivePacket = new DatagramPacket(receiveData, receiveData.length);
//						gameSocket.setSoTimeout(ConstantI.SOCKET_TIMEOUT);
//						gameSocket.receive(receivePacket);
						
						retunMessage(buffer.toByteArray());
						
						socket.getInputStream().close();
						socket.close();
						
					} catch (SocketTimeoutException e) {
						e.printStackTrace();
						
//						showAlertDialog(activity.getString(R.string.error), e.getMessage());
						
					} catch (SocketException e) {
						e.printStackTrace();
						
						showAlertDialog(activity.getString(R.string.error), e.getMessage());
						
					} catch (UnknownHostException e) {
						e.printStackTrace();
						
						showAlertDialog(activity.getString(R.string.error), e.getMessage());
						
					} catch (IOException e) {
						e.printStackTrace();
						
						showAlertDialog(activity.getString(R.string.error), e.getMessage());
					}
				}
			}
		} else {
			
			System.err.println("################################");
//			System.err.println("Erro playgamethread linha 158");
			System.err.println("Erro playgamethread");
			System.err.println("################################");
		}
		
		
		
		
	}
	
	private void retunMessage(byte[] array) {
		
		Bundle bundle = new Bundle();
		bundle.putByteArray(ConstantI.DATA_ARRAY, array);
		Message m = Message.obtain();
		m.setData(bundle);
		handler.sendMessage(m);
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
}
