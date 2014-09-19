package de.unikiel.programmierpraktikum.monopoly.model;

/**
 * Represents a utility {@link Space} (example in real Monopoly: electricity station, in
 * our version: Large Hadron Collider)
 * 
 * @author Miriam Scharnke, Johan v. Forstner
 */
public class UtilitySpace extends BuyableSpace {
	private static final long serialVersionUID = -8160803779666052766L;
	private static final double BASE_RENT = 375;

	@Override
	public double getRent() {
		return isMortgage() ? 0 : BASE_RENT
				* Math.pow(2, getOwner().getUtilityCount() - 1);
	}

	@Override
	public double getPurchasePrice() {
		return BASE_RENT * 8;
	}

}
