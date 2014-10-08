package de.unikiel.programmierpraktikum.monopoly.model;

import de.unikiel.programmierpraktikum.monopoly.utilities.Utilities;

/**
 * Represents a street {@link Space}
 * 
 * @author Miriam Scharnke, Johan v. Forstner
 */
public class StreetSpace extends BuyableSpace {
	private static final long serialVersionUID = 3101782499194398218L;

	public enum Category {
		BROWN, LIGHT_BLUE, PINK, ORANGE, RED, YELLOW, GREEN, DARK_BLUE;
	}
	private Category category;
	private double baseRent;
	private int housesCount;
	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(Category category) {
		this.category = category;
	}
	/**
	 * @return the rent
	 */
	public double getRent() {
		if(isMortgage()) {
			return 0;
		} else {
			switch (housesCount) {
				case 0: return baseRent;
				case 1: return Utilities.round(100, baseRent * 5);
				case 2: return Utilities.round(100, baseRent * 15);
				case 3: return Utilities.round(1000, baseRent * 35);
				case 4: return Utilities.round(1000, baseRent * 42);
				case 5: return Utilities.round(1000, baseRent * 50);
				default: return 0;
			}
		}
	}
	
	/**
	 * @return the purchasing price
	 */
	public double getPurchasePrice() {
		return Utilities.round(100, baseRent * 12);
	}
	
	public double getHousePrice() {
		return Utilities.round(100, baseRent * 8);
	}
	/**
	 * @param baseRent the baseRent to set
	 */
	public void setBaseRent(double baseRent) {
		this.baseRent = baseRent;
	}
	/**
	 * @return the housesCount
	 */
	public int getHousesCount() {
		return housesCount;
	}
	
	public int getRealHousesCount() {
		if (housesCount < 5)
			return housesCount;
		else
			return 0;
	}
	public int getHotelCount() {
		return housesCount == 5 ? 1 : 0;
	}
	/**
	 * 
	 */
	public void addHouse() {
		this.housesCount ++;
	}
	
	public void removeHouse() {
		this.housesCount --;
	}
}
