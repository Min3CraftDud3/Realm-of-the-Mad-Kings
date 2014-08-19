package me.faris.rotmk.helpers.gui;

import me.faris.rotmk.Main;
import me.faris.rotmk.helpers.PlayerStatistics;
import me.faris.rotmk.helpers.utils.ItemUtilities;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import java.util.HashMap;
import java.util.Map;

public class DeathGUI implements Listener {
    public static Map<String, DeathGUI> deathPlayers = new HashMap<String, DeathGUI>();
    private Inventory deathInventory;
    private Player player;
    private PlayerStatistics playerStats;
    private double maxHealth;
    private int playerLevel;

    public DeathGUI(Player player, PlayerStatistics playerStats, double maxHealth, int playerLevel) {
        this.player = player;
        this.deathInventory = this.player.getServer().createInventory(this.player, 9, ChatColor.RED + this.player.getName() + ", you died!");
        this.playerStats = playerStats;
        this.maxHealth = maxHealth;
        this.playerLevel = playerLevel;

        this.player.getServer().getPluginManager().registerEvents(this, Main.getInstance());
    }

    public void openInventory() {
        if (this.player != null && !deathPlayers.containsKey(this.player.getName())) {
            if (this.deathInventory == null)
                this.deathInventory = this.player.getServer().createInventory(this.player, 9, ChatColor.RED + this.player.getName() + ", you died!");
            this.deathInventory.clear();

            this.deathInventory.addItem(ItemUtilities.setLore(ItemUtilities.renameItem(ItemUtilities.addPotionEffect(null, PotionType.INSTANT_HEAL), ChatColor.DARK_RED + "Max Health:"), ChatColor.LIGHT_PURPLE + String.valueOf(this.maxHealth)));
            this.deathInventory.addItem(ItemUtilities.setLore(ItemUtilities.renameItem(ItemUtilities.addPotionEffect(null, PotionType.INVISIBILITY), ChatColor.DARK_RED + "Max Magic:"), ChatColor.DARK_PURPLE + String.valueOf(this.playerStats.getMagic())));
            this.deathInventory.addItem(ItemUtilities.setLore(ItemUtilities.renameItem(ItemUtilities.addPotionEffect(null, PotionType.STRENGTH), ChatColor.RED + "Attack:"), ChatColor.DARK_PURPLE + String.valueOf(this.playerStats.getAttack())));
            this.deathInventory.addItem(ItemUtilities.setLore(ItemUtilities.renameItem(ItemUtilities.addPotionEffect(null, PotionType.FIRE_RESISTANCE), ChatColor.RED + "Defense:"), ChatColor.DARK_PURPLE + String.valueOf(this.playerStats.getDefense())));
            this.deathInventory.addItem(ItemUtilities.setLore(ItemUtilities.renameItem(ItemUtilities.addPotionEffect(null, PotionType.SPEED), ChatColor.RED + "Speed:"), ChatColor.DARK_PURPLE + String.valueOf(this.playerStats.getSpeed())));
            this.deathInventory.addItem(ItemUtilities.setLore(ItemUtilities.renameItem(new ItemStack(Material.EXP_BOTTLE), ChatColor.RED + "Level:"), ChatColor.GREEN + String.valueOf(this.playerLevel)));
            this.player.openInventory(this.deathInventory);

            deathPlayers.put(this.player.getName(), this);
        }
    }

    public void closeInventory(boolean closeInventory, boolean showClassmenu) {
        if (this.player != null && deathPlayers.containsKey(this.player.getName())) {
            HandlerList.unregisterAll(this);
            if (closeInventory) {
                this.player.closeInventory();
                deathPlayers.remove(this.player.getName());
            }
            if (showClassmenu || !Main.getInstance().hasClass(this.player.getName())) {
                if (!closeInventory) deathPlayers.remove(this.player.getName());
                ClassGUI classGUI = new ClassGUI(this.player);
                classGUI.openInventory();
            }
        }
    }

    public Player getPlayer() {
        return this.player;
    }

    @EventHandler
    protected void onPlayerClickInventory(InventoryClickEvent event) {
        try {
            if (event.getWhoClicked() instanceof Player) {
                Player player = (Player) event.getWhoClicked();
                if (event.getInventory().getHolder() instanceof Player) {
                    if (event.getInventory().getName().equals(this.deathInventory.getName())) {
                        if (!player.getName().equals(((Player) event.getInventory().getHolder()).getName()))
                            player.sendMessage(ChatColor.RED + "You're not the holder of this inventory.");
                        event.setResult(Result.DENY);
                    }
                }
            }
        } catch (Exception ex) {
        }
    }

    @EventHandler
    protected void onPlayerCloseInventory(InventoryCloseEvent event) {
        try {
            if (event.getPlayer() instanceof Player) {
                Player player = (Player) event.getPlayer();
                if (event.getInventory().getHolder() instanceof Player) {
                    if (event.getInventory().getName().equals(this.deathInventory.getName())) {
                        if (player.getName().equals(((Player) event.getInventory().getHolder()).getName())) {
                            this.closeInventory(false, true);
                        } else {
                            player.sendMessage(ChatColor.RED + "You're not the holder of this inventory.");
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }
    }
}
