package de.unikiel.programmierpraktikum.monopoly.utilities;

public class Utilities {
	public static int randomInt(int min, int max) {
		return min + (int)(Math.random() * ((max - min) + 1));
	}
}
