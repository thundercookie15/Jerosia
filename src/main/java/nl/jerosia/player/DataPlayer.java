package nl.jerosia.player;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class DataPlayer {

    private static final HashMap<Player, DataPlayer> dataPlayers = new HashMap<>();
    private static final HashMap<OfflinePlayer, User> users = new HashMap<>();

    public final Player player;

    private DataPlayer(Player player) {
        this.player = player;
    }

    public static DataPlayer get(Player player) {
        return dataPlayers.get(player);
    }

    public static User getUser(Player player) {
        return getUser((OfflinePlayer) player);
    }

    public static User getUser(UUID uuid) {
        return getUser(Bukkit.getOfflinePlayer(uuid));
    }

    public static User getUser(OfflinePlayer player) {
        users.computeIfAbsent(player, User::new);
        return users.get(player);
    }

    public static void registerPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            dataPlayers.computeIfAbsent(player, DataPlayer::new);
            users.computeIfAbsent(player, k -> new User(player));
        }
    }

    public static void unregister() {
        final Set<Player> dP = dataPlayers.keySet();
        final Set<OfflinePlayer> uS = users.keySet();

        while (!dP.isEmpty()) {
            unregisterPlayer(dP.iterator().next());
        }
        while (!uS.isEmpty()) {
            unregisterPlayer(uS.iterator().next());
        }
    }

    public static List<User> getUserList() {
        return new ArrayList<>(users.values());
    }

    public static void unregisterPlayer(OfflinePlayer player) {
        if (player instanceof Player) {
            dataPlayers.remove(player);
        }
        users.remove(player);
    }
}
