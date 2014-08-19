package me.faris.rotmk.realms;

import me.faris.rotmk.Main;
import me.faris.rotmk.helpers.LootBag;
import me.faris.rotmk.helpers.utils.Utilities;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.util.ArrayList;
import java.util.List;

public class Portal extends RealmBase {
    private World portalWorld = null;
    private Location portalDoor = null;

    /**
     * Create a new Portal instance.
     * Note: The maximum players that can join is 15 by default. Use the other constructor to input a maximum players integer.
     *
     * @param realmWorld - The realm the portal door is in.
     * @param portalName - The name of the portal.
     * @param portalDoor - The door to enter this portal.
     * @param portalWorld - The portal's world.
     */
    public Portal(World realmWorld, String portalName, Location portalDoor, World portalWorld) {
        super(portalName, realmWorld, 15);
        this.portalWorld = portalWorld;
        this.portalDoor = portalDoor;
    }

    /**
     * Create a new Portal instance.
     *
     * @param realmWorld - The realm the portal door is in.
     * @param portalName - The name of the portal.
     * @param portalDoor - The door to enter this portal.
     * @param portalWorld - The portal's world.
     * @param maxPlayers - The maximum players that can join. (By default: 15)
     */
    public Portal(World realmWorld, String portalName, Location portalDoor, World portalWorld, int maxPlayers) {
        super(portalName, realmWorld, maxPlayers >= 5 ? maxPlayers : 5);
        this.portalWorld = portalWorld;
        this.portalDoor = portalDoor;
    }

    public Location getDoor() {
        return this.portalDoor;
    }

    @Override
    public List<LootBag> getLootBags() {
        List<LootBag> lootBags = new ArrayList<LootBag>();
        for (LootBag lootBag : Main.getInstance().getLootBags()) {
            if (lootBag.getLocation().getWorld().getName().equals(this.portalWorld.getName())) lootBags.add(lootBag);
        }
        return lootBags;
    }

    @Override
    public List<Player> getPlayers() {
        return this.portalWorld.getPlayers();
    }

    public Location getPortalSpawn() {
        return this.portalWorld.getSpawnLocation();
    }

    public World getPortalWorld() {
        return this.portalWorld;
    }

    public boolean recreateWorld() {
        List<Player> playerList = this.getPlayers();
        for (Player player : playerList) {
            player.teleport(this.realmWorld.getSpawnLocation(), TeleportCause.PLUGIN);
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
        }
        this.portalDoor.getBlock().setType(Material.AIR);
        Utilities.deleteWorld(this.portalWorld);
        return true;
    }

    public void setSpawn(int spawnX, int spawnY, int spawnZ) {
        this.portalWorld.setSpawnLocation(spawnX, spawnY, spawnZ);
    }

}
