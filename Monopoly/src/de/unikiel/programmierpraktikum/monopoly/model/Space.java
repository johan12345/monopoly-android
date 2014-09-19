/**
 * 
 */
package de.unikiel.programmierpraktikum.monopoly.model;

import java.io.Serializable;

import android.util.Log;

/**
 * @author Miriam Scharnke, Johan v. Forstner Speichert die Eigenschaften eines
 *         Feldes auf dem Spielfeld
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

	// @Override
	// public boolean equals(Object other) {
	// if (other instanceof Space) {
	// Log.d("monopoly", this.name + " == " + ((Space) other).getName() + "?");
	// return this.name.equals(((Space) other).getName());
	// } else
	// return false;
	// }
}
