package nl.jerosia.wands.magic;

import nl.jerosia.player.User;

public interface ISpell {

    void fire(User user);
    int getSpellId();
    String getSpellName();

    default void startCasting(User user) {}
    default void stopCasting(User user) {}
    default boolean isCastingSpell(User user) {return false;}
    default boolean isToggleSpell() {
        return false;
    }
}
