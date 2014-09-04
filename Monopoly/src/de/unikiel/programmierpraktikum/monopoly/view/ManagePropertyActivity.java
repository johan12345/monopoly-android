package de.unikiel.programmierpraktikum.monopoly.view;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.unikiel.programmierpraktikum.monopoly.R;
import de.unikiel.programmierpraktikum.monopoly.controller.GameController;
import de.unikiel.programmierpraktikum.monopoly.exceptions.LackOfMoneyException;
import de.unikiel.programmierpraktikum.monopoly.exceptions.UnableToEditHousesException;
import de.unikiel.programmierpraktikum.monopoly.exceptions.UnableToRaiseMortgageException;
import de.unikiel.programmierpraktikum.monopoly.model.BuyableSpace;
import de.unikiel.programmierpraktikum.monopoly.model.Space;
import de.unikiel.programmierpraktikum.monopoly.model.StationSpace;
import de.unikiel.programmierpraktikum.monopoly.model.StreetSpace;
import de.unikiel.programmierpraktikum.monopoly.model.UtilitySpace;
import de.unikiel.programmierpraktikum.monopoly.utilities.Utilities;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ManagePropertyActivity extends Activity {
	private MonopolyApplication app;
	private GameController controller;
	private List<BuyableSpace> property;
	private ListView list;
	private PropertyAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_property);

		app = (MonopolyApplication) getApplication();
		controller = app.getGameController();
		property = controller.getGame().getProperty(
				controller.whoseTurnIsIt().getPlayer());
		Collections.sort(property, new SpaceComparator());
		Log.d("monopoly", String.valueOf(property.size()));
		list = (ListView) findViewById(R.id.list);
		adapter = new PropertyAdapter(this);
		list.setAdapter(adapter);
	}

	private class PropertyAdapter extends ArrayAdapter<BuyableSpace> {

		private LayoutInflater inflater;

		public PropertyAdapter(Context context) {
			super(context, 0);
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return property.size();
		}

		@Override
		public BuyableSpace getItem(int pos) {
			return property.get(pos);
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup container) {
			View view;
			if (convertView != null)
				view = convertView;
			else
				view = inflater.inflate(R.layout.listitem_space, container,
						false);

			final BuyableSpace space = getItem(pos);
			DecimalFormat format = Utilities.moneyFormat();

			View color = view.findViewById(R.id.color);
			TextView name = (TextView) view.findViewById(R.id.name);
			ToggleButton mortgage = (ToggleButton) view
					.findViewById(R.id.mortgage);
			ImageView house1 = (ImageView) view.findViewById(R.id.imgHouse1);
			ImageView house2 = (ImageView) view.findViewById(R.id.imgHouse2);
			ImageView house3 = (ImageView) view.findViewById(R.id.imgHouse3);
			ImageView house4 = (ImageView) view.findViewById(R.id.imgHouse4);
			ImageView hotel = (ImageView) view.findViewById(R.id.imgHotel);
			Button plus = (Button) view.findViewById(R.id.btnPlus);
			Button minus = (Button) view.findViewById(R.id.btnMinus);
			TextView rent = (TextView) view.findViewById(R.id.txtRent);
			TextView housePrice = (TextView) view
					.findViewById(R.id.txtHousePrice);

			if (space instanceof StreetSpace)
				color.setBackgroundResource(Utilities
						.getCategoryColor(((StreetSpace) space).getCategory()));
			else
				color.setBackgroundColor(Color.parseColor("#00000000"));

			name.setText(space.getName());
			mortgage.setTextOff("Hypothek ("
					+ format.format(space.getMortgageValue()) + ")");

			house1.setVisibility(houseVisibility(space, 1));
			house2.setVisibility(houseVisibility(space, 2));
			house3.setVisibility(houseVisibility(space, 3));
			house4.setVisibility(houseVisibility(space, 4));
			hotel.setVisibility(hotelVisibility(space));
			plus.setVisibility(visibility(space instanceof StreetSpace));
			minus.setVisibility(visibility(space instanceof StreetSpace));
			mortgage.setChecked(space.isMortgage());
			rent.setText("Miete: " + format.format(space.getRent()));
			if (space instanceof StreetSpace) {
				housePrice.setText("1 Haus/Hotel kostet: "
						+ format.format(((StreetSpace) space).getHousePrice()));
				housePrice.setVisibility(View.VISIBLE);
			} else {
				housePrice.setVisibility(View.GONE);
			}

			plus.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					try {
						controller.whoseTurnIsIt()
								.addHouse((StreetSpace) space);
					} catch (LackOfMoneyException e) {
						Toast.makeText(ManagePropertyActivity.this,
								"Nicht genug Geld!", Toast.LENGTH_SHORT).show();
					} catch (UnableToEditHousesException e) {
						Toast.makeText(ManagePropertyActivity.this,
								"Hauskauf nicht möglich!", Toast.LENGTH_SHORT)
								.show();
					}
					notifyDataSetChanged();
				}

			});

			minus.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					try {
						controller.whoseTurnIsIt().removeHouse(
								(StreetSpace) space);
					} catch (UnableToEditHousesException e) {
						Toast.makeText(ManagePropertyActivity.this,
								"Hausverkauf nicht möglich!",
								Toast.LENGTH_SHORT).show();
					}
					notifyDataSetChanged();
				}

			});

			mortgage.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton arg0,
						boolean mortgage) {
					try {
						controller.whoseTurnIsIt().setMortgage(mortgage, space);
					} catch (LackOfMoneyException e) {
						Toast.makeText(ManagePropertyActivity.this,
								"Nicht genug Geld!", Toast.LENGTH_SHORT).show();
					} catch (UnableToRaiseMortgageException e) {
						// Should not happen
					}
					notifyDataSetChanged();
				}

			});

			return view;
		}

		private int houseVisibility(BuyableSpace space, int num) {
			if (space instanceof StreetSpace) {
				return visibility(((StreetSpace) space).getRealHousesCount() >= num);
			} else {
				return View.INVISIBLE;
			}
		}

		private int hotelVisibility(BuyableSpace space) {
			if (space instanceof StreetSpace) {
				return visibility(((StreetSpace) space).getHotelCount() == 1);
			} else {
				return View.INVISIBLE;
			}
		}

		private int visibility(boolean visible) {
			return visible ? View.VISIBLE : View.INVISIBLE;
		}

	}
	
	private class SpaceComparator implements Comparator<Space> {

		@Override
		public int compare(Space a, Space b) {
			return catNumber(a).compareTo(catNumber(b));
		}
		
		private Integer catNumber(Space space) {
			if (space instanceof StreetSpace)
				return ((StreetSpace) space).getCategory().ordinal();
			else if (space instanceof UtilitySpace)
				return -1;
			else if (space instanceof StationSpace)
				return -2;
			else
				return -3;
		}
		
	}
}
