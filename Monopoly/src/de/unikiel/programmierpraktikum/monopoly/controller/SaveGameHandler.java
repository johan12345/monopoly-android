package de.unikiel.programmierpraktikum.monopoly.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import de.unikiel.programmierpraktikum.monopoly.view.GameActivity.Status;
import android.content.Context;

public class SaveGameHandler {
	public static class SaveGame {
		private GameController controller;
		private Status status;
		
		public SaveGame(GameController controller, Status status) {
			this.controller = controller;
			this.status = status;
		}

		public GameController getController() {
			return controller;
		}

		public Status getStatus() {
			return status;
		}
	}
	
	public void saveGame(Context context, SaveGame game, String name) {
		try {
			FileOutputStream fos = context.openFileOutput("test.game",
					Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(game.controller);
			oos.writeInt(game.status.ordinal());
			oos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public SaveGame loadGame(Context context, String name) throws IOException, ClassNotFoundException {
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
