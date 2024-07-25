package nl.jerosia.events;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.milkbowl.vault.chat.Chat;
import nl.jerosia.Jerosia;
import nl.jerosia.utils.FormatUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.bukkit.event.server.ServiceUnregisterEvent;

import static nl.jerosia.Jerosia.CHATFORMAT;

public class ChatFormatter implements Listener {
    private static final String NAME_PLACEHOLDER = "{name}";
    private static final String DISPLAYNAME_PLACEHOLDER = "{displayname}";
    private static final String MESSAGE_PLACEHOLDER = "{message}";
    private static final String PREFIX_PLACEHOLDER = "{prefix}";
    private static final String SUFFIX_PLACEHOLDER = "{suffix}";
    private static final String DEFAULT_FORMAT = "<{prefix}{name}{suffix}> {message}";
    private final Jerosia plugin;
    private String format;
    private Chat vaultChat = null;

    private LuckPerms luckPerms;

    public ChatFormatter(Jerosia plugin) {
        this.plugin = plugin;
        this.luckPerms = this.plugin.getServer().getServicesManager().load(LuckPerms.class);

        this.reloadConfigValues();
        this.refreshVault();
    }

    private static String colorize(String s) {
        return FormatUtils.parseColors(s);
    }

    public void reloadConfigValues() {
        this.format = colorize(DEFAULT_FORMAT.replace(DISPLAYNAME_PLACEHOLDER, "%1$s").replace(MESSAGE_PLACEHOLDER, "%2$s"));
    }

    public void refreshVault() {
        Chat vaultChat = this.plugin.getServer().getServicesManager().load(Chat.class);
        if (vaultChat != this.vaultChat) {
            this.plugin.getLogger().info("New Vault Chat implementation registered: " + (vaultChat == null ? "null" : vaultChat.getName()));
        }

        this.vaultChat = vaultChat;
    }

    @EventHandler
    public void onServiceChange(ServiceRegisterEvent event) {
        if (event.getProvider().getService() == Chat.class) this.refreshVault();
    }

    @EventHandler
    public void onServiceChange(ServiceUnregisterEvent event) {
        if (event.getProvider().getService() == Chat.class) this.refreshVault();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatLow(AsyncPlayerChatEvent event) {
        if (format != null) event.setFormat(this.format);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChatHigh(AsyncPlayerChatEvent e) {
        String format = e.getFormat();
        final Player player = e.getPlayer();

        final CachedMetaData metaData = this.luckPerms.getPlayerAdapter(Player.class).getMetaData(player);

        if (this.vaultChat != null) {
            format = format.replace(PREFIX_PLACEHOLDER, colorize(metaData.getPrefix() != null ? metaData.getPrefix() : this.vaultChat.getPlayerPrefix(player)));
            format = format.replace(SUFFIX_PLACEHOLDER, colorize(metaData.getSuffix() != null ? metaData.getSuffix() : this.vaultChat.getPlayerSuffix(player)));
        }
        format = format.replace(NAME_PLACEHOLDER, e.getPlayer().getName());
        e.setFormat(format);
    }
}
