package me.faris.rotmk.listeners;

import lib.simplecache.CachedPlayer;
import lib.simplecache.SimpleCache;
import lib.simplecache.exception.UUIDException;
import me.faris.rotmk.Main;
import me.faris.rotmk.helpers.Cuboid;
import me.faris.rotmk.helpers.LootBag;
import me.faris.rotmk.helpers.PlayerStatistics;
import me.faris.rotmk.helpers.RotMKClass;
import me.faris.rotmk.helpers.gui.ClassGUI;
import me.faris.rotmk.helpers.gui.DeathGUI;
import me.faris.rotmk.helpers.utils.RealmUtilities;
import me.faris.rotmk.helpers.utils.Utilities;
import me.faris.rotmk.realms.Portal;
import me.faris.rotmk.realms.Realm;
import me.faris.rotmk.realms.RealmBase;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

public class PlayerListener implements Listener {

    /**
     * Called when a player logs in *
     */
    @EventHandler
    public void onPlayerJoin(PlayerLoginEvent event) {
        try {
            if (!Main.mySQLConnected)
                event.disallow(Result.KICK_OTHER, ChatColor.RED + "There was an error when setting up MySQL.\nPlease contact the server owner as fast as you can until resolved.");
        } catch (Exception ex) {
        }
    }

    /**
     * Called when a player joins *
     */
    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        try {
            event.getPlayer().setAllowFlight(true);
            RealmUtilities.teleportToNexus(event.getPlayer());
            if (!this.getPlugin().hasClass(event.getPlayer().getName())) {
                final Player player = event.getPlayer();
                player.getServer().getScheduler().runTaskLater(this.getPlugin(), new Runnable() {
                    public void run() {
                        if (player != null && player.isOnline()) {
                            if (!getPlugin().hasClass(player.getName()) && !ClassGUI.classPlayers.containsKey(player.getName())) {
                                ClassGUI classGUI = new ClassGUI(player);
                                classGUI.openInventory();
                            }
                        }
                    }
                }, 1L);
            }

            Scoreboard playerScoreboard = event.getPlayer().getServer().getScoreboardManager().getNewScoreboard();
            Objective statsObjective = playerScoreboard.registerNewObjective("stats", "dummy");
            statsObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
            Realm playerRealm = RealmUtilities.getRealRealm(event.getPlayer());
            if (playerRealm == null) playerRealm = Realm.REALM_NEXUS;
            statsObjective.setDisplayName(ChatColor.GOLD + playerRealm.getName());
            statsObjective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Health")).setScore((int) event.getPlayer().getMaxHealth());
            CachedPlayer cachedPlayer = SimpleCache.getCachedPlayer(event.getPlayer());
            statsObjective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Magic")).setScore(cachedPlayer.getMagic());
            statsObjective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Coins")).setScore(cachedPlayer.getCoins());
            statsObjective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Attack")).setScore(cachedPlayer.getAttack());
            statsObjective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Defense")).setScore(cachedPlayer.getDefense());
            statsObjective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Dexterity")).setScore(cachedPlayer.getDexterity());
            statsObjective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Speed")).setScore(cachedPlayer.getSpeed());
            event.getPlayer().setScoreboard(playerScoreboard);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Called when a player leaves/disconnects *
     */
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        try {
            this.onPlayerLeave(event.getPlayer(), 0);
        } catch (Exception ex) {
        }
    }

    /**
     * Called when a player is kicked *
     */
    @EventHandler
    public void onPlayerKicked(PlayerKickEvent event) {
        try {
            this.onPlayerLeave(event.getPlayer(), 1);
        } catch (Exception ex) {
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        try {
            if (this.getPlugin().hasClass(event.getPlayer().getName())) {
                RotMKClass playerClass = this.getPlugin().getClass(event.getPlayer().getName());
                event.setFormat(ChatColor.DARK_GRAY + "[" + playerClass.getColour() + playerClass.getName() + ChatColor.DARK_GRAY + "] " + (event.getPlayer().isOp() ? ChatColor.DARK_RED : ChatColor.YELLOW) + event.getPlayer().getDisplayName() + ChatColor.RESET + ": " + (event.getPlayer().hasPermission("essentials.chat.color") ? ChatColor.translateAlternateColorCodes('&', event.getMessage()) : event.getMessage()));
            } else {
                event.setFormat(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "None" + ChatColor.DARK_GRAY + "] " + (event.getPlayer().isOp() ? ChatColor.DARK_RED : ChatColor.YELLOW) + event.getPlayer().getDisplayName() + ChatColor.RESET + ": " + (event.getPlayer().hasPermission("essentials.chat.color") ? ChatColor.translateAlternateColorCodes('&', event.getMessage()) : event.getMessage()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        try {
            Player player = event.getEntity();
            RealmBase playerRealm = RealmUtilities.inPortal(player) ? RealmUtilities.getPortal(player.getWorld().getName()) : RealmUtilities.getRealm(player);
            playerRealm.broadcastMessage(player.getName() + " died at level " + player.getLevel() + ".");
            this.getPlugin().removeClass(player.getName());
            player.setLevel(0);
            event.setKeepLevel(false);
            event.setDroppedExp(0);
            event.setDeathMessage("");
            this.getPlugin().resetStats(player.getName());
            this.getPlugin().removeClass(player.getName());
            for (LootBag lootBag : this.getPlugin().getLootBags()) {
                if (lootBag.getOwner() != null && lootBag.getOwner().getName().equals(player.getName())) {
                    lootBag.getInventory().clear();
                    lootBag.getBlock().setType(lootBag.getOldBlockType());
                    lootBag.getBlock().setData(lootBag.getOldBlockData());
                    lootBag.getLocation().add(0, 1, 0).getBlock().setType(lootBag.getOldBlockTopType());
                    lootBag.getLocation().add(0, 1, 0).getBlock().setData(lootBag.getOldBlockTopData());
                    getPlugin().removeLootBag(lootBag);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        try {
            final Player player = event.getPlayer();
            event.getPlayer().getServer().getScheduler().runTaskLater(this.getPlugin(), new Runnable() {
                public void run() {
                    if (player != null)
                        player.kickPlayer(ChatColor.RED + "Error: You unnaturally died! \nYou have been kicked for safety reasons, please join again.");
                }
            }, 10L);
            event.setRespawnLocation(Realm.REALM_NEXUS.getSpawn());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        try {
            List<UUID> currentDrops = this.getPlugin().playerDrops.containsKey(event.getPlayer().getName()) ? this.getPlugin().playerDrops.get(event.getPlayer().getName()) : new ArrayList<UUID>();
            final UUID itemUUID = event.getItemDrop().getUniqueId();
            currentDrops.add(itemUUID);
            this.getPlugin().playerDrops.put(event.getPlayer().getName(), currentDrops);

            final Player player = event.getPlayer();
            final String pName = player.getName();
            event.getPlayer().getServer().getScheduler().runTaskLater(this.getPlugin(), new Runnable() {
                public void run() {
                    try {
                        if (getPlugin().playerDrops.containsKey(pName)) {
                            List<UUID> playerUUIDs = getPlugin().playerDrops.containsKey(pName) ? getPlugin().playerDrops.get(pName) : new ArrayList<UUID>();
                            for (UUID playerUUID : playerUUIDs) {
                                for (World world : Bukkit.getServer().getWorlds()) {
                                    for (Item itemStack : world.getEntitiesByClass(Item.class)) {
                                        if (itemStack.getUniqueId().equals(playerUUID)) {
                                            itemStack.remove();
                                            playerUUIDs.remove(playerUUID);
                                        }
                                    }
                                }
                            }
                            if (playerUUIDs.contains(itemUUID)) playerUUIDs.remove(itemUUID);
                            if (!playerUUIDs.isEmpty()) getPlugin().playerDrops.put(pName, playerUUIDs);
                            else getPlugin().playerDrops.remove(player.getName());
                        }
                    } catch (Exception ex) {
                    }
                }
            }, 600L);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        try {
            UUID itemUUID = event.getItem().getUniqueId();
            for (Entry<String, List<UUID>> dropItem : this.getPlugin().playerDrops.entrySet()) {
                if (dropItem.getValue().contains(itemUUID)) {
                    List<UUID> uuidList = dropItem.getValue();
                    uuidList.remove(itemUUID);
                    if (uuidList.isEmpty()) this.getPlugin().playerDrops.remove(dropItem.getKey());
                    else this.getPlugin().playerDrops.put(dropItem.getKey(), uuidList);
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Check if a player goes past boundaries and for portals to realms/portals *
     */
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        try {
            final Location moveTo = event.getTo();
            int fromX = (int) event.getFrom().getX(), fromY = (int) event.getFrom().getY(), fromZ = (int) event.getFrom().getZ();
            int toX = (int) moveTo.getX(), toY = (int) moveTo.getY(), toZ = (int) moveTo.getZ();
            boolean boundaryPlayer = false;
            if (fromX != toX || fromY != toY || fromZ != toZ) {
                if (toX > Main.maxBoundary || toZ > Main.maxBoundary) {
                    event.getPlayer().sendMessage(ChatColor.RED + "You cannot go past the boundaries!");
                    event.setCancelled(true);
                    boundaryPlayer = true;
                } else if (toX == Main.maxBoundary || toZ == Main.maxBoundary) {
                    event.getPlayer().sendMessage(ChatColor.RED + "You are on the boundary line.");
                    boundaryPlayer = true;
                } else if (toX < -Main.maxBoundary || toZ < -Main.maxBoundary) {
                    event.getPlayer().sendMessage(ChatColor.RED + "You cannot go past the boundaries!");
                    event.setCancelled(true);
                    boundaryPlayer = true;
                } else if (toX == -Main.maxBoundary || toZ == -Main.maxBoundary) {
                    event.getPlayer().sendMessage(ChatColor.RED + "You are on the boundary line.");
                    boundaryPlayer = true;
                } else {
                    if (!ClassGUI.classPlayers.containsKey(event.getPlayer().getName()) && !this.getPlugin().hasClass(event.getPlayer().getName())) {
                        ClassGUI classGUI = new ClassGUI(event.getPlayer());
                        classGUI.openInventory();
                        event.setCancelled(true);
                    }
                }
            }
            if (!boundaryPlayer) {
                List<Cuboid> realmPortals = this.getPlugin().getSettings().getPortals();
                for (int i = 0; i < realmPortals.size(); i++) {
                    Cuboid realmPortal = realmPortals.get(i);
                    if (realmPortal != null) {
                        if (realmPortal.contains(event.getPlayer().getLocation())) {
                            Realm targetRealm = this.getPlugin().getSettings().getRealm(i);
                            if (targetRealm != Realm.REALM_NEXUS) {
                                if (event.getPlayer().hasPermission("rotmk.realms." + targetRealm.getName().toLowerCase().replaceAll(" ", "_"))) {
                                    RealmUtilities.joinRealm(targetRealm, event.getPlayer());
                                    return;
                                } else {
                                    event.getPlayer().sendMessage(ChatColor.RED + "You do not have permission to join this realm!");
                                    continue;
                                }
                            }
                        }
                    }
                }
                for (Portal portal : this.getPlugin().getPortalList()) {
                    if (Utilities.compareLocations(moveTo, portal.getDoor())) {
                        event.getPlayer().teleport(portal.getWorld().getSpawnLocation(), TeleportCause.PLUGIN);
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Teleport player to nexus *
     */
    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        try {
            if (event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
                if (event.isFlying()) {
                    event.setCancelled(true);
                    RealmUtilities.joinRealm(Realm.REALM_NEXUS, event.getPlayer());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerEXPChange(PlayerExpChangeEvent event) {
        try {
            if (event.getPlayer().getLevel() > 20) {
                event.setAmount(0);
                event.getPlayer().setLevel(20);
                event.getPlayer().setExp(0.99F);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerLevelChange(PlayerLevelChangeEvent event) {
        try {
            if (event.getPlayer().getLevel() > 20) {
                event.getPlayer().setLevel(20);
                event.getPlayer().setExp(0.99F);
                RotMKClass playerClass = this.getPlugin().getClass(event.getPlayer().getName());
                if (playerClass != null) {
                    CachedPlayer cachedPlayer = SimpleCache.getCachedPlayer(event.getPlayer());
                    cachedPlayer.setAttack(playerClass.getAttack().getMaximum());
                    cachedPlayer.setDefense(playerClass.getDefense().getMaximum());
                    cachedPlayer.setDexterity(playerClass.getDexterity().getMaximum());
                    cachedPlayer.setSpeed(playerClass.getSpeed().getMaximum());
                    cachedPlayer.setMagic(playerClass.getMagic().getMaximum());
                    event.getPlayer().setMaxHealth(playerClass.getHealth().getMaximum());
                    this.updatePlayerScoreboard(event.getPlayer(), null, 0);
                }
            } else if (event.getPlayer().getLevel() == 20) {
                event.getPlayer().setLevel(20);
                event.getPlayer().setExp(0.99F);
            } else {
                int incrementAmount = event.getNewLevel() - event.getOldLevel();
                RotMKClass playerClass = this.getPlugin().getClass(event.getPlayer().getName());
                if (playerClass != null) {
                    CachedPlayer cachedPlayer = SimpleCache.getCachedPlayer(event.getPlayer());
                    cachedPlayer.incrementAttack(playerClass.getIncrementAttack() * incrementAmount);
                    cachedPlayer.incrementDefense(playerClass.getIncrementDefense() * incrementAmount);
                    cachedPlayer.incrementDexterity(playerClass.getIncrementDexterity() * incrementAmount);
                    cachedPlayer.incrementSpeed(playerClass.getIncrementSpeed() * incrementAmount);
                    cachedPlayer.incrementMagic(playerClass.getIncrementMagic() * incrementAmount);
                    double newHealth = event.getPlayer().getMaxHealth() + (playerClass.getIncrementHealth() * incrementAmount);
                    if (newHealth < 2D) newHealth = 2D;
                    else if (newHealth > playerClass.getHealth().getMaximum())
                        newHealth = playerClass.getHealth().getMaximum();
                    event.getPlayer().setMaxHealth(newHealth);
                    this.updatePlayerScoreboard(event.getPlayer(), null, 0);
                }
            }
            event.getPlayer().setHealth(event.getPlayer().getMaxHealth());
            event.getPlayer().setFoodLevel(20);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerChangeGamemode(PlayerGameModeChangeEvent event) {
        try {
            if (event.getNewGameMode() == GameMode.SURVIVAL) {
                final Player player = event.getPlayer();
                event.getPlayer().setAllowFlight(true);
                event.getPlayer().getServer().getScheduler().runTaskLater(this.getPlugin(), new Runnable() {
                    public void run() {
                        if (player != null && player.isOnline()) {
                            player.setAllowFlight(true);
                        }
                    }
                }, 2L);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        try {
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (event.getClickedBlock().getType() == Material.CHEST) {
                    Realm playerRealm = RealmUtilities.getRealRealm(event.getPlayer());
                    if (playerRealm == null || playerRealm == Realm.REALM_NEXUS) {
                        event.setCancelled(true);
                    } else {
                        for (LootBag lootBag : playerRealm.getLootBags()) {
                            if (Utilities.compareLocations(event.getClickedBlock().getLocation(), lootBag.getLocation())) {
                                event.setCancelled(true);
                                event.getPlayer().sendMessage(ChatColor.RED + "This loot bag belongs to " + lootBag.getOwner().getName() + ".");
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerOpenInventory(InventoryOpenEvent event) {
        try {
            if (event.getPlayer() instanceof Player) {
                //Player player = (Player) event.getPlayer();
                if (event.getInventory().getType() == InventoryType.ANVIL) {
                    if (!event.getPlayer().isOp()) event.setCancelled(true);
                }
                /**else if (event.getInventory().getType() == InventoryType.CHEST) {
                 if (RealmUtilities.inRealm(player)) {
                 Realm realm = RealmUtilities.getRealRealm(player);
                 if (realm != null) {
                 for (LootBag lootBag : realm.getLootBags()) {
                 if (lootBag.getOwner() != null) {
                 if (event.getInventory().getContents().equals(lootBag.getInventory().getContents())) {
                 if (!lootBag.getOwner().getName().equals(player.getName())) {
                 event.setCancelled(true);
                 player.sendMessage(ChatColor.RED + "This loot bag belongs to " + lootBag.getOwner().getName() + ".");
                 }
                 }
                 }
                 }
                 }
                 } else {
                 event.setCancelled(true);
                 }
                 }**/
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerConsumeItem(PlayerItemConsumeEvent event) {
        try {
            if (event.getItem().getType() == Material.POTION) {
                event.setCancelled(true);
                event.getPlayer().updateInventory();

                ItemMeta itemMeta = event.getItem().getItemMeta();
                PotionMeta potionMeta = (PotionMeta) itemMeta;
                if (itemMeta != null) {
                    String displayName = itemMeta.getDisplayName();
                    if (displayName != null) {
                        displayName = ChatColor.stripColor(displayName);
                        int amplifier = 0;
                        for (PotionEffect customEffect : potionMeta.getCustomEffects()) {
                            amplifier = customEffect.getAmplifier();
                            break;
                        }
                        amplifier += 1;
                        CachedPlayer cachedPlayer = SimpleCache.getCachedPlayer(event.getPlayer());
                        RotMKClass playerClass = this.getPlugin().getClass(event.getPlayer().getUniqueId());
                        if (displayName.equalsIgnoreCase("Health Potion")) {
                            double maxHealth = event.getPlayer().getMaxHealth() + (0.2D * (amplifier + 1));
                            if (maxHealth > playerClass.getHealth().getMaximum()) {
                                event.getPlayer().setMaxHealth(playerClass.getHealth().getMaximum());
                            } else {
                                event.getPlayer().setMaxHealth(maxHealth);
                            }
                            this.updatePlayerScoreboard(event.getPlayer(), "Health", (int) event.getPlayer().getMaxHealth());
                        } else if (displayName.equalsIgnoreCase("Attack Potion")) {
                            if (cachedPlayer.getAttack() + amplifier > playerClass.getAttack().getMaximum())
                                cachedPlayer.setAttack(playerClass.getAttack().getMaximum());
                            else cachedPlayer.incrementAttack(amplifier);
                            this.updatePlayerScoreboard(event.getPlayer(), "Attack", cachedPlayer.getAttack());
                        } else if (displayName.equalsIgnoreCase("Defense Potion")) {
                            if (cachedPlayer.getDefense() + amplifier > playerClass.getDefense().getMaximum())
                                cachedPlayer.setDefense(playerClass.getDefense().getMaximum());
                            else cachedPlayer.incrementDefense(amplifier);
                            this.updatePlayerScoreboard(event.getPlayer(), "Defense", cachedPlayer.getDefense());
                        } else if (displayName.equalsIgnoreCase("Dexterity Potion")) {
                            if (cachedPlayer.getDexterity() + amplifier > playerClass.getDexterity().getMaximum())
                                cachedPlayer.setDexterity(playerClass.getDexterity().getMaximum());
                            else cachedPlayer.incrementDexterity(amplifier);
                            this.updatePlayerScoreboard(event.getPlayer(), "Dexterity", cachedPlayer.getDexterity());
                        } else if (displayName.equalsIgnoreCase("Speed Potion")) {
                            if (cachedPlayer.getSpeed() + amplifier > playerClass.getSpeed().getMaximum())
                                cachedPlayer.setSpeed(playerClass.getSpeed().getMaximum());
                            else cachedPlayer.incrementSpeed(amplifier);
                            this.updatePlayerScoreboard(event.getPlayer(), "Speed", cachedPlayer.getSpeed());
                        } else if (displayName.equalsIgnoreCase("Magic Potion")) {
                            if (cachedPlayer.getMagic() + amplifier > playerClass.getMagic().getMaximum())
                                cachedPlayer.setMagic(playerClass.getMagic().getMaximum());
                            else cachedPlayer.incrementMagic(amplifier);
                            this.updatePlayerScoreboard(event.getPlayer(), "Magic", cachedPlayer.getMagic());
                        } else {
                            return;
                        }
                        ItemStack item = event.getItem();
                        if (item.getAmount() - 1 <= 0) {
                            event.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR));
                        } else {
                            item.setAmount(item.getAmount() - 1);
                            event.getPlayer().getInventory().setItemInHand(item);
                        }
                    }
                }
            } else if (event.getItem().getType() == Material.GOLDEN_APPLE) {
                event.setCancelled(true);
                CachedPlayer cachedPlayer = SimpleCache.getCachedPlayer(event.getPlayer());
                if (event.getItem().getDurability() == 1) {
                    cachedPlayer.incrementCoins(10);
                } else {
                    cachedPlayer.incrementCoins(5);
                }
                ItemStack item = event.getItem();
                if (item.getAmount() - 1 <= 0) {
                    event.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR));
                } else {
                    item.setAmount(item.getAmount() - 1);
                    event.getPlayer().getInventory().setItemInHand(item);
                }
                this.updatePlayerScoreboard(event.getPlayer(), "Coins", cachedPlayer.getCoins());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerBrew(BrewEvent event) {
        try {
            event.setCancelled(true);
        } catch (Exception ex) {
        }
    }

    @EventHandler
    public void onPlayerCraft(CraftItemEvent event) {
        try {
            event.setCancelled(true);
        } catch (Exception ex) {
        }
    }

    @EventHandler
    public void onPlayerBurnFurnace(FurnaceBurnEvent event) {
        try {
            event.setBurning(false);
        } catch (Exception ex) {
        }
    }

    @EventHandler
    public void onPlayerExtractFurnace(FurnaceExtractEvent event) {
        try {
            event.setExpToDrop(0);
        } catch (Exception ex) {
        }
    }

    @EventHandler
    public void onPlayerSmeltFurnace(FurnaceSmeltEvent event) {
        try {
            event.setCancelled(true);
        } catch (Exception ex) {
        }
    }

    /**
     * Called when a player disconnects from the server.
     *
     * @param player - The player.
     * @param leaveType - The way the player left. 0 for disconnecting, 1 for kicked.
     */
    private void onPlayerLeave(Player player, int leaveType) {
        if (RealmUtilities.inRealm(player) || RealmUtilities.inPortal(player)) {
            if (RealmUtilities.inRealm(player) && leaveType == 0)
                player.sendMessage(ChatColor.RED + "Disconnecting from " + RealmUtilities.getRealm(player).getName() + "...");
        }
        player.setScoreboard(Main.plainScoreboard);
    }

    @SuppressWarnings("deprecation")
    public void onPlayerDeath(Player player, String killerEntity) {
        if (ClassGUI.classPlayers.containsKey(player.getName())) {
            ClassGUI classGUI = ClassGUI.classPlayers.get(player.getName());
            if (classGUI != null) classGUI.closeInventory(true);
        }
        RealmBase playerRealm = RealmUtilities.inPortal(player) ? RealmUtilities.getPortal(player.getWorld().getName()) : RealmUtilities.getRealm(player);
        RealmUtilities.teleportToNexus(player);
        double maxHealth = player.getMaxHealth();
        int playerLevel = player.getLevel();

        PlayerStatistics playerStats = null;
        try {
            playerStats = new PlayerStatistics(player.getName(), SimpleCache.getCachedPlayer(player));
            player.setMaxHealth(20D);
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
            player.setExhaustion(0F);
            player.setLevel(0);
            player.setExp(0F);

            playerRealm.broadcastMessage(player.getName() + " died at level " + playerLevel + (killerEntity != null ? " by a " + killerEntity : "") + ".");
            this.getPlugin().resetStats(player.getName());
            SimpleCache.getCachedPlayer(player).incrementDeaths(1);
            this.getPlugin().removeClass(player.getName());
        } catch (UUIDException ex) {
            ex.printStackTrace();

            for (LootBag lootBag : this.getPlugin().getLootBags()) {
                if (lootBag.getOwner() != null && lootBag.getOwner().getName().equals(player.getName())) {
                    lootBag.getInventory().clear();
                    lootBag.getBlock().setType(lootBag.getOldBlockType());
                    lootBag.getBlock().setData(lootBag.getOldBlockData());
                    lootBag.getLocation().add(0, 1, 0).getBlock().setType(lootBag.getOldBlockTopType());
                    lootBag.getLocation().add(0, 1, 0).getBlock().setData(lootBag.getOldBlockTopData());
                    getPlugin().removeLootBag(lootBag);
                }
            }

            if (ClassGUI.classPlayers.containsKey(player.getName())) {
                ClassGUI oldClassGUI = ClassGUI.classPlayers.get(player.getName());
                oldClassGUI.closeInventory(true);
            }
            ClassGUI classGUI = new ClassGUI(player);
            classGUI.openInventory();
            return;
        }
        try {
            if (!DeathGUI.deathPlayers.containsKey(player.getName())) {
                DeathGUI deathGUI = new DeathGUI(player, playerStats, maxHealth, playerLevel);
                deathGUI.openInventory();
            } else {
                DeathGUI deathGUI = DeathGUI.deathPlayers.get(player.getName());
                if (deathGUI != null) deathGUI.closeInventory(true, false);
                else DeathGUI.deathPlayers.remove(player.getName());
                DeathGUI newDeathGUI = new DeathGUI(player, playerStats, maxHealth, playerLevel);
                newDeathGUI.openInventory();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        for (LootBag lootBag : this.getPlugin().getLootBags()) {
            if (lootBag.getOwner() != null && lootBag.getOwner().getName().equals(player.getName())) {
                lootBag.getInventory().clear();
                lootBag.getBlock().setType(lootBag.getOldBlockType());
                lootBag.getBlock().setData(lootBag.getOldBlockData());
                lootBag.getLocation().add(0, 1, 0).getBlock().setType(lootBag.getOldBlockTopType());
                lootBag.getLocation().add(0, 1, 0).getBlock().setData(lootBag.getOldBlockTopData());
                getPlugin().removeLootBag(lootBag);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void updatePlayerScoreboard(Player player, String strStatistic, int score) {
        Scoreboard playerScoreboard = null;
        if (strStatistic == null) {
            playerScoreboard = player.getServer().getScoreboardManager().getNewScoreboard();
            Objective statsObjective = playerScoreboard.registerNewObjective("stats", "dummy");
            CachedPlayer cachedPlayer;
            try {
                cachedPlayer = SimpleCache.getCachedPlayer(player);
                statsObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
                statsObjective.setDisplayName(ChatColor.GOLD + RealmUtilities.getRealm(player).getName());
                statsObjective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Health")).setScore((int) player.getMaxHealth());
                statsObjective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Magic")).setScore(cachedPlayer.getMagic());
                statsObjective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Coins")).setScore(cachedPlayer.getCoins());
                statsObjective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Attack")).setScore(cachedPlayer.getAttack());
                statsObjective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Defense")).setScore(cachedPlayer.getDefense());
                statsObjective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Dexterity")).setScore(cachedPlayer.getDexterity());
                statsObjective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Speed")).setScore(cachedPlayer.getSpeed());
            } catch (UUIDException ex) {
                ex.printStackTrace();
            }
        } else {
            playerScoreboard = player.getScoreboard() != null ? player.getScoreboard() : player.getServer().getScoreboardManager().getNewScoreboard();
            Objective statsObjective = playerScoreboard.getObjective("stats");
            CachedPlayer cachedPlayer = null;
            if (statsObjective == null) {
                try {
                    cachedPlayer = SimpleCache.getCachedPlayer(player);
                    statsObjective = playerScoreboard.registerNewObjective("stats", "dummy");
                    statsObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
                    statsObjective.setDisplayName(ChatColor.GOLD + RealmUtilities.getRealm(player).getName());
                    statsObjective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Health")).setScore((int) player.getMaxHealth());
                    statsObjective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Magic")).setScore(cachedPlayer.getMagic());
                    statsObjective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Coins")).setScore(cachedPlayer.getCoins());
                    statsObjective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Attack")).setScore(cachedPlayer.getAttack());
                    statsObjective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Defense")).setScore(cachedPlayer.getDefense());
                    statsObjective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Dexterity")).setScore(cachedPlayer.getDexterity());
                    statsObjective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Speed")).setScore(cachedPlayer.getSpeed());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                statsObjective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + strStatistic)).setScore(score);
            }
        }
        player.setScoreboard(playerScoreboard);
    }

    // TODO: Players can't go past the 9th slot (only hotbar).

    private Main getPlugin() {
        return Main.getInstance();
    }

}
