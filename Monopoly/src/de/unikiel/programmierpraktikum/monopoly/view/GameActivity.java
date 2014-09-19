package de.unikiel.programmierpraktikum.monopoly.view;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import de.unikiel.programmierpraktikum.monopoly.R;
import de.unikiel.programmierpraktikum.monopoly.controller.GameController;
import de.unikiel.programmierpraktikum.monopoly.controller.PlayerController;
import de.unikiel.programmierpraktikum.monopoly.controller.SaveGameHandler;
import de.unikiel.programmierpraktikum.monopoly.controller.SaveGameHandler.SaveGame;
import de.unikiel.programmierpraktikum.monopoly.exceptions.AlreadyBoughtException;
import de.unikiel.programmierpraktikum.monopoly.exceptions.LackOfMoneyException;
import de.unikiel.programmierpraktikum.monopoly.exceptions.WrongSpaceException;
import de.unikiel.programmierpraktikum.monopoly.model.BuyableSpace;
import de.unikiel.programmierpraktikum.monopoly.model.ChanceCard;
import de.unikiel.programmierpraktikum.monopoly.model.ChanceSpace;
import de.unikiel.programmierpraktikum.monopoly.model.CommunityChanceSpace;
import de.unikiel.programmierpraktikum.monopoly.model.FreeParkingSpace;
import de.unikiel.programmierpraktikum.monopoly.model.Game;
import de.unikiel.programmierpraktikum.monopoly.model.GoToJailChanceCard;
import de.unikiel.programmierpraktikum.monopoly.model.GoToJailSpace;
import de.unikiel.programmierpraktikum.monopoly.model.JailSpace;
import de.unikiel.programmierpraktikum.monopoly.model.MoveAmountChanceCard;
import de.unikiel.programmierpraktikum.monopoly.model.PaySpace;
import de.unikiel.programmierpraktikum.monopoly.model.Player;
import de.unikiel.programmierpraktikum.monopoly.model.Space;
import de.unikiel.programmierpraktikum.monopoly.utilities.Utilities;

/**
 * Main Activity for the Monopoly game
 * 
 * @author Johan v. Forstner, Miriam Scharnke
 */
public class GameActivity extends Activity {
	private Game game;
	private GameController controller;
	private HorizontalScrollView fieldScroller;
	private LinearLayout spaces;
	private ProgressBar fieldProgress;
	private ViewCreator viewCreator;
	private TextView playerName;
	private TextView money;
	private Status status;
	private Button btnThrowDice;
	private Button btnManageProperty;
	private Button btnNext;
	private Button btnBail;
	private Button btnBuy;
	private ImageView imgPlayer;
	private ImageView imgPlayerBg;
	private FrameLayout players;

	/**
	 * Defines possible statuses of the {@link GameActivity}. Used for showing
	 * the appropriate action buttons on the bottom.
	 * 
	 * @author Johan v. Forstner, Miriam Scharnke
	 */
	public enum Status {
		DICE_NOT_THROWN, DICE_THROWN, DICE_THROWN_ON_BUYABLE, JAIL_THROW_DICE, JAIL_DO_NOT_THROW_DICE, LACK_OF_MONEY
	}

	private static final int SPACE_WIDTH = 225;

	/**
	 * Called when the Activity is first created. Shows a dialog if the user
	 * wants to start a new game or continue an existing one and sets up
	 * {@link OnClickListener}s for the action buttons.
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupViews();
		fieldScroller.getViewTreeObserver().addOnScrollChangedListener(
				new OnScrollChangedListener() {
					@Override
					public void onScrollChanged() {
						int scroll = fieldScroller.getScrollX();
						fieldProgress.setProgress(scroll
								% fieldProgress.getMax());
					}
				});

		SaveGame savegame = null;
		try {
			savegame = SaveGameHandler.loadGame(this, "test.game");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (savegame != null) {
			// Spiel existiert bereits
			new AlertDialog.Builder(this)
					.setTitle("Monopoly")
					.setMessage(
							"Möchten Sie mit dem letzten Spiel fortfahren oder ein neues Spiel beginnen?")
					.setCancelable(false)
					.setPositiveButton("Fortfahren",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// Do nothing, game will be loaded in
									// onResume
								}

							})
					.setNegativeButton("Neues Spiel",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent = new Intent(
											GameActivity.this,
											SetupGameActivity.class);
									startActivity(intent);
								}

							}).create().show();
		}

		viewCreator = new ViewCreator(this);

		btnThrowDice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (status == Status.JAIL_THROW_DICE) {
					boolean success = Utilities.diceDouble();
					if (success) {
						Toast toast = Toast
								.makeText(
										GameActivity.this,
										"Sie haben einen Pasch gewürfelt und sind frei!",
										Toast.LENGTH_SHORT);
						toast.show();
						controller.whoseTurnIsIt().getPlayer().setInJail(0);
						animatePlayerToJail(controller.whoseTurnIsIt(), false);
					} else {
						Toast toast = Toast.makeText(GameActivity.this,
								"Sie haben leider keinen Pasch gewürfelt.",
								Toast.LENGTH_SHORT);
						toast.show();
						controller.whoseTurnIsIt().getPlayer()
								.increaseJailCounter();
					}
				} else {
					int number = controller.whoseTurnIsIt().throwTheDice();
					Toast toast = Toast.makeText(GameActivity.this,
							"Sie haben eine " + number + " gewürfelt!",
							Toast.LENGTH_SHORT);
					toast.show();

					PlayerController player = controller.whoseTurnIsIt();
					animatePlayerToPosition(player);
				}
				Space space = controller.whoseTurnIsIt().getCurrentSpace();
				if (space instanceof BuyableSpace
						&& ((BuyableSpace) space).getOwner() == null)
					setStatus(Status.DICE_THROWN_ON_BUYABLE);
				else
					setStatus(Status.DICE_THROWN);
			}

		});

		btnBail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				setStatus(Status.DICE_THROWN);
				try {
					controller.whoseTurnIsIt().payBail();
					animatePlayerToJail(controller.whoseTurnIsIt(), false);
					refreshTextFields();
				} catch (LackOfMoneyException e) {
					if (controller.whoseTurnIsIt().getPlayer().getJailCounter() == 4) {

					} else {
						Toast toast = Toast
								.makeText(
										GameActivity.this,
										"Sie haben nicht genug Geld, um die Kaution zu bezahlen!",
										Toast.LENGTH_SHORT);
						toast.show();
					}
				}
			}

		});

		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (status == Status.LACK_OF_MONEY) {
					Player currentPlayer = controller.whoseTurnIsIt()
							.getPlayer();
					if (currentPlayer.getDebt() <= currentPlayer.getMoney()) {
						try {
							currentPlayer.pay(currentPlayer.getDebt());
							Toast toast = Toast
									.makeText(
											GameActivity.this,
											"Sie haben "
													+ Utilities
															.moneyFormat()
															.format(currentPlayer
																	.getDebt())
													+ " ausstehende Zahlungen abbezahlt.",
											Toast.LENGTH_SHORT);
							toast.show();
							currentPlayer.setDebt(0);
						} catch (LackOfMoneyException e) {
							// Should not happen
						}
					} else {
						Toast toast = Toast
								.makeText(
										GameActivity.this,
										"Sie haben immer noch nicht genug Geld, um dies zu bezahlen!",
										Toast.LENGTH_SHORT);
						toast.show();
						return;
					}
				}
				controller.nextTurn();
				Player player = controller.whoseTurnIsIt().getPlayer();
				if (player.getJailCounter() == 4)
					setStatus(Status.JAIL_DO_NOT_THROW_DICE);
				else if (player.getJailCounter() > 0)
					setStatus(Status.JAIL_THROW_DICE);
				else
					setStatus(Status.DICE_NOT_THROWN);
				scrollToSpace(controller.whoseTurnIsIt().getPlayer()
						.getCurrentPos(), false);
				refresh();
			}

		});

		btnManageProperty.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(GameActivity.this,
						ManagePropertyActivity.class);
				GameActivity.this.startActivity(intent);
			}
		});

		btnBuy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					controller.whoseTurnIsIt().buySpace();
					refresh();
				} catch (LackOfMoneyException e) {
					Toast toast = Toast
							.makeText(
									GameActivity.this,
									"Sie haben nicht genug Geld, um das Feld zu kaufen!",
									Toast.LENGTH_SHORT);
					toast.show();
				} catch (AlreadyBoughtException e) {
					// won't happen
				} catch (WrongSpaceException e) {
					// won't happen
				}
			}
		});

	}

	/**
	 * Sets the current {@link Status} of the game and updates the visibility of
	 * the action buttons appropriately.
	 * 
	 * @param status
	 *            the status to set
	 */
	private void setStatus(Status status) {
		this.status = status;
		switch (status) {
		case DICE_NOT_THROWN:
			btnThrowDice.setVisibility(View.VISIBLE);
			btnNext.setVisibility(View.GONE);
			btnBail.setVisibility(View.GONE);
			btnBuy.setVisibility(View.GONE);
			break;
		case DICE_THROWN:
			btnThrowDice.setVisibility(View.GONE);
			btnNext.setVisibility(View.VISIBLE);
			btnBail.setVisibility(View.GONE);
			btnBuy.setVisibility(View.GONE);
			break;
		case DICE_THROWN_ON_BUYABLE:
			btnThrowDice.setVisibility(View.GONE);
			btnNext.setVisibility(View.VISIBLE);
			btnBail.setVisibility(View.GONE);
			btnBuy.setVisibility(View.VISIBLE);
			break;
		case JAIL_THROW_DICE:
			btnThrowDice.setVisibility(View.VISIBLE);
			btnNext.setVisibility(View.GONE);
			btnBail.setVisibility(View.VISIBLE);
			btnBuy.setVisibility(View.GONE);
			break;
		case JAIL_DO_NOT_THROW_DICE:
			btnThrowDice.setVisibility(View.GONE);
			btnNext.setVisibility(View.GONE);
			btnBail.setVisibility(View.VISIBLE);
			btnBuy.setVisibility(View.GONE);
			break;
		case LACK_OF_MONEY:
			btnThrowDice.setVisibility(View.GONE);
			btnNext.setVisibility(View.VISIBLE);
			btnBail.setVisibility(View.GONE);
			btnBuy.setVisibility(View.GONE);
		}
	}

	/**
	 * Refresh the Text and Image views in the bottom right corner to reflect
	 * the current player and his current balance
	 */
	private void refreshTextFields() {
		Player player = controller.whoseTurnIsIt().getPlayer();
		playerName.setText(player.getName());

		DecimalFormat format = Utilities.moneyFormat();
		money.setText(format.format(player.getMoney()));

		imgPlayer.setImageResource(Utilities.getPegDrawable(player.getPeg()));
		imgPlayerBg.setImageResource(Utilities.getCircleDrawable(player
				.getIndex()));
	}

	/**
	 * Animate a player view to the current position. Animates always to the
	 * right side by default and executes the action required on the new field.
	 * 
	 * @see animatePlayerToPosition(PlayerController, boolean, boolean)
	 * @param player
	 *            the PlayerController whose view should be animated
	 */
	private void animatePlayerToPosition(PlayerController player) {
		animatePlayerToPosition(player, true, true);
	}

	/**
	 * Animate a player view to the current position. If alwaysRight is set to
	 * true, the animation will go always to the right and in case the
	 * destination is in the other direction, fake views will be added to the
	 * field to simulate a continuous scroll to the right, and removed after the
	 * animation.
	 * 
	 * @param player
	 *            the PlayerController whose view should be animated
	 * @param executeAction
	 *            whether to execute the action required on the new field
	 * @param alwaysRight
	 *            whether to move always to the right
	 */
	private void animatePlayerToPosition(final PlayerController player,
			final boolean executeAction, final boolean alwaysRight) {
		final View view = getPlayerView(player.getPlayer().getIndex());
		final float x = Utilities.dpToPx((int) (SPACE_WIDTH * (player
				.getPlayer().getCurrentPos() + 0.5)), GameActivity.this)
				+ calculateSpaceRelativePositionX(player.getPlayer());
		float y = calculatePositionY(player.getPlayer());
		view.setY(y);
		if (x > view.getX() || !alwaysRight) {
			ObjectAnimator animator = ObjectAnimator.ofFloat(view, "x", x);
			animator.setDuration(800);
			animator.addListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					if (executeAction)
						doFieldAction(player);
				}
			});
			animator.start();
		} else if (x == view.getX()) {
			// do nothing
		} else {
			ObjectAnimator animator = ObjectAnimator.ofFloat(view, "x",
					spaces.getWidth() + x);
			animator.setDuration(800);
			animator.addListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					view.setX(x);
					if (executeAction)
						doFieldAction(player);
				}
			});
			animator.start();
		}

		if (!(x == view.getX()))
			scrollToSpace(player.getPlayer().getCurrentPos(), alwaysRight);
	}

	/**
	 * Animate a player's view to the top or bottom, depending on whether he is
	 * imprisoned or visitor.
	 * 
	 * @param player
	 *            the PlayerController whose view will be animated
	 * @param inJail
	 *            whether the player is imprisoned or not
	 */
	private void animatePlayerToJail(PlayerController player, boolean inJail) {
		float y = spaces.getHeight()
				* (inJail ? 1f / 6 : 2f / 3)
				+ (player.getPlayer().getIndex() - game.getPlayers().size() / 2)
				* spaces.getHeight() / (4 * game.getPlayers().size());
		ObjectAnimator animator = ObjectAnimator.ofFloat(getPlayerView(player
				.getPlayer().getIndex()), "y", y);
		animator.setDuration(800);
		animator.start();
	}

	/**
	 * Execute the action corresponding to a field the given player has landed
	 * on
	 * 
	 * @param player
	 *            the PlayerController to use
	 */
	private void doFieldAction(final PlayerController player) {
		Space space = player.getCurrentSpace();
		if (space instanceof BuyableSpace) {
			if (((BuyableSpace) space).getOwner() != null) {
				double rent;
				try {
					rent = player.payRent();
					if (rent != 0) {
						String rentString = Utilities.moneyFormat()
								.format(rent);
						Toast toast = Toast.makeText(GameActivity.this,
								"Sie haben "
										+ rentString
										+ " Miete an "
										+ ((BuyableSpace) space).getOwner()
												.getName() + " gezahlt.",
								Toast.LENGTH_SHORT);
						toast.show();
					}
				} catch (LackOfMoneyException e) {
					handleLackOfMoney(e, player);
				} catch (WrongSpaceException e) {
					// won't happen
				}
			}
		} else if (space instanceof FreeParkingSpace) {

		} else if (space instanceof GoToJailSpace) {
			player.goToJail();
			animatePlayerToPosition(player, false, false);
			animatePlayerToJail(player, true);
		} else if (space instanceof JailSpace) {
			animatePlayerToJail(player, false);
		} else if (space instanceof ChanceSpace) {
			boolean community = space instanceof CommunityChanceSpace;
			final ChanceCard card = community ? game.getCommunityCard() : game
					.getChanceCard();
			new AlertDialog.Builder(this)
					.setTitle(community ? "Gemeinschaftsfeld" : "Ereignisfeld")
					.setMessage(card.getText())
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									try {
										player.executeChanceCard(card);
										refresh();
										if (card instanceof GoToJailChanceCard) {
											animatePlayerToPosition(player,
													false, false);
											animatePlayerToJail(player, true);
										} else if (card instanceof MoveAmountChanceCard
												&& ((MoveAmountChanceCard) card)
														.getAmount() < 0) {
											animatePlayerToPosition(player,
													true, false);
										} else {
											animatePlayerToPosition(player);
										}
										Space space = player.getCurrentSpace();
										if (space instanceof BuyableSpace
												&& ((BuyableSpace) space)
														.getOwner() == null)
											setStatus(Status.DICE_THROWN_ON_BUYABLE);
										else
											setStatus(Status.DICE_THROWN);
									} catch (LackOfMoneyException e) {
										handleLackOfMoney(e, player);
									}
								}

							}).create().show();
		} else if (space instanceof PaySpace) {
			try {
				player.payPaySpace();
			} catch (LackOfMoneyException e) {
				handleLackOfMoney(e, player);
			} catch (WrongSpaceException e) {
				// won't happen
			}
		}
		refresh();
	}

	/**
	 * Handles a {@link LackOfMoneyException} by showing a message to the player
	 * and not letting the player go until he has paid, or showing a dialog that
	 * the player is bankrupt and lost the game.
	 * 
	 * @param e
	 *            the LackOfMoneyException to handle
	 * @param player
	 *            the corresponding PlayerController
	 */
	private void handleLackOfMoney(LackOfMoneyException e,
			PlayerController player) {
		double amount = e.getMoneyToPay();
		double funds = player.getFunds();
		if (funds >= amount) {
			if (status != Status.JAIL_DO_NOT_THROW_DICE)
				setStatus(Status.LACK_OF_MONEY);
			player.getPlayer().setDebt(e.getMoneyToPay());
			Toast toast = Toast.makeText(GameActivity.this,
					"Sie haben nicht genug Geld, um dies zu bezahlen!",
					Toast.LENGTH_SHORT);
			toast.show();
		} else {
			showBankruptDialog(player);
		}
	}

	/**
	 * Make a player lose the game and, if there is only one player remaining,
	 * end the game.
	 * 
	 * @param player
	 */
	private void lose(PlayerController player) {
		Player lastPlayer = player.lose();
		refresh();
		if (lastPlayer != null) {
			Intent intent = new Intent(this, GameEndedActivity.class);
			intent.putExtra("playerName", lastPlayer.getName());
			startActivity(intent);
		}
	}

	/**
	 * Show a dialog that a player has lost
	 * 
	 * @param player
	 *            the PlayerController whose player has lost
	 */
	private void showBankruptDialog(final PlayerController player) {
		new AlertDialog.Builder(this)
				.setMessage(
						"Sie können nicht genug Geld für diese Aktion aufbringen. Sie haben verloren.")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						FrameLayout players = (FrameLayout) findViewById(R.id.players);
						lose(player);
						players.removeView(getPlayerView(player.getPlayer()
								.getIndex()));

					}

				}).create().show();
	}

	/**
	 * Shorthand method for {@link refreshTextFields()} and {@link
	 * refreshSpaces()}.
	 */
	private void refresh() {
		refreshTextFields();
		refreshSpaces();
	}

	/**
	 * Scroll to a space on the game field. If alwaysRight is set to true, the
	 * animation will go always to the right and in case the destination is in
	 * the other direction, fake views will be added to the field to simulate a
	 * continuous scroll to the right, and removed after the animation.
	 * 
	 * @param pos
	 *            the number of the space to scroll to
	 * @param alwaysRight
	 *            whether to always scroll to the right
	 */
	private void scrollToSpace(int pos, boolean alwaysRight) {
		int halfWidth = fieldScroller.getWidth() / 2;
		int calculatedScrollPos = Utilities.dpToPx(
				(int) (SPACE_WIDTH * (pos + 0.5)), this) - halfWidth;
		if (calculatedScrollPos < 0) // Make sure that we don't scroll
			calculatedScrollPos = 0; // left of the first field
		final int scrollPos = calculatedScrollPos;

		if (scrollPos > fieldScroller.getScrollX() || !alwaysRight) {
			animateFieldScroll(scrollPos, 800, null);
		} else {
			final int maxScroll = spaces.getWidth();
			final LinearLayout fakeLayout = new LinearLayout(this);
			// Create fake views to make it look like the field scrolls
			// over the right end back to the start
			List<View> fakeViews = new ArrayList<View>();
			for (int i = 0; i < spaces.getChildCount(); i++) {
				Bitmap bitmap = Utilities.loadBitmapFromView(spaces
						.getChildAt(i));
				ImageView view = new ImageView(this);
				view.setImageBitmap(bitmap);
				fakeViews.add(view);
				fakeLayout.addView(view);
			}

			spaces.addView(fakeLayout);

			ViewTreeObserver vto = spaces.getViewTreeObserver();
			vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					spaces.getViewTreeObserver().removeGlobalOnLayoutListener(
							this);
					animateFieldScroll(maxScroll + scrollPos, 800,
							new AnimatorListenerAdapter() {
								@Override
								public void onAnimationEnd(Animator animation) {
									fieldScroller.scrollTo(scrollPos, 0);
									spaces.removeView(fakeLayout);
								}
							});
				}
			});
		}
	}

	/**
	 * Utility function for {@link scrollToSpace}. Animates a scroll of the game
	 * field to a specific position
	 * 
	 * @param pos
	 *            Position in pixels
	 * @param duration
	 *            duration of the scroll
	 * @param listener
	 *            {@link AnimatorListener} to add to the animation (optional)
	 */
	private void animateFieldScroll(int pos, int duration,
			AnimatorListener listener) {
		ObjectAnimator animator = ObjectAnimator.ofInt(fieldScroller,
				"scrollX", pos);
		animator.setDuration(duration);
		if (listener != null)
			animator.addListener(listener);
		animator.start();
	}

	/**
	 * Sets up views for all the players
	 */
	private void buildPlayers() {
		LayoutInflater inflater = getLayoutInflater();
		int i = 0;
		for (Player player : game.getPlayers()) {
			View view = inflater.inflate(R.layout.player, players, false);
			ImageView image = (ImageView) view.findViewById(R.id.image);
			ImageView circle = (ImageView) view.findViewById(R.id.color);
			circle.setImageDrawable(getResources().getDrawable(
					Utilities.getCircleDrawable(i)));
			image.setImageDrawable(getResources().getDrawable(
					Utilities.getPegDrawable(player.getPeg())));
			view.setTag(i);
			view.setX(Utilities.dpToPx(
					(int) (SPACE_WIDTH * (player.getCurrentPos() + 0.5)), this)
					+ calculateSpaceRelativePositionX(player));
			view.setY(calculatePositionY(player));
			players.addView(view);

			i++;
		}
	}

	/**
	 * Utility function for all functions related to moving the player views.
	 * Calculates the X position of a player relative to the center of his
	 * current space, so that the players don't all sit on the same spot.
	 * 
	 * @param player
	 *            the player to calculate the position for
	 * @return X position relative to the center of the current space, in pixels
	 */
	private int calculateSpaceRelativePositionX(Player player) {
		return Utilities.dpToPx((player.getIndex() % 2 == 0 ? 1 : -1) * 26,
				this);
	}

	/**
	 * Utility function for all functions related to moving the player views.
	 * Calculates the Y position of a player, so that the players don't all sit
	 * on the same spot.
	 * 
	 * @param player
	 *            the player to calculate the position for
	 * @return Y position in pixels
	 */
	private int calculatePositionY(Player player) {
		return Utilities.dpToPx(96 + 32 * (player.getIndex() / 2), this);
	}

	/**
	 * @param number
	 *            the index of a player
	 * @return the View corresponding to this player on the field
	 */
	private View getPlayerView(int number) {
		FrameLayout players = (FrameLayout) findViewById(R.id.players);
		for (int i = 0; i < players.getChildCount(); i++) {
			View view = players.getChildAt(i);
			if ((int) view.getTag() == number) {
				return view;
			}
		}
		return null;
	}

	/**
	 * Get references to all the important views and save them in the
	 * corresponding variables
	 */
	private void setupViews() {
		spaces = (LinearLayout) findViewById(R.id.spaces);
		fieldScroller = (HorizontalScrollView) findViewById(R.id.scroll);
		fieldProgress = (ProgressBar) findViewById(R.id.scrollDisplay);
		playerName = (TextView) findViewById(R.id.txtPlayerName);
		money = (TextView) findViewById(R.id.txtMoney);
		btnThrowDice = (Button) findViewById(R.id.btnThrowDice);
		btnManageProperty = (Button) findViewById(R.id.btnProperty);
		btnNext = (Button) findViewById(R.id.btnNext);
		btnBail = (Button) findViewById(R.id.btnBail);
		btnBuy = (Button) findViewById(R.id.btnBuy);
		imgPlayer = (ImageView) findViewById(R.id.player);
		imgPlayerBg = (ImageView) findViewById(R.id.player_bg);
		players = (FrameLayout) findViewById(R.id.players);
	}

	/**
	 * Build Views for all the spaces on the game field
	 */
	private void buildField() {
		for (Space space : game.getSpaces()) {
			View spaceView = viewCreator.createSpaceView(space, spaces);
			spaces.addView(spaceView);
		}
		refreshFieldsWidth();
	}

	/**
	 * Refreshes the spaces on the game field (for example, when the owner of a
	 * field has changed)
	 */
	private void refreshSpaces() {
		for (int i = 0; i < game.getSpaces().size(); i++) {
			View spaceView = spaces.getChildAt(i);
			viewCreator.refreshSpaceView(game.getSpaces().get(i), spaceView);
		}
	}

	/**
	 * Wait for a layout change to occur and then refresh the maximum value of
	 * the progress bar on the top
	 */
	private void refreshFieldsWidth() {
		spaces.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						spaces.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						fieldProgress.setMax(spaces.getWidth());
					}
				});
	}

	/**
	 * Called every time the app is started or the main game Activity is brought
	 * to the front again. Loads the game and refreshes all the views. Also
	 * starts the {@link SetupGameActivity} if no game is currently saved.
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		// Load old game if it exists
		SaveGame savegame = null;
		try {
			savegame = SaveGameHandler.loadGame(this, "test.game");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (savegame != null) {
			controller = savegame.getController();
			game = controller.getGame();
			setStatus(savegame.getStatus());
			clearFieldAndPlayers();
			buildField();
			buildPlayers();
			refresh();
		} else {
			Intent intent = new Intent(this, SetupGameActivity.class);
			startActivity(intent);
		}
	}

	/**
	 * Remove all player and space views from their respective containers
	 */
	private void clearFieldAndPlayers() {
		spaces.removeAllViews();
		players.removeAllViews();
	}

	/**
	 * Called when the GameActivity is hidden. Automatically saves the game.
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		if (controller != null && game != null)
			try {
				SaveGameHandler.saveGame(this,
						new SaveGame(controller, status), "test.game");
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

}
