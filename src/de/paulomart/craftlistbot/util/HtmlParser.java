package de.paulomart.craftlistbot.util;

public class HtmlParser {

	public static String serverNamePrefix = "<h2 style=\"padding-right:20px;\">";
	public static String serverNameSubfix = "<a href=\"/reports/server";
	public static String serverAdressPrefix = "<strong>Server:</strong>                </div>                <div class=\"col-sm-9\">                    ";
	public static String serverAdressSubfix = "                </div>            </div>            <div class=\"row\">                <div class=\"col-sm-3\">                    <strong>Status:</strong>";
	public static String serverVersionPrefix = "<span class=\"glyphicon glyphicon-cog\"></span> ";
	public static String serverVersionSubfix = "                </div>            </div>                            <div class=\"row\">                    <div class=\"col-sm-3\" style=\"margin:2px 0;\">";
	public static String serverVotePrefix = "<span class=\"glyphicon glyphicon-star\"></span> ";
	public static String serverVoteSubfix = "</strong></button>                        <a href=\"/vote/";
	public static String serverIdPrefix = "<a href=\"/vote/";
	public static String serverIdSubfix = "\" class=\"btn btn-warning btn-lg\">Jetzt Voten!</a>";
	public static String serverDescription = "Serverbeschreibung";
	public static CharSequence premiumServerContent = "Premium";
	public static CharSequence serverOnlineContent = "Server ist online";
	public static CharSequence serverAdressReplaceOut = "<span class=\"text-muted\">";
	
	public static MinecraftServer parse(String website){
		if (!website.contains(serverDescription)){
			return null;
		}
		
		String serverName = "";
		String serverIP = "";
		String minecraftVersion = "";
		String serverRawIP = "127.0.0.1:24566";
		int serverPort = 0;
		int voteScore = 0;
		int serverId = 0;
		boolean isOfflineModeServer = false;
		boolean isOnline = false;
		String temp = "";
		
		serverName = website.substring(website.indexOf(serverNamePrefix) + serverNamePrefix.length(), website.indexOf(serverNameSubfix));
		minecraftVersion = website.substring(website.indexOf(serverVersionPrefix) + serverVersionPrefix.length(), website.indexOf(serverVersionSubfix));
		serverRawIP = website.substring(website.indexOf(serverAdressPrefix) + serverAdressPrefix.length(), website.indexOf(serverAdressSubfix));
		
		temp = website.substring(website.indexOf(serverVotePrefix) + serverVotePrefix.length(), website.indexOf(serverVoteSubfix)).trim();
		if (Util.isInteger(temp)){
			voteScore = Integer.valueOf(temp);
		}
		
		temp = website.substring(website.indexOf(serverIdPrefix) + serverIdPrefix.length(), website.indexOf(serverIdSubfix)).trim();
		if (Util.isInteger(temp)){
			serverId = Integer.valueOf(temp);
		}
		
		isOnline = website.contains(serverOnlineContent);			
		isOfflineModeServer = !website.contains(premiumServerContent);
			
		serverRawIP = serverRawIP.replace("</span>", "");
		serverRawIP = serverRawIP.replace(serverAdressReplaceOut, "");
		
		serverIP = serverRawIP.split(":")[0];
					
		if (Util.isInteger(serverRawIP.split(":")[1].trim())){
			serverPort = Integer.valueOf(serverRawIP.split(":")[1].trim());
		}
			
		return new MinecraftServer(serverName.trim(), serverIP.trim(), minecraftVersion.trim(), serverPort, voteScore, serverId, isOfflineModeServer, isOnline);
	}


}
