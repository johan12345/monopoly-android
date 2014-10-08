package de.unikiel.programmierpraktikum.monopoly.exceptions;

/**
 * Thrown when the player needs to pay an amount of money, but hasn't got
 * enough. It can save the amount that needs to be paid.
 * 
 * @author Miriam Scharnke, Johan v. Forstner
 *
 */
@SuppressWarnings("serial")
public class LackOfMoneyException extends Exception {
	private double moneyToPay;

	public LackOfMoneyException(double moneyToPay) {
		this.moneyToPay = moneyToPay;
	}

	/**
	 * @return the money to pay
	 */
	public double getMoneyToPay() {
		return moneyToPay;
	}
}
