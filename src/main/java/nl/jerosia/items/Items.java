package nl.jerosia.items;

import nl.jerosia.Jerosia;
import nl.jerosia.items.customitems.WandItems;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public enum Items {
    EMPIRE_WAND(WandItems.EMPIRE_WAND_ITEM(), ItemTypes.WAND, 0),;
    private final ItemStack item;
    private final ItemTypes itemType;
    private final int itemId;

    Items(ItemStack item, ItemTypes itemType, int itemId) {
        this.item = item;
        this.itemType = itemType;
        this.itemId = itemId;
    }

    enum ItemTypes {
        WAND(),
    }

    public ItemStack getItem() {
        return item;
    }

    public int getItemId() {
        return itemId;
    }

    public List<String> items() {
        List<String> items = new ArrayList<>();
        for (Items item : Items.values()) {
            items.add(item.name());
        }
        return items;
    }

    public static Items getItemByName(String itemName) {
        for (Items item : Items.values()) {
            if (item.name().equals(itemName)) {
                return item;
            }
        }
        return null;
    }

    public static boolean isHoldingCustomItem(ItemStack item, Items customItem) {
        NamespacedKey key = new NamespacedKey(Jerosia.getInstance(), "jerosia_item_id");
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        if (container.has(key, PersistentDataType.INTEGER)) {
            return container.get(key, PersistentDataType.INTEGER) == customItem.getItemId();
        }
        return false;
    }
}
