package de.unikiel.programmierpraktikum.monopoly.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import de.unikiel.programmierpraktikum.monopoly.view.GameActivity.Status;

/**
 * Contains static methods to save and load Monopoly games.
 * 
 * @author Miriam Scharnke, Johan v. Forstner
 *
 */
public class SaveGameHandler {
	/**
	 * Represents a game that should be saved or was loaded from a file.
	 * Contains a {@link GameController} and a {@link Status}.
	 * 
	 * @author Miriam Scharnke, Johan v. Forstner
	 *
	 */
	public static class SaveGame {
		private GameController controller;
		private Status status;

		public SaveGame(GameController controller, Status status) {
			this.controller = controller;
			this.status = status;
		}

		/**
		 * @return the controller
		 */
		public GameController getController() {
			return controller;
		}

		/**
		 * @return the status
		 */
		public Status getStatus() {
			return status;
		}
	}

	/**
	 * Save a Monopoly game to a file.
	 * 
	 * @param context A Context, needed to save a file
	 * @param game The {@link SaveGame} to save
	 * @param name The file name to save this game under
	 * @throws IOException When there is an error saving the game
	 */
	public static void saveGame(Context context, SaveGame game, String name) throws IOException {
		FileOutputStream fos = context.openFileOutput(name,
				Context.MODE_PRIVATE);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(game.controller);
		oos.writeInt(game.status.ordinal());
		oos.close();
		fos.close();
	}

	/**
	 * Load a game from a file
	 * 
	 * @param context A Context, needed to read a file
	 * @param name The file name from which the game should be loaded
	 * @return A {@link SaveGame}, representing the loaded game
	 * @throws IOException When there is an error while loading the game
	 * @throws ClassNotFoundException When there is an error while loading the game
	 */
	public static SaveGame loadGame(Context context, String name)
			throws IOException, ClassNotFoundException {
		File file = new File(context.getFilesDir(), name);
		if (file.exists()) {
			FileInputStream fis;
			fis = context.openFileInput(name);
			ObjectInputStream ois = new ObjectInputStream(fis);
			GameController controller = (GameController) ois.readObject();
			Status status = Status.values()[ois.readInt()];
			ois.close();
			fis.close();
			return new SaveGame(controller, status);
		}
		return null;
	}
}
