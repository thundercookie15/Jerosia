package nl.jerosia.commands;

import nl.jerosia.Jerosia;
import nl.jerosia.items.Items;
import nl.jerosia.player.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class CmdItem implements TabExecutor {

    private final Jerosia plugin;

    public CmdItem(Jerosia plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;
        User user = this.plugin.getUser((Player) sender);
        if (args.length == 0) {
            user.send("Usage: /item <item>");
            return false;
        }
        Items item = Items.getItemByName(args[0]);
        if (item == null) {
            user.send("Item not found");
            return false;
        }

        user.getBase().getInventory().addItem(item.getItem());
        user.send("Item %s added to your inventory".formatted(item.name()));
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of(Arrays.toString(Items.values()));
    }
}
