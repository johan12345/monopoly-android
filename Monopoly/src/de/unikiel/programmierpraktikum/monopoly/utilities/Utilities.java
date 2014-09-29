package de.unikiel.programmierpraktikum.monopoly.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import de.unikiel.programmierpraktikum.monopoly.R;
import de.unikiel.programmierpraktikum.monopoly.model.Player.Peg;
import de.unikiel.programmierpraktikum.monopoly.model.StreetSpace.Category;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Contains useful methods that are important for the game.
 * 
 * @author Miriam Scharnke, Johan v. Forstner
 * 
 */
public class Utilities {
	/**
	 * Calculate a random integer
	 * 
	 * @param min
	 *            minimum value
	 * @param max
	 *            maximum value
	 * @return a random integer between the min and max values
	 */
	public static int randomInt(int min, int max) {
		return min + (int) (Math.random() * ((max - min) + 1));
	}

	/**
	 * @return A random Integer between 2 and 12, analogous to throwing two dice
	 */
	public static int dice() {
		return randomInt(1, 6) + randomInt(1, 6);
	}

	/**
	 * Throw two dice and check if they are the same value
	 * 
	 * @return a boolean corresponding to the result
	 */
	public static boolean diceDouble() {
		return randomInt(1, 6) == randomInt(1, 6);
	}

	/**
	 * Convert Android's density independent pixels to pixels
	 * 
	 * @param dp
	 *            the amount of dps to convert
	 * @param context
	 *            a context
	 * @return the conversion to pixels
	 */
	public static int dpToPx(int dp, Context context) {
		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();
		int px = Math.round(dp
				* (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		return px;
	}

	/**
	 * Convert pixels to Android's density independent pixels
	 * 
	 * @param px
	 *            the amount of pixels to convert
	 * @param context
	 *            a context
	 * @return the conversion to dps
	 */
	public static int pxToDp(int px, Context context) {
		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();
		int dp = Math.round(px
				/ (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		return dp;
	}

	/**
	 * Draw a {@link View} and save the output to a bitmap
	 * 
	 * @param v
	 *            the View to use
	 * @return the bitmap
	 */
	public static Bitmap loadBitmapFromView(View v) {
		Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
		v.draw(c);
		return b;
	}

	/**
	 * Get the colored circle Drawable ID corresponding to a player index
	 * 
	 * @param index
	 *            the player index
	 * @return Drawable ID, or -1 if unknown
	 */
	public static int getCircleDrawable(int index) {
		switch (index) {
		case 0:
			return R.drawable.ic_circle_1;
		case 1:
			return R.drawable.ic_circle_2;
		case 2:
			return R.drawable.ic_circle_3;
		case 3:
			return R.drawable.ic_circle_4;
		case 4:
			return R.drawable.ic_circle_5;
		case 5:
			return R.drawable.ic_circle_6;
		case 6:
			return R.drawable.ic_circle_7;
		case 7:
			return R.drawable.ic_circle_8;
		case 8:
			return R.drawable.ic_circle_9;
		case 9:
			return R.drawable.ic_circle_10;
		default:
			return -1;
		}
	}

	/**
	 * Get the Drawable ID corresponding to a specific {@link Peg}
	 * 
	 * @param peg
	 *            the Peg
	 * @return the corresponding Drawable ID, or -1 if unknown
	 */
	public static int getPegDrawable(Peg peg) {
		switch (peg) {
		case ALBERT_EINSTEIN:
			return R.drawable.ic_albert_einstein;
		case EMMY_NOETHER:
			return R.drawable.ic_emmy_noether;
		case ERWIN_SCHROEDINGER:
			return R.drawable.ic_erwin_schroedinger;
		case MARIE_CURIE:
			return R.drawable.ic_marie_curie;
		case MAX_PLANCK:
			return R.drawable.ic_max_planck;
		case MICHAEL_FARADAY:
			return R.drawable.ic_michael_faraday;
		case RICHARD_FEYNMAN:
			return R.drawable.ic_richard_feynman;
		case STEPHEN_HAWKING:
			return R.drawable.ic_stephen_hawking;
		case WERNER_HEISENBERG:
			return R.drawable.ic_werner_heisenberg;
		case WOLFGANG_PAULI:
			return R.drawable.ic_wolfgang_pauli;
		default:
			return -1;
		}
	}

	/**
	 * Get the Color ID corresponding to a field {@link Category}
	 * 
	 * @param category
	 *            the category
	 * @return the color ID, or -1 if unknown
	 */
	public static int getCategoryColor(Category category) {
		switch (category) {
		case BROWN:
			return R.color.category_brown;
		case LIGHT_BLUE:
			return R.color.category_light_blue;
		case PINK:
			return R.color.category_pink;
		case ORANGE:
			return R.color.category_orange;
		case RED:
			return R.color.category_red;
		case YELLOW:
			return R.color.category_yellow;
		case GREEN:
			return R.color.category_green;
		case DARK_BLUE:
			return R.color.category_dark_blue;
		default:
			return -1;
		}
	}

	/**
	 * @return the {@link DecimalFormat} used for formatting currencies in this
	 *         game
	 */
	public static DecimalFormat moneyFormat() {
		return new DecimalFormat("#,##0 eV");
	}

	/**
	 * Shortcut method for reading an input stream into a String
	 * 
	 * @param input
	 *            an InputStream
	 * @return the String read from the stream
	 * @throws IOException
	 *             when there is an error reading the stream
	 */
	public static String readStream(InputStream input) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(input));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			return sb.toString();
		} finally {
			br.close();
		}
	}

	/**
	 * Shortcut method for Android's visibility attributes
	 * 
	 * @param visible
	 *            visible or not
	 * @return {@link View#VISIBLE} if visible == true, otherwise
	 *         {@link View#INVISIBLE}
	 */
	public static int visibility(boolean visible) {
		return visible ? View.VISIBLE : View.INVISIBLE;
	}

	/**
	 * Converts a given string into title case
	 * ("Capital Letter At The Start Of Each Word").
	 * 
	 * @param input
	 *            a String in any case
	 * @return the same String, converted to title case
	 */
	public static String toTitleCase(String input) {
		StringBuilder titleCase = new StringBuilder();
		boolean nextTitleCase = true;
		for (char c : input.toCharArray()) {
			if (Character.isSpaceChar(c)) {
				nextTitleCase = true;
			} else if (nextTitleCase) {
				c = Character.toUpperCase(c);
				nextTitleCase = false;
			} else {
				c = Character.toLowerCase(c);
			}

			titleCase.append(c);
		}
		return titleCase.toString();
	}
}
