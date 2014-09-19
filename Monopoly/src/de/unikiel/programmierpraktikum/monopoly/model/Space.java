package de.unikiel.programmierpraktikum.monopoly.model;

import java.io.Serializable;

/**
 * Abstract class for spaces on the Monopoly game field
 * 
 * @author Miriam Scharnke, Johan v. Forstner
 */
public abstract class Space implements Serializable {
	private static final long serialVersionUID = 1425641427402340284L;
	private String name;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
