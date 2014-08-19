package me.faris.rotmk.listeners;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import lib.simplecache.SimpleCache;
import lib.simplecache.exception.UUIDException;
import me.faris.rotmk.Main;
import me.faris.rotmk.helpers.Cuboid;
import me.faris.rotmk.helpers.utils.RealmUtilities;
import me.faris.rotmk.helpers.utils.Utilities;
import me.faris.rotmk.realms.Realm;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class CommandListener implements CommandExecutor {
    public Permission permission_setPortal = new Permission("rotmk.commands.setportal");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equals("rotmk")) {
            try {
                if (args.length > 0) {
                    String strCommand = args[0];
                    if (strCommand.equalsIgnoreCase("setportal")) {
                        if (!this.sendConsoleMessage(sender)) {
                            Player player = (Player) sender;
                            if (player.hasPermission(this.permission_setPortal)) {
                                if (args.length == 2) {
                                    String strRealmID = args[1];
                                    if (Utilities.isInteger(strRealmID)) {
                                        int realmID = Integer.parseInt(strRealmID);
                                        Realm realm = RealmUtilities.getRealm(realmID);
                                        if (realm.getID() != Realm.REALM_NEXUS.getID()) {
                                            Selection playerSelection = this.getPlugin().getWorldEdit().getSelection(player);
                                            if (playerSelection != null && playerSelection instanceof CuboidSelection) {
                                                CuboidSelection cuboidSelection = (CuboidSelection) playerSelection;
                                                Cuboid portalCuboid = new Cuboid(cuboidSelection.getMinimumPoint(), cuboidSelection.getMaximumPoint());
                                                this.getPlugin().getSettings().setPortal(realm, portalCuboid);
                                                this.getPlugin().getConfig().set("Portals.Portal " + (realm.getID() - 1), portalCuboid.toString());
                                                this.getPlugin().saveConfig();
                                                player.sendMessage(ChatColor.GOLD + realm.getName() + "'s portal set.");
                                            } else {
                                                player.sendMessage(ChatColor.RED + "Please make a valid cuboid selection using WorldEdit.");
                                            }
                                        } else {
                                            player.sendMessage(ChatColor.RED + "You cannot set a portal for the Nexus.");
                                        }
                                    } else {
                                        player.sendMessage(ChatColor.RED + "Please enter a valid integer for the Realm ID.");
                                    }
                                } else {
                                    this.sendUsage(player, "rotmk setportal <realmID>");
                                }
                            } else {
                                this.sendNoAccess(sender);
                            }
                        }
                    } else if (strCommand.equalsIgnoreCase("givecoins")) {
                        if (!this.sendConsoleMessage(sender)) {
                            Player player = (Player) sender;
                            SimpleCache.getCachedPlayer(player).setCoins(SimpleCache.getCachedPlayer(player).getCoins() + 100); // TODO: Remove this command make an official one.
                        }
                    } else {
                        this.sendPluginMessage(sender);
                    }
                } else {
                    this.sendPluginMessage(sender);
                }
            } catch (UUIDException ex) {
                ex.printStackTrace();
                sender.sendMessage(ChatColor.RED + "An error occured while performing that command. Please contact server administrators as soon as possible. Information to note: " + ex.getClass().getSimpleName());
            }
            return true;
        }
        return false;
    }

    private Main getPlugin() {
        return Main.getInstance();
    }

    private boolean sendConsoleMessage(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use that command.");
            return true;
        }
        return false;
    }

    private void sendNoAccess(CommandSender sender) {
        sender.sendMessage(ChatColor.DARK_RED + "You do not have access to that command.");
    }

    private void sendPluginMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "Realm of the Mad King " + this.getPlugin().getDescription().getVersion() + " by KingFaris10");
    }

    private void sendUsage(CommandSender sender, String usage) {
        sender.sendMessage(ChatColor.GOLD + "Usage: " + ChatColor.DARK_RED + "/" + usage);
    }

}
