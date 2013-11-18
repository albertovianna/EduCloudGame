package android.client.activity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.app.AlertDialog;
import android.client.playGame.CommandThread;
import android.client.playGame.PlayGameThread;
import android.client.playGame.ServerWorkerDTO;
import android.client.recoveryGames.GameDTO;
import android.client.util.ConstantI;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class PlayGameActivity extends Activity implements OnTouchListener {

	private AlertDialog alertDialog;
	
	private GameDTO gameDTO;
	private DatagramSocket gameSocket;
	private Thread playGame;
	private Thread commandGame;
	private PlayGameThread playGameThread;
	private CommandThread commandThread;
	private ServerWorkerDTO serverWorkerDTO;
	
	private ImageView ivGameImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.play_game);
		
		ivGameImage = (ImageView)this.findViewById(R.id.imageViewPlayGame);
		ivGameImage.setOnTouchListener(this);
		
		Bundle bundle = this.getIntent().getExtras();
		
		if (bundle != null) {
			
			String name = bundle.getString(ConstantI.NAME);
			
			if (name != null && ! name.equals("")) {
				
				gameDTO = new GameDTO();
				gameDTO.setName(name);
				
				enviarJogoEscolhido(gameDTO);
				
			} else {

				showAlertDialog(getString(R.string.error), "Jogo inválido");
			}
		}
	}
	
	private void freeWorker(InetAddress workerInetAddress, Integer workerStopPort, String message) {
		
		byte[] sendData;
		
		try {
			
			DatagramSocket datagramSocket = new DatagramSocket();

			sendData = message.getBytes();
			
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, workerInetAddress, workerStopPort);
			
			datagramSocket.send(sendPacket);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		if (playGame != null) {
			playGameThread.stopThread();
			playGame.interrupt();
			
		}
		if (commandGame != null) {
			commandThread.stopThread();
			commandGame.interrupt();
			
		}
		finish();
	}
	
	/**
	 * GAME_ESCOLHIDO # <Game game> 
	 * @return 
	 */
	private void enviarJogoEscolhido(GameDTO gameDTO) {
		
		try {
			
			gameSocket = new DatagramSocket();
			
			InetAddress IPAddress = InetAddress.getByName(ConstantI.MANAGER_HOSTNAME);
			
			String sendString = ConstantI.GAME_ESCOLHIDO+"#"+gameDTO.getName();
			byte[] sendData = sendString.getBytes();
			
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, ConstantI.MANAGER_PORT);
			
			gameSocket.send(sendPacket);
			
			byte[] receiveData = new byte[ConstantI.DATA_SIZE];
			
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			
			gameSocket.setSoTimeout(ConstantI.SOCKET_TIMEOUT);
			
			gameSocket.receive(receivePacket);
			
			String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
			
			serverWorkerDTO = new ServerWorkerDTO(receivePacket.getAddress());
			
			this.initiateThreads(message);
			
			receiveData = new byte[ConstantI.DATA_SIZE];
			
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
			
			gameSocket.setSoTimeout(ConstantI.SOCKET_TIMEOUT);
			
			gameSocket.receive(receivePacket);
			
			message = new String(receivePacket.getData(), 0, receivePacket.getLength());
			
			this.initiateThreads(message);
			
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			
			showAlertDialog(getString(R.string.error), e.getMessage());
			
		} catch (SocketException e) {
			e.printStackTrace();
			
			showAlertDialog(getString(R.string.error), e.getMessage());
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
			
			showAlertDialog(getString(R.string.error), e.getMessage());
			
		} catch (IOException e) {
			e.printStackTrace();
			
			showAlertDialog(getString(R.string.error), e.getMessage());
		}
	}
	
	public void initiateThreads(String message) {
		
		if (message.contains(ConstantI.ERROR)) {
			
			String[] firstStringArray = message.split("#");
			
//			String type = firstStringArray[0];
			String errorMessage = firstStringArray[1];
			
			showAlertDialog(getString(R.string.error), errorMessage);
			
		} else {
			
			if (message.contains(ConstantI.PLAY_MANAGER)) {
				
				String[] firstStringArray = message.split("#");
				
//				String type = firstStringArray[0];
				
				serverWorkerDTO.setPort(Integer.parseInt(firstStringArray[1]));
				
				playGameThread = new PlayGameThread(gameSocket, serverWorkerDTO, this, handlerMessage);
//				PlayGameThread playGameThread = new PlayGameThread(gameSocket, this, handlerMessage);
				playGame = new Thread(playGameThread);
//				runOnUiThread(playGame);
				playGame.start();
				
			} else if (message.contains(ConstantI.COMMAND_MANAGER)) {
				
//				System.out.println("teste");
//				System.out.println("teste");
//				System.out.println("teste");
//				System.out.println("teste");
//				System.out.println("teste");
				
				String[] firstStringArray = message.split("#");
				
				serverWorkerDTO.setCommandPort(Integer.parseInt(firstStringArray[1]));
				
				commandThread = new CommandThread(gameSocket, serverWorkerDTO, this, null);
				commandGame = new Thread(commandThread);
				commandGame.start();
			}
		}
	}
	
	private void showAlertDialog(String title, String message) {
		
		alertDialog = new AlertDialog.Builder(PlayGameActivity.this).create();
		alertDialog.setButton(PlayGameActivity.this.getString(R.string.ok), 
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
	
	private Handler handlerMessage = new Handler() {

		public void handleMessage(Message msg) {

			byte[] byteArray = msg.getData().getByteArray(ConstantI.DATA_ARRAY);

			Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
			ivGameImage.setImageBitmap(bitmap);
			
//			if (byteArray[0] == 0) {
//				
//				ivGameImage.setImageResource(R.drawable.ic_launcher);
//				
//			} else {
//				
//				ivGameImage.setImageResource(R.drawable.driver);
//			}
		}
	};

	public boolean onTouch(View v, MotionEvent event) {
		
//		System.out.println("################################");
//		System.out.println("X: "+event.getX()+" Y: "+event.getY());
//		System.out.println("################################");
		
		if (commandThread != null) {
			
			commandThread.setCommand(""+event.getX()+"|"+event.getY());
		}
		
		return false;
	}
}
