package com.devcamp;

import android.graphics.drawable.Drawable;
import android.widget.LinearLayout;

public class SplashScreen extends com.devcamp.screens.SplashScreen {

	@Override
	protected void modifyRootLayout(LinearLayout layout) {
		
	}

	@Override
	protected Drawable getImageBackground() {
		return getResources().getDrawable(R.drawable.cbc_logo);
	}

	@Override
	protected int getSleepTime() {
		return 1400;
	}

	@Override
	protected boolean goHome() {
		return true;
	}

	@Override
	protected String getHomeScreen() {
		return "HomeScreen";
	}

	@Override
	protected String getFinalScreen() {
		return getHomeScreen();
	}

}
