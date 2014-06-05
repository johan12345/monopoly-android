/**
 * 
 */
package de.unikiel.programmierpraktikum.monopoly.model;

import java.util.List;

import de.unikiel.programmierpraktikum.monopoly.utilities.Utilities;

/**
 * @author Miriam Scharnke, Johan v. Forstner
 * Speichert die Spieler, das Spielfeld und ...
 */
public class Game {
	private List<Player> players;
	private List<Space> spaces;
	private List<ChanceCard> chanceCards;
	private List<ChanceCard> communityCards;
	public final static double SALARY = 4000;
	public final static double START_MONEY = 30000;
	
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
		this.players = players;
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
		this.spaces = spaces;
	}
	/**
	 * @return the chanceCards
	 */
	public ChanceCard getRandomChanceCard() {
		return chanceCards.get(Utilities.randomInt(0,chanceCards.size() - 1));
	}
	/**
	 * @param chanceCards the chanceCards to set
	 */
	public void setChanceCards(List<ChanceCard> chanceCards) {
		this.chanceCards = chanceCards;
	}
	/**
	 * @return the communityCards
	 */
	public ChanceCard getRandomCommunityCard() {
		return communityCards.get(Utilities.randomInt(0,communityCards.size() - 1));
	}
	/**
	 * @param communityCards the communityCards to set
	 */
	public void setCommunityCards(List<ChanceCard> communityCards) {
		this.communityCards = communityCards;
	}
	
	public int getJailPos() {
		for(int i = 0; i < spaces.size(); i++) {
			if(spaces.get(i) instanceof JailSpace)
				return i;
		}
		return -1;
	}
	
}
