/**
 * 
 */
package de.unikiel.programmierpraktikum.monopoly.model;

/**
 * @author johan_000
 *
 */
public class PayChanceCard extends ChanceCard {

	private static final long serialVersionUID = 4285026603393180000L;
	private double amount;

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}
}
