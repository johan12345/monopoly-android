package de.unikiel.programmierpraktikum.monopoly.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import de.unikiel.programmierpraktikum.monopoly.R;
import de.unikiel.programmierpraktikum.monopoly.controller.*;
import de.unikiel.programmierpraktikum.monopoly.model.*;
import de.unikiel.programmierpraktikum.monopoly.model.Player.Peg;
import de.unikiel.programmierpraktikum.monopoly.utilities.Utilities;

public class GameActivity extends Activity {
	private Game game;
	private GameController controller;
	private HorizontalScrollView fieldScroller;
	private LinearLayout spaces;
	private ProgressBar fieldProgress;
	private ViewCreator viewCreator;

	private static final int SPACE_WIDTH = 225;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		viewCreator = new ViewCreator(this);

		try {
			String field = readStream(getAssets().open("field/field.json"));
			String chanceCards = readStream(getAssets().open(
					"chance_cards/chance_cards.json"));
			String communityCards = readStream(getAssets().open(
					"chance_cards/chance_cards.json")); // TODO:
			List<Player> players = new ArrayList<Player>();
			players.add(new Player("TestSpieler", Peg.ALBERT_EINSTEIN));
			players.add(new Player("TestSpieler2", Peg.WERNER_HEISENBERG));
			players.add(new Player("TestSpieler", Peg.ALBERT_EINSTEIN));
			players.add(new Player("TestSpieler2", Peg.WERNER_HEISENBERG));
			players.add(new Player("TestSpieler", Peg.ALBERT_EINSTEIN));
			players.add(new Player("TestSpieler2", Peg.WERNER_HEISENBERG));
			players.add(new Player("TestSpieler", Peg.ALBERT_EINSTEIN));
			players.add(new Player("TestSpieler2", Peg.WERNER_HEISENBERG));

			game = GameFieldLoader.createGame(field, chanceCards,
					communityCards, players);
			controller = new GameController(game);
			setupViews();
			buildField();
			buildPlayers();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Button btnThrowDice = (Button) findViewById(R.id.btnThrowDice);
		btnThrowDice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				int number = controller.whoseTurnIsIt().throwTheDice();
				Toast toast = Toast.makeText(GameActivity.this, "Du hast eine "
						+ number + " gewürfelt!", Toast.LENGTH_SHORT);
				toast.show();

				Player player = controller.whoseTurnIsIt().getPlayer();
				animatePlayerToPosition(player);
				
			}

		});

	}
	
	private void animatePlayerToPosition(Player player) {
		final View view = getPlayerView(controller.getCurrentPlayerNumber());
		final float x = Utilities.dpToPx(
				(int) (SPACE_WIDTH * player.getCurrentPos() + (SPACE_WIDTH / 2)),
				GameActivity.this);
		if (x > view.getX()) {
			ObjectAnimator animator = ObjectAnimator.ofFloat(
					view, "x", x);
			animator.setDuration(800);
			animator.start();
		} else {
			ObjectAnimator animator = ObjectAnimator.ofFloat(
					view, "x", spaces.getWidth() + x);
			animator.setDuration(800);
			animator.addListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					view.setX(x);
				}
			});
			animator.start();
		}
//		FrameLayout.LayoutParams params = (LayoutParams) view
//				.getLayoutParams();
//		params.setMargins(
//				Utilities.dpToPx(
//						(int) (SPACE_WIDTH * player.getCurrentPos() + (SPACE_WIDTH / 2)),
//						GameActivity.this), Utilities.dpToPx(96,
//						GameActivity.this), 0, 0);
//		view.setLayoutParams(params);
		scrollToField(player.getCurrentPos(), true);
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
			Log.d("monopoly", "maxScroll " + maxScroll);
			Bitmap bitmap = Utilities.loadBitmapFromView(spaces);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, scrollPos
					+ fieldScroller.getWidth(), bitmap.getHeight());
			final ImageView view = new ImageView(this);
			view.setImageBitmap(bitmap);
			spaces.addView(view);

			ViewTreeObserver vto = spaces.getViewTreeObserver();
			vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					spaces.getViewTreeObserver().removeGlobalOnLayoutListener(
							this);
					ObjectAnimator animator = ObjectAnimator.ofInt(
							fieldScroller, "scrollX", maxScroll + scrollPos);
					animator.setDuration(800);
					animator.addListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							fieldScroller.scrollTo(scrollPos, 0);
							spaces.removeView(view);
						}
					});
					animator.start();
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
			FrameLayout.LayoutParams params = (LayoutParams) view
					.getLayoutParams();
			params.setMargins(
					Utilities.dpToPx((int) (225 * player.getCurrentPos()
							+ (i % 2) * 53 + 62), this),
					Utilities.dpToPx(96 + 32 * (i / 2), this), 0, 0);
			players.addView(view);

//			final ProgressBar bar = (ProgressBar) inflater.inflate(
//					R.layout.progress_bar, progressBars).findViewById(R.id.progress);
//			view.addOnLayoutChangeListener(new OnLayoutChangeListener() {
//
//				@Override
//				public void onLayoutChange(View v, int left, int top,
//						int right, int bottom, int oldLeft, int oldTop,
//						int oldRight, int oldBottom) {
//					bar.setMax(players.getWidth());
//					bar.setProgress(left);
//				}
//
//			});

			i++;
		}
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
	}

	private void buildField() {
		for (Space space : game.getSpaces()) {
			View spaceView = viewCreator.createSpaceView(space, spaces);
			spaces.addView(spaceView);
		}
		refreshFieldsWidth();
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

}
