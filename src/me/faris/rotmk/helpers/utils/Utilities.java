package me.faris.rotmk.helpers.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

public class Utilities {
	private static Random random = new Random();

	/**
	 * Check if a location is the same as another (in terms of block locations)
	 * @param loc1 - Location 1.
	 * @param loc2 - Location 2.
	 * @return True if the locations are the same, false if not.
	 */
	public static boolean compareLocations(Location loc1, Location loc2) {
		if (loc1 == null || loc2 == null) return false;
		int x1 = (int) loc1.getX(), y1 = (int) loc1.getY(), z1 = (int) loc1.getZ();
		int x2 = (int) loc2.getX(), y2 = (int) loc2.getY(), z2 = (int) loc2.getZ();
		return x1 == x2 && y1 == y2 && z1 == z2;
	}
	
	/**
	 * Convert a location into a string.
	 * @param location - The string in the location format: {world} {x} {y} {z}
	 * @param roundedValues - If the x, y and z values should be rounded.
	 * @param containsYawAndPitch - The location contains a yaw and a pitch.
	 * @return A string with the data of a location.
	 */
	public static String convertLocationToString(Location location, boolean roundedValues, boolean containsYawAndPitch) {
		if (location == null) return "world " + 0D + " " + 50D + " " + 0D + (containsYawAndPitch ? " " + 0F + " " + 0F : "");
		String strLoc = (location.getWorld() != null ? location.getWorld().getName() : "world") + " ";
		if (roundedValues) strLoc += (double) ((int) location.getX()) + " " + (double) ((int) location.getY()) + " " + (double) ((int) location.getZ());
		else strLoc += location.getX() + " " + location.getY() + " " + location.getZ();
		if (containsYawAndPitch) strLoc += " " + location.getPitch() + " " + location.getYaw();
		return strLoc;
	}

	/**
	 * Convert a string into a location.
	 * @param strLocation - The location as a string.
	 * @param containsYawAndPitch - The location contains a yaw and a pitch.
	 * @return A location with data from the string.
	 */
	public static Location convertStringToLocation(String strLocation, boolean containsYawAndPitch) {
		if (strLocation == null) return null;
		try {
			if (strLocation.contains(" ")) {
				String[] locSplit = strLocation.split(" ");
				World w = Bukkit.getWorld(locSplit[0]);
				if (w == null) Bukkit.createWorld(WorldCreator.name(locSplit[0]));
				double x = Double.parseDouble(locSplit[1]), y = Double.parseDouble(locSplit[2]), z = Double.parseDouble(locSplit[3]);
				float yaw = 0F, pitch = 0F;
				if (containsYawAndPitch) {
					yaw = Float.parseFloat(locSplit[4]);
					pitch = Float.parseFloat(locSplit[5]);
				}
				return new Location(w, x, y, z, yaw, pitch);
			}
		} catch (Exception ex) {
		}
		return null;
	}

	/**
	 * Copy a folder from one location to another.
	 * @param src - The source folder.
	 * @param dest - The destination.
	 * @throws java.io.IOException
	 */
	public static void copyFolder(File src, File dest) throws IOException {
		if (src.isDirectory()) {
			if (!dest.exists()) {
				dest.mkdir();
			}

			String files[] = src.list();

			for (String file : files) {
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				copyFolder(srcFile, destFile);
			}

		} else {
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dest);

			byte[] buffer = new byte[1024];

			int length;
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}

			in.close();
			out.close();
		}
	}

	/**
	 * Delete a world.
	 * @param world - The world to delete.
	 */
	public static void deleteWorld(World world) {
		try {
			File worldFolder = world.getWorldFolder();
			Bukkit.getServer().unloadWorld(world, false);
			deleteDirectory(worldFolder);
		} catch (Exception ex) {
		}
	}

	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File files[] = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	public static void feedPlayer(Player player, int amount) {
		if (amount < 0) amount *= -1;
		if (player.getFoodLevel() + amount > 20) player.setFoodLevel(20);
		else player.setFoodLevel(player.getFoodLevel() + amount);
	}

	/**
	 * Get a Random object.
	 * @return The Random instance.
	 */
	public static Random getRandom() {
		return random;
	}

	public static void healPlayer(Player player, int amount) {
		if (amount < 0) amount *= -1;
		if (player.getHealth() + amount > player.getMaxHealth()) player.setHealth(player.getMaxHealth());
		else player.setHealth(player.getHealth() + amount);
	}

	/**
	 * Check whether a String has a numeric value above Integer.MIN_VALUE and above Integer.MAX_VALUE
	 * @param aString - A String
	 * @return True if the String is an integer, false if not.
	 */
	public static boolean isInteger(String aString) {
		try {
			Integer.parseInt(aString);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public static void resetPlayer(Player player) {
		player.setHealth(player.getMaxHealth());
		player.setFoodLevel(20);
	}

}
