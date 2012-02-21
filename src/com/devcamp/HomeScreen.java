package com.devcamp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.devcamp.session.CBCSessionVars;
import com.devcamp.session.Persona;

public class HomeScreen extends Activity {
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button browse = (Button) findViewById(R.id.browse_button);
        browse.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CBCSessionVars.persona.setPersona(Persona.Type.GENERAL_BROWSING);
				goToListScreen();
			}
		});
        
        Button favs = (Button) findViewById(R.id.favs_button);
        favs.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CBCSessionVars.persona.setPersona(Persona.Type.FAVORITES);
				goToListScreen();
			}
		});
        
        Button highabv = (Button) findViewById(R.id.highabv_button);
        highabv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CBCSessionVars.persona.setPersona(Persona.Type.HIGH_ABV);
				goToListScreen();
			}
		});
        
        Button lowabv = (Button) findViewById(R.id.lowabv_button);
        lowabv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CBCSessionVars.persona.setPersona(Persona.Type.LOW_ABV);
				goToListScreen();
			}
		});
        
        
    }

	protected void goToListScreen() {
		Intent i = new Intent();
		i.setClass(this, ContentScreen.class);
		startActivity(i);
	}
   
}