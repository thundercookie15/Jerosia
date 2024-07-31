package nl.jerosia.magic.spells;

import nl.jerosia.Jerosia;
import nl.jerosia.player.User;
import nl.jerosia.magic.ISpell;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class ExplosiveSpell implements ISpell {

    private final int spellId = 0;
    private final String spellName = "Explosive";

    private User caster;

    public ExplosiveSpell() {}

    public void fire(User user) {
        caster = user;

        Fireball explosive = explosiveFireball();

        new BukkitRunnable()  {
            @Override
            public void run() {
                if (!(explosive.isValid())) {
                    caster.getWorld().createExplosion(explosive.getLocation(), 10F, false, false, caster.getBase());
                    this.cancel();
                }

                if (explosive.getTicksLived() > 20*3) {
                    caster.getWorld().createExplosion(explosive.getLocation(), 10F, false, false, caster.getBase());
                    explosive.remove();
                    this.cancel();
                }
                final Firework firework = explosiveFirework(explosive);
                firework.detonate();
            }
        }.runTaskTimer(Jerosia.getInstance(), 3,1 );
    }

    @Override
    public int getSpellId() {
        return spellId;
    }

    @Override
    public String getSpellName() {
        return spellName;
    }

    private Fireball explosiveFireball() {
        Location spawnLocation = caster.getBase().getLocation();
        spawnLocation.setY(caster.getBase().getLocation().getY() + .8);
        Fireball explosive = caster.getWorld().spawn(spawnLocation, Fireball.class);
        explosive.setShooter(caster.getBase());
        explosive.setVelocity(caster.getBase().getEyeLocation().getDirection().multiply(3));
        explosive.setBounce(false);
        explosive.setIsIncendiary(false);
        explosive.setYield(0F);
        return explosive;

    }

    private Firework explosiveFirework(Fireball explosive) {
        Firework firework = caster.getWorld().spawn(explosive.getLocation(), Firework.class);
        firework.setSilent(true);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        FireworkEffect fireworkEffect = FireworkEffect.builder()
                .flicker(true)
                .trail(false)
                .with(FireworkEffect.Type.BALL)
                .withColor(Color.fromRGB(99, 17, 16), Color.BLACK)
                .build();
        fireworkMeta.addEffect(fireworkEffect);
        firework.setFireworkMeta(fireworkMeta);
        return firework;
    }
}
