package de.unikiel.programmierpraktikum.monopoly.utilities;

import android.content.Context;
import android.util.DisplayMetrics;

public class Utilities {
	public static int randomInt(int min, int max) {
		return min + (int)(Math.random() * ((max - min) + 1));
	}
	
	public static int dice() {
		return randomInt(1,6) + randomInt(1,6);
	}
	
	public static int dpToPx(int dp, Context context) {
	    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
	    int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));       
	    return px;
	}
	
	public static int pxToDp(int px, Context context) {
	    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
	    int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
	    return dp;
	}
}
