package android.client.util;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketConnectionUtil {

	public static ServerSocket initServerSocket(Integer actualWorkerManagerPort) throws IOException {
		
		ServerSocket serverSocket = null;
		
		try {

			serverSocket = new ServerSocket(actualWorkerManagerPort);
			
			return serverSocket;
			
		} catch (IOException e) {
			
			System.err.println("####################################");
			System.err.println("TCP Port "+actualWorkerManagerPort+" is occupied.");
			System.err.println("####################################");
			
			e.printStackTrace();
			
			throw e;
		}
		
	}
	
	public static Socket initSocketServer(final ServerSocket serverSocket) throws IOException {
		
		Socket socket = null;
		
		try {
			
			socket = serverSocket.accept();
			
			return socket; 
			
		} catch (IOException e) {
			
			System.err.println("####################################");
			System.err.println("Accept failed on TCP Port "+serverSocket.getLocalPort()+".");
			System.err.println("####################################");
			
			e.printStackTrace();
			
			throw e;
		}
		
	}
	
	public static Socket initSocketClient(String managerHostname, Integer managerPort) throws IOException {
		
		Socket socket = null;
		
		try {
			
			socket = new Socket(managerHostname, managerPort);
			
			return socket;
			
		} catch (UnknownHostException e) {
			
			System.err.println("####################################");
			System.err.println("Client not connected to host: "+managerHostname);
			System.err.println("####################################");
			
			e.printStackTrace();
			
			throw e;
			
		} catch (IOException e) {
			
			System.err.println("####################################");
			System.err.println("Client not connected to port: "+managerPort);
			System.err.println("####################################");
			
			e.printStackTrace();
			
			throw e;
		}
		
	}
	
	public static void closeSockets(ServerSocket serverSocket, Socket socket) throws IOException {
		
		try {
			
			if (socket != null) {
				socket.close();
			}
			if (serverSocket != null) {
				serverSocket.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			
			throw e;
		}
	}

}
