package de.paulomart.craftlistbot.network;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.paulomart.craftlistbot.CraftlistBot;
import de.paulomart.craftlistbot.util.MinecraftServer;

public class MysqlConnector {
	
	private Connection conn;
	private CraftlistBot craftlistBot;
	//==========================================
	private PreparedStatement tableCreateStmt;
	private PreparedStatement updateServerStmt;
	private PreparedStatement createServerStmt;
	private PreparedStatement existsServerStmt;
	//==========================================
	
	public MysqlConnector() {
		this.craftlistBot = CraftlistBot.getInstance();
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Creates a connection to the Mysql Server
	 */
	public void open(){
		try {
			String url = "jdbc:mysql://"+craftlistBot.getConfig().getMysqlHost()+":"+craftlistBot.getConfig().getMysqlPort()+"/"+craftlistBot.getConfig().getMysqlDatabase();
			String user = craftlistBot.getConfig().getMysqlUser();
			String password = craftlistBot.getConfig().getMysqlPassword();
			conn = DriverManager.getConnection(url, user, password);
			preparePerparedStmts();
			tableCreateStmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			craftlistBot.log("Could not connect to Mysql..");
			conn = null;
			return;	
		}
	}
	
	private void preparePerparedStmts() throws SQLException{
		String tableName = craftlistBot.getConfig().getMysqlServerTable();
		
		tableCreateStmt = conn.prepareStatement("CREATE TABLE IF NOT EXISTS `"+tableName+"` ( `id` bigint(255) NOT NULL, `servername` text CHARACTER SET utf8 COLLATE utf8_unicode_ci, `serverip` text, `serverport` int(11), `votescore` int(11), `minecraftVersion` text, `isOfflineModeServer` text, `isOnline` text ) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
				
		//1. where id
		existsServerStmt = conn.prepareStatement("SELECT * FROM `"+tableName+"` WHERE `id` = ?");
		
		//1: ID | 2: Servername | 3. serverip | 4. serverport | 5. minecraftVersion | 6. votescore | 7. isOfflineModeServer | 8. isOnline 
		createServerStmt = conn.prepareStatement("INSERT INTO `"+tableName+"` (`id`, `servername`, `serverip`, `serverPort`, `minecraftVersion`, `votescore`, `isOfflineModeServer`, `isOnline`) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
	
		//1: Servername | 2. serverip | 3. serverport | 4. minecraftVersion | 5. votescore | 6. isOfflineModeServer | 7. isOnline | 8. where id
		updateServerStmt = conn.prepareStatement("UPDATE `"+tableName+"` SET `servername` = ?, `serverip` = ?, `serverPort` = ?, `minecraftVersion` = ?, `votescore` = ?, `isOfflineModeServer` = ?, `isOnline` = ? WHERE `id` = ?");
	}
	
	/**
	 * Closes the connection to the Mysql Server
	 */
	public void close(){
		if (conn == null){
			return;
		}
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Auto detects if it needs to update or create.
	 * @param server
	 */
	public void push(MinecraftServer server){
		if (exists(server)){
			update(server);
		}else{
			create(server);
		}
	}
	
	public boolean exists(MinecraftServer server){
		try {
			return unsafeExists(server);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public void create(MinecraftServer server){
		try {
			unsafeCreate(server);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void update(MinecraftServer server){
		try {
			unsafeUpdate(server);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*==========================*/
	
	private void unsafeUpdate(MinecraftServer server) throws SQLException{
		updateServerStmt.setString(1, server.getServerName());
		updateServerStmt.setString(2, server.getServerIP());
		updateServerStmt.setInt(3, server.getServerPort());
		updateServerStmt.setString(4, server.getMinecraftVersion());
		updateServerStmt.setInt(5, server.getVoteScore());
		updateServerStmt.setBoolean(6, server.isOfflineModeServer());
		updateServerStmt.setBoolean(7, server.isOnline());
		updateServerStmt.setInt(8, server.getId());
		
		updateServerStmt.executeUpdate();
	}
	
	private void unsafeCreate(MinecraftServer server) throws SQLException{
		createServerStmt.setInt(1, server.getId());
		createServerStmt.setString(2, server.getServerName());
		createServerStmt.setString(3, server.getServerIP());
		createServerStmt.setInt(4, server.getServerPort());
		createServerStmt.setString(5, server.getMinecraftVersion());
		createServerStmt.setInt(6, server.getVoteScore());
		createServerStmt.setBoolean(7, server.isOfflineModeServer());
		createServerStmt.setBoolean(8, server.isOnline());
		
		createServerStmt.executeUpdate();
	}
	
	private boolean unsafeExists(MinecraftServer server) throws SQLException{
		boolean ret = false;
		
		existsServerStmt.setInt(1, server.getId());
		
		ResultSet res = existsServerStmt.executeQuery();
		ret = res.next();
		res.close();

		return ret;
	}
}
