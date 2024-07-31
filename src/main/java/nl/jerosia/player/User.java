package nl.jerosia.player;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;

public class User implements Comparable<User>, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final transient OfflinePlayer player;

    public User(OfflinePlayer player) {
        this.player = player;
    }

    @Override
    public int compareTo(@NotNull User o) {
        return 0;
    }

    public Player getBase() {
        return player.getPlayer();
    }
}
