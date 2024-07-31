package nl.jerosia.player;

import nl.jerosia.magic.ISpell;
import nl.jerosia.magic.Spells;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;

import static nl.jerosia.utils.FormatUtils.MESSAGE_PREFIX;

public class User implements Comparable<User>, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final transient OfflinePlayer player;
    private ISpell selectedSpell;

    public User(OfflinePlayer player) {
        this.player = player;
    }

    @Override
    public int compareTo(@NotNull User o) {
        return 0;
    }

    public Player getBase() {
        return player.getPlayer();
    }

    public void send(String message) {
        getBase().sendMessage(MESSAGE_PREFIX + message);
    }

    public void send(String[] messages) {
        getBase().sendMessage(MESSAGE_PREFIX + Arrays.toString(messages));
    }

    public ItemStack getItemInMainHand() {
        return player.getPlayer().getInventory().getItemInMainHand();
    }

    public Location getLocation() {
        return player.getPlayer().getLocation();
    }

    public World getWorld() {
        return player.getPlayer().getWorld();
    }

    public void fireSpell() {
        getSelectedSpell().fire(this);
    }

    public void fireSpell(ISpell spell) {
        spell.fire(this);
    }

    public ISpell getSelectedSpell() {
        if (selectedSpell == null) selectedSpell = Spells.EXPLOSIVE.getSpell();
        return selectedSpell;
    }

    public void selectNextSpell() {
        if (this.selectedSpell == null) selectedSpell = Spells.EXPLOSIVE.getSpell();
        if (getSelectedSpell().isToggleSpell()) this.selectedSpell.stopCasting(this);
        this.selectedSpell = Spells.nextSpell(getSelectedSpell().getSpellId());
    }


}
