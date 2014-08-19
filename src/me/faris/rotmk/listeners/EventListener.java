package me.faris.rotmk.listeners;

import lib.simplecache.SimpleCache;
import lib.simplecache.exception.UUIDException;
import me.faris.rotmk.Main;
import me.faris.rotmk.helpers.LootBag;
import me.faris.rotmk.helpers.LootBag.LootBagType;
import me.faris.rotmk.helpers.utils.EntityUtilities;
import me.faris.rotmk.helpers.utils.RealmUtilities;
import me.faris.rotmk.helpers.utils.Utilities;
import me.faris.rotmk.realms.Realm;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.ItemStack;

public class EventListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        try {
            if (!event.getPlayer().isOp()) event.setCancelled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        try {
            event.setCancelled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onBlockDamaged(BlockDamageEvent event) {
        try {
            if (!event.getPlayer().isOp()) event.setCancelled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onBlockDecay(LeavesDecayEvent event) {
        try {
            event.setCancelled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onBlockEntityForm(EntityBlockFormEvent event) {
        try {
            event.setCancelled(true);
        } catch (Exception ex) {
        }
    }

    @EventHandler
    public void onBlockEXP(BlockExpEvent event) {
        try {
            event.setExpToDrop(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event) {
        try {
            event.setCancelled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onBlockMove(BlockFromToEvent event) {
        try {
            if (event.getBlock().getType() == Material.DRAGON_EGG) event.setCancelled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onBlockForm(BlockFormEvent event) {
        try {
            event.setCancelled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        try {
            if (event.getPlayer() == null || (event.getPlayer() != null && !event.getPlayer().isOp()))
                event.setCancelled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        try {
            if (!event.getPlayer().isOp()) event.setCancelled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent event) {
        try {
            event.setCancelled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onEntityBreakDoor(EntityBreakDoorEvent event) {
        try {
            event.setCancelled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        try {
            event.setCancelled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onEntityCreatePortal(EntityCreatePortalEvent event) {
        try {
            event.setCancelled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        try {
            event.blockList().clear();
            event.setYield(0);
        } catch (Exception ex) {
        }
    }

    @EventHandler
    public void onEntityDamaged(EntityDamageEvent event) {
        try {
            Realm entityRealm = RealmUtilities.getRealm(event.getEntity().getWorld());
            if (entityRealm.isNexus() || entityRealm.isVault()) {
                event.setCancelled(true);
                return;
            } else {
                if (event instanceof EntityDamageByEntityEvent) {
                    EntityDamageByEntityEvent dEvent = (EntityDamageByEntityEvent) event;
                    if (dEvent.getEntity() instanceof Player) {
                        Player player = (Player) dEvent.getEntity();
                        if (player.getHealth() - event.getDamage() < 2D) {
                            this.getPlugin().getPlayerListener().onPlayerDeath(player, dEvent.getDamager().getType().toString());
                        }
                    }
                } else {
                    if (event.getEntity() instanceof Player) {
                        Player player = (Player) event.getEntity();
                        if (player.getHealth() - event.getDamage() < 2D) {
                            this.getPlugin().getPlayerListener().onPlayerDeath(player, null);
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        try {
            event.setDroppedExp(0);
            event.getDrops().clear();
            if (event.getEntity().getType() != EntityType.PLAYER) {
                if (event.getEntity().getKiller() != null) {
                    this.onEntityDeath(event.getEntity(), event.getEntity().getKiller());
                }
            }
        } catch (Exception ex) {
        }
    }

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent event) {
        try {
            Realm entityRealm = RealmUtilities.getRealm(event.getEntity().getWorld());
            if (entityRealm != null) {
                if (entityRealm.isNexus() || entityRealm.isVault()) {
                    if (!(event.getEntity() instanceof org.bukkit.entity.Animals)) {
                        event.setCancelled(true);
                    } else {
                        if (event.getSpawnReason() != SpawnReason.CUSTOM) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onHangingBlockBreak(HangingBreakEvent event) {
        try {
            if (event instanceof HangingBreakByEntityEvent) {
                HangingBreakByEntityEvent hEvent = (HangingBreakByEntityEvent) event;
                if (hEvent.getRemover() instanceof Player) {
                    if (((Player) hEvent.getRemover()).isOp()) return;
                }
            }
            event.setCancelled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onHangingBlockPlace(HangingPlaceEvent event) {
        try {
            if (!event.getPlayer().isOp()) event.setCancelled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onItemEnchant(EnchantItemEvent event) {
        try {
            if (!event.getEnchanter().isOp()) event.setCancelled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onItemEnchantmentPrepare(PrepareItemEnchantEvent event) {
        try {
            if (!event.getEnchanter().isOp()) event.setCancelled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onPortalCreate(PortalCreateEvent event) {
        try {
            event.setCancelled(true);
        } catch (Exception ex) {
        }
    }

    @SuppressWarnings("deprecation")
    private void onEntityDeath(LivingEntity dead, Player killer) {
        if (killer != null) {
            if (RealmUtilities.inRealm(killer)) {
                try {
                    SimpleCache.getCachedPlayer(killer).incrementKills(1);
                } catch (UUIDException ex) {
                    ex.printStackTrace();
                }
                Realm killerRealm = RealmUtilities.getRealm(killer);
                int expAmount = EntityUtilities.getExp(dead);
                for (Player realmPlayer : killerRealm) {
                    if (!realmPlayer.getName().equals(killer.getName())) {
                        if (realmPlayer.getLocation().toVector().distanceSquared(dead.getLocation().toVector()) <= this.getPlugin().getSettings().getExpShareRadius()) {
                            realmPlayer.giveExp(expAmount);
                        }
                    }
                }
                killer.giveExp(expAmount);
                int playerLevel = killer.getLevel();

                LootBagType lootBagType = EntityUtilities.getEntityLootType(dead);
                if (lootBagType != null) {
                    if (dead.getLocation().getBlock().getType() != Material.CHEST) {
                        Material oldBlock = dead.getLocation().getBlock().getType();
                        byte oldBlockData = dead.getLocation().getBlock().getData();
                        Material oldBlockTop = dead.getLocation().add(0, 1, 0).getBlock().getType();
                        if (oldBlockTop != Material.SNOW && oldBlockTop != Material.CACTUS && oldBlockTop != Material.CAKE)
                            oldBlockTop = Material.AIR;
                        byte oldBlockTopData = oldBlockTop != Material.AIR ? dead.getLocation().add(0, 1, 0).getBlock().getData() : 0;
                        dead.getLocation().getBlock().setType(Material.CHEST);
                        Player bagOwner = lootBagType == LootBagType.NORMAL ? null : killer;
                        final LootBag lootBag = new LootBag(bagOwner, lootBagType, dead.getLocation(), oldBlock, oldBlockData, oldBlockTop, oldBlockTopData);
                        if (lootBag.getType() == LootBagType.NORMAL) {
                            int lootAmount = Utilities.getRandom().nextInt(9) + 1;
                            for (int i = 0; i < lootAmount; i++) {
                                if (Math.random() > 0.7) {
                                    if (Math.random() > 0.75)
                                        lootBag.addItem(new ItemStack(playerLevel <= 5 ? Material.WOOD_SWORD : (playerLevel <= 10 ? Material.STONE_SWORD : (playerLevel <= 15 ? Material.IRON_SWORD : (playerLevel <= 20 ? Material.DIAMOND_SWORD : Material.WOOD_SWORD)))));
                                    else if (Math.random() > 0.8) lootBag.addItem(new ItemStack(Material.BOW));
                                    else if (Math.random() > 0.6)
                                        lootBag.addItem(new ItemStack(Material.ARROW, Utilities.getRandom().nextInt(32) + 1));
                                    else if (Math.random() > 0.7)
                                        lootBag.addItem(new ItemStack(Material.LEATHER_CHESTPLATE));
                                    else if (Math.random() > 0.6)
                                        lootBag.addItem(new ItemStack(Material.LEATHER_HELMET));
                                    else if (Math.random() > 0.6)
                                        lootBag.addItem(new ItemStack(Material.LEATHER_LEGGINGS));
                                    else if (Math.random() > 0.6)
                                        lootBag.addItem(new ItemStack(Material.LEATHER_BOOTS));
                                }
                            }
                            if (lootAmount < 9) {
                                if (Math.random() >= 0.98) lootBag.addItem(new ItemStack(Material.GOLDEN_APPLE, 1));
                            }
                            if (lootBag.isEmpty()) {
                                lootBag.getInventory().clear();
                                lootBag.getBlock().setType(lootBag.getOldBlockType());
                                lootBag.getBlock().setData(lootBag.getOldBlockData());
                                lootBag.getBlock().getLocation().add(0, 1, 0).getBlock().setType(lootBag.getOldBlockTopType());
                                lootBag.getBlock().getLocation().add(0, 1, 0).getBlock().setData(lootBag.getOldBlockTopData());
                                this.getPlugin().removeLootBag(lootBag);
                                return;
                            }
                        } else if (lootBag.getType() == LootBagType.BLUE) {
                            if (Math.random() > 0.6) lootBag.addItem(EntityUtilities.getEntityPotion(dead));
                            lootBag.addItem(new ItemStack(Material.IRON_SWORD));
                            if (Math.random() > 0.8) {
                                if (Math.random() > 0.75)
                                    lootBag.addItem(new ItemStack(Material.GOLDEN_APPLE, 1, (byte) 1));
                                else lootBag.addItem(new ItemStack(Material.GOLDEN_APPLE, 1));
                            }
                        } else if (lootBag.getType() == LootBagType.WHITE) {
                            lootBag.addItem(EntityUtilities.getEntityPotion(dead));
                            if (Math.random() > 0.1) lootBag.addItem(EntityUtilities.getEntityPotion(dead));
                            if (Math.random() > 0.6) lootBag.addItem(EntityUtilities.getEntityPotion(dead));
                            if (Math.random() > 0.98) lootBag.addItem(EntityUtilities.getEntityPotion(dead));
                            if (Math.random() > 0.5) lootBag.addItem(new ItemStack(Material.GOLDEN_APPLE, 1, (byte) 1));
                            if (lootBag.isEmpty())
                                lootBag.addItem(new ItemStack(Math.random() > 0.5 ? Material.DIAMOND_SWORD : Material.IRON_SWORD));
                        }
                        this.getPlugin().addLootBag(lootBag);
                        lootBag.setBukkitTask(dead.getServer().getScheduler().runTaskLater(this.getPlugin(), new Runnable() {
                            public void run() {
                                if (getPlugin().isLootBag(lootBag)) {
                                    lootBag.getInventory().clear();
                                    lootBag.getBlock().setType(lootBag.getOldBlockType());
                                    lootBag.getBlock().setData(lootBag.getOldBlockData());
                                    lootBag.getBlock().getLocation().add(0, 1, 0).getBlock().setType(lootBag.getOldBlockTopType());
                                    lootBag.getBlock().getLocation().add(0, 1, 0).getBlock().setData(lootBag.getOldBlockTopData());
                                    getPlugin().removeLootBag(lootBag);
                                }
                            }
                        }, 400L));
                    }
                }
            }
        }
    }

    private Main getPlugin() {
        return Main.getInstance();
    }

}
