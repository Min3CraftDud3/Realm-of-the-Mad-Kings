package lib.simplecache.exception;

import org.bukkit.entity.Player;

/**
 * @author Goblom
 */
public class UUIDException extends Exception {
    private static final long serialVersionUID = 1L;

    public UUIDException(String player) {
        super("Unable to get the UUID for player '" + player + "'. Is account migrated?");
    }

    public UUIDException(Player player) {
        this(player.getName());
    }
}
