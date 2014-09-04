package de.unikiel.programmierpraktikum.monopoly.view;

import de.unikiel.programmierpraktikum.monopoly.controller.GameController;
import android.app.Application;

public class MonopolyApplication extends Application {
	private GameController gameController;
	
	@Override
	public void onCreate() {
		super.onCreate();
	}

	/**
	 * @return the gameController
	 */
	public GameController getGameController() {
		return gameController;
	}

	/**
	 * @param gameController the gameController to set
	 */
	public void setGameController(GameController gameController) {
		this.gameController = gameController;
	}
	
	
}
