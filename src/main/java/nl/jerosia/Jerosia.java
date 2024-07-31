package nl.jerosia;

import nl.jerosia.commands.CmdItem;
import nl.jerosia.events.ChatFormatter;
import nl.jerosia.events.UserEvents;
import nl.jerosia.player.DataPlayer;
import nl.jerosia.player.User;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;

public final class Jerosia extends JavaPlugin {

    private static Jerosia instance;

    public static String CHATFORMAT = "<{prefix}{name}{suffix}> {message}";

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        this.registerUsers();

        this.getCommand("item").setExecutor(new CmdItem(this));
        this.getServer().getPluginManager().registerEvents(new ChatFormatter(this), this);
        this.getServer().getPluginManager().registerEvents(new UserEvents(this), this);

    }

    @Override
    public void onDisable() {
        this.unregisterUsers();
    }

    public static Jerosia getInstance() {
        return instance;
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

