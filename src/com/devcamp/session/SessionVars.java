package com.devcamp.session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.devcamp.logging.Logger;

public class SessionVars implements Serializable {
	
	static final Logger L = Logger.getLogger(SessionVars.class);
	
	protected SharedPreferences preferences;
	
	public SessionVars(Context context){
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	protected void storeIntPreference(String key, int value){
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(key, value); 
		editor.commit();
	}
	
	protected void storeStringPreference(String key, String value){
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	protected void storeBooleanPreference(String key, boolean value){
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	protected void storeFloatPreference(String key, float value){
		SharedPreferences.Editor editor = preferences.edit();
		editor.putFloat(key, value); 
		editor.commit();
	}
	
	protected int getIntPreference(String key, int defaultValue){
		if( preferences == null ) { 
			return defaultValue;
		}
		return preferences.getInt(key, defaultValue);
	}
	
	protected String getStringPreference(String key, String defaultValue){
		if( preferences == null ) { 
			return defaultValue;
		}
		return preferences.getString(key, defaultValue);
	}
	
	protected boolean getBooleanPreference(String key, boolean defaultValue){
		if( preferences == null ) { 
			return defaultValue;
		}
		return preferences.getBoolean(key, defaultValue);
	}
	
	protected float getFloatPreference(String key, float defaultValue){
		if( preferences == null ) { 
			return defaultValue;
		}
		return preferences.getFloat(key, defaultValue);
	}
	
	/**
	 * Simple method to build a comma separated string via
	 * an array list of strings
	 * @param rv
	 * @return
	 */
	protected String getStringFromArrayList(ArrayList<String> rv, Character appendChar) {
		StringBuilder sb = new StringBuilder();
		boolean addComma = false;
		for(String viewed: rv){
			if(addComma){
				sb.append(",");
			}
			if(appendChar != null ) {
				sb.append(appendChar);
			}
			sb.append(viewed);
			if(appendChar != null ) {
				sb.append(appendChar);
			}
			if( viewed != null && viewed.trim().length() > 0 ){
				addComma = true;
			}
		}
		return sb.toString();
	}
	
	/**
	 * With this method you can set a string preference
	 * based on a arraylist of strings in a list. 
	 * This method will build the csv and store it.
	 * @param key
	 * @param list
	 */
	protected void setStringFromArrayList(String key, ArrayList<String> list){
		String store = getStringFromArrayList(list, null);
		storeStringPreference(key, store);
	}
	
	/**
	 * Builds an array list out of a string preference.
	 * This method will create a new array list and 
	 * initialize an empty string into the string preference.
	 * If it exists, the arraylist will be built from a
	 * comma-separated string preference.
	 * @param key
	 * @return
	 */
	public ArrayList<String> getArrayListFromString(String key){
		String rv = getStringPreference(key, null);
		if( rv != null ){
			String[] _rv = rv.split(",");
			if( _rv != null ){
				List<String> ret = Arrays.asList(_rv);
				return new ArrayList(ret);
			} else { 
				return new ArrayList<String>();
			}
		} else {
			storeStringPreference(key, "");
			return new ArrayList<String>();
		}
	}

}
