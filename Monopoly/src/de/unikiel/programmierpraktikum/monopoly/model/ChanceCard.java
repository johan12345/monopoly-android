/**
 * 
 */
package de.unikiel.programmierpraktikum.monopoly.model;

import java.io.Serializable;

/**
 * Class containing all chance cards, community or chance.
 * @author Johan v. Forstner, Miriam Scharnke
 */
public abstract class ChanceCard implements Serializable {
	private static final long serialVersionUID = 2419745767622257665L;
	private String text;
	/**
	 * @return the text written on a card (containing some delightful jokes)
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
