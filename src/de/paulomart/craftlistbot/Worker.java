package de.paulomart.craftlistbot;

import java.io.FileNotFoundException;
import java.io.IOException;

import lombok.Getter;
import de.paulomart.craftlistbot.network.HttpRequester;
import de.paulomart.craftlistbot.util.HtmlParser;
import de.paulomart.craftlistbot.util.MinecraftServer;

public class Worker extends Thread {

	private WorkerController parent;
	private String[] requestIds;
	private CraftlistBot craftlistBot;
	@Getter
	private int threadId;
	
	public Worker(WorkerController parent, String[] requestIds) {
		this.craftlistBot = CraftlistBot.getInstance();
		this.parent = parent;
		this.requestIds = requestIds;
		this.threadId = parent.getActiveWorkers().size();
	}
	
	@Override
	public void run(){
		for (String requestId : requestIds){
			//WAIT
			try {
				sleep(1000);
			} catch (InterruptedException e1) {
			}			
			log("Loading Server: "+requestId);
			try {
				String website = HttpRequester.requestWebsite(parent.getServerUrl()+requestId);
				MinecraftServer server = HtmlParser.parse(website);
				if (server != null){
					parent.getResults().add(server);
					log("Found: '"+server.getServerName()+"'");
					continue;
				}		
			} catch (FileNotFoundException e) {
				log("Server not Found");
			} catch (IOException e) {
				
			}
			log("Cant parse a server.");
		}
		parent.finish(this);
	}
	
	private void log(Object object){
		craftlistBot.log("[#"+threadId+"] "+object);
	}
}
