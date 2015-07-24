package net.cubespace.geSuitTeleports.tasks;

import net.cubespace.geSuitTeleports.geSuitTeleports;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.util.Collection;

public class PluginMessageTask extends BukkitRunnable {

    private final ByteArrayOutputStream bytes;

    public PluginMessageTask( ByteArrayOutputStream bytes ) {
        this.bytes = bytes;
    }

    public PluginMessageTask( ByteArrayOutputStream b, boolean empty ) {
        this.bytes = b;
    }

    @SuppressWarnings("unchecked")
    public void run() {
        Collection<Player> players = (Collection<Player>) Bukkit.getOnlinePlayers();
        if ( players.size() == 0 ) {
            return;
        }
        Player p = players.iterator().next();
        if ( p == null ) {
            return;
        }
        p.sendPluginMessage( geSuitTeleports.instance, "geSuitTeleport", bytes.toByteArray() );
    }


}