package de.unikiel.programmierpraktikum.monopoly.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import de.unikiel.programmierpraktikum.monopoly.R;
import de.unikiel.programmierpraktikum.monopoly.controller.GameController;
import de.unikiel.programmierpraktikum.monopoly.controller.GameFieldLoader;
import de.unikiel.programmierpraktikum.monopoly.controller.PlayerController;
import de.unikiel.programmierpraktikum.monopoly.model.ChanceChanceSpace;
import de.unikiel.programmierpraktikum.monopoly.model.ChanceSpace;
import de.unikiel.programmierpraktikum.monopoly.model.CommunityChanceSpace;
import de.unikiel.programmierpraktikum.monopoly.model.FreeParkingSpace;
import de.unikiel.programmierpraktikum.monopoly.model.Game;
import de.unikiel.programmierpraktikum.monopoly.model.GoSpace;
import de.unikiel.programmierpraktikum.monopoly.model.GoToJailSpace;
import de.unikiel.programmierpraktikum.monopoly.model.JailSpace;
import de.unikiel.programmierpraktikum.monopoly.model.PaySpace;
import de.unikiel.programmierpraktikum.monopoly.model.Player;
import de.unikiel.programmierpraktikum.monopoly.model.Player.Peg;
import de.unikiel.programmierpraktikum.monopoly.model.Space;
import de.unikiel.programmierpraktikum.monopoly.model.StationSpace;
import de.unikiel.programmierpraktikum.monopoly.model.StreetSpace;
import de.unikiel.programmierpraktikum.monopoly.model.UtilitySpace;
import de.unikiel.programmierpraktikum.monopoly.utilities.Utilities;

public class GameActivity extends Activity {
	private Game game;
	private GameController controller;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		try {
			String field = readStream(getAssets().open("field/test_field.json"));
			String chanceCards = readStream(getAssets().open("chance_cards/chance_cards.json"));
			String communityCards = readStream(getAssets().open("chance_cards/chance_cards.json")); //TODO:
			List<Player> players = new ArrayList<Player>();
			players.add(new Player("TestSpieler", Peg.ALBERT_EINSTEIN));
			players.add(new Player("TestSpieler2", Peg.WERNER_HEISENBERG));
			players.add(new Player("TestSpieler", Peg.ALBERT_EINSTEIN));
			players.add(new Player("TestSpieler2", Peg.WERNER_HEISENBERG));
			players.add(new Player("TestSpieler", Peg.ALBERT_EINSTEIN));
			players.add(new Player("TestSpieler2", Peg.WERNER_HEISENBERG));
			players.add(new Player("TestSpieler", Peg.ALBERT_EINSTEIN));
			players.add(new Player("TestSpieler2", Peg.WERNER_HEISENBERG));

			game = GameFieldLoader.createGame(field, chanceCards, communityCards, players);
			controller = new GameController(game);
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
				Toast toast = Toast.makeText(GameActivity.this, "Du hast eine " + number + " gewürfelt!", Toast.LENGTH_SHORT);
				toast.show();
				
				Player player = controller.whoseTurnIsIt().getPlayer();
				
				View view = getPlayerView(controller.getCurrentPlayerNumber());
				FrameLayout.LayoutParams params = (LayoutParams) view.getLayoutParams();
				params.setMargins(Utilities.dpToPx((int) (225*player.getCurrentPos() + 112.5), GameActivity.this),
						Utilities.dpToPx(96, GameActivity.this), 0, 0);
				view.setLayoutParams(params);
			}
			
		});

	}
	
	private void buildPlayers() {	
		FrameLayout players = (FrameLayout) findViewById(R.id.players);
		LayoutInflater inflater = getLayoutInflater();
		int i = 0;
		for(Player player:game.getPlayers()) {			
			View view = inflater.inflate(R.layout.player, players, false);
			view.setTag(i);
			FrameLayout.LayoutParams params = (LayoutParams) view.getLayoutParams();
			params.setMargins(Utilities.dpToPx((int) (225*player.getCurrentPos() + (i%2)*53 +62), this),
					Utilities.dpToPx(96 + 32*(i/2), this), 0, 0);
			players.addView(view);
			i++;
		}
	}
	
	private View getPlayerView(int number) {
		FrameLayout players = (FrameLayout) findViewById(R.id.players);
		for(int i = 0; i<players.getChildCount(); i++) {
			View view = players.getChildAt(i);
			if((int) view.getTag() == number) {
				return view;
			}
		}
		return null;
	}

	private void buildField() {
		LinearLayout spaces = (LinearLayout) findViewById(R.id.spaces);
		LayoutInflater inflater = getLayoutInflater();
		
		DecimalFormat format = new DecimalFormat("#,##0 eV");
		
		for(Space space:game.getSpaces()) {
			View spaceView = null;
			if(space instanceof GoSpace) {
				spaceView = inflater.inflate(R.layout.layout_go_space, spaces, false);
			} else if (space instanceof ChanceSpace) {
				if(space instanceof ChanceChanceSpace) {
					spaceView = inflater.inflate(R.layout.layout_chance_chance_space, spaces, false);
				} else if(space instanceof CommunityChanceSpace) {
					spaceView = inflater.inflate(R.layout.layout_community_chance_space, spaces, false);
				}
			} else if (space instanceof FreeParkingSpace) {	
				spaceView = inflater.inflate(R.layout.layout_free_parking_space, spaces, false);
			} else if (space instanceof GoToJailSpace) {	
				spaceView = inflater.inflate(R.layout.layout_go_to_jail_space, spaces, false);
			} else if (space instanceof JailSpace) {	
				spaceView = inflater.inflate(R.layout.layout_jail_space, spaces, false);
			} else if (space instanceof PaySpace) {	
				PaySpace pay = (PaySpace) space;
				spaceView = inflater.inflate(R.layout.layout_pay_space, spaces, false);
				
				TextView name = (TextView) spaceView.findViewById(R.id.txtName);
				name.setText(pay.getName());
				
				TextView price = (TextView) spaceView.findViewById(R.id.txtPrice);
				price.setText(format.format(pay.getAmount()));
			} else if (space instanceof StationSpace) {	
				StationSpace station = (StationSpace) space;
				spaceView = inflater.inflate(R.layout.layout_station_space, spaces, false);
				
				TextView name = (TextView) spaceView.findViewById(R.id.txtName);
				name.setText(station.getName());
				
				TextView price = (TextView) spaceView.findViewById(R.id.txtPrice);
				price.setText(format.format(station.getPurchasePrice()));
			} else if (space instanceof UtilitySpace) {	
				UtilitySpace utility = (UtilitySpace) space;
				spaceView = inflater.inflate(R.layout.layout_utility_space, spaces, false);
				
				TextView name = (TextView) spaceView.findViewById(R.id.txtName);
				name.setText(utility.getName());
				
				TextView price = (TextView) spaceView.findViewById(R.id.txtPrice);
				price.setText(format.format(utility.getPurchasePrice()));
			} else if (space instanceof StreetSpace) {
				StreetSpace street = (StreetSpace) space;
				spaceView = inflater.inflate(R.layout.layout_street_space, spaces, false);
				
				TextView name = (TextView) spaceView.findViewById(R.id.txtName);
				name.setText(street.getName());
				
				TextView price = (TextView) spaceView.findViewById(R.id.txtPrice);
				price.setText(format.format(street.getPurchasePrice()));
				
				View category = spaceView.findViewById(R.id.category);
				switch (street.getCategory()) {
				case BROWN:
					category.setBackgroundResource(R.color.category_brown);
					break;
				case LIGHT_BLUE:
					category.setBackgroundResource(R.color.category_light_blue);
					break;
				case PINK:
					category.setBackgroundResource(R.color.category_pink);
					break;
				case ORANGE:
					category.setBackgroundResource(R.color.category_orange);
					break;
				case RED:
					category.setBackgroundResource(R.color.category_red);
					break;
				case YELLOW:
					category.setBackgroundResource(R.color.category_yellow);
					break;
				case GREEN:
					category.setBackgroundResource(R.color.category_green);
					break;
				case DARK_BLUE:
					category.setBackgroundResource(R.color.category_dark_blue);
					break;
					
				}
			}
			spaces.addView(spaceView);
		}
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
