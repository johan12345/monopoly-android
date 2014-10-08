package de.unikiel.programmierpraktikum.monopoly.exceptions;

/**
 * Is thrown when the action is not possible on the current field. For example,
 * when the current space isn't buyable, the
 * {@link de.unikiel.programmierpraktikum.monopoly.controller.PlayerController#buySpace()}
 * method throws this exception.
 * 
 * @author Miriam Scharnke, Johan v. Forstner
 *
 */
@SuppressWarnings("serial")
public class WrongSpaceException extends Exception {

}
