package nl.jerosia.magic.spells;

import nl.jerosia.Jerosia;
import nl.jerosia.magic.Spell;
import nl.jerosia.player.User;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class LightningStormSpell implements Spell {
    @Override
    public int getSpellId() {
        return 2;
    }

    @Override
    public String getSpellName() {
        return "Lightning Storm";
    }

    @Override
    public void fire(User user) {
        World world = user.getBase().getWorld();
        Location circleOne = user.getBase().getLocation();
        Location circleTwo = user.getBase().getLocation();

        ArmorStand armorStand = (ArmorStand) world.spawnEntity(circleOne, EntityType.ARMOR_STAND);
        armorStand.setInvisible(true);
        armorStand.setGravity(false);

        double posX = user.getBase().getLocation().getX();
        double posZ = user.getBase().getLocation().getZ();
        double y = user.getBase().getLocation().getY() + 30;
        new BukkitRunnable() {
            double rightCircleHalf = 0;
            double leftCircleHalf = 0;
            final double circleRadius = 20;

            @Override
            public void run() {
                rightCircleHalf = rightCircleHalf + Math.PI / 96;
                leftCircleHalf = leftCircleHalf - Math.PI / 96;

                double x1 = posX + circleRadius * Math.cos(rightCircleHalf);
                double z1 = posZ + circleRadius * Math.sin(rightCircleHalf);
                double x2 = posX + circleRadius * Math.cos(leftCircleHalf);
                double z2 = posZ + circleRadius * Math.sin(leftCircleHalf);

                circleOne.setX(x1);
                circleOne.setZ(z1);
                circleOne.setY(y);
                circleTwo.setX(x2);
                circleTwo.setZ(z2);
                circleTwo.setY(y);

                spawnFirework(circleOne);
                spawnFirework(circleTwo);

                if (rightCircleHalf >= Math.PI) {
                    Location finalLocation = armorStand.getLocation();
                    finalLocation.setY(finalLocation.getY() + 18);

                    endingFirework(finalLocation);

                    for (Entity entity : armorStand.getNearbyEntities(circleRadius, 30, circleRadius)) {
                        if (entity instanceof Player || entity instanceof Monster || entity instanceof Phantom) {
                            if (!(entity == user.getBase()))
                            entity.getWorld().strikeLightning(entity.getLocation());
                        }
                    }

                    armorStand.remove();
                    this.cancel();
                }

            }
        }.runTaskTimer(Jerosia.getInstance(), 0, 1);
    }

    private void spawnFirework(Location location) {
        Firework firework = location.getWorld().spawn(location, Firework.class);
        firework.setSilent(true);
        FireworkMeta meta = firework.getFireworkMeta();
        meta.setPower(0);
        FireworkEffect effect = FireworkEffect.builder()
                .flicker(false)
                .trail(false)
                .with(FireworkEffect.Type.BALL)
                .withColor(org.bukkit.Color.AQUA)
                .build();
        meta.addEffect(effect);
        firework.setFireworkMeta(meta);
        firework.detonate();
    }

    private void endingFirework(Location location) {
        Firework firework = location.getWorld().spawn(location, Firework.class);
        firework.setSilent(true);
        FireworkMeta meta = firework.getFireworkMeta();
        meta.setPower(0);
        FireworkEffect effect = FireworkEffect.builder()
                .flicker(false)
                .trail(false)
                .with(FireworkEffect.Type.STAR)
                .withColor(Color.AQUA)
                .build();
        meta.addEffect(effect);
        firework.setFireworkMeta(meta);
        firework.detonate();
    }
}
