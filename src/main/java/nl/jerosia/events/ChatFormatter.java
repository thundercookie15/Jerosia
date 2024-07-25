package nl.jerosia.events;

import net.milkbowl.vault.chat.Chat;
import nl.jerosia.Jerosia;
import nl.jerosia.utils.FormatUtils;
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

    public ChatFormatter(Jerosia plugin) {
        this.plugin = plugin;

        this.reloadConfigValues();
        this.refreshVault();
    }

    private static String colorize(String s) {
        return FormatUtils.parseColors(s);
    }

    public void reloadConfigValues() {
        this.format = colorize(CHATFORMAT.replace(DISPLAYNAME_PLACEHOLDER, "%1$s").replace(MESSAGE_PLACEHOLDER, "%2$s"));
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChatHigh(AsyncPlayerChatEvent e) {
        String format = e.getFormat();
        if (vaultChat != null && format.contains(PREFIX_PLACEHOLDER)) {
            format = format.replace(PREFIX_PLACEHOLDER, colorize(vaultChat.getPlayerPrefix(e.getPlayer())));
        }
        if (vaultChat != null && format.contains(SUFFIX_PLACEHOLDER)) {
            format = format.replace(SUFFIX_PLACEHOLDER, colorize(vaultChat.getPlayerSuffix(e.getPlayer())));
        }
        format = format.replace(NAME_PLACEHOLDER, e.getPlayer().getName());
        e.setFormat(format);
    }
}
