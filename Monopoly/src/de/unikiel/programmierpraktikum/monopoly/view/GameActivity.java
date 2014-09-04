package de.unikiel.programmierpraktikum.monopoly.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import de.unikiel.programmierpraktikum.monopoly.controller.GameFieldLoader;
import de.unikiel.programmierpraktikum.monopoly.controller.PlayerController;
import de.unikiel.programmierpraktikum.monopoly.controller.SaveGameHandler;
import de.unikiel.programmierpraktikum.monopoly.controller.SaveGameHandler.SaveGame;
import de.unikiel.programmierpraktikum.monopoly.exceptions.LackOfMoneyException;
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
import de.unikiel.programmierpraktikum.monopoly.model.Player.Peg;
import de.unikiel.programmierpraktikum.monopoly.model.Space;
import de.unikiel.programmierpraktikum.monopoly.model.StreetSpace;
import de.unikiel.programmierpraktikum.monopoly.utilities.Utilities;

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
	private ImageView imgPlayer;
	private ImageView imgPlayerBg;

	public enum Status {
		DICE_NOT_THROWN, DICE_THROWN, JAIL_THROW_DICE, JAIL_DO_NOT_THROW_DICE, LACK_OF_MONEY
	}

	private static final int SPACE_WIDTH = 225;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupViews();

		viewCreator = new ViewCreator(this);

		// Load old game if it exists
		SaveGame savegame = null;
		try {
			savegame = new SaveGameHandler().loadGame(this, "test.game");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (savegame != null) {
			controller = savegame.getController();
			game = controller.getGame();
			setStatus(savegame.getStatus());
			buildField();
			buildPlayers();
			refresh();
		} else {
			try {
				String field = readStream(getAssets().open("field/field.json"));
				String chanceCards = readStream(getAssets().open(
						"chance_cards/chance_cards.json"));
				String communityCards = readStream(getAssets().open(
						"chance_cards/community_cards.json")); // TODO:
				List<Player> players = new ArrayList<Player>();
				Player einstein = new Player("Einstein", Peg.ALBERT_EINSTEIN);
				players.add(einstein);
				players.add(new Player("Heisenberg", Peg.WERNER_HEISENBERG));

				game = GameFieldLoader.createGame(field, chanceCards,
						communityCards, players);
				controller = new GameController(game);
				controller.giveStartMoney();
				((StreetSpace) game.getSpaces().get(37)).setOwner(einstein);
				((StreetSpace) game.getSpaces().get(39)).setOwner(einstein);
				buildField();
				buildPlayers();
			} catch (IOException e) {
				e.printStackTrace();
			}

			refreshTextFields();
			setStatus(Status.DICE_NOT_THROWN);
		}

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
				scrollToField(controller.whoseTurnIsIt().getPlayer()
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

	}

	private void setStatus(Status status) {
		this.status = status;
		switch (status) {
		case DICE_NOT_THROWN:
			btnThrowDice.setVisibility(View.VISIBLE);
			btnNext.setVisibility(View.GONE);
			btnBail.setVisibility(View.GONE);
			break;
		case DICE_THROWN:
			btnThrowDice.setVisibility(View.GONE);
			btnNext.setVisibility(View.VISIBLE);
			btnBail.setVisibility(View.GONE);
			break;
		case JAIL_THROW_DICE:
			btnThrowDice.setVisibility(View.VISIBLE);
			btnNext.setVisibility(View.GONE);
			btnBail.setVisibility(View.VISIBLE);
			break;
		case JAIL_DO_NOT_THROW_DICE:
			btnThrowDice.setVisibility(View.GONE);
			btnNext.setVisibility(View.GONE);
			btnBail.setVisibility(View.VISIBLE);
			break;
		case LACK_OF_MONEY:
			btnThrowDice.setVisibility(View.GONE);
			btnNext.setVisibility(View.VISIBLE);
			btnBail.setVisibility(View.GONE);
		}
	}

	private void refreshTextFields() {
		Player player = controller.whoseTurnIsIt().getPlayer();
		playerName.setText(player.getName());

		DecimalFormat format = Utilities.moneyFormat();
		money.setText(format.format(player.getMoney()));

		imgPlayer.setImageResource(Utilities.getPegDrawable(player.getPeg()));
		imgPlayerBg.setImageResource(Utilities.getCircleDrawable(player
				.getIndex()));
	}

	private void animatePlayerToPosition(PlayerController player) {
		animatePlayerToPosition(player, true, true);
	}

	private void animatePlayerToPosition(final PlayerController player,
			final boolean executeAction, final boolean alwaysRight) {
		final View view = getPlayerView(player.getPlayer().getIndex());
		final float x = Utilities.dpToPx((int) (SPACE_WIDTH * (player
				.getPlayer().getCurrentPos() + 0.5)), GameActivity.this)
				+ calculateFieldRelativePositionX(player.getPlayer());
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
			scrollToField(player.getPlayer().getCurrentPos(), alwaysRight);
	}

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
					if (player.getFunds() < ((BuyableSpace) space).getRent())
						showBankruptDialog(player);
					else
						setStatus(Status.LACK_OF_MONEY);
				}
			} else {
				new AlertDialog.Builder(this)
						.setMessage(
								"Sie sind auf "
										+ space.getName()
										+ " gelandet. Möchten Sie dieses Feld kaufen?")
						.setCancelable(true)
						.setPositiveButton("ja",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										try {
											player.buySpace();
											refresh();
										} catch (LackOfMoneyException e) {
											Toast toast = Toast
													.makeText(
															GameActivity.this,
															"Sie haben nicht genug Geld, um das Feld zu kaufen!",
															Toast.LENGTH_SHORT);
											toast.show();
										}
									}

								})
						.setNegativeButton("nein",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

									}

								}).create().show();
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
			}
		}
		refresh();
	}

	private void handleLackOfMoney(LackOfMoneyException e,
			PlayerController player) {
		double amount = e.getMoneyToPay();
		double funds = player.getFunds();
		if (funds >= amount) {
			if (status != Status.JAIL_DO_NOT_THROW_DICE)
				setStatus(Status.LACK_OF_MONEY);

			Toast toast = Toast.makeText(GameActivity.this,
					"Sie haben nicht genug Geld, um dies zu bezahlen!",
					Toast.LENGTH_SHORT);
			toast.show();
		} else {
			Toast toast = Toast
					.makeText(
							GameActivity.this,
							"Sie haben nicht genug Geld, um dies zu bezahlen! Sie haben verloren!",
							Toast.LENGTH_LONG);
			toast.show();
			lose(player);
			setStatus(Status.DICE_THROWN);
		}
	}

	private void lose(PlayerController player) {
		Player lastPlayer = player.lose();
		if (lastPlayer != null) {
			Toast toast = Toast
					.makeText(GameActivity.this, lastPlayer.getName()
							+ " hat gewonnen!", Toast.LENGTH_SHORT);
			toast.show();
		}
	}

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

	private void refresh() {
		refreshTextFields();
		refreshSpaces();
	}

	private void scrollToField(int pos, boolean alwaysRight) {
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
			
			// Create fake views to make it look like the field scrolls
			// over the right end back to the start
			final List<View> fakeViews = new ArrayList<View>();
			for (int i = 0; i < spaces.getChildCount(); i++) {
				Bitmap bitmap = Utilities.loadBitmapFromView(spaces
						.getChildAt(i));
				ImageView view = new ImageView(this);
				view.setImageBitmap(bitmap);
				fakeViews.add(view);
			}

			for (View view : fakeViews) {
				spaces.addView(view);
			}

			ViewTreeObserver vto = spaces.getViewTreeObserver();
			vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					spaces.getViewTreeObserver().removeGlobalOnLayoutListener(
							this);
					animateFieldScroll(maxScroll + scrollPos, 800, new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							fieldScroller.scrollTo(scrollPos, 0);
							for (View view : fakeViews) {
								spaces.removeView(view);
							}
						}
					});
				}
			});
		}
	}

	private void animateFieldScroll(int pos, int duration,
			AnimatorListener listener) {
		ObjectAnimator animator = ObjectAnimator.ofInt(fieldScroller,
				"scrollX", pos);
		animator.setDuration(duration);
		if (listener != null)
			animator.addListener(listener);
		animator.start();
	}

	private void buildPlayers() {
		final FrameLayout players = (FrameLayout) findViewById(R.id.players);
		FrameLayout progressBars = (FrameLayout) findViewById(R.id.progressBars);
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
					+ calculateFieldRelativePositionX(player));
			view.setY(calculatePositionY(player));
			players.addView(view);

			// final ProgressBar bar = (ProgressBar) inflater.inflate(
			// R.layout.progress_bar, progressBars).findViewById(R.id.progress);
			// view.addOnLayoutChangeListener(new OnLayoutChangeListener() {
			//
			// @Override
			// public void onLayoutChange(View v, int left, int top,
			// int right, int bottom, int oldLeft, int oldTop,
			// int oldRight, int oldBottom) {
			// bar.setMax(players.getWidth());
			// bar.setProgress(left);
			// }
			//
			// });

			i++;
		}
	}

	private int calculateFieldRelativePositionX(Player player) {
		return Utilities.dpToPx((player.getIndex() % 2 == 0 ? 1 : -1) * 26,
				this);
	}

	private int calculatePositionY(Player player) {
		return Utilities.dpToPx(96 + 32 * (player.getIndex() / 2), this);
	}

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

	private void setupViews() {
		spaces = (LinearLayout) findViewById(R.id.spaces);
		fieldScroller = (HorizontalScrollView) findViewById(R.id.scroll);
		fieldProgress = (ProgressBar) findViewById(R.id.scrollDisplay);
		fieldScroller.getViewTreeObserver().addOnScrollChangedListener(
				new OnScrollChangedListener() {
					@Override
					public void onScrollChanged() {
						int scroll = fieldScroller.getScrollX();
						fieldProgress.setProgress(scroll
								% fieldProgress.getMax());
					}
				});
		playerName = (TextView) findViewById(R.id.txtPlayerName);
		money = (TextView) findViewById(R.id.txtMoney);
		btnThrowDice = (Button) findViewById(R.id.btnThrowDice);
		btnManageProperty = (Button) findViewById(R.id.btnProperty);
		btnNext = (Button) findViewById(R.id.btnNext);
		btnBail = (Button) findViewById(R.id.btnBail);
		imgPlayer = (ImageView) findViewById(R.id.player);
		imgPlayerBg = (ImageView) findViewById(R.id.player_bg);
	}

	private void buildField() {
		for (Space space : game.getSpaces()) {
			View spaceView = viewCreator.createSpaceView(space, spaces);
			spaces.addView(spaceView);
		}
		refreshFieldsWidth();
	}

	private void refreshSpaces() {
		for (int i = 0; i < game.getSpaces().size(); i++) {
			View spaceView = spaces.getChildAt(i);
			viewCreator.refreshSpaceView(game.getSpaces().get(i), spaceView);
		}
	}

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

	private static String readStream(InputStream input) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(input));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			return sb.toString();
		} finally {
			br.close();
		}
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onPause() {
		super.onPause();
		new SaveGameHandler().saveGame(this, new SaveGame(controller, status),
				"test.game");
	}

}
