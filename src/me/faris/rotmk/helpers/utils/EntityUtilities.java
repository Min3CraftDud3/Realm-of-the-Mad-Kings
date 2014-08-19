package me.faris.rotmk.helpers.utils;

import me.faris.rotmk.helpers.LootBag.LootBagType;
import me.faris.rotmk.helpers.PotionID;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class EntityUtilities {

    public static LootBagType getEntityLootType(LivingEntity livingEntity) {
        if (livingEntity != null) {
            EntityType leType = livingEntity.getType();
            double randomNumber = Math.random();
            if (leType == EntityType.GIANT) {
                return LootBagType.WHITE;
            } else if (leType == EntityType.ENDER_DRAGON) {
                if (randomNumber > 0.5) return LootBagType.WHITE;
                else if (randomNumber > 0.2) return LootBagType.BLUE;
                else return LootBagType.NORMAL;
            } else if (leType == EntityType.WITHER || leType == EntityType.GHAST || leType == EntityType.IRON_GOLEM) {
                if (randomNumber > 0.8) return LootBagType.WHITE;
                else if (randomNumber > 0.5) return LootBagType.BLUE;
                else if (randomNumber > 0.25) return LootBagType.NORMAL;
                else return null;
            } else if (leType == EntityType.ENDERMAN || leType == EntityType.MAGMA_CUBE) {
                if (randomNumber > 0.96) return LootBagType.WHITE;
                else if (randomNumber > 0.8) return LootBagType.BLUE;
                else if (randomNumber > 0.25) return LootBagType.NORMAL;
                else return null;
            } else if (leType == EntityType.PIG_ZOMBIE) {
                if (randomNumber > 0.8) return LootBagType.BLUE;
                else if (randomNumber > 0.3) return LootBagType.NORMAL;
                else return null;
            } else if (leType == EntityType.CREEPER) {
                if (randomNumber > 0.85) return LootBagType.BLUE;
                else if (randomNumber > 0.3) return LootBagType.NORMAL;
                else return null;
            } else {
                if (Math.random() > getRandomAboveHalf(0.8)) return LootBagType.NORMAL;
                else return null;
            }
        } else {
            return null;
        }
    }

    public static String getEntityName(LivingEntity livingEntity) {
        // TODO: Get custom entity name.
        return "";
    }

    public static ItemStack getEntityPotion(LivingEntity livingEntity) {
        if (livingEntity != null) {
            EntityType leType = livingEntity.getType();
            if (leType == EntityType.GIANT) {
                return ItemUtilities.renameItem(ItemUtilities.createPotion(PotionID.STRENGTH), "Attack Potion");
            } else if (leType == EntityType.ENDER_DRAGON) {
                return ItemUtilities.renameItem(ItemUtilities.createPotions(PotionID.SWIFTNESS, Utilities.getRandom().nextInt(2) + 1), "Speed Potion");
            } else if (leType == EntityType.WITHER) {
                return ItemUtilities.renameItem(ItemUtilities.createPotion(PotionID.HEALING), "Health Potion");
            } else if (leType == EntityType.GHAST || leType == EntityType.IRON_GOLEM) {
                return ItemUtilities.renameItem(ItemUtilities.createPotion(PotionID.FIRE_RESISTANCE), "Defense Potion");
            } else if (leType == EntityType.ENDERMAN || leType == EntityType.MAGMA_CUBE) {
                return ItemUtilities.renameItem(ItemUtilities.createPotion(PotionID.REGENERATION), "Dexterity Potion");
            } else if (leType == EntityType.PIG_ZOMBIE) {
                if (Math.random() > 0.75)
                    ItemUtilities.renameItem(ItemUtilities.createPotion(PotionID.STRENGTH), "Attack Potion");
            }
        }
        return new ItemStack(Material.AIR);
    }

    public static int getExp(LivingEntity livingEntity) {
        if (livingEntity != null) {
            EntityType leType = livingEntity.getType();
            if (leType == EntityType.GIANT) {
                return 40 + Utilities.getRandom().nextInt(7);
            } else if (leType == EntityType.ENDER_DRAGON) {
                return 40 + Utilities.getRandom().nextInt(5);
            } else if (leType == EntityType.WITHER || leType == EntityType.GHAST || leType == EntityType.IRON_GOLEM) {
                return 20 + Utilities.getRandom().nextInt(11);
            } else if (leType == EntityType.ENDERMAN || leType == EntityType.MAGMA_CUBE) {
                return 17;
            } else if (leType == EntityType.PIG_ZOMBIE) {
                return 6 + Utilities.getRandom().nextInt(2);
            } else if (leType == EntityType.CREEPER) {
                return 3 + Utilities.getRandom().nextInt(6);
            } else {
                return 2 + Utilities.getRandom().nextInt(4);
            }
        }
        return 0;
    }

    private static double getRandomAboveHalf(double below) {
        double randomNumber = Math.random();
        while (randomNumber <= 0.5) {
            randomNumber = Math.random();
            while (randomNumber >= below)
                randomNumber = Math.random();
        }
        while (randomNumber >= below) {
            randomNumber = Math.random();
            while (randomNumber <= 0.5)
                randomNumber = Math.random();
        }
        return randomNumber;
    }
}
