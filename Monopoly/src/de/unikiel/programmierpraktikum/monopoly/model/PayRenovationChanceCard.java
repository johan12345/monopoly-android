/**
 * 
 */
package de.unikiel.programmierpraktikum.monopoly.model;

/**
 * @author johan_000
 *
 */
public class PayRenovationChanceCard extends ChanceCard {
	private static final long serialVersionUID = 2018485354000414262L;
	private double houseAmount;
	private double hotelAmount;
	
	/**
	 * @return the houseAmount
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
	 * @return the hotelAmount
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
