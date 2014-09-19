package de.unikiel.programmierpraktikum.monopoly.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class for a Monopoly Game. Saves the players, the field, the spaces and
 * the chance cards.
 * 
 * @author Miriam Scharnke, Johan v. Forstner
 * 
 */
public class Game implements Serializable {
	private static final long serialVersionUID = -8128552561059232122L;
	private ArrayList<Player> players;
	private ArrayList<Space> spaces;
	private ArrayList<ChanceCard> chanceCards;
	private ArrayList<ChanceCard> communityCards;

	/**
	 * The amount of money someone obtains when stepping on or over
	 * {@link GoSpace}.
	 */
	public final static double SALARY = 4000;

	/**
	 * The amount of money every player gets at the beginning of the game.
	 */
	public final static double START_MONEY = 30000;

	/**
	 * The amount of money to be payed when one wants to leave the jail.
	 */
	public final static double JAIL_BAIL = 1000;

	/**
	 * @return the players
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * @param players
	 *            the players to set
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
	 * @param spaces
	 *            the spaces to set
	 */
	public void setSpaces(List<Space> spaces) {
		this.spaces = new ArrayList<Space>(spaces);
	}

	/**
	 * Get a chance card and then "put the card under the stack" (removes the
	 * first card from the list and puts it at the end)
	 * 
	 * @return chance card
	 */
	public ChanceCard getChanceCard() {
		ChanceCard card = chanceCards.get(0);
		chanceCards.remove(0);
		chanceCards.add(card);
		return card;
	}

	/**
	 * @param chanceCards
	 *            the chanceCards to set
	 */
	public void setChanceCards(List<ChanceCard> chanceCards) {
		this.chanceCards = new ArrayList<ChanceCard>(chanceCards);
	}

	/**
	 * Get a community card and then "put the card under the stack" (removes the
	 * first card from the list and puts it at the end)
	 * 
	 * @return community card
	 */
	public ChanceCard getCommunityCard() {
		ChanceCard card = communityCards.get(0);
		communityCards.remove(0);
		communityCards.add(card);
		return card;
	}

	/**
	 * @param communityCards
	 *            the communityCards to set
	 */
	public void setCommunityCards(List<ChanceCard> communityCards) {
		this.communityCards = new ArrayList<ChanceCard>(communityCards);
	}

	/**
	 * Looks for the jail space and returns its position amongst all spaces.
	 * 
	 * @return position of the jail space. If there is no jail space in the list
	 *         of spaces -1, since there is supposed to be a jail space.
	 */
	public int getJailPos() {
		for (int i = 0; i < spaces.size(); i++) {
			if (spaces.get(i) instanceof JailSpace)
				return i;
		}
		return -1;
	}

	/**
	 * @return the chance cards
	 */
	public List<ChanceCard> getChanceCards() {
		return chanceCards;
	}

	/**
	 * @return the community cards
	 */
	public List<ChanceCard> getCommunityCards() {
		return communityCards;
	}

	/**
	 * @return all buyable spaces a player owns.
	 */
	public List<BuyableSpace> getProperty(Player player) {
		List<BuyableSpace> property = new ArrayList<BuyableSpace>();
		for (Space space : spaces) {
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
