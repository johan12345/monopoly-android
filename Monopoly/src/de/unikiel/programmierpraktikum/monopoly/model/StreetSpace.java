package de.unikiel.programmierpraktikum.monopoly.model;

/**
 * @author Miriam Scharnke, Johan v. Forstner
 * Speichert die Eigenschaften eines Straﬂen-Feldes auf dem Spielfeld
 */
public class StreetSpace extends BuyableSpace {
	public enum Category {
		BROWN, LIGHT_BLUE, PINK, ORANGE, RED, YELLOW, GREEN, DARK_BLUE;
		public static Category fromString(String name) {
			if(name.equals("brown")) return BROWN;
			else if (name.equals("light_blue")) return LIGHT_BLUE;
			else if (name.equals("pink")) return PINK;
			else if (name.equals("orange")) return ORANGE;
			else if (name.equals("red")) return RED;
			else if (name.equals("yellow")) return YELLOW;
			else if (name.equals("green")) return GREEN;
			else if (name.equals("dark_blue")) return DARK_BLUE;
			else return null;
		}
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
	public double getRent(Player player) {
		if(isMortgage() || player.equals(getOwner())) {
			return 0;
		} else {
			switch (housesCount) {
				case 0: return baseRent;
				case 1: return round(100, baseRent * 5);
				case 2: return round(100, baseRent * 15);
				case 3: return round(1000, baseRent * 35);
				case 4: return round(1000, baseRent * 42);
				case 5: return round(1000, baseRent * 50);
				default: return 0;
			}
		}
	}
	
	/**
	 * @return the purchasing price
	 */
	public double getPurchasePrice() {
		return round(100, baseRent * 12);
	}
	
	public double getHousePrice() {
		return round(100, baseRent * 8);
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
	/**
	 * 
	 */
	public void addHouse() {
		this.housesCount ++;
	}
	
	public void removeHouse() {
		this.housesCount --;
	}
	
	private double round(double multiple, double value) {
		return value - (value % multiple);
	}
}
