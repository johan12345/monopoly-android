package de.unikiel.programmierpraktikum.monopoly.exceptions;

/**
 * Thrown when the mortgage status given to
 * {@link de.unikiel.programmierpraktikum.monopoly.controller.PlayerController#setMortgage(boolean, BuyableSpace)}
 * already matches the current state.
 * 
 * @author Miriam Scharnke, Johan v. Forstner
 *
 */
@SuppressWarnings("serial")
public class UnableToRaiseMortgageException extends Exception {

}
