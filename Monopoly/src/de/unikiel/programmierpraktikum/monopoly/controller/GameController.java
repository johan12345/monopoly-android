package de.unikiel.programmierpraktikum.monopoly.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.unikiel.programmierpraktikum.monopoly.model.Game;
import de.unikiel.programmierpraktikum.monopoly.model.Player;

public class GameController implements Serializable {
	private static final long serialVersionUID = 4150825991221148567L;
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
		
		if (playerControllers.get(currentPlayerNumber).getPlayer().hasLost())
			nextTurn();
	}
	
	public void giveStartMoney() {
		for (PlayerController controller:playerControllers) {
			controller.getPlayer().earn(Game.START_MONEY);
		}
	}

	/**
	 * @return the game
	 */
	public Game getGame() {
		return game;
	}

}
