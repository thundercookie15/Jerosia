package nl.jerosia.events;

import nl.jerosia.Jerosia;
import nl.jerosia.player.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static nl.jerosia.items.Items.EMPIRE_WAND;
import static nl.jerosia.items.Items.isHoldingCustomItem;

public class UserEvents implements Listener {

    private final Jerosia plugin;

    public UserEvents(Jerosia plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEmpireWandUse(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        final User user = this.plugin.getUser(event.getPlayer());

        if (!(isHoldingEmpireWand(user))) return;

        if (event.getAction() == Action.LEFT_CLICK_AIR) {
            user.fireSpell();
        }

        if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            user.selectNextSpell();
        }
    }

    private boolean isHoldingEmpireWand(User user) {
        return isHoldingCustomItem(user.getItemInMainHand(), EMPIRE_WAND);
    }
}