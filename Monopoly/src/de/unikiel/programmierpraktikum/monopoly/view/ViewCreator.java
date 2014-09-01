package de.unikiel.programmierpraktikum.monopoly.view;

import java.text.DecimalFormat;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.unikiel.programmierpraktikum.monopoly.R;
import de.unikiel.programmierpraktikum.monopoly.model.ChanceChanceSpace;
import de.unikiel.programmierpraktikum.monopoly.model.ChanceSpace;
import de.unikiel.programmierpraktikum.monopoly.model.CommunityChanceSpace;
import de.unikiel.programmierpraktikum.monopoly.model.FreeParkingSpace;
import de.unikiel.programmierpraktikum.monopoly.model.GoSpace;
import de.unikiel.programmierpraktikum.monopoly.model.GoToJailSpace;
import de.unikiel.programmierpraktikum.monopoly.model.JailSpace;
import de.unikiel.programmierpraktikum.monopoly.model.PaySpace;
import de.unikiel.programmierpraktikum.monopoly.model.Space;
import de.unikiel.programmierpraktikum.monopoly.model.StationSpace;
import de.unikiel.programmierpraktikum.monopoly.model.StreetSpace;
import de.unikiel.programmierpraktikum.monopoly.model.UtilitySpace;
import de.unikiel.programmierpraktikum.monopoly.utilities.Utilities;

/**
 * @author johan_000
 * Generiert Views aus Spielobjekten
 */
public class ViewCreator {
	private LayoutInflater inflater;
	
	public ViewCreator(Activity activity) {
		this.inflater = activity.getLayoutInflater();
	}
	
	public View createSpaceView(Space space, ViewGroup container) {
		DecimalFormat format = new DecimalFormat("#,##0 eV");
		
		View spaceView = null;
		if (space instanceof GoSpace) {
			spaceView = inflater.inflate(R.layout.layout_go_space, container,
					false);
		} else if (space instanceof ChanceSpace) {
			if (space instanceof ChanceChanceSpace) {
				spaceView = inflater.inflate(
						R.layout.layout_chance_chance_space, container, false);
			} else if (space instanceof CommunityChanceSpace) {
				spaceView = inflater.inflate(
						R.layout.layout_community_chance_space, container,
						false);
			}
		} else if (space instanceof FreeParkingSpace) {
			spaceView = inflater.inflate(
					R.layout.layout_free_parking_space, container, false);
		} else if (space instanceof GoToJailSpace) {
			spaceView = inflater.inflate(R.layout.layout_go_to_jail_space,
					container, false);
		} else if (space instanceof JailSpace) {
			spaceView = inflater.inflate(R.layout.layout_jail_space,
					container, false);
		} else if (space instanceof PaySpace) {
			PaySpace pay = (PaySpace) space;
			spaceView = inflater.inflate(R.layout.layout_pay_space, container,
					false);

			TextView name = (TextView) spaceView.findViewById(R.id.txtName);
			name.setText(pay.getName());

			TextView price = (TextView) spaceView
					.findViewById(R.id.txtPrice);
			price.setText(format.format(pay.getAmount()));
		} else if (space instanceof StationSpace) {
			StationSpace station = (StationSpace) space;
			spaceView = inflater.inflate(R.layout.layout_station_space,
					container, false);

			TextView name = (TextView) spaceView.findViewById(R.id.txtName);
			name.setText(station.getName());

			TextView price = (TextView) spaceView
					.findViewById(R.id.txtPrice);
			price.setText(format.format(station.getPurchasePrice()));
		} else if (space instanceof UtilitySpace) {
			UtilitySpace utility = (UtilitySpace) space;
			spaceView = inflater.inflate(R.layout.layout_utility_space,
					container, false);

			TextView name = (TextView) spaceView.findViewById(R.id.txtName);
			name.setText(utility.getName());

			TextView price = (TextView) spaceView
					.findViewById(R.id.txtPrice);
			price.setText(format.format(utility.getPurchasePrice()));
		} else if (space instanceof StreetSpace) {
			StreetSpace street = (StreetSpace) space;
			spaceView = inflater.inflate(R.layout.layout_street_space,
					container, false);

			TextView name = (TextView) spaceView.findViewById(R.id.txtName);
			name.setText(street.getName());

			TextView price = (TextView) spaceView
					.findViewById(R.id.txtPrice);
			price.setText(format.format(street.getPurchasePrice()));

			View category = spaceView.findViewById(R.id.category);
			category.setBackgroundResource(Utilities
					.getCategoryColor(street.getCategory()));
		}
		return spaceView;
	}
}
