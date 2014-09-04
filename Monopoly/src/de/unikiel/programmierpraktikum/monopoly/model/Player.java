package de.unikiel.programmierpraktikum.monopoly.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.unikiel.programmierpraktikum.monopoly.exceptions.LackOfMoneyException;
import de.unikiel.programmierpraktikum.monopoly.model.StreetSpace.Category;

/**
 * @author Miriam Scharnke, Johan v. Forstner
 * Speichert die Eigenschaften eines Spielers, z.B. Position
 */
public class Player implements Serializable {
	private static final long serialVersionUID = 8343266192449232070L;

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
	 * @return the currentPos
	 */
	public int getCurrentPos() {
		return currentPos;
	}
	/**
	 * @param currentPos the currentPos to set
	 */
	public void setCurrentPos(int currentPos) {
		this.currentPos = currentPos;
	}
	/**
	 * @return the money
	 */
	public double getMoney() {
		return money;
	}
	public void pay(double amount) throws LackOfMoneyException {
		if(money >= amount) {
			money -= amount;
		} else {
			throw new LackOfMoneyException(amount);
		}
	}
	public void earn(double amount) {
		money += amount;
	}
	/**
	 * @return the peg
	 */
	public Peg getPeg() {
		return peg;
	}
	/**
	 * @param peg the peg to set
	 */
	public void setPeg(Peg peg) {
		this.peg = peg;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the inJail
	 */
	public boolean isInJail() {
		return inJail > 0;
	}
	public int getJailCounter() {
		return inJail;
	}
	public void increaseJailCounter() {
		inJail ++;
	}
	/**
	 * @param inJail the inJail to set
	 */
	public void setInJail(int inJail) {
		this.inJail = inJail;
	}
	
	public int getStationCount() {
		int count = 0;
		for(Space space:property) {
			if(space instanceof StationSpace)
				count ++;
		}
		return count;
	}
	
	public int getUtilityCount() {
		int count = 0;
		for(Space space:property) {
			if(space instanceof UtilitySpace)
				count ++;
		}
		return count;
	}
	/**
	 * @return the property
	 */
	public List<Space> getProperty() {
		return property;
	}
	/**
	 * @param property the property to set
	 */
	public void setProperty(List<Space> property) {
		this.property = property;
	}
	
	public boolean isAbleToBuyHouse(StreetSpace space, Game game) {
		Category category = space.getCategory();
		int maxHouseCount = 0;
		for(Space currentSpace:game.getSpaces()) {
			if(currentSpace instanceof StreetSpace) {
				StreetSpace streetSpace = (StreetSpace) currentSpace;				
				if(streetSpace.getCategory() == category && !streetSpace.equals(space)) {
					if(streetSpace.getHousesCount() > maxHouseCount)
						maxHouseCount = streetSpace.getHousesCount();
					
					if(!this.equals(streetSpace.getOwner()))
						return false;
				}
			}
		}
		return space.getHousesCount() <= maxHouseCount;
	}
	
	public boolean isAbleToSellHouse(StreetSpace space, Game game) {
		Category category = space.getCategory();
		int maxHouseCount = 0;
		for(Space currentSpace:game.getSpaces()) {
			if(currentSpace instanceof StreetSpace) {
				StreetSpace streetSpace = (StreetSpace) currentSpace;
				if(streetSpace.getCategory() == category && !streetSpace.equals(space)) {
					if(streetSpace.getHousesCount() > maxHouseCount)
						maxHouseCount = streetSpace.getHousesCount();
					
					if(!this.equals(streetSpace.getOwner()))
						return false;
				}
			}
		}
		return space.getHousesCount() >= maxHouseCount;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public boolean hasLost() {
		return hasLost;
	}
	
	public void setHasLost() {
		hasLost = true;
	}

	public double getDebt() {
		return debt;
	}

	public void setDebt(double debt) {
		this.debt = debt;
	}
}
