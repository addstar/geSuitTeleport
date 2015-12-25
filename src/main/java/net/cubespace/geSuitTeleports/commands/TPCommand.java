package net.cubespace.geSuitTeleports.commands;

import net.cubespace.geSuitTeleports.managers.TeleportsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        /** Console Commands **/
        if (!(sender instanceof Player)) {
            if (args.length < 2 || args.length > 6) {
                return false;
            }

            Player p = Bukkit.getPlayer(args[0]);
            // tp Player Player
            if (args.length == 2) {
                p.saveData();
                p.teleport(Bukkit.getPlayer(args[1]));
                return true;
            }
            // tp Player X Y Z
            if (args.length == 4) {
                p.saveData();
                p.teleport(new Location(
                        p.getWorld(),
                        args[1].startsWith("~") ? p.getLocation().getX() + (args[1].length() > 1 ? Double.valueOf(args[1].substring(1)) : 0) : Double.valueOf(args[1]),
                        args[2].startsWith("~") ? p.getLocation().getY() + (args[2].length() > 1 ? Double.valueOf(args[2].substring(1)) : 0) : Double.valueOf(args[2]),
                        args[3].startsWith("~") ? p.getLocation().getZ() + (args[3].length() > 1 ? Double.valueOf(args[3].substring(1)) : 0) : Double.valueOf(args[3]),
                        p.getLocation().getYaw(),
                        p.getLocation().getPitch()));
                return true;
            }
            // tp Player X Y Z World
            if (args.length == 5) {
                p.saveData();
                p.teleport(new Location(
                        Bukkit.getWorld(args[4]),
                        args[1].startsWith("~") ? p.getLocation().getX() + (args[1].length() > 1 ? Double.valueOf(args[1].substring(1)) : 0) : Double.valueOf(args[1]),
                        args[2].startsWith("~") ? p.getLocation().getY() + (args[2].length() > 1 ? Double.valueOf(args[2].substring(1)) : 0) : Double.valueOf(args[2]),
                        args[3].startsWith("~") ? p.getLocation().getZ() + (args[3].length() > 1 ? Double.valueOf(args[3].substring(1)) : 0) : Double.valueOf(args[3]),
                        p.getLocation().getYaw(),
                        p.getLocation().getPitch()));
                return true;
            }
            // tp Player X Y Z World Server
            if (args.length == 6) {
                TeleportsManager.teleportToLocation(p.getName(), args[5],
                        args[4],
                        args[1].startsWith("~") ? p.getLocation().getX() + (args[1].length() > 1 ? Double.valueOf(args[1].substring(1)) : 0) : Double.valueOf(args[1]),
                        args[2].startsWith("~") ? p.getLocation().getY() + (args[2].length() > 1 ? Double.valueOf(args[2].substring(1)) : 0) : Double.valueOf(args[2]),
                        args[3].startsWith("~") ? p.getLocation().getZ() + (args[3].length() > 1 ? Double.valueOf(args[3].substring(1)) : 0) : Double.valueOf(args[3]),
                        p.getLocation().getYaw(),
                        p.getLocation().getPitch());
                return true;
            }
            return false;
        }

        /** Player Commands **/
        if (args.length < 1 || args.length > 6) {
            return false;
        }

        Player p = Bukkit.getPlayer(sender.getName());

        // tp Player
        if (args.length == 1) {
            p.saveData();
            TeleportsManager.teleportToPlayer(sender, sender.getName(), args[0]);
            return true;
        }

        // tp Player Player
        if (args.length == 2) {
            Bukkit.getPlayer(args[0]).saveData();
            TeleportsManager.teleportToPlayer(sender, args[0], args[1]);
            return true;
        }

        // tp X Y Z
        if (args.length == 3) {
            p.saveData();
            TeleportsManager.teleportToLocation(sender.getName(), "",
                    p.getWorld().getName(),
                    args[0].startsWith("~") ? p.getLocation().getX() + (args[0].length() > 1 ? Double.valueOf(args[0].substring(1)) : 0) : Double.valueOf(args[0]),
                    args[1].startsWith("~") ? p.getLocation().getY() + (args[1].length() > 1 ? Double.valueOf(args[1].substring(1)) : 0) : Double.valueOf(args[1]),
                    args[2].startsWith("~") ? p.getLocation().getZ() + (args[2].length() > 1 ? Double.valueOf(args[2].substring(1)) : 0) : Double.valueOf(args[2]),
                    p.getLocation().getYaw(),
                    p.getLocation().getPitch());
            return true;
        }

        Player p2 = Bukkit.getPlayer(args[0]);

        // tp Player X Y Z
        // tp X Y Z World
        if (args.length == 4) {
            if (p2 != null) {
                if (!p.hasPermission("gesuit.teleports.tp.others")) {
                    p.sendMessage(ChatColor.RED + "You do not have permission to teleport others");
                    return true;
                }
                p2.saveData();
                TeleportsManager.teleportToLocation(args[0], "",
                        p.getWorld().getName(),
                        args[1].startsWith("~") ? p.getLocation().getX() + (args[1].length() > 1 ? Double.valueOf(args[1].substring(1)) : 0) : Double.valueOf(args[1]),
                        args[2].startsWith("~") ? p.getLocation().getY() + (args[2].length() > 1 ? Double.valueOf(args[2].substring(1)) : 0) : Double.valueOf(args[2]),
                        args[3].startsWith("~") ? p.getLocation().getZ() + (args[3].length() > 1 ? Double.valueOf(args[3].substring(1)) : 0) : Double.valueOf(args[3]),
                        p.getLocation().getYaw(),
                        p.getLocation().getPitch());
            } else {
                p.saveData();
                TeleportsManager.teleportToLocation(sender.getName(), "",
                        args[3],
                        args[0].startsWith("~") ? p.getLocation().getX() + (args[0].length() > 1 ? Double.valueOf(args[0].substring(1)) : 0) : Double.valueOf(args[0]),
                        args[1].startsWith("~") ? p.getLocation().getY() + (args[1].length() > 1 ? Double.valueOf(args[1].substring(1)) : 0) : Double.valueOf(args[1]),
                        args[2].startsWith("~") ? p.getLocation().getZ() + (args[2].length() > 1 ? Double.valueOf(args[2].substring(1)) : 0) : Double.valueOf(args[2]),
                        p.getLocation().getYaw(),
                        p.getLocation().getPitch());
            }
            return true;
        }

        // tp Player X Y X World
        // tp X Y Z World Server
        if (args.length == 5) {
            if (p2 != null) {
                if (!p.hasPermission("gesuit.teleports.tp.others")) {
                    p.sendMessage(ChatColor.RED + "You do not have permission to teleport others");
                    return true;
                }
                p2.saveData();
                TeleportsManager.teleportToLocation(args[0], "",
                        args[4],
                        args[1].startsWith("~") ? p.getLocation().getX() + (args[1].length() > 1 ? Double.valueOf(args[1].substring(1)) : 0) : Double.valueOf(args[1]),
                        args[2].startsWith("~") ? p.getLocation().getY() + (args[2].length() > 1 ? Double.valueOf(args[2].substring(1)) : 0) : Double.valueOf(args[2]),
                        args[3].startsWith("~") ? p.getLocation().getZ() + (args[3].length() > 1 ? Double.valueOf(args[3].substring(1)) : 0) : Double.valueOf(args[3]),
                        p.getLocation().getYaw(),
                        p.getLocation().getPitch());
            } else {
                p.saveData();
                TeleportsManager.teleportToLocation(sender.getName(), args[5],
                        args[4],
                        args[1].startsWith("~") ? p.getLocation().getX() + (args[1].length() > 1 ? Double.valueOf(args[1].substring(1)) : 0) : Double.valueOf(args[1]),
                        args[2].startsWith("~") ? p.getLocation().getY() + (args[2].length() > 1 ? Double.valueOf(args[2].substring(1)) : 0) : Double.valueOf(args[2]),
                        args[3].startsWith("~") ? p.getLocation().getZ() + (args[3].length() > 1 ? Double.valueOf(args[3].substring(1)) : 0) : Double.valueOf(args[3]),
                        p.getLocation().getYaw(),
                        p.getLocation().getPitch());
            }
            return true;
        }

        // tp Player X Y Z World Server
        if (args.length == 6) {
            if (!p.hasPermission("gesuit.teleports.tp.others")) {
                p.sendMessage(ChatColor.RED + "You do not have permission to teleport others");
                return true;
            }
            p2.saveData();
            TeleportsManager.teleportToLocation(args[0], args[5],
                    args[4],
                    args[1].startsWith("~") ? p.getLocation().getX() + (args[1].length() > 1 ? Double.valueOf(args[1].substring(1)) : 0) : Double.valueOf(args[1]),
                    args[2].startsWith("~") ? p.getLocation().getY() + (args[2].length() > 1 ? Double.valueOf(args[2].substring(1)) : 0) : Double.valueOf(args[2]),
                    args[3].startsWith("~") ? p.getLocation().getZ() + (args[3].length() > 1 ? Double.valueOf(args[3].substring(1)) : 0) : Double.valueOf(args[3]),
                    p.getLocation().getYaw(),
                    p.getLocation().getPitch());
            return true;
        }

        return false;
    }

}
