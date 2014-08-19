package me.faris.rotmk;

import code.husky.mysql.MySQL;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import lib.simplecache.CachedPlayer;
import lib.simplecache.SimpleCache;
import lib.simplecache.cache.CachedPlayerStats;
import lib.simplecache.exception.UUIDException;
import me.faris.rotmk.helpers.ClassStatistic;
import me.faris.rotmk.helpers.Cuboid;
import me.faris.rotmk.helpers.LootBag;
import me.faris.rotmk.helpers.RotMKClass;
import me.faris.rotmk.helpers.utils.RealmUtilities;
import me.faris.rotmk.helpers.utils.Utilities;
import me.faris.rotmk.listeners.CommandListener;
import me.faris.rotmk.listeners.EventListener;
import me.faris.rotmk.listeners.PlayerListener;
import me.faris.rotmk.realms.Portal;
import me.faris.rotmk.realms.Realm;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import java.sql.Connection;
import java.util.*;

public class Main extends JavaPlugin {
    private static Main pluginInstance;
    public static Permission vaultPermissions = null;
    public static Scoreboard plainScoreboard;
    public static boolean mySQLConnected = false;
    public static int maxBoundary = 500;
    public static int middleBoundary = 200;
    private Settings pluginSettings = new Settings();
    private WorldEditPlugin worldEdit;
    private PlayerListener playerListener;
    private EventListener eventListener;
    private CommandListener cmdListener;
    private MySQL mySQL = null;
    private Connection mySQLConnection = null;

    public List<RotMKClass> rotmkClasses = new ArrayList<RotMKClass>();
    private List<Portal> portalList = new ArrayList<Portal>();
    private Map<String, Location> graveyardLocations = new HashMap<String, Location>();
    private List<LootBag> lootBags = new ArrayList<LootBag>();

    public Map<String, List<UUID>> playerDrops = new HashMap<String, List<UUID>>();

    public void onLoad() {
        pluginInstance = this;
    }

    public void onEnable() {
        plainScoreboard = this.getServer().getScoreboardManager().getNewScoreboard();
        this.setupPermissions();
        this.loadConfiguration();

        this.mySQL = new MySQL(this, this.pluginSettings.getMySQL(1), this.pluginSettings.getMySQL(2), this.pluginSettings.getMySQL(3), this.pluginSettings.getMySQL(4), this.pluginSettings.getMySQL(5));
        this.mySQLConnection = this.mySQL.openConnection();
        SimpleCache.loadCache();
        try {
            mySQLConnected = this.mySQLConnection.isValid(15);
        } catch (Exception ex) {
            mySQLConnected = false;
            ex.printStackTrace();
        }

        // 60 hearts = 300 | 30 hearts = 150 | 1 heart = 5
        this.rotmkClasses.add(new RotMKClass("Rogue", ChatColor.DARK_GREEN, new ClassStatistic(30, 144), new ClassStatistic(100, 250), new ClassStatistic(10, 50), new ClassStatistic(0, 25), new ClassStatistic(15, 75), new ClassStatistic(15, 75)).setIncrementAttack(new ClassStatistic(0, 2)).setIncrementDefense(new ClassStatistic(0, 0)).setIncrementDexterity(new ClassStatistic(1, 2)).setIncrementHealth(new ClassStatistic(4, 6)).setIncrementMagic(new ClassStatistic(2, 8)).setIncrementSpeed(new ClassStatistic(1, 2)));
        this.rotmkClasses.add(new RotMKClass("Archer", ChatColor.GREEN, new ClassStatistic(144, 200), new ClassStatistic(100, 250), new ClassStatistic(10, 50), new ClassStatistic(0, 25), new ClassStatistic(15, 75), new ClassStatistic(15, 75)).setIncrementAttack(new ClassStatistic(1, 2)).setIncrementDefense(new ClassStatistic(0, 0)).setIncrementDexterity(new ClassStatistic(0, 2)).setIncrementHealth(new ClassStatistic(4, 6)).setIncrementMagic(new ClassStatistic(2, 8)).setIncrementSpeed(new ClassStatistic(0, 2)));
        this.rotmkClasses.add(new RotMKClass("Wizard", ChatColor.DARK_PURPLE, new ClassStatistic(20, 134), new ClassStatistic(100, 385), new ClassStatistic(12, 75), new ClassStatistic(0, 25), new ClassStatistic(15, 75), new ClassStatistic(10, 50)).setIncrementAttack(new ClassStatistic(1, 2)).setIncrementDefense(new ClassStatistic(0, 0)).setIncrementDexterity(new ClassStatistic(1, 2)).setIncrementHealth(new ClassStatistic(4, 6)).setIncrementMagic(new ClassStatistic(5, 15)).setIncrementSpeed(new ClassStatistic(0, 2)));
        this.rotmkClasses.add(new RotMKClass("Priest", ChatColor.WHITE, new ClassStatistic(20, 134), new ClassStatistic(100, 385), new ClassStatistic(10, 50), new ClassStatistic(0, 25), new ClassStatistic(12, 50), new ClassStatistic(12, 50)).setIncrementAttack(new ClassStatistic(0, 2)).setIncrementDefense(new ClassStatistic(0, 0)).setIncrementDexterity(new ClassStatistic(0, 2)).setIncrementHealth(new ClassStatistic(4, 6)).setIncrementMagic(new ClassStatistic(5, 15)).setIncrementSpeed(new ClassStatistic(1, 2)));
        this.rotmkClasses.add(new RotMKClass("Warrior", ChatColor.GRAY, new ClassStatistic(40, 144), new ClassStatistic(100, 252), new ClassStatistic(15, 75), new ClassStatistic(0, 25), new ClassStatistic(10, 50), new ClassStatistic(7, 50)).setIncrementAttack(new ClassStatistic(1, 2)).setIncrementDefense(new ClassStatistic(0, 0)).setIncrementDexterity(new ClassStatistic(0, 2)).setIncrementHealth(new ClassStatistic(4, 6)).setIncrementMagic(new ClassStatistic(2, 8)).setIncrementSpeed(new ClassStatistic(0, 2)));
        // TODO: Continue adding classes, converting them into the new format (ClassStatistic)
        /**
         this.rotmkClasses.add(new RotMKClass("Knight", ChatColor.DARK_GRAY, 0, 0, 30, 0, 0));
         this.rotmkClasses.add(new RotMKClass("Paladin", ChatColor.YELLOW, 0, 0, 30, 0, 0));
         this.rotmkClasses.add(new RotMKClass("Assassin", ChatColor.DARK_BLUE, 0, 0, 30, 0, 0));
         this.rotmkClasses.add(new RotMKClass("Necromancer", ChatColor.DARK_PURPLE, 0, 0, 30, 0, 0));
         this.rotmkClasses.add(new RotMKClass("Huntress", ChatColor.DARK_PURPLE, 0, 0, 30, 0, 0));
         this.rotmkClasses.add(new RotMKClass("Mystic", ChatColor.LIGHT_PURPLE, 0, 0, 30, 0, 0));
         this.rotmkClasses.add(new RotMKClass("Trickster", ChatColor.GRAY, 0, 0, 30, 0, 0));
         this.rotmkClasses.add(new RotMKClass("Sorcerer", ChatColor.RED, 0, 0, 30, 0, 0));
         this.rotmkClasses.add(new RotMKClass("Ninja", ChatColor.DARK_GRAY, 0, 0, 30, 0, 0));
         **/

        this.cmdListener = new CommandListener();
        this.playerListener = new PlayerListener();
        this.eventListener = new EventListener();
        this.getServer().getPluginManager().registerEvents(this.playerListener, this);
        this.getServer().getPluginManager().registerEvents(this.eventListener, this);

        this.getCommand("rotmk").setExecutor(new CommandListener());

        for (Realm realm : Realm.REALM_LIST) {
            realm.initRealm();
            try {
                this.getServer().getPluginManager().addPermission(new org.bukkit.permissions.Permission("rotmk.realms." + realm.getName().toLowerCase().replaceAll(" ", "_")));
            } catch (Exception ex) {
                continue;
            }
        }
        for (RotMKClass rotmkClass : this.rotmkClasses) {
            try {
                this.getServer().getPluginManager().addPermission(new org.bukkit.permissions.Permission("rotmk.class." + rotmkClass.getName().toLowerCase()));
            } catch (Exception ex) {
                continue;
            }
        }

        if (!mySQLConnected) {
            this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Error: Could not connect to the MySQL database! Shutting server for safety reasons...");
            this.getServer().getScheduler().runTaskLater(this, new Runnable() {
                public void run() {
                    getServer().shutdown();
                }
            }, 5L);
            return;
        }

        this.getServer().getScheduler().runTaskTimer(this, new Runnable() {
            public void run() {
                if (Realm.REALM_NEXUS.getWorld() != null) {
                    Location nexusSpawn = Realm.REALM_NEXUS.getSpawn();
                    for (Player player : Realm.REALM_NEXUS) {
                        if (player.getLocation().toVector().distanceSquared(nexusSpawn.toVector()) <= 20F) {
                            if (player.getHealth() < player.getMaxHealth())
                                Utilities.healPlayer(player, Utilities.getRandom().nextInt(7));
                            if (player.getFoodLevel() != 20) Utilities.feedPlayer(player, 6);
                        }
                    }
                }
            }
        }, 0L, 40L); // Heal all players in a 20 block radius from the nexus spawn.

        this.getServer().getScheduler().runTaskTimer(this, new Runnable() {
            public void run() {
                Collection<Location> graveyards = getGraveyards();
                for (Location graveyardLocation : graveyards) {
                    if (graveyardLocation.getBlock().getBiome().toString().toLowerCase().startsWith("cold_") && graveyardLocation.getBlock().getLocation().subtract(0, 1, 0).getBlock().getType() == Material.GRASS)
                        graveyardLocation.getBlock().setType(Material.SNOW);
                    else graveyardLocation.getBlock().setType(Material.AIR);
                }
                graveyardLocations.clear();
            }
        }, 0L, 600L); // Remove all the graveyards every 30 seconds.

        this.worldEdit = (WorldEditPlugin) this.getServer().getPluginManager().getPlugin("WorldEdit");
    }

    @SuppressWarnings("deprecation")
    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);
        for (Player player : this.getServer().getOnlinePlayers()) {
            RealmUtilities.teleportToNexus(player);
            player.setScoreboard(plainScoreboard);
            player.kickPlayer(ChatColor.RED + "The Realm of the Mad King plugin has been disabled.\nThis may be due to a restart, please try joining soon!");
        }

        for (LootBag lootBag : this.lootBags) {
            if (lootBag.getBlock().getType() == Material.CHEST) {
                lootBag.getInventory().clear();
                lootBag.getBlock().setType(lootBag.getOldBlockType());
                lootBag.getBlock().setData(lootBag.getOldBlockData());
                lootBag.getLocation().add(0, 1, 0).getBlock().setType(lootBag.getOldBlockTopType());
                lootBag.getLocation().add(0, 1, 0).getBlock().setData(lootBag.getOldBlockTopData());
            }
        }
        this.lootBags.clear();
        this.lootBags = null;

        Collection<Location> graveyards = getGraveyards();
        for (Location graveyardLocation : graveyards) {
            graveyardLocation.getBlock().setType(Material.AIR);
        }
        this.graveyardLocations.clear();
        this.graveyardLocations = null;

        for (Realm realm : Realm.REALM_LIST) {
            for (LivingEntity livingEntity : realm.getEntities()) {
                if (!(livingEntity instanceof Player)) {
                    livingEntity.setHealth(0);
                    livingEntity.remove();
                }
            }
            for (Item item : realm.getWorld().getEntitiesByClass(Item.class)) {
                item.remove();
            }
        }

        for (World pluginWorld : this.getServer().getWorlds()) {
            for (Item itemStack : pluginWorld.getEntitiesByClass(Item.class)) {
                for (List<UUID> itemUUIDs : this.playerDrops.values()) {
                    for (UUID itemUUID : itemUUIDs) {
                        if (itemStack.getUniqueId().equals(itemUUID)) {
                            itemStack.remove();
                        }
                    }
                }
            }
        }
        this.playerDrops.clear();
        this.playerDrops = null;

        for (Portal portal : this.portalList)
            portal.recreateWorld();
        this.portalList.clear();
        this.portalList = null;

        this.rotmkClasses.clear();

        this.cmdListener = null;
        this.eventListener = null;
        this.mySQL = null;
        this.mySQLConnection = null;
        this.rotmkClasses = null;
        this.worldEdit = null;
        this.pluginSettings = null;
        plainScoreboard = null;
        vaultPermissions = null;

        Realm.REALM_LIST.clear();
        pluginInstance = null;
    }

    public void loadConfiguration() {
        this.getConfig().options().header("Realm of the Mad King configuration");
        this.getConfig().addDefault("MySQL.Host", "localhost");
        this.getConfig().addDefault("MySQL.Port", 3306);
        this.getConfig().addDefault("MySQL.Database", "database");
        this.getConfig().addDefault("MySQL.Username", "root");
        this.getConfig().addDefault("MySQL.Password", "password");
        this.getConfig().addDefault("Realm boundary", 500);
        this.getConfig().addDefault("Middle boundary", 200);
        this.getConfig().addDefault("Class cost", 250);
        this.getConfig().addDefault("Exp share radius", 6D);
        String defaultPortal = new Cuboid(this.getServer().getWorlds().get(0).getSpawnLocation()).toString();
        this.getConfig().addDefault("Portals.Portal 1", defaultPortal);
        this.getConfig().options().copyDefaults(true);
        this.getConfig().options().copyHeader(true);
        this.saveConfig();

        maxBoundary = this.getConfig().getInt("Realm boundary", 500);
        middleBoundary = this.getConfig().getInt("Middle boundary", 200);

        this.pluginSettings.setMySQL(1, this.getConfig().getString("MySQL.Host", "localhost"));
        this.pluginSettings.setMySQL(2, String.valueOf(this.getConfig().getInt("MySQL.Port", 3306)));
        this.pluginSettings.setMySQL(3, this.getConfig().getString("MySQL.Database", "database"));
        this.pluginSettings.setMySQL(4, this.getConfig().getString("MySQL.Username", "root"));
        this.pluginSettings.setMySQL(5, this.getConfig().getString("MySQL.Password", "password"));

        this.pluginSettings.setClassCost(this.getConfig().getInt("Class cost", 250));
        this.pluginSettings.setExpShareRadius(this.getConfig().getDouble("Exp share radius", 6D));

        Set<String> portalMap = this.getConfig().getConfigurationSection("Portals").getKeys(false);
        for (String portal : portalMap) {
            try {
                String strRealmID = portal.split(" ")[1];
                if (Utilities.isInteger(strRealmID)) {
                    this.pluginSettings.setPortal(RealmUtilities.getRealm(Integer.parseInt(strRealmID)), Cuboid.deserialize(this.getConfig().getString("Portals." + portal, defaultPortal)));
                }
            } catch (Exception ex) {
                continue;
            }
        }
    }

    public CommandListener getCommandListener() {
        return this.cmdListener;
    }

    public EventListener getEventListener() {
        return this.eventListener;
    }

    public PlayerListener getPlayerListener() {
        return this.playerListener;
    }

    public void addLootBag(LootBag lootBag) {
        this.lootBags.add(lootBag);
    }

    public RotMKClass getClass(String playerName) {
        String className = this.getClassName(playerName);
        for (RotMKClass rotmkClass : Main.getInstance().rotmkClasses) {
            if (rotmkClass.getName().equals(className)) return rotmkClass;
        }
        return null;
    }

    public RotMKClass getClass(UUID playerUUID) {
        for (Player onlinePlayer : this.getServer().getOnlinePlayers()) {
            if (onlinePlayer.getUniqueId().compareTo(playerUUID) == 0) {
                String className = this.getClassName(onlinePlayer.getName());
                for (RotMKClass rotmkClass : Main.getInstance().rotmkClasses) {
                    if (rotmkClass.getName().equals(className)) return rotmkClass;
                }
            }
        }
        return null;
    }

    public RotMKClass getClassByName(String className) {
        for (RotMKClass rotmkClass : Main.getInstance().rotmkClasses) {
            if (rotmkClass.getName().equals(className)) return rotmkClass;
        }
        return null;
    }

    public String getClassName(String playerName) {
        try {
            return SimpleCache.getCachedPlayer(playerName).getClassName();
        } catch (UUIDException ex) {
            ex.printStackTrace();
            return "None";
        }
    }

    public Collection<Location> getGraveyards() {
        return this.graveyardLocations.values();
    }

    public List<LootBag> getLootBags() {
        return this.lootBags;
    }

    public List<Portal> getPortalList() {
        return this.portalList;
    }

    public Settings getSettings() {
        return this.pluginSettings;
    }

    public WorldEditPlugin getWorldEdit() {
        return this.worldEdit;
    }

    public boolean hasClass(String playerName) {
        try {
            CachedPlayer cachedPlayer = SimpleCache.getCachedPlayer(playerName);
            if (cachedPlayer.getClassName() != null)
                return !SimpleCache.getCachedPlayer(playerName).getClassName().equals(CachedPlayerStats.DEFAULT_CLASS);
            else return false;
        } catch (UUIDException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean isClass(String className) {
        for (RotMKClass rotmkClass : this.rotmkClasses) {
            if (rotmkClass.getName().equalsIgnoreCase(className)) return true;
        }
        return false;
    }

    public boolean isLootBag(LootBag lootBag) {
        for (LootBag lootBag2 : this.lootBags) {
            if (Utilities.compareLocations(lootBag.getLocation(), lootBag2.getLocation())) return true;
        }
        return false;
    }

    public boolean isLootBag(Location lootBagLocation) {
        for (LootBag lootBag : this.lootBags) {
            if (Utilities.compareLocations(lootBagLocation, lootBag.getLocation())) return true;
        }
        return false;
    }

    public void removeLootBag(LootBag lootBag) {
        if (this.lootBags.contains(lootBag)) {
            this.lootBags.remove(lootBag);
        } else {
            for (int i = 0; i < this.lootBags.size(); i++) {
                if (Utilities.compareLocations(lootBag.getLocation(), this.lootBags.get(i).getLocation())) {
                    this.lootBags.remove(i);
                    break;
                }
            }
        }
    }

    public void removeLootBag(Location lootBagLocation) {
        for (LootBag lootBag : this.lootBags) {
            if (Utilities.compareLocations(lootBagLocation, lootBag.getLocation())) this.lootBags.remove(lootBag);
        }
    }

    public void removeClass(String playerName) {
        try {
            SimpleCache.getCachedPlayer(playerName).setClass("None");
        } catch (UUIDException ex) {
            ex.printStackTrace();
        }
    }

    public void resetStats(String playerName) {
        try {
            SimpleCache.getCachedPlayer(playerName).setAttack(0);
            SimpleCache.getCachedPlayer(playerName).setDefense(0);
            SimpleCache.getCachedPlayer(playerName).setDexterity(0);
            SimpleCache.getCachedPlayer(playerName).setMagic(0);
            SimpleCache.getCachedPlayer(playerName).setSpeed(0);
        } catch (UUIDException ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    public boolean setClass(String playerName, String className) {
        for (RotMKClass rotmkClass : this.rotmkClasses) {
            if (rotmkClass.getName().equalsIgnoreCase(className)) {
                className = rotmkClass.getName();
                try {
                    SimpleCache.getCachedPlayer(playerName).setClass(className);
                } catch (UUIDException ex) {
                    ex.printStackTrace();
                    if (Bukkit.getPlayerExact(playerName) != null)
                        Bukkit.getPlayerExact(playerName).kickPlayer(ChatColor.RED + "An error occurred!");
                }
                return true;
            }
        }
        return false;
    }

    public static Main getInstance() {
        return pluginInstance;
    }

    public MySQL getMySQL() {
        return this.mySQL;
    }

    public Connection getMySQLConnection() {
        return this.mySQLConnection;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) vaultPermissions = permissionProvider.getProvider();
        return (vaultPermissions != null);
    }

    // TODO: Make a method that converts a normal entity name into a custom entity name. E.g. Wither as the Ghost.
    // TODO: Make tiers for items and detect in PlayerListener.
    // TODO: Add increment values for class.
    // TODO: Make stats for class do something. E.g. Attack increases attack.
}
