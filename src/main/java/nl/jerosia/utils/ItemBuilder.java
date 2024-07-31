package nl.jerosia.utils;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import org.apache.commons.lang.Validate;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ItemBuilder implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private ItemStack item;

    public ItemBuilder(Material material) {
        Validate.notNull(material, "Material is null");
        this.item = new ItemStack(material);
    }

    public ItemBuilder(ItemStack item) {
        Validate.notNull(item, "ItemStack is null");
        this.item = item;
    }

    public ItemBuilder(OfflinePlayer skullOwner) {
        this.item = (new ItemBuilder(Material.PLAYER_HEAD)).skullOwner(skullOwner).create();
    }

    public ItemBuilder amount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public ItemBuilder name(String name) {
        ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(name);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder coloredName(String name) {
        ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(FormatUtils.parseColors(name));
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(String... lore) {
        ItemMeta meta = this.item.getItemMeta();
        meta.setLore(Arrays.asList(lore));
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder coloredLore(String... lore) {
        ItemMeta meta = this.item.getItemMeta();
        meta.setLore(FormatUtils.parseColors(Arrays.asList(lore)));
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        ItemMeta meta = this.item.getItemMeta();
        meta.setLore(lore);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder coloredLore(List<String> lore) {
        ItemMeta meta = this.item.getItemMeta();
        meta.setLore(FormatUtils.parseColors(lore));
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder skullOwner(OfflinePlayer player) {
        Validate.notNull(player, "Skull owner is null");
        SkullMeta meta = (SkullMeta) this.item.getItemMeta();
        meta.setOwningPlayer(player);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder skullTexture(String texture) {
        Validate.notNull(texture, "Texture string is null");
        NBTItem nbt = new NBTItem(this.item);
        NBTCompound skullOwner = nbt.addCompound("SkullOwner");
        skullOwner.setString("Id", UUID.randomUUID().toString());
        skullOwner.addCompound("Properties").getCompoundList("textures").addCompound().setString("Value", texture);
        this.item = nbt.getItem();
        return this;
    }

    public ItemBuilder leatherArmorColor(Color color) {
        Validate.notNull(color, "Color is null");
        LeatherArmorMeta meta = (LeatherArmorMeta) this.item.getItemMeta();
        meta.setColor(color);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder potionColor(Color color) {
        Validate.notNull(color, "Color is null");
        PotionMeta meta = (PotionMeta) this.item.getItemMeta();
        meta.setColor(color);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder enchant(String type, int level) {
        this.item.addEnchantment(this.parseEnchantments(type), level);
        return this;
    }

    public ItemBuilder enchant(Enchantment type, int level) {
        this.item.addEnchantment(type, level);
        return this;
    }

    public ItemBuilder unsafeEnchant(Enchantment type, int level) {
        this.item.addUnsafeEnchantment(type, level);
        return this;
    }

    public ItemBuilder itemFlag(ItemFlag flag) {
        Validate.notNull(flag, "Flag is null");
        this.item.getItemMeta().addItemFlags(flag);
        return this;
    }

    public ItemBuilder material(Material material) {
        this.item.setType(material);
        return this;
    }

    public ItemBuilder type(Material type) {
        this.item.setType(type);
        return this;
    }

    public ItemBuilder unbreakable() {
        ItemMeta meta = this.item.getItemMeta();
        meta.setUnbreakable(true);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder persistentDataContainer(NamespacedKey key, PersistentDataType<Object, Object> type, Object value) {
        ItemMeta meta = this.item.getItemMeta();
        meta.getPersistentDataContainer().set(key, type, value);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder persistentDataContainer(NamespacedKey key, PersistentDataType<Integer, Integer> type, int value) {
        ItemMeta meta = this.item.getItemMeta();
        meta.getPersistentDataContainer().set(key, type, value);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder namePlaceholder(String key, String value) {
        return this.item.getItemMeta() != null ? this.name(this.item.getItemMeta().getDisplayName().replace(key, value)) : this;
    }

    public ItemBuilder namePlaceholders(Map<String, String> placeholders) {
        if (this.item.getItemMeta() != null) {
            this.item.getItemMeta().getDisplayName();
            placeholders.forEach(this::namePlaceholder);
        }
        return this;
    }

    public ItemBuilder namePlaceholderOptional(String key, Supplier<String> value) {
        if (this.item.getItemMeta() != null) {
            String oldName = this.item.getItemMeta().getDisplayName();
            return oldName.contains(key) ? this.name(oldName.replace(key, value.get())) : this;
        } else {
            return this;
        }
    }

    public ItemBuilder namePlaceholdersOptional(Map<String, Supplier<String>> placeholders) {
        if (this.item.getItemMeta() != null) {
            placeholders.forEach(this::namePlaceholderOptional);
        }
        return this;
    }

    public ItemBuilder lorePlaceholder(String key, String value) {
        return this.item.getItemMeta() != null && this.item.getItemMeta().getLore() != null ? this.lore(this.item.getItemMeta().getLore().stream().map((s) -> s.replace(key, value)).collect(Collectors.toList())) : this;
    }

    public ItemBuilder lorePlaceholders(Map<String, String> placeholders) {
        if (this.item.getItemMeta() != null && this.item.getItemMeta().getLore() != null) {
            placeholders.forEach(this::lorePlaceholder);
        }
        return this;
    }

    public ItemBuilder lorePlaceholderOptional(String key, Supplier<String> value) {
        return this.item.getItemMeta() != null && this.item.getItemMeta().getLore() != null ? this.lore(this.item.getItemMeta().getLore().stream().map((s) -> s.contains(key) ? s.replace(key, value.get()) : s).collect(Collectors.toList())) : this;
    }

    public ItemBuilder lorePlaceholdersOptional(Map<String, Supplier<String>> placeholders) {
        if (this.item.getItemMeta() != null && this.item.getItemMeta().getLore() != null) {
            placeholders.forEach(this::lorePlaceholderOptional);
        }
        return this;
    }

    public ItemBuilder placeholder(String key, String value) {
        return this.namePlaceholder(key, value).lorePlaceholder(key, value);
    }

    public ItemBuilder placeholders(Map<String, String> placeholders) {
        return this.namePlaceholders(placeholders).lorePlaceholders(placeholders);
    }

    public ItemBuilder placeholderOptional(String key, Supplier<String> value) {
        return this.namePlaceholderOptional(key, value).lorePlaceholderOptional(key, value);
    }

    public ItemBuilder placeholdersOptional(Map<String, Supplier<String>> placeholders) {
        return this.namePlaceholdersOptional(placeholders).lorePlaceholdersOptional(placeholders);
    }

    public ItemBuilder lorePapi(Player player) {
        return this.item.getItemMeta() != null && this.item.getItemMeta().getLore() != null ? this.lore(this.item.getItemMeta().getLore().stream().map((s) -> FormatUtils.parsePapiPlaceholders(player, s)).collect(Collectors.toList())) : this;
    }

    public ItemBuilder namePapi(Player player) {
        return this.item.getItemMeta() != null ? this.name(FormatUtils.parsePapiPlaceholders(player, this.item.getItemMeta().getDisplayName())) : this;
    }

    public ItemBuilder papi(Player player) {
        this.namePapi(player);
        return this.lorePapi(player);
    }

    private Enchantment parseEnchantments(String enchantment) {
        return switch (enchantment) {
            case "aqua_affinity" -> Enchantment.AQUA_AFFINITY;
            case "bane_of_arthropods" -> Enchantment.BANE_OF_ARTHROPODS;
            case "blast_protection" -> Enchantment.BLAST_PROTECTION;
            case "channeling" -> Enchantment.CHANNELING;
            case "curse_of_binding" -> Enchantment.BINDING_CURSE;
            case "curse_of_vanishing" -> Enchantment.VANISHING_CURSE;
            case "depth_strider" -> Enchantment.DEPTH_STRIDER;
            case "efficiency" -> Enchantment.EFFICIENCY;
            case "feather_falling" -> Enchantment.FEATHER_FALLING;
            case "fire_aspect" -> Enchantment.FIRE_ASPECT;
            case "fire_protection" -> Enchantment.FIRE_PROTECTION;
            case "flame" -> Enchantment.FLAME;
            case "frost_walker" -> Enchantment.FROST_WALKER;
            case "impaling" -> Enchantment.IMPALING;
            case "infinity" -> Enchantment.INFINITY;
            case "knockback" -> Enchantment.KNOCKBACK;
            case "looting" -> Enchantment.LOYALTY;
            case "luck_of_the_sea" -> Enchantment.LUCK_OF_THE_SEA;
            case "lure" -> Enchantment.LURE;
            case "mending" -> Enchantment.MENDING;
            case "piercing" -> Enchantment.PIERCING;
            case "power" -> Enchantment.POWER;
            case "projectile_protection" -> Enchantment.PROJECTILE_PROTECTION;
            case "protection" -> Enchantment.PROTECTION;
            case "punch" -> Enchantment.PUNCH;
            case "quick_charge" -> Enchantment.QUICK_CHARGE;
            case "respiration" -> Enchantment.RESPIRATION;
            case "riptide" -> Enchantment.RIPTIDE;
            case "sharpness" -> Enchantment.SHARPNESS;
            case "silk_touch" -> Enchantment.SILK_TOUCH;
            case "smite" -> Enchantment.SMITE;
            case "sweeping_edge" -> Enchantment.SWEEPING_EDGE;
            case "thorns" -> Enchantment.THORNS;
            case "unbreaking" -> Enchantment.UNBREAKING;
            case "soul_speed" -> Enchantment.SOUL_SPEED;
            default -> null;
        };
    }

    public ItemStack create() {
        return this.item;
    }
}
