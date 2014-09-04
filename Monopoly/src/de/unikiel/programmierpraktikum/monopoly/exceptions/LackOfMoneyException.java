package de.unikiel.programmierpraktikum.monopoly.exceptions;

@SuppressWarnings("serial")
public class LackOfMoneyException extends Exception {
	double moneyToPay;
	
	public LackOfMoneyException(double moneyToPay) {
		this.moneyToPay = moneyToPay;
	}

	/**
	 * @return the moneyToPay
	 */
	public double getMoneyToPay() {
		return moneyToPay;
	}
}
