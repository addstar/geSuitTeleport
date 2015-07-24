package net.cubespace.geSuitTeleports.commands;


import net.cubespace.geSuitTeleports.managers.TeleportsManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPCommand implements CommandExecutor {

    @Override
    public boolean onCommand( CommandSender sender, Command command, String label, String[] args ) {

        if ( !( sender instanceof Player ) ) {
            if ( args.length == 2 ) {
            	Player p = Bukkit.getPlayer(args[0]);
            	if ( p != null ) {
            		p.saveData();
            		p.teleport( Bukkit.getPlayer( args[1] ) );
            	}
            } else if ( args.length == 4 ) {
                Player p = Bukkit.getPlayer( args[0] );
            	if ( p != null ) {
                p.saveData();
                p.teleport( new Location( p.getWorld(), Double.parseDouble( args[1] ), Double.parseDouble( args[2] ), Double.parseDouble( args[3] ) ) );
            	}
            }
        }
        if ( args.length == 1 ) {
            Bukkit.getPlayer(sender.getName()).saveData();
            TeleportsManager.teleportToPlayer( sender, sender.getName(), args[0] );
            return true;

        } else if ( args.length == 2 ) {
            Player p = Bukkit.getPlayer( args[0] );
        	if ( p != null )
        		p.saveData();
            TeleportsManager.teleportToPlayer( sender, args[0], args[1] );
            return true;
        }

        if ( args.length == 3 ) {
            Bukkit.getPlayer(sender.getName()).saveData();
            TeleportsManager.teleportToLocation(sender.getName(), "", ((Player) sender).getWorld().getName(), Double.valueOf(args[0]), Double.valueOf(args[1]), Double.valueOf(args[2]));
            return true;
        }

        if ( args.length == 4) {
            Bukkit.getPlayer(sender.getName()).saveData();
            TeleportsManager.teleportToLocation(sender.getName(), "", args[3], Double.valueOf(args[0]), Double.valueOf(args[1]), Double.valueOf(args[2]));
            return true;
        }

        if ( args.length == 5 ) {
            Player p = Bukkit.getPlayer( args[0] );
        	if ( p != null ) {
        		p.saveData();
                TeleportsManager.teleportToLocation( args[0], "", args[4], Double.valueOf( args[1] ), Double.valueOf( args[2] ), Double.valueOf( args[3] ) );
            } else {
                Bukkit.getPlayer(sender.getName()).saveData();
                TeleportsManager.teleportToLocation( sender.getName(), args[0], args[1], Double.valueOf( args[2] ), Double.valueOf( args[3] ), Double.valueOf( args[4] ) );
            }

            return true;
        }

        return false;
    }

}
