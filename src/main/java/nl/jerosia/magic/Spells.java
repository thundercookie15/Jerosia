package nl.jerosia.magic;

import nl.jerosia.magic.spells.ExplosiveSpell;
import nl.jerosia.magic.spells.LeapSpell;

public enum Spells {
    EXPLOSIVE(new ExplosiveSpell(), "Explosive", 0),
    LEAP(new LeapSpell(), "Leap", 1),;

    private final ISpell spell;

    Spells(ISpell spell, String name, int spellId) {
        this.spell = spell;
    }

    public ISpell getSpell() {
        return spell;
    }

    public static ISpell nextSpell(int selectedSpell) {
        for (Spells spells : Spells.values()) {
            if (spells.getSpell().getSpellId() > selectedSpell) {
                return spells.getSpell();
            }
        }
        return Spells.EXPLOSIVE.getSpell();
    }
}
