/**
 * 
 */
package de.unikiel.programmierpraktikum.monopoly.model;

/**
  * One sort of (@Link ChanceCard); where a player pays or earns money.
 * @author Johan v. Forstner, Miriam Scharnke
 */
public class PayChanceCard extends ChanceCard {

	private static final long serialVersionUID = 4285026603393180000L;
	private double amount;

	/**
	 * @return the amount of money a player must pay; that amount can be negative, in that case the player earns money.
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param amount that amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}
}
