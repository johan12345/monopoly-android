package de.unikiel.programmierpraktikum.monopoly.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.unikiel.programmierpraktikum.monopoly.exceptions.LackOfMoneyException;
import de.unikiel.programmierpraktikum.monopoly.model.StreetSpace.Category;

/**
 * Represents a player and saves his position, amount of money, peg, name,
 * property, index, debt and whether or not he is currently in jail or has lost.
 * 
 * @author Miriam Scharnke, Johan v. Forstner
 */
public class Player implements Serializable {
	private static final long serialVersionUID = 8343266192449232070L;

	/**
	 * All possible pegs.
	 */
	public enum Peg {
		MARIE_CURIE, EMMY_NOETHER, ALBERT_EINSTEIN, MAX_PLANCK, RICHARD_FEYNMAN, STEPHEN_HAWKING, ERWIN_SCHROEDINGER, MICHAEL_FARADAY, WOLFGANG_PAULI, WERNER_HEISENBERG
	}

	private int currentPos;
	private double money;
	private Peg peg;
	private String name;
	private int inJail;
	private List<Space> property;
	private int index;
	private boolean hasLost;
	private double debt;

	public Player(String name, Peg peg) {
		this.name = name;
		this.peg = peg;
		money = 0;
		inJail = 0;
		currentPos = 0;
		property = new ArrayList<Space>();
		hasLost = false;
	}

	/**
	 * @return the current position of this player
	 */
	public int getCurrentPos() {
		return currentPos;
	}

	/**
	 * @param currentPos
	 *            the current position to set
	 */
	public void setCurrentPos(int currentPos) {
		this.currentPos = currentPos;
	}

	/**
	 * @return the amount of money the player owns.
	 */
	public double getMoney() {
		return money;
	}

	/**
	 * Pay a certain amount of money
	 * 
	 * @param amount
	 *            the amount to pay
	 * @throws LackOfMoneyException
	 *             when the player doesn't have enough money
	 */
	public void pay(double amount) throws LackOfMoneyException {
		if (money < 0)
			throw new IllegalArgumentException();
		if (money >= amount) {
			money -= amount;
		} else {
			throw new LackOfMoneyException(amount);
		}
	}

	/**
	 * Earn a certain amount of money
	 * 
	 * @param amount
	 *            the amount to earn
	 */
	public void earn(double amount) {
		if (money < 0)
			throw new IllegalArgumentException();
		money += amount;
	}

	/**
	 * @return the peg of this player
	 */
	public Peg getPeg() {
		return peg;
	}

	/**
	 * @param peg
	 *            the peg to set
	 */
	public void setPeg(Peg peg) {
		this.peg = peg;
	}

	/**
	 * @return the name of this playr
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return whether this player is in jail or visiting
	 */
	public boolean isInJail() {
		return inJail > 0;
	}

	/**
	 * @return how many turns this player has had since having been imprisoned
	 */
	public int getJailCounter() {
		return inJail;
	}

	/**
	 * Increase the jail counter by 1
	 */
	public void increaseJailCounter() {
		inJail++;
	}

	/**
	 * Set whether this player is in jail
	 * 
	 * @param inJail
	 *            whether this player is in jail
	 */
	public void setInJail(int inJail) {
		this.inJail = inJail;
	}

	/**
	 * @return the amount of {@link StationSpace}s this player owns
	 */
	public int getStationCount() {
		int count = 0;
		for (Space space : property) {
			if (space instanceof StationSpace)
				count++;
		}
		return count;
	}

	/**
	 * @return the amount of {@link UtilitySpace}s this player owns
	 */
	public int getUtilityCount() {
		int count = 0;
		for (Space space : property) {
			if (space instanceof UtilitySpace)
				count++;
		}
		return count;
	}

	/**
	 * @return all the spaces this player owns
	 */
	public List<Space> getProperty() {
		return property;
	}

	/**
	 * Check whether this player is able to buy a house on the given space,
	 * taking into account whether he owns all spaces of the category and if
	 * they have allowed counts of houses (houses must be "spread equally")
	 * 
	 * @param space
	 *            the space to check
	 * @param game
	 *            the current game
	 * @return true when the player is able to buy a house here
	 */
	public boolean isAbleToBuyHouse(StreetSpace space, Game game) {
		boolean allSpacesOwned = checkAllSpacesOwned(space.getCategory(), game);
		int maxHouseCount = getMaxHouseCount(space, game,
				space.getHousesCount() + 1);
		int minHouseCount = getMinHouseCount(space, game,
				space.getHousesCount() + 1);
		return allSpacesOwned && maxHouseCount - minHouseCount <= 1;
	}

	/**
	 * Check whether this player is able to sell a house on the given space,
	 * taking into account whether he owns all spaces of the category and if
	 * they have allowed counts of houses (houses must be "spread equally")
	 * 
	 * @param space
	 *            the space to check
	 * @param game
	 *            the current game
	 * @return true when the player is able to sell a house here
	 */
	public boolean isAbleToSellHouse(StreetSpace space, Game game) {
		boolean allSpacesOwned = checkAllSpacesOwned(space.getCategory(), game);
		int maxHouseCount = getMaxHouseCount(space, game,
				space.getHousesCount() - 1);
		int minHouseCount = getMinHouseCount(space, game,
				space.getHousesCount() - 1);
		return allSpacesOwned && maxHouseCount - minHouseCount <= 1;
	}

	/**
	 * Check whether this player owns all the spaces of a given category
	 * 
	 * @param category
	 *            the category to check
	 * @param game
	 *            the current game
	 * @return true, if this player owns all spaces of the category
	 */
	private boolean checkAllSpacesOwned(Category category, Game game) {
		for (Space currentSpace : game.getSpaces()) {
			if (currentSpace instanceof StreetSpace) {
				StreetSpace streetSpace = (StreetSpace) currentSpace;
				if (streetSpace.getCategory() == category) {
					if (!this.equals(streetSpace.getOwner()))
						return false;
				}
			}
		}
		return true;
	}

	/**
	 * Utility method for {@link #isAbleToBuyHouse(StreetSpace, Game)} and
	 * {@link #isAbleToSellHouse(StreetSpace, Game)}.
	 * 
	 * @param space
	 *            a street space
	 * @param game
	 *            the current game
	 * @param startHouseCount
	 *            the number to start with (prospective number of houses on the
	 *            given space)
	 * @return the maximum house count on the spaces of the given space's
	 *         category, excluding that space itself, or the given
	 *         startHouseCount if it is higher
	 */
	private int getMaxHouseCount(StreetSpace space, Game game,
			int startHouseCount) {
		int maxHouseCount = startHouseCount;
		for (Space currentSpace : game.getSpaces()) {
			if (currentSpace instanceof StreetSpace) {
				StreetSpace streetSpace = (StreetSpace) currentSpace;
				if (streetSpace.getCategory() == space.getCategory()
						&& !streetSpace.equals(space)) {
					if (streetSpace.getHousesCount() > maxHouseCount)
						maxHouseCount = streetSpace.getHousesCount();
				}
			}
		}
		return maxHouseCount;
	}

	/**
	 * Utility method for {@link #isAbleToBuyHouse(StreetSpace, Game)} and
	 * {@link #isAbleToSellHouse(StreetSpace, Game)}.
	 * 
	 * @param space
	 *            a street space
	 * @param game
	 *            the current game
	 * @param startHouseCount
	 *            the number to start with (prospective number of houses on the
	 *            given space)
	 * @return the minimum house count on the spaces of the given space's
	 *         category, excluding that space itself, or the given
	 *         startHouseCount if it is lower
	 */
	private int getMinHouseCount(StreetSpace space, Game game,
			int startHouseCount) {
		int minHouseCount = startHouseCount;
		for (Space currentSpace : game.getSpaces()) {
			if (currentSpace instanceof StreetSpace) {
				StreetSpace streetSpace = (StreetSpace) currentSpace;
				if (streetSpace.getCategory() == space.getCategory()
						&& !streetSpace.equals(space)) {
					if (streetSpace.getHousesCount() < minHouseCount)
						minHouseCount = streetSpace.getHousesCount();
				}
			}
		}
		return minHouseCount;
	}

	/**
	 * @return the index of this player on the list of players
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index
	 *            the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return whether this player has already lost
	 */
	public boolean hasLost() {
		return hasLost;
	}

	/**
	 * Save that this player has lost
	 */
	public void setHasLost() {
		hasLost = true;
	}

	/**
	 * @return the debt this player needs to pay before his turn ends
	 */
	public double getDebt() {
		return debt;
	}

	/**
	 * set a debt this player needs to pay before his turn ends
	 * @param debt the debt to set        
	 */
	public void setDebt(double debt) {
		this.debt = debt;
	}
}
