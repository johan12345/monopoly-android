package de.unikiel.programmierpraktikum.monopoly.controller;

import java.util.ArrayList;
import java.util.List;

import de.unikiel.programmierpraktikum.monopoly.model.Game;
import de.unikiel.programmierpraktikum.monopoly.model.Player;

public class GameController {
	private List<PlayerController> playerControllers;
	private Game game;
	private int currentPlayerNumber;
	
	public GameController(Game game) {
		this.game = game;
		playerControllers = new ArrayList<PlayerController>();
		for(Player player:game.getPlayers()) {
			playerControllers.add(new PlayerController(player, game));
		}
	}

	/**
	 * @return the current player
	 */
	public PlayerController whoseTurnIsIt() {
		return playerControllers.get(currentPlayerNumber);
	}
	
	/**
	 * @return the current player's number
	 */
	public int getCurrentPlayerNumber() {
		return currentPlayerNumber;
	}
	
	public void nextTurn() {
		if(currentPlayerNumber < game.getPlayers().size() - 1)
			currentPlayerNumber ++;
		else
			currentPlayerNumber = 0;
	}

}
