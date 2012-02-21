package com.devcamp.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.devcamp.R;

public class FoodDatabaseResultsAdapter extends
		BeerDatabaseResultsAdapter {

	public FoodDatabaseResultsAdapter(Context ctx, int resourceId,
			ArrayList<HashMap<String, Object>> results) {
		super(ctx, resourceId, results);
	}
	
	@Override
	protected View initializeView(int position, View v, HashMap data) {
		HashMap<String, Object> d = (HashMap<String, Object>)data;
		
		initName(v, d);
		initPrice(v, d);
		initCategory(v, d);
		initBackground(position, v, d);
		
		return v;
	}
	
	protected void initName(View v, HashMap<String, Object> d) {
		TextView t1 = (TextView)v.findViewById(R.id.item);
		String item = (String)d.get("item");
		item = Html.fromHtml(item).toString();
		
		t1.setText(item);
	}
	
	protected void initPrice(View v, HashMap<String, Object> d) {
		TextView t2 = (TextView)v.findViewById(R.id.price);
		String price = (String)d.get("price");
		if( price != null && !price.equals("") ) {
			t2.setText("$"+price);
		} else {
			t2.setVisibility(View.INVISIBLE);
		}
	}
	
	private void initCategory(View v, HashMap<String, Object> d) {
		TextView t3 = (TextView)v.findViewById(R.id.info);
		String category = (String)d.get("category");
		String meal = (String)d.get("meal");
		
		t3.setText(category + " (" + meal+ ")");
	}
	
}
