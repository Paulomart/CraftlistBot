package de.paulomart.craftlistbot;

import de.paulomart.craftlistbot.configs.CraftlistBotConfig;
import de.paulomart.craftlistbot.network.MysqlConnector;
import de.paulomart.craftlistbot.util.MinecraftServer;
import lombok.Getter;

public class CraftlistBot {
	
	@Getter
	private static CraftlistBot instance;
	
	@Getter
	private MysqlConnector mysqlConnector;
	@Getter
	private CraftlistBotConfig config;
	
	public static void main(String... args){
		instance = new CraftlistBot();
		instance.start();
	}
	
	public void start(){
		config = new CraftlistBotConfig();
		config.load();
		
		mysqlConnector = new MysqlConnector();
		mysqlConnector.open();
		
		for (MinecraftServer server : new WorkerController().grab(1, 40, 1)){
			mysqlConnector.push(server);
			log("Pushed: #"+server.getId());
		}
	}
	
	public void log(){
		System.out.println();
	}
	
	public void log(Object object){
		System.out.println(object);
	}
	
}
