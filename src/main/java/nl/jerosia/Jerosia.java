package nl.jerosia;

import nl.jerosia.events.ChatFormatter;
import nl.jerosia.player.DataPlayer;
import nl.jerosia.player.User;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;

public final class Jerosia extends JavaPlugin {

    public static String CHATFORMAT = "<{prefix}{name}{suffix}> {message}";

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getServer().getPluginManager().registerEvents(new ChatFormatter(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerUsers() {
        DataPlayer.registerPlayers();
    }

    public void unregisterUsers() {
        DataPlayer.unregister();
    }

    public User getUser(Player player) {
        return DataPlayer.getUser(player);
    }

    public User getUser(OfflinePlayer player) {
        return DataPlayer.getUser(player);
    }

    public User getUser(UUID uuid) {
        return DataPlayer.getUser(uuid);
    }

    public List<User> getOnlineUsers() {
        return DataPlayer.getUserList();
    }
}

