package nl.jerosia.magic.spells;

import nl.jerosia.magic.Spell;
import nl.jerosia.player.User;

public class LightningStormSpell implements Spell {
    @Override
    public int getSpellId() {
        return 3;
    }

    @Override
    public String getSpellName() {
        return "Lightning Storm";
    }

    @Override
    public void fire(User user) {

    }
}
