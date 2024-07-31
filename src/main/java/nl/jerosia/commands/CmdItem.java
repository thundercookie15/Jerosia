package nl.jerosia.commands;

import nl.jerosia.Jerosia;
import nl.jerosia.items.Items;
import nl.jerosia.player.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nl.jerosia.utils.FormatUtils.MESSAGE_PREFIX;

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
            user.send(MESSAGE_PREFIX + "Usage: /item <item>");
            return false;
        }
        Items item = Items.getItemByName(args[0]);
        if (item == null) {
            user.send(MESSAGE_PREFIX + "Item not found");
            return false;
        }

        user.getBase().getInventory().addItem(item.getItem());
        user.send(MESSAGE_PREFIX + "Item &c%s &7added to your inventory.".formatted(item.name()));
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return Arrays.stream(Items.values()).map(Items::getItemName).collect(Collectors.toList());
    }
}
