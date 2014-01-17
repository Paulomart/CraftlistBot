package de.paulomart.craftlistbot.configs;

import lombok.Getter;

@Getter
public class CraftlistBotConfig extends BasicConfig{
	
	/*
	 * Config Settings:
	 */
	private String mysqlUser = "";
	private String mysqlPassword = "";
	private String mysqlDatabase = ""; 
	private String mysqlServerTable = "";
	private String mysqlHost = "";
	private int mysqlPort = 3306;
	
	public CraftlistBotConfig(){
		super("CraftlistBot.properties", "MainConfg");
	}
	
	@Override
	protected void loadVars(){
		mysqlUser = getString("MysqlUser");
		mysqlPassword= getString("MysqlPassword");
		mysqlDatabase= getString("MysqlDatabase");
		mysqlServerTable= getString("MysqlServerTable");
		mysqlHost= getString("MysqlHost");
		mysqlPort= getInt("MysqlPort");
	}
		
	@Override
	protected void setVars(){
		setProperty("MysqlUser", mysqlUser);
		setProperty("MysqlPassword", mysqlPassword);
		setProperty("MysqlDatabase", mysqlDatabase);
		setProperty("MysqlServerTable", mysqlServerTable);
		setProperty("MysqlHost", mysqlHost);
		setProperty("MysqlPort", mysqlPort);
	}
	
}