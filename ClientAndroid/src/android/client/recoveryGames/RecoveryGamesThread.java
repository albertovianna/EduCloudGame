package android.client.recoveryGames;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.client.activity.R;
import android.client.util.ConstantI;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class RecoveryGamesThread extends Thread {

	private Handler handler;
	private Activity activity;
	
	private DatagramSocket gameSocket;
	
	@Override
	public void run() {
		super.run();
		
		try {
			
			gameSocket = new DatagramSocket();
			
			InetAddress IPAddress = InetAddress.getByName(ConstantI.MANAGER_HOSTNAME);
			
			String sendString = ConstantI.GAMES;
			byte[] sendData = sendString.getBytes();
			
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, ConstantI.MANAGER_PORT);
			
			gameSocket.send(sendPacket);
			
			
			byte[] receiveData = new byte[ConstantI.DATA_SIZE];
			
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			
			gameSocket.setSoTimeout(ConstantI.SOCKET_TIMEOUT);
			
			gameSocket.receive(receivePacket);
			
			String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
			
			retunMessage(activity.getString(R.string.ok), message);
			
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			
			retunMessage(activity.getString(R.string.error), e.getMessage());
			
		} catch (SocketException e) {
			e.printStackTrace();
			
			retunMessage(activity.getString(R.string.error), e.getMessage());
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
			
			retunMessage(activity.getString(R.string.error), e.getMessage());
			
		} catch (IOException e) {
			e.printStackTrace();
			
			retunMessage(activity.getString(R.string.error), e.getMessage());
		}
	}
	
	private void retunMessage(String title, String message) {
		
		Bundle bundle = new Bundle();
		bundle.putString(ConstantI.TITLE, title);
		bundle.putString(ConstantI.MESSAGE, message);
		Message m = Message.obtain();
		m.setData(bundle);
		handler.sendMessage(m);
	}
	
	public Handler getHandler() {
		return handler;
	}
	public void setHandler(Handler handler) {
		this.handler = handler;
	}
	public Activity getActivity() {
		return activity;
	}
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	
}
