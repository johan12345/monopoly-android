package de.unikiel.programmierpraktikum.monopoly.exceptions;

/**
 * Thrown when the player tries to buy or sell a house and it is not possible.
 * Reasons for this could be: 
 * - The player does not own all the spaces of this
 *     category 
 * - The houses are unequally distributed on the spaces of the category
 * - There are no houses to remove, or the maximum count of houses (= 1 hotel)
 *     is already reached
 * 
 * @author Miriam Scharnke, Johan v. Forstner
 *
 */
@SuppressWarnings("serial")
public class UnableToEditHousesException extends Exception {

}
