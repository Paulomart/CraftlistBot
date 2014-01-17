package de.paulomart.craftlistbot.configs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import lombok.Getter;

@Getter
public abstract class BasicConfig {

	protected Properties properties;
	protected File configFile;
	protected String configHeader;
	
	public BasicConfig(String FileName, String configHeader) {
		properties = new Properties();
		this.configHeader = configHeader;
		this.configFile = new File(FileName);
	}
	
	public void save(){
		setVars();
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(configFile);
			properties.store(fileOutputStream, configHeader);
			fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void load(){
		if (!configFile.exists()){
			setVars();
			save();
		}
		
		FileInputStream fi;
		try {
			fi = new FileInputStream(configFile);
		} catch (FileNotFoundException e) {
			return;
		}
		
		try {
			properties.load(fi);
			fi.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		loadVars();
	}
	
	public String getString(String key){
		if (getProperty(key) != null){
			return String.valueOf(getProperty(key));
		}
		return "";
	}
	
	public int getInt(String key){
		if (getProperty(key) != null){
			return Integer.valueOf(getProperty(key).toString());
		}
		return 0;
	}
	
	public Boolean getBoolean(String key){
		if (getProperty(key) != null){
			return Boolean.valueOf(getProperty(key).toString());
		}
		return false;
	}
	
	public Object getProperty(String key){
		return properties.getProperty(key);
	}
	
	public void setProperty(String key, Object value){
		properties.setProperty(key, String.valueOf(value));
	}
	
	protected abstract void setVars();
	
	protected abstract void loadVars();
	
}
