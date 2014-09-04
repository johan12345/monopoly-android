package de.unikiel.programmierpraktikum.monopoly.model;

/**
 * @author Miriam Scharnke, Johan v. Forstner
 * Speichert die Eigenschaften eines Bahnhofsfeldes auf dem Spielfeld
 */
public class StationSpace extends BuyableSpace {
	private static final long serialVersionUID = -7200382472306581997L;
	private static final double BASE_RENT = 500;

	@Override
	public double getRent() {
		return isMortgage() ? 0 : BASE_RENT * Math.pow(2, getOwner().getStationCount() - 1);
	}

	@Override
	public double getPurchasePrice() {
		return BASE_RENT * 8;
	}

}
