package de.paulomart.craftlistbot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.paulomart.craftlistbot.util.MinecraftServer;

import lombok.Getter;

public class WorkerController {

	@Getter
	private String serverUrl = "http://craftlist.de/servers/view/";
	@Getter
	private List<MinecraftServer> results = new ArrayList<MinecraftServer>();
	@Getter
	private List<Worker> activeWorkers = new ArrayList<Worker>();
	
	/**
	 * Get Servers.
	 * @param minId
	 * @param maxId
	 * @param threads
	 */
	public List<MinecraftServer> grab(int minId, int maxId){
		return grab(minId, maxId, 1);
	}
	
	/**
	 * Get Servers.
	 * @param minId
	 * @param maxId
	 * @param threads
	 */
	public List<MinecraftServer> grab(int minId, int maxId, int threads){
		if (threads == 0 || threads-1 > (maxId-minId)){
			return results;
		}
		
		List<Integer> tempIds = new ArrayList<Integer>();
		for (int i = minId; i <= maxId; i++){
			tempIds.add(i);
		}
		
		//		WorkerID, ids
		HashMap<Integer, List<String>> ids = new HashMap<Integer, List<String>>();
		
		for (int i = 1; i <= threads; i++){
			ids.put(i, new ArrayList<String>());
		}
		
		int accti = 1;
		for (Integer id : tempIds){
			if (accti == threads){
				accti = 0;
			}
			accti++;
			ids.get(accti).add(String.valueOf(id));
		}

		for (Integer key : ids.keySet()){
			String[] wids = new String[ids.get(key).size()];
			
			int i = 0;
			for (String str : ids.get(key)){
				wids[i] = str;
				i++;
			}
			
			Worker worker = new Worker(this, wids);
			worker.start();
			activeWorkers.add(worker);
		}
		
		while (!activeWorkers.isEmpty()){
			synchronized (this) {
				try {
					this.wait(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return results;		
	}
	
	public void finish(Worker worker){
		activeWorkers.remove(worker);
	}

}
