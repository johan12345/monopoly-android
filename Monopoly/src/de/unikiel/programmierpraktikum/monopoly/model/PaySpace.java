package de.unikiel.programmierpraktikum.monopoly.model;

/**
 * Represents a {@link Space} where a player must pay a definite amount of money
 * (example: taxes).
 * 
 * @author Miriam Scharnke, Johan v. Forstner
 */
public class PaySpace extends Space {
	private static final long serialVersionUID = -4109328055644121861L;
	private double amount;

	/**
	 * @return the amount the player needs to pay
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}
}
