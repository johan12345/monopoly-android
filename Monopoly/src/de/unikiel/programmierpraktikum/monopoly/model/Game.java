/**
 * 
 */
package de.unikiel.programmierpraktikum.monopoly.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.unikiel.programmierpraktikum.monopoly.utilities.Utilities;

/**
 * @author Miriam Scharnke, Johan v. Forstner
 * Saves the players, the field, the spaces and the chance cards.
 */
public class Game implements Serializable {
	private static final long serialVersionUID = -8128552561059232122L;
	private ArrayList<Player> players;
	private ArrayList<Space> spaces;
	private ArrayList<ChanceCard> chanceCards;
	private ArrayList<ChanceCard> communityCards;
	public final static double SALARY = 4000;
	public final static double START_MONEY = 30000;
	public final static double JAIL_BAIL = 1000;
	
	/**
	 * @return the players
	 */
	public List<Player> getPlayers() {
		return players;
	}
	/**
	 * @param players the players to set
	 */
	public void setPlayers(List<Player> players) {
		this.players = new ArrayList<Player>(players);
	}
	/**
	 * @return the spaces
	 */
	public List<Space> getSpaces() {
		return spaces;
	}
	/**
	 * @param spaces the spaces to set
	 */
	public void setSpaces(List<Space> spaces) {
		this.spaces = new ArrayList<Space>(spaces);
	}
	/**
	 * @return the chanceCards
	 */
	public ChanceCard getChanceCard() {
		ChanceCard card = chanceCards.get(0);
		chanceCards.remove(0);
		chanceCards.add(card);
		return card;
	}
	/**
	 * @param chanceCards the chanceCards to set
	 */
	public void setChanceCards(List<ChanceCard> chanceCards) {
		this.chanceCards = new ArrayList<ChanceCard>(chanceCards);
	}
	/**
	 * @return the communityCards
	 */
	public ChanceCard getCommunityCard() {
		ChanceCard card = communityCards.get(0);
		communityCards.remove(0);
		communityCards.add(card);
		return card;
	}
	/**
	 * @param communityCards the communityCards to set
	 */
	public void setCommunityCards(List<ChanceCard> communityCards) {
		this.communityCards = new ArrayList<ChanceCard>(communityCards);
	}
	
	public int getJailPos() {
		for(int i = 0; i < spaces.size(); i++) {
			if(spaces.get(i) instanceof JailSpace)
				return i;
		}
		return -1;
	}
	/**
	 * @return the chanceCards
	 */
	public List<ChanceCard> getChanceCards() {
		return chanceCards;
	}
	/**
	 * @return the communityCards
	 */
	public List<ChanceCard> getCommunityCards() {
		return communityCards;
	}
	
	public List<BuyableSpace> getProperty(Player player) {
		List<BuyableSpace> property = new ArrayList<BuyableSpace>();
		for (Space space:spaces) {
			if (space instanceof BuyableSpace) {
				BuyableSpace buyable = (BuyableSpace) space;
				if (buyable.getOwner() == player) {
					property.add(buyable);
				}
			}
		}
		return property;
	}
	
}
