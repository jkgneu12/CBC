package com.devcamp.fragment;

import java.util.HashMap;
import java.util.List;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devcamp.ContentScreen;
import com.devcamp.R;
import com.devcamp.session.CBCSessionVars;

public class DetailsFragment extends Fragment implements OnCheckedChangeListener{

	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.detail_fragment, container, false);
        v.setBackgroundColor(Color.parseColor("#222222"));
        return v;
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		getContentScreen().setDetailsFrag(this);
	}
	
	@Override
	public void onCheckedChanged(CompoundButton button, boolean isChecked) {
		updateSessionVariables(isChecked);
		getContentScreen().updateRowBackground(isChecked);
		showToast(isChecked);
	}
	
	private void updateSessionVariables(boolean isChecked) {
		CBCSessionVars sv = new CBCSessionVars(getActivity());
		HashMap<String, Object> row = getContentScreen().getRow();
		
		if(isChecked){
			if(isBeerType())
				sv.addBeerTried((String)row.get("pk"));
			else
				sv.addFoodTried((String)row.get("pk"));
		} else {
			if(isBeerType())
				sv.removeBeerTried((String)row.get("pk"));
			else
				sv.removeFoodTried((String)row.get("pk"));
		}	
	}
	
	private void showToast(boolean isChecked){
		String toastMessage;
		if(isBeerType())
			toastMessage = "Beer";
		else
			toastMessage = "Meal";
		
		if(isChecked)
			Toast.makeText(DetailsFragment.this.getActivity(), "Sweet! Enjoy your " + toastMessage + "!", 1000).show();
		else
			Toast.makeText(DetailsFragment.this.getActivity(), toastMessage + " removed.", 1000).show();
	}

	public void updateData() {
		updateData(getView());
	}
	
	public void updateData(View v){
		HashMap<String, Object> row = ((ContentScreen)getActivity()).getRow();
		
		if(row != null){
			updateName(v, row);
	        updateDescription(v, row);
	        updateImage();
	        updateCheckbox(v, row);
	        updateOG(v, row);
	        updateFG(v, row);
		}
	}
	
	private void updateName(View v, HashMap<String, Object> row) {
		String name;
		if(row.containsKey("name"))
			name = (String)row.get("name");
		else
			name = (String)row.get("item");
		name = Html.fromHtml(name).toString();
		
		((TextView)v.findViewById(R.id.title)).setText(name);
	}
	
	private void updateDescription(View v, HashMap<String, Object> row) {
		String description = (String)row.get("description");
		description = Html.fromHtml(description).toString();
		
		((TextView)v.findViewById(R.id.description)).setText(description);
	}

	private void updateImage() {
		ImageView myImage = (ImageView) getActivity().findViewById(R.id.image);
		if( myImage != null ) { 
		    if(isBeerType()){
		    	myImage.setVisibility(View.INVISIBLE);
		    } else {
		    	myImage.setVisibility(View.VISIBLE);
		    	myImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.burger));
		    }
		}
	}
	
	private void updateCheckbox(View v, HashMap<String, Object> row) {
		CBCSessionVars sv = new CBCSessionVars(getActivity());
	    List<String> itemsTried = getItemsTried(getContentScreen().getType(), sv);
	    
	    boolean tried = false;
		if( itemsTried.contains((String)row.get("pk")) ){
			tried = true;
		}
		
		CheckBox drankItCheckbox = ((CheckBox)v.findViewById(R.id.tried));
		drankItCheckbox.setOnCheckedChangeListener(null);//dont want toast to appear
		drankItCheckbox.setChecked(tried);
		drankItCheckbox.setOnCheckedChangeListener(this);
}

	private void updateOG(View v, HashMap<String, Object> row) {
		TextView ogLabel = (TextView) v.findViewById(R.id.og_label);
		TextView ogValue = (TextView) v.findViewById(R.id.og_value);
		if( ogValue != null ) {
			if(row.containsKey("og")){
				ogLabel.setVisibility(View.VISIBLE);
				ogValue.setText((String)row.get("og"));
			}else{
				ogLabel.setVisibility(View.INVISIBLE);
				ogValue.setText("");
			}
		}
	}
	
	private void updateFG(View v, HashMap<String, Object> row) {
		TextView fgLabel = (TextView) v.findViewById(R.id.fg_label);
		TextView fgValue = (TextView) v.findViewById(R.id.fg_value);
		if( fgValue != null ) { 
			if(row.containsKey("fg")){
				fgLabel.setVisibility(View.VISIBLE);
				fgValue.setText((String)row.get("fg"));
			}else{
				fgLabel.setVisibility(View.INVISIBLE);
				fgValue.setText("");
			}
		}
	}

	private List<String> getItemsTried(String type, final CBCSessionVars sv) {
		List<String> beersTried;
		if(type.equals("Beer")){
			beersTried = sv.getBeersTried();
		} else {
			beersTried = sv.getFoodTried();
		}
		return beersTried;
	}
	
	private boolean isBeerType() {
		return getContentScreen().getType().equals("Beer");
	}
	
	private ContentScreen getContentScreen(){
		return (ContentScreen)getActivity();
	}
}
