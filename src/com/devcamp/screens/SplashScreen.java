package com.devcamp.screens;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public abstract class SplashScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initContentView();
	}
	
	protected void initContentView(){
		LinearLayout layout = new LinearLayout(this);
		layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		initLayout(layout);
		setContentView(layout);
	}
	
	protected void initLayout(LinearLayout layout){
		ImageView img = new ImageView(this);
		if( getImageBackground() != null ) { 
			img.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			img.setImageDrawable(getImageBackground());
			img.setScaleType(ScaleType.FIT_XY);
			layout.addView(img);
		}
		modifyRootLayout(layout);
	}
	
	protected abstract void modifyRootLayout(LinearLayout layout);
	
	protected abstract Drawable getImageBackground();
	
	@Override
	protected void onStart() {
		super.onStart();
		HomeScreenHandler handler = new HomeScreenHandler();
		handler.sleep(getSleepTime());
	}
	
	protected abstract int getSleepTime();
	protected abstract boolean goHome();
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	protected void initializeNextIntent(Intent i){
		String nextScreen = getPackageName()+".";
		if( goHome() ) { 
			nextScreen += getHomeScreen();
		} else { 
			nextScreen += getFinalScreen();
		}
		i.setClassName(getPackageName(), nextScreen);
	}
	
	protected abstract String getHomeScreen();
	protected abstract String getFinalScreen();
	
	public void goToHomeScreen(){
		Intent i = new Intent();
		initializeNextIntent(i);
		startActivity(i);
		SplashScreen.this.finish(); // kill from backstack
	}

	public class HomeScreenHandler extends Handler { 
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			goToHomeScreen();
		}
		public void sleep(long millis){
			sendMessageDelayed(obtainMessage(0), millis);
		}
	}
	
}
