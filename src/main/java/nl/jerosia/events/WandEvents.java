package nl.jerosia.events;

import nl.jerosia.Jerosia;
import nl.jerosia.magic.Spells;
import nl.jerosia.player.User;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import static nl.jerosia.items.Items.EMPIRE_WAND;
import static nl.jerosia.items.Items.isHoldingCustomItem;

public class WandEvents implements Listener {

    private final Jerosia plugin;

    public WandEvents(Jerosia plugin) {
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
            user.send("&6[&7X&6] &6Selected: &7%s".formatted(user.getSelectedSpell().getSpellName()));
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        final User user = this.plugin.getUser(event.getPlayer());
        if (isHoldingEmpireWand(user)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLeapFall(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            if (!(event.getEntity() instanceof Player)) return;
            final User user = this.plugin.getUser((Player) event.getEntity());
            if (isHoldingCustomItem(user.getItemInMainHand(), EMPIRE_WAND) && user.getSelectedSpell() == Spells.LEAP.getSpell()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onExplosiveHit(EntityDamageEvent event) {
        if (event.getEntity().hasMetadata("explosive_fireball") && event.getEntity().getType() == EntityType.FIREBALL) {
            event.setDamage(0);
            event.setCancelled(true);
        }
        if (event.getEntity().hasMetadata("explosive_firework") && event.getEntity().getType() == EntityType.FIREWORK_ROCKET) {
            event.setDamage(0);
            event.setCancelled(true);
        }
    }

    private boolean isHoldingEmpireWand(User user) {
        return isHoldingCustomItem(user.getItemInMainHand(), EMPIRE_WAND);
    }
}
