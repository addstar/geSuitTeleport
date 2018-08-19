package net.cubespace.geSuitTeleports.utils;

import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.RegionQuery;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.cubespace.geSuitTeleports.geSuitTeleports;
import net.cubespace.geSuitTeleports.managers.TeleportsManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import static net.cubespace.geSuitTeleports.geSuitTeleports.logDebugMessages;

/**
 * The type Location util.
 */
public class LocationUtil {
    
    /**
     * The type Vector 3 d.
     */
    public static class Vector3D
    {
        /**
         * The X.
         */
        private final int x;
        /**
         * The Y.
         */
        private final int y;
        /**
         * The Z.
         */
        private final int z;
    
        /**
         * Instantiates a new Vector 3 d.
         *
         * @param x the x
         * @param y the y
         * @param z the z
         */
        public Vector3D(int x, int y, int z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    
        /**
         * Get x int.
         *
         * @return the int
         */
        protected int getX(){
            return x;
        }
    
        /**
         * Get y int.
         *
         * @return the int
         */
        protected int getY(){
            return y;
        }
    
        /**
         * Gets z.
         *
         * @return the z
         */
        protected int getZ() {
            return z;
        }
    }
    
    /**
     * The constant RADIUS.
     */
    public static final int RADIUS = 16;
    /**
     * The constant VOLUME.
     */
    public static final Vector3D[] VOLUME;

    static
    {
        List<Vector3D> pos = new ArrayList<>();
        for (int x = -RADIUS; x <= RADIUS; x++)
        {
            for (int y = -RADIUS; y <= RADIUS; y++)
            {
                for (int z = -RADIUS; z <= RADIUS; z++)
                {
                    pos.add(new Vector3D(x, y, z));
                }
            }
        }
        pos.sort(Comparator.comparingInt(a -> (a.getX() * a.getX() + a.getY() * a.getY() + a.getZ() * a
                .getZ())));
        VOLUME = pos.toArray(new Vector3D[0]);
    }
    
    /**
     * Is block above air boolean.
     *
     * @param world the world
     * @param x     the x
     * @param y     the y
     * @param z     the z
     *
     * @return the boolean
     */
    static boolean isBlockAboveAir(final World world, final int x, final int y, final int z) {
        return y > world.getMaxHeight() || !world.getBlockAt(x, y - 1,
                z).getType().isSolid();
    }
    
    /**
     * Is block unsafe boolean.
     *
     * @param world the world
     * @param x     the x
     * @param y     the y
     * @param z     the z
     *
     * @return the boolean
     */
    public static boolean isBlockUnsafe(final World world, final int x, final int y, final int z) {
        return isBlockDamaging(world, x, y, z) || isBlockAboveAir(world, x, y, z);

    }
    
    /**
     * Is block damaging boolean.
     *
     * @param world the world
     * @param x     the x
     * @param y     the y
     * @param z     the z
     *
     * @return the boolean
     */
    public static boolean isBlockDamaging(final World world, final int x, final int y, final int z) {
        final Block below = world.getBlockAt(x, y - 1, z);
        Material magma = null;
        try {
            if (geSuitTeleports.getInstance().getServer().getVersion().equals("1.13"))
                magma = Material.getMaterial("MAGMA_BLOCK");
        } catch (Exception e) {
            magma = Material.getMaterial("MAGMA");
        }
        if (below.getType() == Material.LAVA || below.getType() == magma) {
            return true;
        }
        if (below.getType() == Material.FIRE) {
            return true;
        }
        return (world.getBlockAt(x, y, z).getType()).isSolid() || (world
                .getBlockAt(x, y + 1, z).getType().isSolid());
    }
    
    /**
     * Gets rounded destination.
     *
     * @param loc the loc
     *
     * @return the rounded destination
     */
// Not needed if using getSafeDestination(loc)
    public static Location getRoundedDestination(final Location loc) {
        final World world = loc.getWorld();
        int x = loc.getBlockX();
        int y = (int) Math.round(loc.getY());
        int z = loc.getBlockZ();
        return new Location(world, x + 0.5, y, z + 0.5, loc.getYaw(), loc.getPitch());
    }
    
    /**
     * Gets safe destination.
     *
     * @param player the player
     * @param loc    the loc
     *
     * @return the safe destination
     */
    public static Location getSafeDestination(final Player player, final Location loc) {
        if (loc.getWorld().equals(player.getWorld())
                && ((player.getGameMode() == GameMode.CREATIVE) || (player.isFlying()))) {
            return getRoundedDestination(loc);
        }

        return getSafeDestination(loc);
    }
    
    /**
     * Gets safe destination.
     *
     * @param loc the loc
     *
     * @return the safe destination
     */
    public static Location getSafeDestination(final Location loc) {
        if (loc == null || loc.getWorld() == null) {
            return null;
        }

        final World world = loc.getWorld();
        int x = loc.getBlockX();
        int y = (int) Math.round(loc.getY());
        int z = loc.getBlockZ();
        final int origX = x;
        final int origY = y;
        final int origZ = z;
        while (isBlockAboveAir(world, x, y, z)) {
            y -= 1;
            if (y < 0) {
                y = origY;
                break;
            }
        }
        if (isBlockUnsafe(world, x, y, z)) {
            x = Math.round(loc.getX()) == origX ? x - 1 : x + 1;
            z = Math.round(loc.getZ()) == origZ ? z - 1 : z + 1;
        }
        int i = 0;
        while (isBlockUnsafe(world, x, y, z)) {
            i++;
            if (i >= VOLUME.length) {
                x = origX;
                y = origY + RADIUS;
                z = origZ;
                break;
            }
            x = origX + VOLUME[i].getX();
            y = origY + VOLUME[i].getY();
            z = origZ + VOLUME[i].getZ();
        }
        while (isBlockUnsafe(world, x, y, z)) {
            y += 1;
            if (y >= world.getMaxHeight()) {
                x += 1;
                break;
            }
        }
        while (isBlockUnsafe(world, x, y, z)) {
            y -= 1;
            if (y <= 1) {
                x += 1;
                y = world.getHighestBlockYAt(x, z);
                if (x - 48 > loc.getBlockX()) {
                    return null;
                }
            }
        }
        return new Location(world, x + 0.5, y, z + 0.5, loc.getYaw(), loc.getPitch());
    }
    
    /**
     * Should fly boolean.
     *
     * @param loc the loc
     *
     * @return the boolean
     */
    public static boolean shouldFly(Location loc) {
        final World world = loc.getWorld();
        final int x = loc.getBlockX();
        int y = (int) Math.round(loc.getY());
        final int z = loc.getBlockZ();
        int count = 0;
        while (LocationUtil.isBlockUnsafe(world, x, y, z) && y > -1) {
            y--;
            count++;
            if (count > 2) {
                return true;
            }
        }

        return y < 0;
    }
    
    /**
     * World guard tp allowed boolean.
     *
     * @param l the l
     * @param p the p
     *
     * @return the boolean
     */
    public static boolean worldGuardTpAllowed(Location l, Player p) {
        boolean result = true;
        Logger log = geSuitTeleports.instance.getLogger();
        if(logDebugMessages) log.info("Checking if WG allows TP. Status of Plugin:"+geSuitTeleports.worldGuarded);//Todo remove after debug
        if (geSuitTeleports.worldGuarded) {
            try {
                RegionContainer container = geSuitTeleports.getWorldGuard().getRegionContainer();
                RegionQuery query = container.createQuery();
                ApplicableRegionSet set = query.getApplicableRegions(l);
                if (!set.isVirtual()) {//VirtualSet indicates that there is no region protection to check
                    for (ProtectedRegion region : set) {
                        Set<String> flags = region.getFlag(DefaultFlag.BLOCKED_CMDS);
                        if (flags != null) {
                            if (logDebugMessages) log.info("Blocked Commands Found:" + flags.toString());
                            for (String cmd : flags) {
                                if (geSuitTeleports.deny_Teleport.contains(cmd)) {
                                    if (logDebugMessages) log.info("Test for " + cmd + " was true.");
                                    if (p.hasPermission("worldgaurd.teleports.allregions") || TeleportsManager.administrativeTeleport.contains(p)) {
                                        p.sendMessage(geSuitTeleports.tp_admin_bypass);
                                        if (logDebugMessages)
                                            log.info("Player:" + p.getDisplayName() + ":" + geSuitTeleports.tp_admin_bypass + "Location: Region=" + region.getId());
                                        TeleportsManager.administrativeTeleport.remove(p);
                                        result = true;
                                    } else {
                                        p.sendMessage(geSuitTeleports.location_blocked);
                                        result = false;
                                    }
                                }
                            }
                            if (logDebugMessages)
                                log.info("Tests on List:" + geSuitTeleports.deny_Teleport.toString() + " completed");
                        } else {
                            if (logDebugMessages)
                                log.info("FLAGS was null");
                        }
                    }
                } else {
                    if (logDebugMessages)
                        log.info("Region set was virtual");
                }
            }catch (NoClassDefFoundError e){
                e.printStackTrace();
                result = true;
            }
        }
        log.info("World gaurd check for TP completed: Player=" + p.getDisplayName() + " Location=(" + l.toString() + ") Region TP Allowed=" + result);
        return result;
    }
}
