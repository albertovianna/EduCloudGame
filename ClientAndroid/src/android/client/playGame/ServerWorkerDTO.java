package android.client.playGame;

import java.net.InetAddress;

public class ServerWorkerDTO {

	private InetAddress inetAddress;
	private Integer port;
	private Integer commandPort;
	
	
	public ServerWorkerDTO() {
		
	}
	public ServerWorkerDTO(InetAddress inetAddress) {
		this.inetAddress = inetAddress;
	}
	public ServerWorkerDTO(InetAddress inetAddress, Integer port) {
		this.inetAddress = inetAddress;
		this.port = port;
	}


	public InetAddress getInetAddress() {
		return inetAddress;
	}
	public void setInetAddress(InetAddress inetAddress) {
		this.inetAddress = inetAddress;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public Integer getCommandPort() {
		return commandPort;
	}
	public void setCommandPort(Integer commandPort) {
		this.commandPort = commandPort;
	}
	
}
