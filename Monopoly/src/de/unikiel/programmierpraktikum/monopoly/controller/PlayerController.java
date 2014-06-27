package de.unikiel.programmierpraktikum.monopoly.controller;

import de.unikiel.programmierpraktikum.monopoly.model.BuyableSpace;
import de.unikiel.programmierpraktikum.monopoly.model.ChanceCard;
import de.unikiel.programmierpraktikum.monopoly.model.Game;
import de.unikiel.programmierpraktikum.monopoly.model.GoToJailChanceCard;
import de.unikiel.programmierpraktikum.monopoly.model.GoToJailSpace;
import de.unikiel.programmierpraktikum.monopoly.model.MoveAmountChanceCard;
import de.unikiel.programmierpraktikum.monopoly.model.MoveToChanceCard;
import de.unikiel.programmierpraktikum.monopoly.model.PayChanceCard;
import de.unikiel.programmierpraktikum.monopoly.model.PaySpace;
import de.unikiel.programmierpraktikum.monopoly.model.Player;
import de.unikiel.programmierpraktikum.monopoly.model.Space;
import de.unikiel.programmierpraktikum.monopoly.model.StreetSpace;
import de.unikiel.programmierpraktikum.monopoly.utilities.Utilities;

/**
 * @author Miriam Scharnke, Johan v. Forstner
 * Lässt die Spieler mit dem Spiel interagieren
 */
public class PlayerController {
	private Player player;
	private Game game;
	
	public PlayerController(Player player, Game game) {
		this.player = player;
		this.game = game;
	}
	
	public int throwTheDice() {
		int count = Utilities.dice();
		moveAmount(count);
		return count;
	}
	
	public Space moveAmount(int amount) {
		int newPos = (player.getCurrentPos() + amount) % game.getSpaces().size();
		checkPassedGoSpace(player.getCurrentPos(), newPos);
		player.setCurrentPos(newPos);
		return getCurrentSpace();
	}
	
	public Space moveTo(int pos) {
		int newPos = pos % game.getSpaces().size();
		checkPassedGoSpace(player.getCurrentPos(), newPos);
		player.setCurrentPos(newPos);
		return game.getSpaces().get(pos);
	}
	
	private void checkPassedGoSpace(int currentPos, int newPos) {
		if(currentPos < newPos) {
			player.earn(Game.SALARY);
		}
	}

	public void buySpace() {
		if(getCurrentSpace() instanceof BuyableSpace) {
			BuyableSpace space = (BuyableSpace) getCurrentSpace();
			space.setOwner(player);
			player.getProperty().add(space);
			player.pay(space.getPurchasePrice());
		} else {
			//TODO: Ausnahme
		}
	}
	
	public void addHouse(StreetSpace space) {
		if(player.equals(space.getOwner()) && space.getHousesCount() < 5
				&& player.isAbleToBuyHouse(space, game)) {
			space.addHouse();
			player.pay(space.getHousePrice());
		} else {
			//TODO: Ausnahme
		}
	}
	
	public void removeHouse(StreetSpace space) {
		if(player.equals(space.getOwner()) && space.getHousesCount() > 0
				&& player.isAbleToSellHouse(space, game)) {
			space.removeHouse();
			player.earn(space.getHousePrice());
		} else {
			//TODO: Ausnahme
		}
	}
	
	public void setMortgage(boolean mortgage, BuyableSpace space) {
		if(space.isMortgage() != mortgage && player.equals(space.getOwner())) {
			space.setMortgage(mortgage);
			if(mortgage) {
				player.earn(space.getMortgageValue());
			} else {
				player.pay(space.getMortgageValue());
			}
		} else {
			//TODO: Ausnahme
		}
	}
	
	public void executeChanceCard(ChanceCard card) {
		if(card instanceof PayChanceCard) {
			player.pay(((PayChanceCard) card).getAmount());
		} else if (card instanceof GoToJailChanceCard) {
			goToJail();
		} else if (card instanceof MoveToChanceCard) {
			moveTo(((MoveToChanceCard) card).getSpacePos());
		} else if (card instanceof MoveAmountChanceCard) {
			moveAmount(((MoveAmountChanceCard) card).getAmount());
		}
	}
	
	public void executeCurrentSpace() {
		if(getCurrentSpace() instanceof GoToJailSpace) {
			goToJail();
		} else if (getCurrentSpace() instanceof PaySpace) {
			player.pay(((PaySpace) getCurrentSpace()).getAmount());
		}
	}
	
	private void goToJail() {
		moveTo(game.getJailPos());
		player.setInJail(true);
	}
	
	private Space getCurrentSpace() {
		return game.getSpaces().get(player.getCurrentPos());
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * @return the game
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * @param game the game to set
	 */
	public void setGame(Game game) {
		this.game = game;
	}
}
