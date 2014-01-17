package de.paulomart.craftlistbot.util;

import lombok.Getter;

@Getter
public class MinecraftServer {
	
	public MinecraftServer(String serverName, String serverIP, String minecraftVerion, int serverPort, int voteScore, int id, boolean isOfflineModeServer, boolean isOnline) {
		this.serverName = serverName;
		this.serverIP = serverIP;
		this.serverPort = serverPort;
		this.voteScore = voteScore;
		this.id = id;
		this.isOfflineModeServer = isOfflineModeServer;
		this.isOnline = isOnline;
		this.minecraftVersion = minecraftVerion;
	}

	private String serverName;
	private String serverIP;
	private String minecraftVersion;
	private int serverPort;
	private int voteScore;
	private int id;
	private boolean isOfflineModeServer;
	private boolean isOnline;

}
