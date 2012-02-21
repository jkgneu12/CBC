package com.devcamp.session;

import java.util.ArrayList;

import android.content.Context;

public class CBCSessionVars extends SessionVars {

	static final String FOOD_TRIED_KEY = "CBC_FOOD_TRIED";
	static final String BEERS_TRIED_KEY = "CBC_BEERS_TRIED";
	
	public static Persona persona = new Persona();
	
	public String beersTried;
	public String foodTried;
	
	public CBCSessionVars(Context context) {
		super(context);
	}
	
	public void addBeerTried(String key){
		ArrayList<String> beersTried = getBeersTried();
		if(beersTried.contains(key)){
			beersTried.remove(key);
		}
		beersTried.add(key);
		setStringFromArrayList(BEERS_TRIED_KEY, beersTried);
	}
	
	public void removeBeerTried(String key){
		ArrayList<String> beersTried = getBeersTried();
		if(beersTried.contains(key)){
			beersTried.remove(key);
		}
		setStringFromArrayList(BEERS_TRIED_KEY, beersTried);
	}
	
	public void clearBeersTried(){
		setStringFromArrayList(BEERS_TRIED_KEY, new ArrayList<String>());
	}
	
	public String getBeersTriedString(){
		return getStringPreference(BEERS_TRIED_KEY, "");
	}
	
	public ArrayList<String> getBeersTried(){
		return getArrayListFromString(BEERS_TRIED_KEY);
	}
	
	public void addFoodTried(String key){
		ArrayList<String> foodTried = getFoodTried();
		if(foodTried.contains(key)){
			foodTried.remove(key);
		}
		foodTried.add(key);
		setStringFromArrayList(FOOD_TRIED_KEY, foodTried);
	}
	
	public void removeFoodTried(String key){
		ArrayList<String> foodTried = getFoodTried();
		if(foodTried.contains(key)){
			foodTried.remove(key);
		}
		setStringFromArrayList(FOOD_TRIED_KEY, foodTried);
	}
	
	public void clearFoodTried(){
		setStringFromArrayList(FOOD_TRIED_KEY, new ArrayList<String>());
	}
	
	public String getFoodTriedString(){
		return getStringPreference(FOOD_TRIED_KEY, "");
	}
	
	public ArrayList<String> getFoodTried(){
		return getArrayListFromString(FOOD_TRIED_KEY);
	}
	
}
