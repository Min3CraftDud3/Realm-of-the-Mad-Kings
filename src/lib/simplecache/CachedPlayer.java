package lib.simplecache;

import lib.simplecache.cache.CachedPlayerData;
import lib.simplecache.cache.CachedPlayerStats;
import lib.simplecache.exception.UUIDException;
import lib.simplecache.util.uuid.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Goblom
 */
public class CachedPlayer {
    private final String playerName;
    private final UUID uuid;

    private final CachedPlayerData.Row playerData;
    private final CachedPlayerStats.Row playerStats;

    public CachedPlayer(String player) throws UUIDException {
        this.playerName = player;
        try {
            this.uuid = UUIDFetcher.call(player);
        } catch (Exception e) {
            throw new UUIDException(player);
        }

        if (SimpleCache.cache.playerData.getRow(uuid) == null) {
            SimpleCache.cache.playerData.newEntry(uuid);
        }

        if (SimpleCache.cache.playerStats.getRow(uuid) == null) {
            SimpleCache.cache.playerStats.newEntry(uuid);
        }

        this.playerData = SimpleCache.cache.playerData.getRow(uuid);
        this.playerStats = SimpleCache.cache.playerStats.getRow(uuid);
    }

    public String getUsername() {
        return playerName;
    }

    public UUID getUUID() {
        return uuid;
    }

    @SuppressWarnings("deprecation")
    public Player getPlayer() {
        return Bukkit.getPlayerExact(this.playerName);
    }

    public int getCoins() {
        return playerData.getCoins();
    }

    public int getAttack() {
        return playerData.getAttack();
    }

    public int getDefense() {
        return playerData.getDefense();
    }

    public int getDexterity() {
        return playerData.getDexterity();
    }

    public int getSpeed() {
        return playerData.getSpeed();
    }

    public int getMagic() {
        return playerData.getMagic();
    }

    public void incrementCoins(int value) {
        playerData.incrementCoins(value);
    }

    public void incrementAttack(int value) {
        playerData.incrementAttack(value);
    }

    public void incrementDefense(int value) {
        playerData.incrementDefense(value);
    }

    public void incrementDexterity(int value) {
        playerData.incrementDexterity(value);
    }

    public void incrementSpeed(int value) {
        playerData.incrementSpeed(value);
    }

    public void incrementMagic(int value) {
        playerData.incrementMagic(value);
    }

    public void setCoins(int value) {
        playerData.setCoins(value);
    }

    public void setAttack(int value) {
        playerData.setAttack(value);
    }

    public void setDefense(int value) {
        playerData.setDefense(value);
    }

    public void setDexterity(int value) {
        playerData.setDexterity(value);
    }

    public void setSpeed(int value) {
        playerData.setSpeed(value);
    }

    public void setMagic(int value) {
        playerData.setMagic(value);
    }

    public int getKills() {
        return playerStats.getKills();
    }

    public int getDeaths() {
        return playerStats.getDeaths();
    }

    public void incrementDeaths(int value) {
        playerStats.incrementDeaths(value);
    }

    public void incrementKills(int value) {
        playerStats.incrementKills(value);
    }

    public String getClassName() {
        return playerStats.getClassName();
    }

    public void setKills(int value) {
        playerStats.setKills(value);
    }

    public void setDeaths(int value) {
        playerStats.setDeaths(value);
    }

    public void setClass(String value) {
        playerStats.setClass(value);
    }

}
