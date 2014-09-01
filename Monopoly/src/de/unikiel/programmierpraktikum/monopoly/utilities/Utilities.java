package de.unikiel.programmierpraktikum.monopoly.utilities;

import de.unikiel.programmierpraktikum.monopoly.R;
import de.unikiel.programmierpraktikum.monopoly.model.Player.Peg;
import de.unikiel.programmierpraktikum.monopoly.model.StreetSpace.Category;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * @author johan_000
 * Enthält nützliche static-Methoden
 */
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
	
	public static Bitmap loadBitmapFromView(View v) {
		Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
		v.draw(c);
		return b;
	}

	public static int getCircleDrawable(int index) {
		switch(index) {
		case 0: return R.drawable.ic_circle_1;
		case 1: return R.drawable.ic_circle_2;
		case 2: return R.drawable.ic_circle_3;
		case 3: return R.drawable.ic_circle_4;
		case 4: return R.drawable.ic_circle_5;
		case 5: return R.drawable.ic_circle_6;
		case 6: return R.drawable.ic_circle_7;
		case 7: return R.drawable.ic_circle_8;
		case 8: return R.drawable.ic_circle_9;
		case 9: return R.drawable.ic_circle_10;
		default: return -1;
		}
	}

	public static int getPegDrawable(Peg peg) {
		switch(peg) {
		case ALBERT_EINSTEIN: return R.drawable.ic_albert_einstein;
		case EMMY_NOETHER: return R.drawable.ic_emmy_noether;
		case ERWIN_SCHROEDINGER: return R.drawable.ic_erwin_schroedinger;
		case MARIE_CURIE: return R.drawable.ic_marie_curie;
		case MAX_PLANCK: return R.drawable.ic_max_planck;
		case MICHAEL_FARADAY: return R.drawable.ic_michael_faraday;
		case RICHARD_FEYNMAN: return R.drawable.ic_richard_feynman;
		case STEPHEN_HAWKING: return R.drawable.ic_stephen_hawking;
		case WERNER_HEISENBERG: return R.drawable.ic_werner_heisenberg;
		case WOLFGANG_PAULI: return R.drawable.ic_wolfgang_pauli;
		default: return -1;
		}
	}

	public static int getCategoryColor(Category category) {
		switch (category) {
		case BROWN: return R.color.category_brown;			
		case LIGHT_BLUE: return R.color.category_light_blue;			
		case PINK: return R.color.category_pink;			
		case ORANGE: return R.color.category_orange;		
		case RED: return R.color.category_red;			
		case YELLOW: return R.color.category_yellow;
		case GREEN: return R.color.category_green;		
		case DARK_BLUE: return R.color.category_dark_blue;		
		default: return -1;
		}
	}
}
