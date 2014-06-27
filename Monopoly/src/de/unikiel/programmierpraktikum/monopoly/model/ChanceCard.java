/**
 * 
 */
package de.unikiel.programmierpraktikum.monopoly.model;

/**
 * @author johan_000
 *
 */
public abstract class ChanceCard {
	private String text;
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
}
