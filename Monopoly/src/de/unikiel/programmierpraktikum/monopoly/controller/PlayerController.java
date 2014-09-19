package de.unikiel.programmierpraktikum.monopoly.controller;

import java.io.Serializable;
import java.util.List;

import de.unikiel.programmierpraktikum.monopoly.exceptions.AlreadyBoughtException;
import de.unikiel.programmierpraktikum.monopoly.exceptions.LackOfMoneyException;
import de.unikiel.programmierpraktikum.monopoly.exceptions.UnableToEditHousesException;
import de.unikiel.programmierpraktikum.monopoly.exceptions.UnableToRaiseMortgageException;
import de.unikiel.programmierpraktikum.monopoly.exceptions.WrongSpaceException;
import de.unikiel.programmierpraktikum.monopoly.model.*;
import de.unikiel.programmierpraktikum.monopoly.utilities.Utilities;

/**
 * Controller class for a single {@link Player}.
 * 
 * @author Miriam Scharnke, Johan v. Forstner
 */
public class PlayerController implements Serializable {
	private static final long serialVersionUID = 8438198135183026381L;

	private Player player;
	private Game game;

	public PlayerController(Player player, Game game) {
		this.player = player;
		this.game = game;
	}

	/**
	 * Throw the dice and move to the corresponding position.
	 * 
	 * @return The amount that was thrown
	 */
	public int throwTheDice() {
		int count = Utilities.dice();
		moveAmount(count);
		return count;
	}

	/**
	 * Move a specific amount of spaces on the game field. The player
	 * automatically earns his salary if he passes the {@link GoSpace}.
	 * 
	 * @param amount
	 *            the number of spaces to move
	 * @return the {@link Space} the player arrived on
	 */
	public Space moveAmount(int amount) {
		return moveAmount(amount, true);
	}

	/**
	 * Move a specific amount of spaces on the game field. The player
	 * automatically earns his salary if he passes the {@link GoSpace} only if
	 * checkGo is set to true.
	 * 
	 * @param amount
	 *            the number of spaces to move
	 * @param checkGo
	 *            if the player should earn his salary when he passes the
	 *            GoSpace
	 * @return the {@link Space} the player arrived on
	 */
	public Space moveAmount(int amount, boolean checkGo) {
		int newPos = (player.getCurrentPos() + amount)
				% game.getSpaces().size();
		if (checkGo)
			checkPassedGoSpace(player.getCurrentPos(), newPos);
		player.setCurrentPos(newPos);
		return getCurrentSpace();
	}

	/**
	 * Move to a specific position on the game field. The player automatically
	 * earns his salary if he passes the {@link GoSpace}.
	 * 
	 * @param pos
	 *            the position to move to
	 * @return the {@link Space} the player arrived on
	 */
	public Space moveTo(int pos) {
		return moveTo(pos, true);
	}

	/**
	 * Move to a specific position on the game field. The player automatically
	 * earns his salary if he passes the {@link GoSpace}.
	 * 
	 * @param pos
	 *            the position to move to
	 * @param checkGo
	 *            if the player should earn his salary when he passes the
	 *            GoSpace
	 * @return the {@link Space} the player arrived on
	 */
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

	/**
	 * Buy the space this player is currently on
	 * 
	 * @throws LackOfMoneyException
	 *             when the player doesn't have enough money
	 * @throws AlreadyBoughtException
	 *             when the current space already has an owner
	 * @throws WrongSpaceException
	 *             when the current space isn't buyable
	 */
	public void buySpace() throws LackOfMoneyException, AlreadyBoughtException,
			WrongSpaceException {
		if (getCurrentSpace() instanceof BuyableSpace) {
			if (((BuyableSpace) getCurrentSpace()).getOwner() == null) {
				BuyableSpace space = (BuyableSpace) getCurrentSpace();
				player.pay(space.getPurchasePrice());
				space.setOwner(player);
				player.getProperty().add(space);
			} else {
				throw new AlreadyBoughtException();
			}
		} else {
			throw new WrongSpaceException();
		}
	}

	/**
	 * Add a House or transform four houses into a hotel on a specific space
	 * 
	 * @param space
	 *            the space to add houses to
	 * @throws LackOfMoneyException
	 *             when the player doesn't have enough money
	 * @throws UnableToEditHousesException
	 *             when the space or other spaces in this category are not owned
	 *             by the player, there is already a hotel, or the other spaces
	 *             of this group don't have an allowed count of houses
	 */
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

	/**
	 * Remoe a House or transform a hotel into four houses on a specific space
	 * 
	 * @param space
	 *            the space to remove houses from
	 * @throws UnableToEditHousesException
	 *             when the space or other spaces in this category are not owned
	 *             by the player, there are no houses on this space, or the
	 *             other spaces of this group don't have an allowed count of
	 *             houses
	 */
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

	/**
	 * Create or cancel a mortgage a specific space
	 * 
	 * @param mortgage
	 *            If you want to create or cancel the mortgage
	 * @param space
	 *            The space you want to set the mortgage on
	 * @throws LackOfMoneyException
	 *             If you don't have enough money to cancel the mortgage
	 * @throws UnableToRaiseMortgageException
	 *             If you don't own the space or the mortgage parameter already
	 *             matches the current state
	 */
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

	/**
	 * Execute a {@link ChanceCard} that the player got from a
	 * {@link ChanceSpace}
	 * 
	 * @param card
	 *            The chance card to execute
	 * @throws LackOfMoneyException
	 *             When the player doesn't have enough money to execute this
	 *             card
	 */
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

	/**
	 * Pay the rent on the current space
	 * 
	 * @return the amount of rent paid (0 if the space is mortgaged, has no
	 *         owner or is the player's own property)
	 * @throws LackOfMoneyException
	 *             If the player doesn't have enough money to pay the rent
	 * @throws WrongSpaceException
	 *             when the current space is not buyable
	 */
	public double payRent() throws LackOfMoneyException, WrongSpaceException {
		if (getCurrentSpace() instanceof BuyableSpace) {
			if (((BuyableSpace) getCurrentSpace()).getOwner() != null
					&& !((BuyableSpace) getCurrentSpace()).getOwner().equals(
							player)) {
				BuyableSpace space = (BuyableSpace) getCurrentSpace();
				player.pay(space.getRent());
				space.getOwner().earn(space.getRent());
				return space.getRent();
			} else {
				return 0;
			}
		} else {
			throw new WrongSpaceException();
		}
	}

	/**
	 * Go to and enter the Jail
	 */
	public void goToJail() {
		moveTo(game.getJailPos(), false);
		player.setInJail(1);
	}

	/**
	 * Pay the amount requested by a {@link PaySpace}
	 * 
	 * @throws LackOfMoneyException
	 *             If the player doesn't have enough money to pay the amount
	 * @throws WrongSpaceException
	 *             If the current space is not a PaySpace
	 */
	public void payPaySpace() throws LackOfMoneyException, WrongSpaceException {
		if (getCurrentSpace() instanceof PaySpace) {
			player.pay(((PaySpace) getCurrentSpace()).getAmount());
		} else {
			throw new WrongSpaceException();
		}
	}

	/**
	 * @return the {@link Space} the player is currently on.
	 */
	public Space getCurrentSpace() {
		return game.getSpaces().get(player.getCurrentPos());
	}

	/**
	 * @return the Player this PlayerController is controlling
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @return the Game this PlayerController belongs to
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * Pay the bail in order to escape the {@link JailSpace}
	 * 
	 * @throws LackOfMoneyException
	 *             If the player doesn't have enough money
	 */
	public void payBail() throws LackOfMoneyException {
		player.pay(Game.JAIL_BAIL);
		player.setInJail(0);
	}

	/**
	 * Get the funds of this player, which is the amount of money he has plus
	 * the value of all the houses/hotels he could sell and the sum of the
	 * mortgage values of all his property
	 * 
	 * @return the funds of this player
	 */
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

	/**
	 * Make this player lose the game and return all his property to the bank
	 * 
	 * @return If there is only one player left, the last player in the game.
	 *         Otherwise null.
	 */
	public Player lose() {
		player.setHasLost();
		for (BuyableSpace space:game.getProperty(player)) {
			space.setOwner(null);
			space.setMortgage(false);
		}
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
