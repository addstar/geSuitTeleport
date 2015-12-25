package net.cubespace.geSuitTeleports;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.cubespace.geSuitTeleports.commands.BackCommand;
import net.cubespace.geSuitTeleports.commands.TPACommand;
import net.cubespace.geSuitTeleports.commands.TPAHereCommand;
import net.cubespace.geSuitTeleports.commands.TPAcceptCommand;
import net.cubespace.geSuitTeleports.commands.TPAllCommand;
import net.cubespace.geSuitTeleports.commands.TPCommand;
import net.cubespace.geSuitTeleports.commands.TPDenyCommand;
import net.cubespace.geSuitTeleports.commands.TPHereCommand;
import net.cubespace.geSuitTeleports.commands.TPPosCommand;
import net.cubespace.geSuitTeleports.commands.ToggleCommand;
import net.cubespace.geSuitTeleports.commands.TopCommand;
import net.cubespace.geSuitTeleports.listeners.TeleportsListener;
import net.cubespace.geSuitTeleports.listeners.TeleportsMessageListener;

public class geSuitTeleports extends JavaPlugin {
    public static geSuitTeleports instance;

    @Override
    public void onEnable() {
        instance = this;
        registerListeners();
        registerChannels();
        registerCommands();
    }

    private void registerCommands() {
        getCommand("tp").setExecutor(new TPCommand());
        getCommand("tppos").setExecutor(new TPPosCommand());
        getCommand("tphere").setExecutor(new TPHereCommand());
        getCommand("tpall").setExecutor(new TPAllCommand());
        getCommand("tpa").setExecutor(new TPACommand());
        getCommand("tpahere").setExecutor(new TPAHereCommand());
        getCommand("tpaccept").setExecutor(new TPAcceptCommand());
        getCommand("tpdeny").setExecutor(new TPDenyCommand());
        getCommand("back").setExecutor(new BackCommand());
        getCommand("tptoggle").setExecutor(new ToggleCommand());
        getCommand("top").setExecutor(new TopCommand());
    }

    private void registerChannels() {
        Bukkit.getMessenger().registerIncomingPluginChannel(this,
                "geSuitTeleport", new TeleportsMessageListener());
        Bukkit.getMessenger().registerOutgoingPluginChannel(this,
                "geSuitTeleport");
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(
                new TeleportsListener(), this);
    }

    public static geSuitTeleports getInstance() {
        return instance;
    }

}
