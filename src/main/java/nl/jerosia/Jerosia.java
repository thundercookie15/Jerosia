package nl.jerosia;

import nl.jerosia.events.ChatFormatter;
import org.bukkit.plugin.java.JavaPlugin;

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
}
