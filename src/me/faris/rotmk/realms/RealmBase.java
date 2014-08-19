package me.faris.rotmk.realms;

import me.faris.rotmk.Main;
import me.faris.rotmk.helpers.LootBag;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RealmBase implements Iterable<Player> {
    protected String realmName;
    protected int realmID;
    protected World realmWorld;
    protected int maxPlayers;
    private boolean isPortal;

    /**
     * Create a new Portal instance.
     *
     * @param portalName - The portal name.
     * @param portalWorld - The portal's world.
     */
    public RealmBase(String portalName, World portalWorld, int maxPlayers) {
        this.realmID = -1;
        this.realmName = portalName;
        this.realmWorld = portalWorld;
        this.isPortal = true;
        this.maxPlayers = maxPlayers;
    }

    /**
     * Create a new Realm instance.
     *
     * @param realmID - The ID of the Realm.
     * @param realmName - The Realm's name.
     */
    public RealmBase(int realmID, String realmName) {
        this.realmID = realmID;
        this.realmName = realmName;
        this.maxPlayers = 80;
    }

    public void broadcastMessage(String message) {
        List<Player> playerList = this.getPlayers();
        for (Player player : playerList)
            player.sendMessage(ChatColor.YELLOW + message);
    }

    public List<LivingEntity> getEntities() {
        return this.realmWorld.getLivingEntities();
    }

    public int getID() {
        return this.realmID;
    }

    public List<LootBag> getLootBags() {
        List<LootBag> lootBags = new ArrayList<LootBag>();
        for (LootBag lootBag : Main.getInstance().getLootBags()) {
            if (lootBag.getLocation().getWorld().getName().equals(this.realmWorld.getName())) lootBags.add(lootBag);
        }
        return lootBags;
    }

    public final int getMaximumPlayers() {
        if (this.isNexus()) {
            int max = Bukkit.getServer().getMaxPlayers() / (Realm.REALM_LIST.size() - 1);
            if (max < 10) max = 10;
            return max < 50 ? 50 : max;
        } else {
            return this.maxPlayers;
        }
    }

    public String getName() {
        return this.realmName;
    }

    public List<Player> getPlayers() {
        return this.realmWorld.getPlayers();
    }

    public Location getSpawn() {
        return this.realmWorld.getSpawnLocation();
    }

    public World getWorld() {
        return this.realmWorld;
    }

    public List<String> getUsernames() {
        List<Player> playerList = this.getPlayers();
        List<String> usernameList = new ArrayList<String>();
        for (Player player : playerList) {
            if (player != null && player.isOnline()) usernameList.add(player.getName());
        }
        return usernameList;
    }

    public final boolean isNexus() {
        return this.realmName.equals("Nexus");
    }

    public final boolean isPortal() {
        return this.isPortal;
    }

    public final boolean isVault() {
        return this.realmName.equals("Vault");
    }

    public void setSpawn(Location spawnLocation) {
        this.setSpawn((int) spawnLocation.getX(), (int) spawnLocation.getY(), (int) spawnLocation.getZ());
    }

    public void setSpawn(int spawnX, int spawnY, int spawnZ) {
        this.realmWorld.setSpawnLocation(spawnX, spawnY, spawnZ);
    }

    @Override
    public Iterator<Player> iterator() {
        return this.realmWorld.getPlayers().iterator();
    }

    protected Server getServer() {
        return Bukkit.getServer();
    }

}
