package de.unikiel.programmierpraktikum.monopoly.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.unikiel.programmierpraktikum.monopoly.model.ChanceChanceSpace;
import de.unikiel.programmierpraktikum.monopoly.model.CommunityChanceSpace;
import de.unikiel.programmierpraktikum.monopoly.model.FreeParkingSpace;
import de.unikiel.programmierpraktikum.monopoly.model.Game;
import de.unikiel.programmierpraktikum.monopoly.model.GoSpace;
import de.unikiel.programmierpraktikum.monopoly.model.GoToJailSpace;
import de.unikiel.programmierpraktikum.monopoly.model.JailSpace;
import de.unikiel.programmierpraktikum.monopoly.model.PaySpace;
import de.unikiel.programmierpraktikum.monopoly.model.Space;
import de.unikiel.programmierpraktikum.monopoly.model.StationSpace;
import de.unikiel.programmierpraktikum.monopoly.model.StreetSpace;
import de.unikiel.programmierpraktikum.monopoly.model.StreetSpace.Category;
import de.unikiel.programmierpraktikum.monopoly.model.UtilitySpace;

/**
 * @author Miriam Scharnke, Johan v. Forstner
 * Erstellt das Spielfeld und lädt die Spielfelder und Karten aus JSON-Dateien
 */
public class GameFieldLoader {
	
	public static Game createGame(String field) {
		Game game = new Game();
		
		try {
			JSONArray spaces = new JSONArray(field);
			List<Space> spacesList = new ArrayList<Space>();
			for(int i = 0; i < spaces.length(); i++) {
				JSONObject spaceJson = spaces.getJSONObject(i);
				String type = spaceJson.getString("type");
				Space space = null;
				if(type.equals("go")) {
					space = new GoSpace();
				} else if (type.equals("street")) {
					StreetSpace street = new StreetSpace();
					street.setBaseRent(spaceJson.getDouble("base_rent"));
					street.setName(spaceJson.getString("name"));
					
					String category = spaceJson.getString("category");
					if(category.equals("brown")) {
						street.setCategory(Category.BROWN);
					} else if (category.equals("light_blue")) {
						street.setCategory(Category.LIGHT_BLUE);
					} else if (category.equals("pink")) {
						street.setCategory(Category.PINK);
					} else if (category.equals("orange")) {
						street.setCategory(Category.ORANGE);
					} else if (category.equals("red")) {
						street.setCategory(Category.RED);
					} else if (category.equals("yellow")) {
						street.setCategory(Category.YELLOW);
					} else if (category.equals("green")) {
						street.setCategory(Category.GREEN);
					} else if (category.equals("dark_blue")) {
						street.setCategory(Category.DARK_BLUE);
					} 
					
					space = street;
				} else if (type.equals("pay")) {
					PaySpace pay = new PaySpace();
					pay.setName(spaceJson.getString("name"));
					pay.setAmount(spaceJson.getDouble("amount"));
					space = pay;
				} else if (type.equals("station")) {
					space = new StationSpace();
				} else if (type.equals("utility")) {
					space = new UtilitySpace();
				} else if (type.equals("jail")) {
					space = new JailSpace();
				} else if (type.equals("go_to_jail")) {
					space = new GoToJailSpace();
				} else if (type.equals("chance")) {
					space = new ChanceChanceSpace();
				} else if (type.equals("community")) {
					space = new CommunityChanceSpace();
				} else if (type.equals("free_parking")) {
					space = new FreeParkingSpace();
				}
				spacesList.add(space);
			}
			game.setSpaces(spacesList);
			
			return game;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
