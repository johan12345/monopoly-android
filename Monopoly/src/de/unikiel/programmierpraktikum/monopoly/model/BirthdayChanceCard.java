/**
 * 
 */
package de.unikiel.programmierpraktikum.monopoly.model;

/**
 * One sort of (@Link ChanceCard); the one where you get money from everyone else,
 * usually for your birthday, hence the name.
 *
 * @author Johan v. Forstner, Miriam Scharnke
 */
public class BirthdayChanceCard extends ChanceCard {
	private static final long serialVersionUID = -11989111947849631L;
	private double amount;

	/**
	 * @return the amount that will have to be payed by each player
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
