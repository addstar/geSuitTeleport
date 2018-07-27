package net.cubespace.geSuitTeleports.tasks;

import com.google.common.collect.Iterables;
import net.cubespace.geSuitTeleports.geSuitTeleports;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;

public class PluginMessageTask extends BukkitRunnable {

    private final ByteArrayOutputStream bytes;

    public PluginMessageTask( ByteArrayOutputStream bytes ) {
        this.bytes = bytes;
    }

    public PluginMessageTask( ByteArrayOutputStream b, boolean empty ) {
        this.bytes = b;
    }

    public void run() {
        Player p = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        if ( p == null ) {
            return;
        }
        p.sendPluginMessage( geSuitTeleports.instance, geSuitTeleports.CHANNEL_NAME, bytes.toByteArray() );
    }


}