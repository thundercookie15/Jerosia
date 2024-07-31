package nl.jerosia.wands.magic;

import nl.jerosia.wands.magic.spells.ExplosiveSpell;

public enum Spells {
    EXPLOSIVE(new ExplosiveSpell(), "Explosive", 0),;

    private final ISpell spell;

    Spells(ISpell spell, String name, int spellId) {
        this.spell = spell;
    }

    public ISpell getSpell() {
        return spell;
    }

    public static ISpell nextSpell(int selectedSpell) {
        return Spells.EXPLOSIVE.getSpell();
    }
}
