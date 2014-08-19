package me.faris.rotmk.helpers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class LootBag {
    private LootBagType bagType = null;
    private Location bagLocation = null;
    private Player bagOwner = null;
    private Material oldBlockType = null;
    private byte oldBlockData = 0;
    private Material oldBlockTopType = null;
    private byte oldBlockTopData = 0;

    private BukkitTask lootBagTask = null;

    public LootBag(Player bagOwner, LootBagType lootBagType, Location lootBagLocation) {
        this(bagOwner, lootBagType, lootBagLocation, Material.AIR, (byte) 0, Material.AIR, (byte) 0);
    }

    public LootBag(Player bagOwner, LootBagType lootBagType, Location lootBagLocation, Material oldBlockType, byte oldBlockData, Material oldBlockTopType, byte oldBlockTopData) {
        this.bagType = lootBagType;
        this.bagLocation = lootBagLocation;
        this.bagOwner = bagOwner;
        this.oldBlockType = oldBlockType;
        this.oldBlockData = oldBlockData;
        this.oldBlockTopType = oldBlockType;
        this.oldBlockTopData = oldBlockData;
    }

    public LootBag addItem(ItemStack... itemStacks) {
        this.getInventory().addItem(itemStacks);
        return this;
    }

    public LootBag clearBag() {
        this.getInventory().clear();
        return this;
    }

    public Block getBlock() {
        return this.bagLocation.getBlock();
    }

    public BukkitTask getBukkitTask() {
        return this.lootBagTask;
    }

    public Chest getChest() {
        return this.getBlock().getState() instanceof Chest ? (Chest) this.getBlock().getState() : null;
    }

    public Inventory getInventory() {
        Chest bagChest = this.getChest();
        if (bagChest == null) return Bukkit.getServer().createInventory(this.bagOwner, 27, "");
        else return this.getChest().getBlockInventory();
    }

    public Location getLocation() {
        return this.bagLocation;
    }

    public byte getOldBlockData() {
        return this.oldBlockData;
    }

    public Material getOldBlockType() {
        return this.oldBlockType;
    }

    public byte getOldBlockTopData() {
        return this.oldBlockTopData;
    }

    public Material getOldBlockTopType() {
        return this.oldBlockTopType;
    }

    public Player getOwner() {
        return this.bagOwner;
    }

    public LootBagType getType() {
        return this.bagType;
    }

    public boolean isEmpty() {
        ItemStack[] itemList = this.getInventory().getContents();
        for (int i = 0; i < itemList.length; i++) {
            ItemStack itemStack = itemList[i];
            if (itemStack != null && itemStack.getType() != Material.AIR) return true;
        }
        return false;
    }

    public LootBag setBukkitTask(BukkitTask bukkitTask) {
        this.lootBagTask = bukkitTask;
        return this;
    }

    public LootBag setItem(int itemSlot, ItemStack itemStack) {
        this.getInventory().setItem(itemSlot, itemStack);
        return this;
    }

    public static enum LootBagType {
        NORMAL, BLUE, WHITE;
    }
}
