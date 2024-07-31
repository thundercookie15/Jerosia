package nl.jerosia.magic.spells;

import nl.jerosia.magic.Spell;
import nl.jerosia.player.User;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.util.Vector;

public class LeapSpell implements Spell {

    @Override
    public String getSpellName() {
        return "Leap";
    }

    @Override
    public int getSpellId() {
        return 1;
    }

    @Override
    public void fire(User user) {
        user.getBase().playSound(user.getBase().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 100, 1F);
        Location jump = user.getBase().getEyeLocation();
        Vector vector = jump.getDirection().normalize().multiply(4);
        user.getBase().setVelocity(vector);
    }
}
