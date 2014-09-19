package de.unikiel.programmierpraktikum.monopoly.controller;

import java.io.Serializable;
import java.util.List;

import de.unikiel.programmierpraktikum.monopoly.exceptions.LackOfMoneyException;
import de.unikiel.programmierpraktikum.monopoly.exceptions.UnableToEditHousesException;
import de.unikiel.programmierpraktikum.monopoly.exceptions.UnableToRaiseMortgageException;
import de.unikiel.programmierpraktikum.monopoly.model.BirthdayChanceCard;
import de.unikiel.programmierpraktikum.monopoly.model.BuyableSpace;
import de.unikiel.programmierpraktikum.monopoly.model.ChanceCard;
import de.unikiel.programmierpraktikum.monopoly.model.Game;
import de.unikiel.programmierpraktikum.monopoly.model.GoToJailChanceCard;
import de.unikiel.programmierpraktikum.monopoly.model.MoveAmountChanceCard;
import de.unikiel.programmierpraktikum.monopoly.model.MoveToChanceCard;
import de.unikiel.programmierpraktikum.monopoly.model.PayChanceCard;
import de.unikiel.programmierpraktikum.monopoly.model.PayRenovationChanceCard;
import de.unikiel.programmierpraktikum.monopoly.model.PaySpace;
import de.unikiel.programmierpraktikum.monopoly.model.Player;
import de.unikiel.programmierpraktikum.monopoly.model.Space;
import de.unikiel.programmierpraktikum.monopoly.model.StreetSpace;
import de.unikiel.programmierpraktikum.monopoly.utilities.Utilities;

/**
 * @author Miriam Scharnke, Johan v. Forstner Lässt die Spieler mit dem Spiel
 *         interagieren
 */
public class PlayerController implements Serializable {
	private static final long serialVersionUID = 8438198135183026381L;
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
		return moveAmount(amount, true);
	}

	public Space moveAmount(int amount, boolean checkGo) {
		int newPos = (player.getCurrentPos() + amount)
				% game.getSpaces().size();
		if (checkGo)
			checkPassedGoSpace(player.getCurrentPos(), newPos);
		player.setCurrentPos(newPos);
		return getCurrentSpace();
	}

	public Space moveTo(int pos) {
		return moveTo(pos, true);
	}

	public Space moveTo(int pos, boolean checkGo) {
		int newPos = pos % game.getSpaces().size();
		if (checkGo)
			checkPassedGoSpace(player.getCurrentPos(), newPos);
		player.setCurrentPos(newPos);
		return game.getSpaces().get(pos);
	}

	private void checkPassedGoSpace(int currentPos, int newPos) {
		if (newPos < currentPos) {
			player.earn(Game.SALARY);
		}
	}

	public void buySpace() throws LackOfMoneyException {
		if (getCurrentSpace() instanceof BuyableSpace
				&& ((BuyableSpace) getCurrentSpace()).getOwner() == null) {
			BuyableSpace space = (BuyableSpace) getCurrentSpace();
			player.pay(space.getPurchasePrice());
			space.setOwner(player);
			player.getProperty().add(space);
		} else {
			// TODO: Ausnahme
		}
	}

	public void addHouse(StreetSpace space) throws LackOfMoneyException,
			UnableToEditHousesException {
		if (player.equals(space.getOwner()) && space.getHousesCount() < 5
				&& player.isAbleToBuyHouse(space, game)) {			
			player.pay(space.getHousePrice());
			space.addHouse();
		} else {
			throw new UnableToEditHousesException();
		}
	}

	public void removeHouse(StreetSpace space)
			throws UnableToEditHousesException {
		if (player.equals(space.getOwner()) && space.getHousesCount() > 0
				&& player.isAbleToSellHouse(space, game)) {
			space.removeHouse();
			player.earn(space.getHousePrice());
		} else {
			throw new UnableToEditHousesException();
		}
	}

	public void setMortgage(boolean mortgage, BuyableSpace space)
			throws LackOfMoneyException, UnableToRaiseMortgageException {
		if (space.isMortgage() != mortgage && player.equals(space.getOwner())) {
			if (mortgage) {
				player.earn(space.getMortgageValue());
			} else {
				player.pay(space.getMortgageValue());
			}
			space.setMortgage(mortgage);
		} else {
			throw new UnableToRaiseMortgageException();
		}
	}

	public void executeChanceCard(ChanceCard card) throws LackOfMoneyException {
		if (card instanceof PayChanceCard) {
			player.pay(((PayChanceCard) card).getAmount());
		} else if (card instanceof GoToJailChanceCard) {
			goToJail();
		} else if (card instanceof MoveToChanceCard) {
			moveTo(((MoveToChanceCard) card).getSpacePos());
		} else if (card instanceof MoveAmountChanceCard) {
			moveAmount(((MoveAmountChanceCard) card).getAmount(), false);
		} else if (card instanceof PayRenovationChanceCard) {
			List<BuyableSpace> property = game.getProperty(player);
			double amount = 0;
			for (BuyableSpace space : property) {
				if (space instanceof StreetSpace) {
					StreetSpace street = (StreetSpace) space;
					amount += street.getRealHousesCount()
							* ((PayRenovationChanceCard) card).getHouseAmount();
					amount += street.getHotelCount()
							* ((PayRenovationChanceCard) card).getHotelAmount();
				}
			}
			player.pay(amount);
		} else if (card instanceof BirthdayChanceCard) {
			double amount = ((BirthdayChanceCard) card).getAmount();
			for (Player player : game.getPlayers()) {
				player.pay(amount);
				this.player.earn(amount);
			}
		} else {
			throw new RuntimeException();
		}
	}

	public double payRent() throws LackOfMoneyException {
		if (getCurrentSpace() instanceof BuyableSpace
				&& ((BuyableSpace) getCurrentSpace()).getOwner() != null
				&& !((BuyableSpace) getCurrentSpace()).getOwner().equals(player)) {
			BuyableSpace space = (BuyableSpace) getCurrentSpace();
			player.pay(space.getRent());
			space.getOwner().earn(space.getRent());
			return space.getRent();
		} else {
			// TODO: Exception
			return 0;
		}
	}

	public void goToJail() {
		moveTo(game.getJailPos(), false);
		player.setInJail(1);
	}

	public void payPaySpace() throws LackOfMoneyException {
		if (getCurrentSpace() instanceof PaySpace) {
			player.pay(((PaySpace) getCurrentSpace()).getAmount());
		} else {
			// TODO: Exception
		}
	}

	public Space getCurrentSpace() {
		return game.getSpaces().get(player.getCurrentPos());
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @param player
	 *            the player to set
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
	 * @param game
	 *            the game to set
	 */
	public void setGame(Game game) {
		this.game = game;
	}

	public void payBail() throws LackOfMoneyException {
		player.pay(Game.JAIL_BAIL);
		player.setInJail(0);
	}

	public double getFunds() {
		double funds = player.getMoney();
		for (BuyableSpace space : game.getProperty(player)) {
			if (!space.isMortgage())
				funds += space.getMortgageValue();
			if (space instanceof StreetSpace) {
				StreetSpace street = (StreetSpace) space;
				funds += street.getHousesCount() * street.getHousePrice() / 2;
			}
		}
		return funds;
	}

	public Player lose() {
		player.setHasLost();
		int count = 0;
		Player lastPlayer = null;
		for (Player player : game.getPlayers()) {
			if (!player.hasLost()) {
				count++;
				lastPlayer = player;
			}
		}
		return count > 1 ? null : lastPlayer;
	}
}
