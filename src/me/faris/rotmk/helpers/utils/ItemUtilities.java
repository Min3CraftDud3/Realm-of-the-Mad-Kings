package me.faris.rotmk.helpers.utils;

import me.faris.rotmk.helpers.PotionID;
import me.faris.rotmk.helpers.Tier;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemUtilities {

    public static ItemStack addEnchantment(ItemStack itemStack, Enchantment enchantment) {
        itemStack.addUnsafeEnchantment(enchantment, enchantment.getMaxLevel());
        return itemStack;
    }

    /**
     * Add a lore to an item.
     *
     * @param itemStack - The item.
     * @param itemLore - The lore.
     * @return The item with the existing lore + the lore.
     */
    public static ItemStack addLore(ItemStack itemStack, String itemLore) {
        if (itemStack == null) itemStack = new ItemStack(Material.DIRT);
        if (itemStack.getItemMeta() != null) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            List<String> lores = itemMeta.getLore();
            if (lores == null) lores = new ArrayList<String>();
            lores.add(itemLore);
            itemMeta.setLore(lores);
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    /**
     * Add a lore to an item.
     *
     * @param itemStack - The item.
     * @param itemLores - The lore.
     * @return The item with the existing lore + the lore.
     */
    public static ItemStack addLore(ItemStack itemStack, List<String> itemLores) {
        if (itemStack == null) itemStack = new ItemStack(Material.DIRT);
        if (itemStack.getItemMeta() != null) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            List<String> lores = itemMeta.getLore();
            if (lores == null) lores = new ArrayList<String>();
            for (String itemLore : itemLores)
                lores.add(ChatColor.translateAlternateColorCodes('&', itemLore));
            itemMeta.setLore(lores);
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    /**
     * Add a lore at specific line to an item.
     *
     * @param index - The index (line).
     * @param itemStack - The item.
     * @param itemLore - The lore.
     * @return The item with the existing lore + the lore.
     */
    public static ItemStack addLore(int index, ItemStack itemStack, String itemLore) {
        if (itemStack == null) itemStack = new ItemStack(Material.DIRT);
        if (itemStack.getItemMeta() != null) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            List<String> lores = itemMeta.getLore();
            if (lores == null) lores = new ArrayList<String>();
            lores.add(index, itemLore);
            itemMeta.setLore(lores);
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    /**
     * Add a potion effect to a potion.
     *
     * @param itemStack - The potion item.
     * @param potionEffectType - The type of potion.
     * @return The potion with the potion effect.
     */
    public static ItemStack addPotionEffect(ItemStack itemStack, PotionType potionEffectType) {
        if (itemStack == null) itemStack = new ItemStack(Material.POTION);
        if (itemStack.getType() != Material.POTION) itemStack.setType(Material.POTION);
        Potion potion = new Potion(potionEffectType, potionEffectType.getMaxLevel());
        potion.apply(itemStack);
        return itemStack;
    }

    /**
     * Add a potion effect to a potion.
     *
     * @param itemStack - The potion item.
     * @param potionEffectTypes - An array of potion effect types.
     * @return The potion with the potion effects.
     */
    public static ItemStack addPotionEffects(ItemStack itemStack, PotionType... potionEffectTypes) {
        if (itemStack == null) itemStack = new ItemStack(Material.POTION);
        if (itemStack.getType() != Material.POTION) itemStack.setType(Material.POTION);
        for (PotionType potionEffectType : potionEffectTypes) {
            if (potionEffectType != null) {
                Potion potion = new Potion(potionEffectType, potionEffectType.getMaxLevel());
                potion.apply(itemStack);
            }
        }
        return itemStack;
    }

    /**
     * Create a potion ItemStack.
     *
     * @param potionID - The Potion type.
     * @return The potion item.
     */
    public static ItemStack createPotion(PotionID potionID) {
        return createPotions(potionID, 1);
    }

    /**
     * Create a potion ItemStack.
     *
     * @param potionID - The Potion type.
     * @param potionAmount - The amount of potions.
     * @return The potion item.
     */
    public static ItemStack createPotions(PotionID potionID, int potionAmount) {
        return new ItemStack(Material.POTION, potionAmount, potionID.getPotionID());
    }

    /**
     * Get the name of an ItemStack.
     *
     * @param item - The ItemStack
     * @return The name of the ItemStack.
     */
    public static String getItemName(ItemStack item) {
        if (item != null) {
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta != null) {
                if (itemMeta.getDisplayName() != null) {
                    return itemMeta.getDisplayName();
                } else {
                    return item.getType().toString();
                }
            } else {
                return item.getType().toString();
            }
        } else {
            return "AIR";
        }
    }

    /**
     * Get the tier of an item.
     * Note: Returns -1 when the item is UT.
     *
     * @param itemStack - The item.
     * @return The tier of the item.
     */
    public static Tier getTier(ItemStack itemStack) {
        if (itemStack != null) {
            String itemName = getItemName(itemStack);
            if (itemName != null) {
                if (itemName.contains(" ")) {
                    String tierName = itemName.split(" ")[1];
                    if (tierName.equals("UT")) {
                        return Tier.UT;
                    } else {
                        String tierSuffix = tierName.replaceFirst("T", "");
                        if (Utilities.isInteger(tierSuffix)) return Tier.getTier(Integer.parseInt(tierSuffix));
                    }
                }
            }
        }
        return Tier.TIER_0;
    }

    /**
     * Rename an item.
     *
     * @param itemStack - The item.
     * @param itemName - The name of the item.
     * @return The renamed item.
     */
    public static ItemStack renameItem(ItemStack itemStack, String itemName) {
        if (itemStack == null) itemStack = new ItemStack(Material.DIRT);
        if (itemStack.getItemMeta() != null) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(itemName);
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    /**
     * Set the lore of an item.
     *
     * @param itemStack - The item.
     * @param itemLores - The lore.
     * @return The item with a lore.
     */
    public static ItemStack setLore(ItemStack itemStack, List<String> itemLores) {
        if (itemStack == null) itemStack = new ItemStack(Material.DIRT);
        if (itemStack.getItemMeta() != null) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setLore(itemLores);
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    /**
     * Set the lore of an item.
     *
     * @param itemStack - The item.
     * @param itemLore - The lore.
     * @return The item with a lore.
     */
    public static ItemStack setLore(ItemStack itemStack, String itemLore) {
        if (itemStack == null) itemStack = new ItemStack(Material.DIRT);
        if (itemStack.getItemMeta() != null) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setLore(Arrays.asList(itemLore));
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    /**
     * Make an item unbreakable.
     *
     * @param itemStack - The item.
     * @return An unbreakable item.
     */
    public static ItemStack setUnbreakable(ItemStack itemStack) {
        if (itemStack == null) return new ItemStack(Material.AIR);
        itemStack.setDurability((short) 0);
        return itemStack;
    }

    public static class InventoryUtilities {
        public static void addItem(ItemStack itemStack, Player player) {
            // TODO: Continue. Check 8 slots.
        }
    }

}
