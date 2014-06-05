package de.unikiel.programmierpraktikum.monopoly.model;

import java.util.List;

import de.unikiel.programmierpraktikum.monopoly.model.StreetSpace.Category;

/**
 * @author Miriam Scharnke, Johan v. Forstner
 * Speichert die Eigenschaften eines Spielers, z.B. Position
 */
public class Player {
	public enum Peg {
		
	}
	private int currentPos;
	private double money;
	private Peg peg;
	private String name;
	private boolean inJail;
	private List<Space> property;
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
	public void pay(double amount) {
		if(money >= amount) {
			money -= amount;
		} else {
			//TODO: Ausnahme
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
		return inJail;
	}
	/**
	 * @param inJail the inJail to set
	 */
	public void setInJail(boolean inJail) {
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
			if(space instanceof StreetSpace) {
				StreetSpace streetSpace = (StreetSpace) currentSpace;
				if(streetSpace.getCategory() == category) {
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
			if(space instanceof StreetSpace) {
				StreetSpace streetSpace = (StreetSpace) currentSpace;
				if(streetSpace.getCategory() == category) {
					if(streetSpace.getHousesCount() > maxHouseCount)
						maxHouseCount = streetSpace.getHousesCount();
					
					if(!this.equals(streetSpace.getOwner()))
						return false;
				}
			}
		}
		return space.getHousesCount() == maxHouseCount;
	}
}
