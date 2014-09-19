/**
 * 
 */
package de.unikiel.programmierpraktikum.monopoly.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.unikiel.programmierpraktikum.monopoly.R;
import de.unikiel.programmierpraktikum.monopoly.controller.GameController;
import de.unikiel.programmierpraktikum.monopoly.controller.GameFieldLoader;
import de.unikiel.programmierpraktikum.monopoly.controller.SaveGameHandler;
import de.unikiel.programmierpraktikum.monopoly.controller.SaveGameHandler.SaveGame;
import de.unikiel.programmierpraktikum.monopoly.model.Game;
import de.unikiel.programmierpraktikum.monopoly.model.Player;
import de.unikiel.programmierpraktikum.monopoly.model.Player.Peg;
import de.unikiel.programmierpraktikum.monopoly.utilities.Utilities;
import de.unikiel.programmierpraktikum.monopoly.view.GameActivity.Status;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author johan_000
 *
 */
public class SetupGameActivity extends Activity {
	private List<Player> players;
	private Map<Peg, Boolean> availablePegs;
	private Button btnAdd;
	private PlayerAdapter adapter;
	private ListView list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_game);
		btnAdd = (Button) findViewById(R.id.btnAdd);
		list = (ListView) findViewById(R.id.list);

		players = new ArrayList<Player>();
		adapter = new PlayerAdapter(this);
		list.setAdapter(adapter);

		availablePegs = new HashMap<Peg, Boolean>();
		for (Peg peg : Peg.values()) {
			availablePegs.put(peg, true);
		}
		btnAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final Map<String, Peg> pegs = new HashMap<String, Peg>();
				for (Entry<Peg, Boolean> entry : availablePegs.entrySet()) {
					if (entry.getValue()) {
						String name = entry.getKey().toString();
						name = toTitleCase(name.replace("_", " "));
						pegs.put(name, entry.getKey());
					}
				}
				final List<String> pegNames = new ArrayList<String>(pegs
						.keySet());
				AlertDialog.Builder builder = new AlertDialog.Builder(
						SetupGameActivity.this);
				builder.setItems(pegNames.toArray(new String[] {}),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface intf, int number) {
								final Peg selectedPeg = pegs.get(pegNames
										.get(number));

								AlertDialog.Builder builder = new AlertDialog.Builder(
										SetupGameActivity.this);
								final EditText edittext = new EditText(
										SetupGameActivity.this);
								builder.setView(edittext);
								builder.setTitle("Geben Sie Ihren Namen ein");
								builder.setPositiveButton("OK",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												String name = edittext
														.getText().toString();
												players.add(new Player(name,
														selectedPeg));
												availablePegs.put(selectedPeg,
														false);
												adapter.notifyDataSetChanged();
											}
										});
								builder.create().show();
							}

						});
				builder.setTitle("Wählen Sie die Spielfigur");
				builder.create().show();
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.setup_game, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.startGame) {
			if (players.size() < 2) {
				Toast.makeText(this, "Zu wenig Spieler!", Toast.LENGTH_SHORT).show();
			} else {
				try {
					String field = Utilities.readStream(getAssets().open(
							"field/field.json"));
					String chanceCards = Utilities.readStream(getAssets().open(
							"chance_cards/chance_cards.json"));
					String communityCards = Utilities.readStream(getAssets()
							.open("chance_cards/community_cards.json"));

					Game game = GameFieldLoader.createGame(field, chanceCards,
							communityCards, players);
					GameController controller = new GameController(game);
					controller.giveStartMoney();

					new SaveGameHandler().saveGame(this, new SaveGame(
							controller, Status.DICE_NOT_THROWN), "test.game");
					finish();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	private String toTitleCase(String input) {
		StringBuilder titleCase = new StringBuilder();
		boolean nextTitleCase = true;
		for (char c : input.toCharArray()) {
			if (Character.isSpaceChar(c)) {
				nextTitleCase = true;
			} else if (nextTitleCase) {
				c = Character.toUpperCase(c);
				nextTitleCase = false;
			} else {
				c = Character.toLowerCase(c);
			}

			titleCase.append(c);
		}
		return titleCase.toString();
	}

	private class PlayerAdapter extends ArrayAdapter<Player> {

		private LayoutInflater inflater;

		public PlayerAdapter(Context context) {
			super(context, 0);
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return players.size();
		}

		@Override
		public Player getItem(int pos) {
			return players.get(pos);
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup container) {
			View view;
			if (convertView != null)
				view = convertView;
			else
				view = inflater.inflate(R.layout.listitem_player, container,
						false);

			ImageView imgCircle = (ImageView) view.findViewById(R.id.imgCircle);
			ImageView imgPicture = (ImageView) view
					.findViewById(R.id.imgPicture);
			TextView txtName = (TextView) view.findViewById(R.id.txtName);
			Button btnDelete = (Button) view.findViewById(R.id.btnDelete);
			final Player player = getItem(pos);

			imgCircle.setImageResource(Utilities.getCircleDrawable(pos));
			imgPicture.setImageResource(Utilities.getPegDrawable(player
					.getPeg()));
			txtName.setText(player.getName());
			btnDelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					players.remove(player);
					availablePegs.put(player.getPeg(), true);
					notifyDataSetChanged();
				}

			});

			return view;
		}
	}
}
