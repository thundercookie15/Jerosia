package nl.jerosia.magic;

import nl.jerosia.magic.spells.ExplosiveSpell;
import nl.jerosia.magic.spells.LeapSpell;
import nl.jerosia.magic.spells.LightningStormSpell;

import java.util.Arrays;

public enum Spells {
    EXPLOSIVE(new ExplosiveSpell(), "Explosive", 0),
    LEAP(new LeapSpell(), "Leap", 1),
    LIGHTNING_STORM(new LightningStormSpell(), "Lightning Storm", 2),;

    private final Spell spell;

    Spells(Spell spell, String name, int spellId) {
        this.spell = spell;
    }

    public Spell getSpell() {
        return spell;
    }

    public int getSpellId() {
        return spell.getSpellId();
    }

    public static Spell nextSpell(int selectedSpell) {
        for (Spells spells : Arrays.stream(Spells.values()).toList()) {
            if (spells.getSpell().getSpellId() > selectedSpell) {
                return spells.getSpell();
            }
        }
        return Spells.EXPLOSIVE.getSpell();
    }
}
