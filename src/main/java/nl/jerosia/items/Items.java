package nl.jerosia.items;

import nl.jerosia.items.customitems.WandItems;
import org.bukkit.inventory.ItemStack;

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
}
