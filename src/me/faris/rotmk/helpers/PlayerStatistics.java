package me.faris.rotmk.helpers;

import lib.simplecache.CachedPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerStatistics {
    private String playerName = "";

    private int coins, attack, defense, dexterity, speed, magic;

    public PlayerStatistics(String player, int coins, int attack, int defense, int dexterity, int speed, int magic) {
        this.playerName = player;
        this.coins = coins;
        this.attack = attack;
        this.defense = defense;
        this.dexterity = dexterity;
        this.speed = speed;
        this.magic = magic;
    }

    public PlayerStatistics(String player, CachedPlayer cachedPlayer) {
        this.playerName = player;
        this.coins = cachedPlayer.getCoins();
        this.attack = cachedPlayer.getAttack();
        this.defense = cachedPlayer.getDefense();
        this.dexterity = cachedPlayer.getDexterity();
        this.speed = cachedPlayer.getSpeed();
        this.magic = cachedPlayer.getMagic();
    }

    public int getAttack() {
        return this.attack;
    }

    public int getCoins() {
        return this.coins;
    }

    public int getDefense() {
        return this.defense;
    }

    public int getDexterity() {
        return this.dexterity;
    }

    public int getMagic() {
        return this.magic;
    }

    public String getName() {
        return this.playerName;
    }

    @SuppressWarnings("deprecation")
    public Player getPlayer() {
        return Bukkit.getPlayerExact(this.playerName);
    }

    public int getSpeed() {
        return this.speed;
    }

}
