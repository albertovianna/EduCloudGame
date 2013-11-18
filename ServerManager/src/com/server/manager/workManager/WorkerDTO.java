package com.server.manager.workManager;

import java.net.InetAddress;

public class WorkerDTO {

	private InetAddress ip;
	private Integer port;
	private boolean used;
	
	public WorkerDTO() {
		
	}

	public InetAddress getIp() {
		return ip;
	}
	public void setIp(InetAddress ip) {
		this.ip = ip;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public boolean isUsed() {
		return used;
	}
	public void setUsed(boolean used) {
		this.used = used;
	}
}
