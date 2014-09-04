/**
 * 
 */
package de.unikiel.programmierpraktikum.monopoly.model;

import java.io.Serializable;

/**
 * @author johan_000
 *
 */
public abstract class ChanceCard implements Serializable {
	private static final long serialVersionUID = 2419745767622257665L;
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
