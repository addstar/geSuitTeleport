package net.cubespace.geSuitTeleports.listeners;

import net.cubespace.geSuitTeleports.geSuitTeleports;
import net.cubespace.geSuitTeleports.managers.TeleportsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class TeleportsMessageListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived( String channel, Player player, byte[] message ) {
        DataInputStream in = new DataInputStream( new ByteArrayInputStream( message ) );
        String task;
        try {
            task = in.readUTF();
            if ( task.equals( "TeleportToPlayer" ) ) {
                // Player1 Player2
                TeleportsManager.teleportPlayerToPlayer( in.readUTF(), in.readUTF() );
            }

            if ( task.equals( "TeleportToLocation" ) ) {
                // Player World X Y Z Yaw Pitch
                TeleportsManager.teleportPlayerToLocation( in.readUTF(), in.readUTF(), in.readDouble(), in.readDouble(), in.readDouble(), in.readFloat(), in.readFloat() );
            }
            
            if ( task.equals( "TeleportAccept" ) ) {
                TeleportsManager.finishTPA(Bukkit.getPlayerExact(in.readUTF()), in.readUTF());
            }
            
            if ( task.equals( "LeavingServer" ) ) {
                TeleportsManager.doLeaveServer(Bukkit.getPlayerExact(in.readUTF()));
            }

        } catch ( IOException e ) {
            e.printStackTrace();
            return;
        }
        if ( task.equals( "GetVersion" ) ) {
            String name = null;
            try {
                name = in.readUTF();
            } catch ( IOException ignored) {

            }
            if ( name != null ) {
                Player p = Bukkit.getPlayer( name );
                p.sendMessage( ChatColor.RED + "Teleports - " + ChatColor.GOLD + geSuitTeleports.instance.getDescription().getVersion() );
            }
            TeleportsManager.sendVersion();
            Bukkit.getConsoleSender().sendMessage( ChatColor.RED + "Teleports - " + ChatColor.GOLD + geSuitTeleports.instance.getDescription().getVersion() );
        }
    }

}
