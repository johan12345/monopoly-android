package de.unikiel.programmierpraktikum.monopoly.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.unikiel.programmierpraktikum.monopoly.model.Game;
import de.unikiel.programmierpraktikum.monopoly.model.Player;

/**
 * Controller class for a Monopoly game. Saves the {@link Game} data, the list
 * of {@link PlayerController}s and whose turn it is.
 * 
 * @author Miriam Scharnke, Johan v. Forstner
 *
 */
public class GameController implements Serializable {
	private static final long serialVersionUID = 4150825991221148567L;
	private List<PlayerController> playerControllers;
	private Game game;
	private int currentPlayerNumber;

	public GameController(Game game) {
		this.game = game;
		playerControllers = new ArrayList<PlayerController>();
		for (Player player : game.getPlayers()) {
			playerControllers.add(new PlayerController(player, game));
		}
	}

	/**
	 * Finds out whose turn it is and returns the corresponding
	 * {@link PlayerController}
	 * 
	 * @return the {@link PlayerController} whose turn it is
	 */
	public PlayerController whoseTurnIsIt() {
		return playerControllers.get(currentPlayerNumber);
	}

	/**
	 * @return the current player's index
	 */
	public int getCurrentPlayerNumber() {
		return currentPlayerNumber;
	}

	/**
	 * Finishes the current turn and determines the next player. Players that
	 * have already lost are left out.
	 */
	public void nextTurn() {
		if (currentPlayerNumber < game.getPlayers().size() - 1)
			currentPlayerNumber++;
		else
			currentPlayerNumber = 0;

		if (playerControllers.get(currentPlayerNumber).getPlayer().hasLost())
			nextTurn();
	}

	/**
	 * Give every player his starting money
	 */
	public void giveStartMoney() {
		for (PlayerController controller : playerControllers) {
			controller.getPlayer().earn(Game.START_MONEY);
		}
	}

	/**
	 * @return the Game
	 */
	public Game getGame() {
		return game;
	}

}
