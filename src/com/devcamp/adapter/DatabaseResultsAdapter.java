package com.devcamp.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public abstract class DatabaseResultsAdapter extends ArrayAdapter<HashMap<String, Object>> { 

	protected List<HashMap<String, Object>> items;
	protected int layout = -1;
	LayoutInflater mLayoutInflater;

	public DatabaseResultsAdapter(Context ctx, int resourceId, ArrayList<HashMap<String, Object>> results) {
		super(ctx, resourceId, results);
		this.items = results;
		this.layout = resourceId;
		mLayoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		HashMap o = items.get(position);
        if (v == null) {
            v = createView(position, parent, layout, o);
        }
        return initializeView(position, v, o);
	}

	protected View createView(int position, ViewGroup parentView, int resourceId, HashMap data){
        return mLayoutInflater.inflate(resourceId, parentView, false);
	}

	@Override
	public int getItemViewType(int position) {
		return super.getItemViewType(position);
	}

	@Override
	public int getViewTypeCount() {
		return super.getViewTypeCount();
	}

	protected abstract View initializeView(int position, View v, HashMap data);

}