package de.unikiel.programmierpraktikum.monopoly.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.text.DecimalFormat;

import de.unikiel.programmierpraktikum.monopoly.R;
import de.unikiel.programmierpraktikum.monopoly.controller.GameFieldLoader;
import de.unikiel.programmierpraktikum.monopoly.model.Game;
import de.unikiel.programmierpraktikum.monopoly.model.GoSpace;
import de.unikiel.programmierpraktikum.monopoly.model.Space;
import de.unikiel.programmierpraktikum.monopoly.model.StreetSpace;
import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameActivity extends Activity {
	private Game game;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		try {
			String json = readStream(getAssets().open("field/test_field.json"));
			game = GameFieldLoader.createGame(json);
			buildField();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void buildField() {
		LinearLayout spaces = (LinearLayout) findViewById(R.id.spaces);
		LayoutInflater inflater = getLayoutInflater();
		
		for(Space space:game.getSpaces()) {
			View spaceView = null;
			if(space instanceof GoSpace) {
				spaceView = inflater.inflate(R.layout.layout_go_space, spaces, false);
			} else if (space instanceof StreetSpace) {
				StreetSpace street = (StreetSpace) space;
				spaceView = inflater.inflate(R.layout.layout_street_space, spaces, false);
				
				TextView name = (TextView) spaceView.findViewById(R.id.txtName);
				name.setText(street.getName());
				
				DecimalFormat format = new DecimalFormat("#,##0 eV");
				
				TextView price = (TextView) spaceView.findViewById(R.id.txtPrice);
				price.setText(format.format(street.getPurchasePrice()));
				
				View category = spaceView.findViewById(R.id.category);
				switch (street.getCategory()) {
				case BROWN:
					category.setBackgroundResource(R.color.category_brown);
					break;
				case LIGHT_BLUE:
					category.setBackgroundResource(R.color.category_light_blue);
					break;
				case PINK:
					category.setBackgroundResource(R.color.category_pink);
					break;
				case ORANGE:
					category.setBackgroundResource(R.color.category_orange);
					break;
				case RED:
					category.setBackgroundResource(R.color.category_red);
					break;
				case YELLOW:
					category.setBackgroundResource(R.color.category_yellow);
					break;
				case GREEN:
					category.setBackgroundResource(R.color.category_green);
					break;
				case DARK_BLUE:
					category.setBackgroundResource(R.color.category_dark_blue);
					break;
					
				}
			}
			spaces.addView(spaceView);
		}
	}
	
	
	private static String readStream(InputStream input) throws IOException {
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

}
