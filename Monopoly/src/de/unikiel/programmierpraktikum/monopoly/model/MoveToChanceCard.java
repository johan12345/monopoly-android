package de.unikiel.programmierpraktikum.monopoly.model;

/**
 * One sort of {@link ChanceCard}; where a player will be sent to a different,
 * definite location.
 * 
 * @author Johan v. Forstner, Miriam Scharnke
 */
public class MoveToChanceCard extends ChanceCard {
	private static final long serialVersionUID = 2600106692955387546L;
	private int spacePos;

	/**
	 * @return the destination
	 */
	public int getSpacePos() {
		return spacePos;
	}

	/**
	 * @param spacePos
	 *            the spacePos to set
	 */
	public void setSpacePos(int spacePos) {
		this.spacePos = spacePos;
	}

}
