package com.devcamp.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.devcamp.ContentScreen;
import com.devcamp.R;
import com.devcamp.adapter.BeerDatabaseResultsAdapter;
import com.devcamp.adapter.FoodDatabaseResultsAdapter;
import com.devcamp.constants.Constants;
import com.devcamp.db.CBCdb;
import com.devcamp.db.DatabaseUtils;
import com.devcamp.session.CBCSessionVars;


public class ListItemFragment extends ListFragment implements TextWatcher, Constants {
	
	private BeerDatabaseResultsAdapter adapter;
	
	boolean isFoodType(){
		if( getContentScreen().getType() != null ) { 
			return getContentScreen().getType().equals("Food");
		}
		return false;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		v.setBackgroundColor(Color.parseColor("#222222"));
		return v;
	}
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        
        getContentScreen().setListFrag(this);
        
		getContentScreen().getSearchBar().addTextChangedListener(this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
    	super.onListItemClick(l, v, position, id);
    	HashMap<String, Object> row = (HashMap<String,Object>)getListAdapter().getItem(position);
    	
    	if(((ContentScreen)getActivity()).isTabletLayout()){
    		getContentScreen().setRow(row, position);
    		getContentScreen().getDetailsFrag().updateData();
    	} else {
    		getContentScreen().goToDetailsScreen(row, position);
    	}
    }

	@Override
	public void afterTextChanged(Editable s) {
		filter(s.toString());
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {}
	
	

	public void updateData() {
		String type = getContentScreen().getType();
		CBCSessionVars sessionVars = new CBCSessionVars(getContentScreen());
		
		updateMenuTitle(type);
		
		ArrayList<HashMap<String, Object>> results = executeQuery(type, sessionVars);
        
		updateResultsWithTried(results, sessionVars);
		
		if( results != null ) { 
	        createListAdapter(results);
	        
	        filter(getContentScreen().getFilterText());
	        
	        refreshListAndDetails();
		}
	}
	
	private void updateMenuTitle(String type) {
		Menu menu = ((ContentScreen)getActivity()).getMenu();
		if(menu != null)
			((MenuItem)menu.getItem(0)).setTitle(type);
	}
	
	private ArrayList<HashMap<String, Object>> executeQuery(String type, CBCSessionVars sessionVars) {
		if( CBCdb.isUsable() ) {
        	try {
				CBCdb db = new CBCdb(getActivity(), 1, true);
				String qry = "select * from " + type + "s";
				if( isFoodType() ) { 
					qry = "select MI.pk, MI.item, MI.description, MC.category, MM.meal, MI.price " + 
							"from MenuItems as MI, MenuCategories as MC, MenuMeals as MM " + 
							"where MI.categoryPk = MC.pk " + 
							"and MI.mealPk = MM.pk";
				}
				if(!isFoodType()){
					if(CBCSessionVars.persona.isFavoritesPreferred()){
						String favs = sessionVars.getBeersTriedString();
						qry += " where pk in ("+favs+")";
					}else if( CBCSessionVars.persona.isHighABV() ){
						qry += " where abv != '' order by abv desc";
					}else if( CBCSessionVars.persona.isLowABV() ) { 
						qry += " where abv != '' order by abv asc";
					}
				} else { 
					if(CBCSessionVars.persona.isFavoritesPreferred()){
						String favs = sessionVars.getFoodTriedString();
						qry += " and MI.pk in ("+favs+")";
					}
				}
				return DatabaseUtils.getResultsTable(db.query(qry, null));
				
        	} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private void updateResultsWithTried(ArrayList<HashMap<String, Object>> results, CBCSessionVars sessionVars) {
		ArrayList<String> triedYet;
		if( isFoodType() )
			triedYet = sessionVars.getFoodTried();
		else
			triedYet = sessionVars.getBeersTried();
		
		for(HashMap<String,Object> result: results){
			if( triedYet.contains((String)result.get("pk")) ){
				result.put(TRIED_KEY, true);
			}
		}
	}
	
	private void createListAdapter(ArrayList<HashMap<String, Object>> results) {
		if( isFoodType() )
			adapter = new FoodDatabaseResultsAdapter(getActivity(), R.layout.listadapter_food, results);
		else
			adapter = new BeerDatabaseResultsAdapter(getActivity(), R.layout.listadapter_beer, results);	
		
		setListAdapter(adapter);
	}
	
	public void filter(String s){
		adapter.getFilter().filter(s);
	}

	private void refreshListAndDetails() {
		DetailsFragment detailsFrag = ((DetailsFragment)getFragmentManager().findFragmentById(R.id.detailfrag));
		if(detailsFrag != null ) { 
			getContentScreen().setRow((HashMap<String, Object>)adapter.getItem(0), 0);
			detailsFrag.updateData();
		}
	}
	
	private ContentScreen getContentScreen(){
		return (ContentScreen)getActivity();
	}
}
