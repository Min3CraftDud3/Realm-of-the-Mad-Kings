package lib.simplecache;

import lib.simplecache.cache.CachedPlayerData;
import lib.simplecache.cache.CachedPlayerStats;
import lib.simplecache.exception.UUIDException;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Goblom
 */
public class SimpleCache {
    protected static SimpleCache cache = null;

    public static CachedPlayer getCachedPlayer(String player) throws UUIDException {
        loadCache();
        return cache.getPlayer(player);
    }

    public static CachedPlayer getCachedPlayer(Player player) throws UUIDException {
        loadCache();
        return cache.getPlayer(player);
    }

    public static void loadCache() {
        if (cache == null) {
            cache = new SimpleCache();
        }
    }

    /**
     * *******************************
     * Start with actual class
     * ********************************
     */

    public SimpleCache() {
        this.playerData = new CachedPlayerData();
        this.playerStats = new CachedPlayerStats();

		/* Create Table on Database */
        playerData.createTable();
        playerStats.createTable();

		/* Load data from database */
        playerData.update();
        playerStats.update();
    }

    protected final CachedPlayerData playerData;
    protected final CachedPlayerStats playerStats;

    private final Map<String, CachedPlayer> players = new HashMap<String, CachedPlayer>();

    protected CachedPlayer getPlayer(String player) throws UUIDException {
        if (!this.players.containsKey(player)) {
            this.players.put(player, new CachedPlayer(player));
        }
        return this.players.get(player);
    }

    protected CachedPlayer getPlayer(Player player) throws UUIDException {
        return getPlayer(player.getName());
    }
}
