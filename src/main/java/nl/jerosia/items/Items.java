package nl.jerosia.items;

import nl.jerosia.Jerosia;
import nl.jerosia.items.customitems.WandItems;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

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

    public static boolean isHoldingCustomItem(ItemStack item, Items customItem) {
        NamespacedKey key = new NamespacedKey(Jerosia.getInstance(), "jerosia_item_id");
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        if (container.has(key, PersistentDataType.INTEGER)) {
            return container.get(key, PersistentDataType.INTEGER) == customItem.getItemId();
        }
        return false;
    }
}
