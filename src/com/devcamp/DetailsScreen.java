package com.devcamp;

import java.util.HashMap;

import android.os.Bundle;
import android.view.Menu;

public class DetailsScreen extends ContentScreen {
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		row = (HashMap<String, Object>)getIntent().getExtras().get("row");
		index = getIntent().getIntExtra("index", 0);
		
		super.onCreate(savedInstanceState);
	}
	
	protected void setContentView() {
		setContentView(R.layout.detail_view);
		
	}
	
	protected void initActionBar(){}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
    }
	
}
