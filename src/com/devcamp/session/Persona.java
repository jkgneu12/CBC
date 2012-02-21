package com.devcamp.session;

public class Persona {
	
	public enum Type {
		GENERAL_BROWSING,
		HIGH_ABV,
		LOW_ABV,
		FAVORITES
	}
	
	public Type type;
	
	public Persona(){
		this.type = Type.GENERAL_BROWSING;
	}
	
	public boolean isGeneralBrowsing(){
		return this.type.equals(Type.GENERAL_BROWSING);
	}
	
	public boolean isHighABV(){
		return this.type.equals(Type.HIGH_ABV);
	}
	
	public boolean isLowABV(){
		return this.type.equals(Type.LOW_ABV);
	}
	
	public boolean isFavoritesPreferred(){
		return this.type.equals(Type.FAVORITES);
	}
	
	public void setPersona(Type t){
		this.type = t;
	}

}
