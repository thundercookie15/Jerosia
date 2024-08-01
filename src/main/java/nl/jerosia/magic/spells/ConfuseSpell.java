package nl.jerosia.magic.spells;

import nl.jerosia.magic.Spell;
import nl.jerosia.player.User;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ConfuseSpell implements Spell {
    @Override
    public int getSpellId() {
        return 3;
    }

    @Override
    public String getSpellName() {
        return "Confuse";
    }

    @Override
    public void fire(User user) {
        World w = user.getBase().getWorld();
        Location target = user.getBase().getTargetBlock(null, 40).getLocation();

        final Firework firework = w.spawn(getCenter(target), Firework.class);
        FireworkMeta fwmeta = firework.getFireworkMeta();
        FireworkEffect effect = FireworkEffect.builder()
                .flicker(true)
                .trail(false)
                .with(FireworkEffect.Type.BURST)
                .withColor(Color.PURPLE, Color.BLACK)
                .build();

        fwmeta.addEffect(effect);
        firework.setFireworkMeta(fwmeta);

        for (int x = target.getBlockX() - 5; x < target.getBlockX() + 5; x++) {
            for (int z = target.getBlockZ() - 7; z < target.getBlockZ() + 7; z++) {
                for (int y = target.getBlockY(); y < target.getBlockY() + 7; y++) {
                    w.spawnParticle(Particle.SMOKE, x, y, z, 2, 0, 0, 0, 0.01);
                }
            }
        }

        for (Entity entity : firework.getNearbyEntities(5, 7, 7)) {
            if (entity instanceof Player) {
                Player plr = ((Player) entity).getPlayer();
                plr.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 20 * 15, 1));
            }
        }
        firework.detonate();
    }

    private Location getCenter(Location loc) {
        return new Location(loc.getWorld(),
                getRelativeCoord(loc.getBlockX()),
                loc.getBlockY() + 1,
                getRelativeCoord(loc.getBlockZ()));
    }

    private double getRelativeCoord(int i) {
        return (double) i + .5;
    }
}
