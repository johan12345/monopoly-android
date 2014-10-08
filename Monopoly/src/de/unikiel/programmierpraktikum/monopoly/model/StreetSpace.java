package de.unikiel.programmierpraktikum.monopoly.model;

import de.unikiel.programmierpraktikum.monopoly.utilities.Utilities;

/**
 * Represents a street {@link Space}
 * 
 * @author Miriam Scharnke, Johan v. Forstner
 */
public class StreetSpace extends BuyableSpace {
	private static final long serialVersionUID = 3101782499194398218L;

	/**
	 * Represents the categories (colors) a Street can be assigned to
	 */
	public enum Category {
		BROWN, LIGHT_BLUE, PINK, ORANGE, RED, YELLOW, GREEN, DARK_BLUE;
	}

	private Category category;
	private double baseRent;
	private int housesCount;

	/**
	 * @return the Category this street is assigned to
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * Assign this space to a Category (color)
	 * 
	 * @param category
	 *            the Category to set
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	public double getRent() {
		if (isMortgage()) {
			return 0;
		} else {
			switch (housesCount) {
			case 0:
				return baseRent;
			case 1:
				return Utilities.round(100, baseRent * 5);
			case 2:
				return Utilities.round(100, baseRent * 15);
			case 3:
				return Utilities.round(1000, baseRent * 35);
			case 4:
				return Utilities.round(1000, baseRent * 42);
			case 5:
				return Utilities.round(1000, baseRent * 50);
			default:
				return 0;
			}
		}
	}

	@Override
	public double getPurchasePrice() {
		return Utilities.round(100, baseRent * 12);
	}

	/**
	 * @return the price a player needs to pay to add a house to this space or
	 *         convert 4 houses to a hotel.
	 */
	public double getHousePrice() {
		return Utilities.round(100, baseRent * 8);
	}

	/**
	 * Assign a base rent to this field. {@link getPurchasePrice()},
	 * {@link getRent} and {@link getHousePrice} use this to automatically
	 * calculate suitable values.
	 * 
	 * @param baseRent
	 *            the base rent to set
	 */
	public void setBaseRent(double baseRent) {
		this.baseRent = baseRent;
	}

	/**
	 * @return The count of houses on this space. Returns 5 when there is a
	 *         hotel.
	 */
	public int getHousesCount() {
		return housesCount;
	}

	/**
	 * @return The count of houses on this space. Returns 0 when there is a
	 *         hotel.
	 */
	public int getRealHousesCount() {
		if (housesCount < 5)
			return housesCount;
		else
			return 0;
	}

	/**
	 * @return The count of hotels on this space. May only be 0 or 1.
	 */
	public int getHotelCount() {
		return housesCount == 5 ? 1 : 0;
	}

	/**
	 * Add a house to this field or convert 4 houses into a hotel
	 */
	public void addHouse() {
		this.housesCount++;
	}

	/**
	 * Remove a house from this field or convert a hotel into 4 houses
	 */
	public void removeHouse() {
		this.housesCount--;
	}
}
