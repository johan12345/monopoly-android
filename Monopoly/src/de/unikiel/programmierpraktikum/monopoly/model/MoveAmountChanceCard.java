package de.unikiel.programmierpraktikum.monopoly.model;

/**
 * One sort of {@link ChanceCard}; where a player will be sent to a different
 * location depending on his current position.
 * 
 * @author Johan v. Forstner, Miriam Scharnke
 */
public class MoveAmountChanceCard extends ChanceCard {
	private static final long serialVersionUID = -6451999155066376394L;
	private int amount;

	/**
	 * @return the amount of steps a player must take
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            that amount to set
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}
}
