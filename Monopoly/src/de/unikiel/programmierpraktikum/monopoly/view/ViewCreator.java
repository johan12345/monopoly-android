package de.unikiel.programmierpraktikum.monopoly.view;

import java.text.DecimalFormat;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import de.unikiel.programmierpraktikum.monopoly.R;
import de.unikiel.programmierpraktikum.monopoly.model.BuyableSpace;
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
 * @author johan_000 Generiert Views aus Spielobjekten
 */
public class ViewCreator {
	private LayoutInflater inflater;

	public ViewCreator(Activity activity) {
		this.inflater = activity.getLayoutInflater();
	}

	/**
	 * Refresh all the texts and images shown on a {@link View} returned by
	 * {@link createSpaceView}
	 * 
	 * @param space
	 *            the {@link Space} to use for all the values
	 * @param spaceView
	 *            a view originally returned by {@link createSpaceView}
	 */
	public void refreshSpaceView(Space space, View spaceView) {
		DecimalFormat format = Utilities.moneyFormat();
		if (space instanceof GoSpace) {

		} else if (space instanceof ChanceSpace) {
			if (space instanceof ChanceChanceSpace) {

			} else if (space instanceof CommunityChanceSpace) {

			}
		} else if (space instanceof FreeParkingSpace) {

		} else if (space instanceof GoToJailSpace) {

		} else if (space instanceof JailSpace) {

		} else if (space instanceof PaySpace) {
			PaySpace pay = (PaySpace) space;

			TextView name = (TextView) spaceView.findViewById(R.id.txtName);
			name.setText(pay.getName());

			TextView price = (TextView) spaceView.findViewById(R.id.txtPrice);
			price.setText(format.format(pay.getAmount()));
		} else if (space instanceof StationSpace) {
			StationSpace station = (StationSpace) space;

			TextView name = (TextView) spaceView.findViewById(R.id.txtName);
			name.setText(station.getName());

			TextView price = (TextView) spaceView.findViewById(R.id.txtPrice);
			price.setText(format.format(station.getPurchasePrice()));

			setBuyableSpaceOwnerImage((BuyableSpace) space, spaceView);
		} else if (space instanceof UtilitySpace) {
			UtilitySpace utility = (UtilitySpace) space;

			TextView name = (TextView) spaceView.findViewById(R.id.txtName);
			name.setText(utility.getName());

			TextView price = (TextView) spaceView.findViewById(R.id.txtPrice);
			price.setText(format.format(utility.getPurchasePrice()));

			setBuyableSpaceOwnerImage((BuyableSpace) space, spaceView);
		} else if (space instanceof StreetSpace) {
			StreetSpace street = (StreetSpace) space;

			TextView name = (TextView) spaceView.findViewById(R.id.txtName);
			name.setText(street.getName());

			TextView price = (TextView) spaceView.findViewById(R.id.txtPrice);
			price.setText(format.format(street.getPurchasePrice()));

			View category = spaceView.findViewById(R.id.category);
			category.setBackgroundResource(Utilities.getCategoryColor(street
					.getCategory()));

			setBuyableSpaceOwnerImage((BuyableSpace) space, spaceView);

			ImageView house1 = (ImageView) spaceView
					.findViewById(R.id.imgHouse1);
			ImageView house2 = (ImageView) spaceView
					.findViewById(R.id.imgHouse2);
			ImageView house3 = (ImageView) spaceView
					.findViewById(R.id.imgHouse3);
			ImageView house4 = (ImageView) spaceView
					.findViewById(R.id.imgHouse4);
			ImageView hotel = (ImageView) spaceView.findViewById(R.id.imgHotel);

			house1.setVisibility(houseVisibility(street, 1));
			house2.setVisibility(houseVisibility(street, 2));
			house3.setVisibility(houseVisibility(street, 3));
			house4.setVisibility(houseVisibility(street, 4));
			hotel.setVisibility(hotelVisibility(street));
		}
	}

	/**
	 * Checks if a space has a hotel and returns the visibility of the
	 * corresponding ImageView.
	 * 
	 * @param space
	 *            the space to use
	 * @return {@link View#VISIBLE} or {@link View#INVISIBLE}
	 */
	private int hotelVisibility(StreetSpace space) {
		return Utilities.visibility(space.getHotelCount() == 1);
	}

	/**
	 * Checks if a space has a minimum number of houses and returns the
	 * visibility of the corresponding ImageView.
	 * 
	 * @param space
	 *            the space to use
	 * @param num
	 *            the minimum house number
	 * @return {@link View#VISIBLE} or {@link View#INVISIBLE}
	 */
	private int houseVisibility(StreetSpace space, int num) {
		return Utilities.visibility(space.getRealHousesCount() >= num);
	}

	/**
	 * Creates a View representing a space on the field.
	 * 
	 * @param space
	 *            the Space to use
	 * @param container
	 *            a {@link ViewGroup} where the View will be attached later
	 *            (needed to calculate the right LayoutParams)
	 * @return a View representing the given Space
	 */
	public View createSpaceView(Space space, ViewGroup container) {
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
			spaceView = inflater.inflate(R.layout.layout_free_parking_space,
					container, false);
		} else if (space instanceof GoToJailSpace) {
			spaceView = inflater.inflate(R.layout.layout_go_to_jail_space,
					container, false);
		} else if (space instanceof JailSpace) {
			spaceView = inflater.inflate(R.layout.layout_jail_space, container,
					false);
		} else if (space instanceof PaySpace) {
			spaceView = inflater.inflate(R.layout.layout_pay_space, container,
					false);
		} else if (space instanceof StationSpace) {
			spaceView = inflater.inflate(R.layout.layout_station_space,
					container, false);
		} else if (space instanceof UtilitySpace) {
			spaceView = inflater.inflate(R.layout.layout_utility_space,
					container, false);
		} else if (space instanceof StreetSpace) {
			spaceView = inflater.inflate(R.layout.layout_street_space,
					container, false);
		}
		refreshSpaceView(space, spaceView);
		return spaceView;
	}

	/**
	 * Puts an image of the owner's peg on the View of a {@link BuyableSpace}
	 * 
	 * @param space
	 *            a {@link BuyableSpace}
	 * @param spaceView
	 *            the View corresponding to this space
	 */
	private void setBuyableSpaceOwnerImage(BuyableSpace space, View spaceView) {
		ImageView owner = (ImageView) spaceView.findViewById(R.id.owner);
		ImageView ownerBg = (ImageView) spaceView.findViewById(R.id.owner_bg);
		if (space.getOwner() != null) {
			owner.setImageResource(Utilities.getPegDrawable(space.getOwner()
					.getPeg()));
			ownerBg.setImageResource(Utilities.getCircleDrawable(space
					.getOwner().getIndex()));
		}
	}
}
