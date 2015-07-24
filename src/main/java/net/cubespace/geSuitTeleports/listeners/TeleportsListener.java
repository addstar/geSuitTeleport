package net.cubespace.geSuitTeleports.listeners;

import net.cubespace.geSuitTeleports.geSuitTeleports;
import net.cubespace.geSuitTeleports.managers.PermissionsManager;
import net.cubespace.geSuitTeleports.managers.TeleportsManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class TeleportsListener implements Listener {
	
	@EventHandler
	public void playerConnect (PlayerSpawnLocationEvent e){
		if (e.getPlayer().hasMetadata("NPC")) return; // Ignore NPCs
		if(TeleportsManager.pendingTeleports.containsKey(e.getPlayer().getName())){
			Player t = TeleportsManager.pendingTeleports.get(e.getPlayer().getName());
			TeleportsManager.pendingTeleports.remove(e.getPlayer().getName());
			if ((t == null) || (!t.isOnline())) {
				e.getPlayer().sendMessage("Player is no longer online");
				return;
			}
			TeleportsManager.ignoreTeleport.add(e.getPlayer());
			e.setSpawnLocation(t.getLocation());
		}else if (TeleportsManager.pendingTeleportLocations.containsKey(e.getPlayer().getName())){
			Location l = TeleportsManager.pendingTeleportLocations.get(e.getPlayer().getName());
			TeleportsManager.ignoreTeleport.add(e.getPlayer());
			e.setSpawnLocation(l);
		}
	}
	
	@EventHandler (ignoreCancelled = true)
	public void playerTeleport(PlayerTeleportEvent e){
		if(e.isCancelled()){
			return;
		}
		if(e.getCause() != TeleportCause.PLUGIN && e.getCause() != TeleportCause.COMMAND){
			return;
		}
		if (e.getPlayer().hasMetadata("NPC")) return; // Ignore NPCs
		
		if(TeleportsManager.ignoreTeleport.contains(e.getPlayer())){
			TeleportsManager.ignoreTeleport.remove(e.getPlayer());
			return;
		}
		TeleportsManager.sendTeleportBackLocation(e.getPlayer(), false);	
	}
	
	@EventHandler
	public void playerLeave(PlayerQuitEvent e){
		if (e.getPlayer().hasMetadata("NPC")) return; // Ignore NPCs
		TeleportsManager.RemovePlayer(e.getPlayer());
		boolean empty = false;
		if(Bukkit.getOnlinePlayers().size()==1){
			empty = true;
		}
		TeleportsManager.sendTeleportBackLocation(e.getPlayer(), empty);
	}
	
	@EventHandler(ignoreCancelled = true)
	public void playerDeath(PlayerDeathEvent e){
		if (e.getEntity().hasMetadata("NPC")) return; // Ignore NPCs
		TeleportsManager.sendDeathBackLocation(e.getEntity());
        TeleportsManager.ignoreTeleport.add(e.getEntity());
	}
	

	@EventHandler(priority = EventPriority.NORMAL)
	public void setFormatChat(final PlayerLoginEvent e) {
		if (e.getPlayer().hasMetadata("NPC")) return; // Ignore NPCs
		if(e.getPlayer().hasPermission("gesuit.*")){
			PermissionsManager.addAllPermissions(e.getPlayer());
		}else if(e.getPlayer().hasPermission("gesuit.admin")){
			PermissionsManager.addAdminPermissions(e.getPlayer());
		}else if(e.getPlayer().hasPermission("gesuit.vip")){
			PermissionsManager.addVIPPermissions(e.getPlayer());
		}else if(e.getPlayer().hasPermission("gesuit.user")){
			PermissionsManager.addUserPermissions(e.getPlayer());
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void playerJoin(final PlayerJoinEvent event) {
		if (event.getPlayer().hasMetadata("NPC")) return; // Ignore NPCs
	    // This is to prevent recording the back location when teleporting across servers, on the destination server
	    TeleportsManager.ignoreTeleport.add(event.getPlayer());
	    Bukkit.getScheduler().runTaskLaterAsynchronously( geSuitTeleports.instance, new Runnable() {
            @Override
            public void run() {
                TeleportsManager.ignoreTeleport.remove(event.getPlayer());
            }
        }, 20 );
	}
}
