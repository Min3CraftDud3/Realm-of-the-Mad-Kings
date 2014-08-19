package me.faris.rotmk.helpers.gui;

import lib.simplecache.CachedPlayer;
import lib.simplecache.SimpleCache;
import me.faris.rotmk.Main;
import me.faris.rotmk.helpers.RotMKClass;
import me.faris.rotmk.helpers.utils.ItemUtilities;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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

public class ClassGUI implements Listener {
    public static Map<String, ClassGUI> classPlayers = new HashMap<String, ClassGUI>();

    private Inventory classInventory;
    private Player player;

    public ClassGUI(Player player) {
        this.player = player;
        this.classInventory = this.player.getServer().createInventory(this.player, 18, ChatColor.AQUA + "Class menu");

        try {
            this.player.getServer().getPluginManager().registerEvents(this, Main.getInstance());
        } catch (Exception ex) {
        }
    }

    public void openInventory() {
        if (this.player != null) {
            if (this.classInventory == null)
                this.classInventory = this.player.getServer().createInventory(this.player, 18, ChatColor.AQUA + "Class menu");
            this.classInventory.clear();

            this.classInventory.addItem(this.getPermissionBasedItem(ItemUtilities.renameItem(ItemUtilities.addPotionEffect(null, PotionType.INVISIBILITY), ChatColor.DARK_GREEN + "Rogue")));
            this.classInventory.addItem(this.getPermissionBasedItem(ItemUtilities.renameItem(new ItemStack(Material.BOW), ChatColor.GREEN + "Archer")));
            this.classInventory.addItem(this.getPermissionBasedItem(ItemUtilities.renameItem(new ItemStack(Material.STICK), ChatColor.DARK_RED + "Wizard")));
            this.classInventory.addItem(this.getPermissionBasedItem(ItemUtilities.renameItem(ItemUtilities.addPotionEffect(null, PotionType.INSTANT_HEAL), ChatColor.WHITE + "Priest")));
            this.classInventory.addItem(this.getPermissionBasedItem(ItemUtilities.renameItem(new ItemStack(Material.DIAMOND_CHESTPLATE), ChatColor.GRAY + "Warrior")));
            this.classInventory.addItem(this.getPermissionBasedItem(ItemUtilities.renameItem(new ItemStack(Material.CHAINMAIL_CHESTPLATE), ChatColor.DARK_GRAY + "Knight")));
            this.classInventory.addItem(this.getPermissionBasedItem(ItemUtilities.renameItem(new ItemStack(Material.DIAMOND_SWORD), ChatColor.YELLOW + "Paladin")));
            this.classInventory.addItem(this.getPermissionBasedItem(ItemUtilities.renameItem(new ItemStack(Material.IRON_SWORD), ChatColor.DARK_BLUE + "Assassin")));
            this.classInventory.addItem(this.getPermissionBasedItem(ItemUtilities.renameItem(ItemUtilities.addEnchantment(new ItemStack(Material.STICK), Enchantment.DAMAGE_ALL), ChatColor.DARK_PURPLE + "Necromancer")));
            this.classInventory.addItem(this.getPermissionBasedItem(ItemUtilities.renameItem(ItemUtilities.addEnchantment(new ItemStack(Material.BOW), Enchantment.ARROW_DAMAGE), ChatColor.DARK_PURPLE + "Huntress")));
            this.classInventory.addItem(this.getPermissionBasedItem(ItemUtilities.renameItem(new ItemStack(Material.EXP_BOTTLE), ChatColor.LIGHT_PURPLE + "Mystic")));
            this.classInventory.addItem(this.getPermissionBasedItem(ItemUtilities.renameItem(new ItemStack(Material.GOLD_SWORD), ChatColor.GRAY + "Trickster")));
            this.classInventory.addItem(this.getPermissionBasedItem(ItemUtilities.renameItem(new ItemStack(Material.BLAZE_ROD), ChatColor.RED + "Sorcerer")));
            this.classInventory.addItem(this.getPermissionBasedItem(ItemUtilities.renameItem(ItemUtilities.addPotionEffect(null, PotionType.SPEED), ChatColor.DARK_GRAY + "Ninja")));

            classPlayers.put(this.player.getName(), this);
            this.player.closeInventory();
            this.player.openInventory(this.classInventory);
        }
    }

    public void closeInventory(boolean closeInventory) {
        if (this.player != null) {
            HandlerList.unregisterAll(this);
            classPlayers.remove(this.player.getName());
            if (closeInventory) this.player.closeInventory();
        }
    }

    public Player getPlayer() {
        return this.player;
    }

    @EventHandler
    protected void onPlayerClickInventory(InventoryClickEvent event) {
        try {
            if (event.getWhoClicked() instanceof Player) {
                if (event.getInventory().getHolder() instanceof Player) {
                    if (event.getInventory().getName().equals(this.classInventory.getName())) {
                        event.setResult(Result.DENY);
                        if (((Player) event.getWhoClicked()).getName().equals(((Player) event.getInventory().getHolder()).getName())) {
                            ItemStack itemStack = event.getSlot() >= 0 && event.getSlot() < event.getInventory().getSize() ? event.getInventory().getItem(event.getSlot()) : new ItemStack(Material.AIR);
                            if (itemStack != null && itemStack.getType() != Material.AIR) {
                                String itemName = ChatColor.stripColor(ItemUtilities.getItemName(itemStack));
                                if (!this.player.hasPermission("rotmk.class." + itemName.toLowerCase())) {
                                    int playerCoins = SimpleCache.getCachedPlayer(this.player).getCoins();
                                    if (playerCoins >= Main.getInstance().getSettings().getClassCost()) {
                                        playerCoins -= Main.getInstance().getSettings().getClassCost();
                                        SimpleCache.getCachedPlayer(this.player).setCoins(playerCoins);
                                        Main.vaultPermissions.playerAdd(this.player, "rotmk.class." + itemName.toLowerCase());
                                        this.player.sendMessage(ChatColor.GREEN + "You bought " + itemName + " for " + Main.getInstance().getSettings().getClassCost() + " coins!");
                                        Main.getInstance().getPlayerListener().updatePlayerScoreboard(this.player, "Coins", playerCoins);
                                    } else {
                                        this.player.sendMessage(ChatColor.DARK_RED + "You do not have enough coins to unlock this class!");
                                        return;
                                    }
                                }
                                if (Main.getInstance().setClass(this.player.getName(), itemName)) {
                                    this.player.sendMessage(ChatColor.RED + "You are now a(n) " + itemName + "!");
                                    RotMKClass rotmkClass = Main.getInstance().getClassByName(itemName);
                                    if (rotmkClass != null) {
                                        this.player.setMaxHealth(rotmkClass.getHealth().getMinimum());
                                        this.player.setHealth(this.player.getMaxHealth());
                                        CachedPlayer cachedPlayer = SimpleCache.getCachedPlayer(this.player);
                                        cachedPlayer.setAttack(rotmkClass.getAttack().getMinimum());
                                        cachedPlayer.setDefense(rotmkClass.getDefense().getMinimum());
                                        cachedPlayer.setDexterity(rotmkClass.getDexterity().getMinimum());
                                        cachedPlayer.setSpeed(rotmkClass.getSpeed().getMinimum());
                                        cachedPlayer.setMagic(rotmkClass.getMagic().getMinimum());
                                        Main.getInstance().getPlayerListener().updatePlayerScoreboard(this.player, null, 0);
                                    }
                                    this.closeInventory(true);
                                } else {
                                    this.player.sendMessage(ChatColor.DARK_RED + "That class does not exist.");
                                }
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
    protected void onPlayerCloseInventory(InventoryCloseEvent event) {
        try {
            if (event.getPlayer() instanceof Player) {
                if (event.getInventory().getName().equals(this.classInventory.getName())) {
                    HandlerList.unregisterAll(this);
                    classPlayers.remove(this.player.getName());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private ItemStack getPermissionBasedItem(ItemStack itemStack) {
        String itemName = ChatColor.stripColor(ItemUtilities.getItemName(itemStack));
        return this.player.hasPermission("rotmk.class." + itemName.toLowerCase()) ? ItemUtilities.setLore(itemStack, ChatColor.GREEN + "You have unlocked this class!") : ItemUtilities.setLore(itemStack, ChatColor.DARK_RED + "You have not unlocked this class. Click to unlock for " + Main.getInstance().getSettings().getClassCost() + " coins!");
    }
}