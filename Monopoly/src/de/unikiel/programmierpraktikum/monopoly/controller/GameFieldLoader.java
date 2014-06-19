package de.unikiel.programmierpraktikum.monopoly.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.unikiel.programmierpraktikum.monopoly.model.Game;
import de.unikiel.programmierpraktikum.monopoly.model.GoSpace;
import de.unikiel.programmierpraktikum.monopoly.model.Space;
import de.unikiel.programmierpraktikum.monopoly.model.StreetSpace;
import de.unikiel.programmierpraktikum.monopoly.model.StreetSpace.Category;

/**
 * @author Miriam Scharnke, Johan v. Forstner
 * Erstellt das Spielfeld und lädt die Spielfelder aus JSON-Dateien
 */
public class GameFieldLoader {
	
	public static Game createGame(String content) {
		Game game = new Game();
		
		try {
			JSONArray spaces = new JSONArray(content);
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
					} else if (category.equals("red")) {
						street.setCategory(Category.RED);
					}
					
					space = street;
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
