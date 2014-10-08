package de.unikiel.programmierpraktikum.monopoly.model;

/**
 * Abstract {@link Space} that describes all spaces that a player can buy when
 * reaching it, such as streets, utilities and stations.
 */

public abstract class BuyableSpace extends Space {
	private static final long serialVersionUID = -7671400846336979628L;

	private Player owner;

	private boolean mortgage; // Hypothek

	/**
	 * The rent another player but the owner has to pay when stepping upon a
	 * buyable space. Houses or hotels are included in the calculation.
	 */
	public abstract double getRent();

	/**
	 * The price that has to be payed, should a player wish to acquire a buyable
	 * space.
	 */
	public abstract double getPurchasePrice();

	/**
	 * The amount of money someone receives when mortgaging a space.
	 */
	public double getMortgageValue() {
		return getPurchasePrice() / 2;
	}

	/**
	 * @return the owner of this space
	 */
	public Player getOwner() {
		return owner;
	}

	/**
	 * @param owner
	 *            the owner to set
	 */
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	/**
	 * @return the mortgage status
	 */
	public boolean isMortgage() {
		return mortgage;
	}

	/**
	 * @param mortgage
	 *            the mortgage to set
	 */
	public void setMortgage(boolean mortgage) {
		this.mortgage = mortgage;
	}
}
