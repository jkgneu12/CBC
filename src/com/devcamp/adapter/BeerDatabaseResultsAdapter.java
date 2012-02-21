package com.devcamp.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.devcamp.ContentScreen;
import com.devcamp.R;
import com.devcamp.constants.Constants;

public class BeerDatabaseResultsAdapter extends DatabaseResultsAdapter implements Filterable, Constants {
	
	protected List<HashMap<String, Object>> filteredItems;
	protected boolean[] tried;

	public BeerDatabaseResultsAdapter(Context ctx, int resourceId, ArrayList<HashMap<String, Object>> results) {
		super(ctx, resourceId, results);
		filteredItems = results;
		tried = new boolean[results.size()];
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		HashMap o = filteredItems.get(position);
        if (v == null) {
            v = createView(position, parent, layout, o);
        }
        return initializeView(position, v, o);
	}
	
	@Override
	protected View createView(int position, ViewGroup parentView, int resourceId, HashMap data) {
		HashMap<String, Object> d = (HashMap<String, Object>)data;
		setTried(position, d.containsKey(TRIED_KEY));
		return super.createView(position, parentView, resourceId, data);
	}

	@Override
	protected View initializeView(int position, View v, HashMap data) {
		HashMap<String, Object> d = (HashMap<String, Object>)data;
		
		initName(v, d);
		initPercent(v, d);
		initBackground(position, v, d);
		
		return v;
	}
	
	protected void initName(View v, HashMap<String, Object> d) {
		TextView t1 = (TextView)v.findViewById(R.id.title);
		String name = (String)d.get("name");
		name = Html.fromHtml(name).toString();
		
		t1.setText(name);
	}
	
	protected void initPercent(View v, HashMap<String, Object> d) {
		TextView t2 = (TextView)v.findViewById(R.id.percent);
		String abv = (String)d.get("abv");
		
		if( abv != null && !abv.equals("")) { 
			t2.setText(abv+"%");
			t2.setVisibility(View.VISIBLE);
		} else { 
			t2.setVisibility(View.INVISIBLE);
		}
	}

	protected void initBackground(int position, View v, HashMap<String, Object> d) {
		int drawable;
		if(getContentScreen().getIndex() == position && getContentScreen().isTabletLayout())
			drawable = R.drawable.selected_row;
		else if( tried[position] )
			drawable = R.drawable.tried_row;
		else
			drawable = R.drawable.untried_row;
		
		v.setBackgroundDrawable(getContentScreen().getResources().getDrawable(drawable));
	}
	
	public void setTried(int position, boolean tried){
		this.tried[position] = tried;
	}
	
	@Override
	public int getCount() {
		return filteredItems.size();
	}
	
	@Override
	public HashMap<String, Object> getItem(int position) {
		return filteredItems.get(position);
	}
	
	@Override
	public Filter getFilter() {
		return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
            	filteredItems = (ArrayList<HashMap<String, Object>>) results.values;
            	
                BeerDatabaseResultsAdapter.this.notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
            	Object filteredResults = getFilteredResults(constraint);

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }

			private Object getFilteredResults(CharSequence constraint) {
				ArrayList<HashMap<String, Object>> filtered = new ArrayList<HashMap<String, Object>>();
				
				for(HashMap<String, Object> row : items){
					String keyword = null;
					if( row.containsKey("name") ){
						keyword = (String)row.get("name");
					} else if ( row.containsKey("item") ) {
						keyword = (String)row.get("item");
					}
					
					if(keyword.replaceAll(" ", "").toLowerCase().contains(constraint.toString().toLowerCase()))
						filtered.add(row);
				}
				
				return filtered;
			}
        };
	}
	
	private ContentScreen getContentScreen(){
		return (ContentScreen)getContext();
	}

}
