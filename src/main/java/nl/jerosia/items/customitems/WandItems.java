package nl.jerosia.items.customitems;

import nl.jerosia.Jerosia;
import nl.jerosia.items.Items;
import nl.jerosia.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class WandItems {
    public static ItemStack EMPIRE_WAND_ITEM() {
        ItemBuilder item = new ItemBuilder(Material.BLAZE_ROD);
        item.amount(1);
        item.setMaxStackSize(1);
        item.fireResistant();
        item.coloredName("&cEmpire Wand");
        item.coloredLore("&7This wand is surrounded by a powerful magical aura.", "&7It is said that this wand was once used by the", "&7old gods of this world.");
        item.persistentDataContainer(new NamespacedKey(Jerosia.getInstance(), "jerosia_item_id"), PersistentDataType.INTEGER, 0);
        return item.create();
    }

}
