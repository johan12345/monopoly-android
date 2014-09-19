/**
 * 
 */
package de.unikiel.programmierpraktikum.monopoly.model;

/**
  * One sort of (@Link ChanceCard); where a player will have to pay a certain amount of money for each of his houses and another for his hotels.
 * Johan v. Forstner, Miriam Scharnke
 */
public class PayRenovationChanceCard extends ChanceCard {
	private static final long serialVersionUID = 2018485354000414262L;
	private double houseAmount;
	private double hotelAmount;
	
	/**
	 * @return the price to pay for each house
	 */
	public double getHouseAmount() {
		return houseAmount;
	}
	/**
	 * @param houseAmount the houseAmount to set
	 */
	public void setHouseAmount(double houseAmount) {
		this.houseAmount = houseAmount;
	}
	/**
	 * @return the price to pay for each hotel
	 */
	public double getHotelAmount() {
		return hotelAmount;
	}
	/**
	 * @param hotelAmount the hotelAmount to set
	 */
	public void setHotelAmount(double hotelAmount) {
		this.hotelAmount = hotelAmount;
	}

	
}
