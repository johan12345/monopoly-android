/**
 * 
 */
package de.unikiel.programmierpraktikum.monopoly.model;

/**
 * @author Miriam Scharnke, Johan v. Forstner
 * Speichert die Eigenschaften eines Feldes auf dem Spielfeld
 */
public abstract class Space {
	private String name;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
