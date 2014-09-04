package de.unikiel.programmierpraktikum.monopoly.model;

public class PaySpace extends Space {
	private static final long serialVersionUID = -4109328055644121861L;
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
