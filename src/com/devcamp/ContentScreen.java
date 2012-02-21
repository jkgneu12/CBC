package com.devcamp;

import java.util.HashMap;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.widget.EditText;

import com.devcamp.adapter.BeerDatabaseResultsAdapter;
import com.devcamp.fragment.DetailsFragment;
import com.devcamp.fragment.ListItemFragment;

public class ContentScreen extends Activity implements OnMenuItemClickListener  {
	
	private boolean tabletLayout;
	 
	protected DetailsFragment detailsFrag;
	private ListItemFragment listFrag;

	protected Menu menu;
	
	protected HashMap<String, Object> row;
	protected int index;

	protected String type = "Beer";

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        tabletLayout = getWindowManager().getDefaultDisplay().getWidth() > 721;
        
        setContentView();
        
        initActionBar();
	}
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
    	if(listFrag != null)
    		listFrag.updateData();
    	if(detailsFrag != null)
    		detailsFrag.updateData();
    }

	protected void setContentView() {
		if(tabletLayout)
        	setContentView(R.layout.tablet_content_view);
       else
        	setContentView(R.layout.content_view);
	}
    
	protected void initActionBar(){
    	getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(R.layout.search_bar);
        getActionBar().setDisplayShowHomeEnabled(true);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.menu = menu;
		
		SubMenu sub = menu.addSubMenu(type);
		sub.add("Beer").setOnMenuItemClickListener(this);
		sub.add("Food").setOnMenuItemClickListener(this);
		
		sub.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    	
    	return super.onCreateOptionsMenu(menu);
    }

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		String newType = item.getTitle().toString();
		if(!type.equals(newType)){
			type = newType;
			listFrag.updateData();
		}
		return true;
	}
	
	public void goToDetailsScreen(HashMap<String, Object> row, int index) {
		Intent i = new Intent();
		i.setClass(this, DetailsScreen.class);
		i.putExtra("row", row);
		i.putExtra("index", index);
		startActivity(i);
	}
	
	public void updateRowBackground(boolean isChecked) {
		if(listFrag != null)
			((BeerDatabaseResultsAdapter)listFrag.getListAdapter()).setTried(index, isChecked);
	}

	public boolean isTabletLayout() {
		return tabletLayout;
	}

	public DetailsFragment getDetailsFrag() {
		return detailsFrag;
	}

	public void setDetailsFrag(DetailsFragment detailsFrag) {
		this.detailsFrag = detailsFrag;
	}

	public ListItemFragment getListFrag() {
		return listFrag;
	}

	public void setListFrag(ListItemFragment listFrag) {
		this.listFrag = listFrag;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	
	public EditText getSearchBar(){
		return ((EditText)getActionBar().getCustomView().findViewById(R.id.searchText));
	}
	
	public String getFilterText() {
		return getSearchBar().getText().toString();
	}

	public HashMap<String, Object> getRow() {
		return row;
	}

	public int getIndex() {
		return index;
	}

	public void setRow(HashMap<String, Object> row, int position) {
		this.row = row;
		this.index = position;
	}

	public String getType() {
		return type;
	}

	
	
}
