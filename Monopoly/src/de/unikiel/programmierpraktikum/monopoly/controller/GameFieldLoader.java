package de.unikiel.programmierpraktikum.monopoly.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.unikiel.programmierpraktikum.monopoly.model.*;
import de.unikiel.programmierpraktikum.monopoly.model.StreetSpace.Category;

/**
 * @author Miriam Scharnke, Johan v. Forstner
 * Erstellt das Spielfeld und lädt die Spielfelder und Karten aus JSON-Dateien
 */
public class GameFieldLoader {
	
	/**
	 * 
	 * @param field JSON String for the field
	 * @param chanceCards JSON String for the chance cards
	 * @param communityCards JSON String for the community cards
	 * @param players List of players
	 * @return a new Game with all the attributes loaded
	 */
	public static Game createGame(String field, String chanceCards, String communityCards, List<Player> players) {
		Game game = new Game();
		game.setPlayers(players);
		game.setChanceCards(loadChanceCards(chanceCards));
		game.setCommunityCards(loadChanceCards(communityCards));		
		game.setSpaces(loadSpaces(field));
		return game;
	}
	
	private static List<ChanceCard> loadChanceCards(String chanceCards) {
		try {
			JSONArray cards = new JSONArray(chanceCards);
			List<ChanceCard> cardsList = new ArrayList<ChanceCard>();
			for(int i = 0; i < cards.length(); i++) {
				JSONObject cardJson = cards.getJSONObject(i);
				ChanceCard card = null;
				String type = cardJson.getString("type");
				if(type.equals("GoToJailChanceCard")) {
					GoToJailChanceCard goToJail = new GoToJailChanceCard();
					card = goToJail;
				} else if(type.equals("MoveAmountChanceCard")) {
					MoveAmountChanceCard moveAmount = new MoveAmountChanceCard();
					moveAmount.setAmount(cardJson.getInt("amount"));
					card = moveAmount;
				} else if(type.equals("MoveToChanceCard")) {
					MoveToChanceCard moveTo = new MoveToChanceCard();
					moveTo.setSpacePos(cardJson.getInt("position"));
					card = moveTo;
				} else if(type.equals("PayChanceCard")) {
					PayChanceCard pay = new PayChanceCard();
					pay.setAmount(cardJson.getDouble("payamount"));
					card = pay;
				} else if(type.equals("PayRenovationChanceCard")) {
					PayRenovationChanceCard pay = new PayRenovationChanceCard();
					pay.setHouseAmount(cardJson.getJSONArray("payamount").getDouble(0));
					pay.setHotelAmount(cardJson.getJSONArray("payamount").getDouble(1));
					card = pay;
				}
				card.setText(cardJson.getString("text"));
				cardsList.add(card);
			}
			return cardsList;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static List<Space> loadSpaces(String field) {
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
					StationSpace station = new StationSpace();
					station.setName(spaceJson.getString("name"));
					space = station;
				} else if (type.equals("utility")) {
					UtilitySpace utility = new UtilitySpace();
					utility.setName(spaceJson.getString("name"));
					space = utility;
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
			
			return spacesList;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
