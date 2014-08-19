package me.faris.rotmk.helpers.utils;

import java.util.Random;

import me.faris.rotmk.Main;
import me.faris.rotmk.realms.Portal;
import me.faris.rotmk.realms.Realm;
import me.faris.rotmk.realms.RealmBase;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class RealmUtilities {

	public static Portal getPortal(String worldName) {
		if (worldName != null) {
			for (Portal realmPortal : Main.getInstance().getPortalList()) {
				if (realmPortal.getWorld() != null && realmPortal.getWorld().getName().equals(worldName)) return realmPortal;
			}
		}
		return null;
	}

	public static int getRandomSpawn() {
		Random random = Utilities.getRandom();
		int randomPosition = random.nextInt(Main.maxBoundary - 5);
		int boundary = (Main.maxBoundary / 4) * 3;
		int attempts = 0;
		while (randomPosition <= boundary) {
			if (attempts < 5) {
				randomPosition = random.nextInt(Main.maxBoundary - 5);
				int attempts2 = 0;
				while (randomPosition <= Main.middleBoundary) {
					if (attempts2 < 5) {
						randomPosition = random.nextInt(Main.maxBoundary - 5);
						attempts2++;
						break;
					}
				}
				attempts++;
			} else {
				return Math.random() > 0.5 ? -400 : 400;
			}
		}
		while (randomPosition <= Main.middleBoundary) {
			if (attempts < 5) {
				randomPosition = random.nextInt(Main.maxBoundary - 5);
				int attempts2 = 0;
				while (randomPosition <= boundary) {
					if (attempts2 < 5) {
						randomPosition = random.nextInt(Main.maxBoundary - 5);
						attempts2++;
						break;
					}
				}
				attempts++;
			} else {
				return Math.random() > 0.5 ? -400 : 400;
			}
		}
		if (Math.random() > 0.5) randomPosition *= -1;
		return randomPosition;
	}

	public static Realm getRealm(int realmID) {
		for (Realm realm : Realm.REALM_LIST) {
			if (realm.getID() == realmID) return realm;
		}
		return Realm.REALM_NEXUS;
	}

	public static Realm getRealm(Player player) {
		for (Realm realm : Realm.REALM_LIST) {
			if (realm.getUsernames().contains(player.getName())) return realm;
		}
		return Realm.REALM_NEXUS;
	}

	public static Realm getRealm(World world) {
		if (world != null) {
			for (Realm realm : Realm.REALM_LIST) {
				if (realm.getWorld() != null && realm.getWorld().getName().equals(world.getName())) return realm;
			}
		}
		return null;
	}

	public static Realm getRealRealm(Player player) {
		for (Realm realm : Realm.REALM_LIST) {
			if (realm.getUsernames().contains(player.getName())) return realm;
		}
		return null;
	}

	public static boolean inPortal(Player player) {
		for (Portal portal : Main.getInstance().getPortalList()) {
			if (portal.getUsernames().contains(player.getName())) return true;
		}
		return false;
	}

	public static boolean inRealm(Player player) {
		return !inPortal(player) && getRealm(player) != Realm.REALM_NEXUS;
	}

	public static boolean inRealRealm(Player player) {
		return !inPortal(player) && getRealm(player) != null;
	}

	public static boolean inRealm(RealmBase realm, Player player) {
		return realm.getUsernames().contains(player.getName());
	}

	public static boolean isPortal(String worldName) {
		return getPortal(worldName) != null;
	}

	public static boolean isRealm(World world) {
		return getRealm(world) != null;
	}

	public static void joinRealm(Realm realm, Player player) {
		if (!inRealm(realm, player)) {
			if (player.teleport(realm.getSpawn(), TeleportCause.PLUGIN)) player.sendMessage(ChatColor.RED + "Connected to " + realm.getName() + "!");
			else player.sendMessage(ChatColor.RED + "Error! Could not teleport to " + realm.getName() + "'s spawn.");
		} else {
			player.sendMessage(ChatColor.RED + "You are already connected to that server! (" + realm.getName() + ")");
		}
	}

	public static void teleportToNexus(Player player) {
		if (!inRealm(Realm.REALM_NEXUS, player)) {
			player.teleport(Realm.REALM_NEXUS.getSpawn(), TeleportCause.PLUGIN);
		}
	}
}
