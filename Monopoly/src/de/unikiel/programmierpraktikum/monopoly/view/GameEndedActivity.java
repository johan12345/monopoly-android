/**
 * 
 */
package de.unikiel.programmierpraktikum.monopoly.view;

import de.unikiel.programmierpraktikum.monopoly.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author johan_000
 *
 */
public class GameEndedActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_ended);
		
		TextView name = (TextView) findViewById(R.id.txtHasWon);
		name.setText(getIntent().getStringExtra("playerName") + " hat gewonnen!");
		
		Button newGame = (Button) findViewById(R.id.btnNewGame);
		newGame.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GameEndedActivity.this, SetupGameActivity.class);
				startActivity(intent);
			}
			
		});
	}
}
