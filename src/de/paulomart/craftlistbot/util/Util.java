package de.paulomart.craftlistbot.util;

public class Util {

	public static boolean isInteger(String s){
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
	}	
}
