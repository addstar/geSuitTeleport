package net.cubespace.geSuitTeleports;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import net.cubespace.geSuitTeleports.commands.BackCommand;
import net.cubespace.geSuitTeleports.commands.TPACommand;
import net.cubespace.geSuitTeleports.commands.TPAHereCommand;
import net.cubespace.geSuitTeleports.commands.TPAcceptCommand;
import net.cubespace.geSuitTeleports.commands.TPAllCommand;
import net.cubespace.geSuitTeleports.commands.TPCommand;
import net.cubespace.geSuitTeleports.commands.TPPosCommand;
import net.cubespace.geSuitTeleports.commands.TPDenyCommand;
import net.cubespace.geSuitTeleports.commands.TPHereCommand;
import net.cubespace.geSuitTeleports.commands.ToggleCommand;
import net.cubespace.geSuitTeleports.commands.TopCommand;
import net.cubespace.geSuitTeleports.listeners.TeleportsListener;
import net.cubespace.geSuitTeleports.listeners.TeleportsMessageListener;

import java.util.List;

public class geSuitTeleports extends JavaPlugin {
    public static geSuitTeleports instance;
    public static String teleportinitiated;
    public static String teleporting;
    public static String aborted;
    public static String unsafe_location;
    public static String tptop;
    public static String invalid_offline;
    public static String relative_coords_not_valid;
    public static String invalid_x_coordinate_or_player;
    public static String invalid;
    public static String coordinate;
    public static String no_longer_online;
    public static String no_perms_for_teleporting_others;
    public static String sending;
    public static String to;
    public static String in_world;
    public static String on_server;
    public static String invalid_yaw;
    public static String invalid_pitch;
    public static String location_blocked;
    public static List<String> deny_Teleport;
    public static String tp_admin_bypass;
    private static WorldGuardPlugin mWorldGuard;
    public static final String CHANNEL_NAME = "bungeecord:gesuitteleport";
    public static boolean worldGuarded;
    public static boolean geSuitSpawns;
    public static boolean logDebugMessages;

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        getConfig().options().copyDefaults(true);
		saveConfig();
        teleportinitiated = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.teleport_initiated"));
        teleporting = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.teleporting"));
        aborted = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.aborted"));
        unsafe_location = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.unsafe_location"));
        tptop = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.tptop"));
        invalid_offline = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.invalid_or_offline_player"));
        relative_coords_not_valid = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.invalid_or_offline_player"));
        invalid = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.invalid"));
        coordinate = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.coordinate"));
        no_longer_online = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.no_longer_online"));
        no_perms_for_teleporting_others = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.no_perms_for_teleporting_others"));
        sending = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.sending"));
        to = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.to"));
        in_world = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.in_world"));
        on_server = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.on_server"));
        invalid_yaw = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.invalid_yaw"));
        invalid_pitch = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.invalid_pitch"));
        location_blocked = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.location_blocked"));
        tp_admin_bypass = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.tp_admin_bypass"));
        if(Bukkit.getPluginManager().isPluginEnabled("WorldGuard")){//check for WorldGuard
            mWorldGuard = (WorldGuardPlugin)Bukkit.getPluginManager().getPlugin("WorldGuard");
            worldGuarded = true;
            deny_Teleport = getConfig().getStringList("teleport.denyon.cmdlist");
        }else{
            mWorldGuard = null;
            worldGuarded = false;
            deny_Teleport = null;
        }

        logDebugMessages = getConfig().getBoolean("options.debug_logging");

        //check for geSuitSpawns
        geSuitSpawns = Bukkit.getPluginManager().isPluginEnabled("geSuitSpawn");


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
                CHANNEL_NAME, new TeleportsMessageListener());
        Bukkit.getMessenger().registerOutgoingPluginChannel(this,
                CHANNEL_NAME);
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(
                new TeleportsListener(), this);
    }

    public static geSuitTeleports getInstance() {
        return instance;
    }

    public static WorldGuardPlugin getWorldGuard() {
        return mWorldGuard;
    }

}
