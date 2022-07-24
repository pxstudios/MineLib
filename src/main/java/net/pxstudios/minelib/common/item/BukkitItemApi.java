package net.pxstudios.minelib.common.item;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.List;
import java.util.Objects;

public final class BukkitItemApi {

    private static final ItemFactory ITEM_FACTORY_FROM_BUKKIT = Bukkit.getItemFactory();

    @Getter
    private final BukkitItemFactory factory = new BukkitItemFactory();

    public ItemStack withGlowing(ItemStack itemStack) {
        BukkitItem bukkitItem = factory.getByStack(itemStack)
                .getModifySession()

                .withEnchant(Enchantment.DURABILITY, 1).withFlag(ItemFlag.HIDE_ENCHANTS)
                .complete();

        return bukkitItem.getItem();
    }

    public boolean equalsModifiedItems(Player player, BukkitItem firstItem, BukkitItem secondItem) {
        return equalsItems(firstItem.getModifiedItem(player), secondItem.getModifiedItem(player));
    }

    public boolean equalsItems(BukkitItem firstItem, BukkitItem secondItem) {
        return equalsItems(firstItem.getItem(), secondItem.getItem());
    }

    public boolean equalsItems(ItemStack firstItem, ItemStack secondItem) {
        return firstItem.isSimilar(secondItem);
    }

    public boolean equalsItemsWithoutAmount(ItemStack firstItem, ItemStack secondItem) {
        firstItem = firstItem.clone();
        secondItem = secondItem.clone();

        firstItem.setAmount(1);
        secondItem.setAmount(1);

        return firstItem.isSimilar(secondItem);
    }

    public boolean isMetaApplicable(ItemMeta itemMeta, Material material) {
        return ITEM_FACTORY_FROM_BUKKIT.isApplicable(itemMeta, material);
    }

    public boolean isMetaApplicable(ItemMeta itemMeta, ItemStack itemStack) {
        return ITEM_FACTORY_FROM_BUKKIT.isApplicable(itemMeta, itemStack);
    }

    public Color getDefaultLeatherColor() {
        return ITEM_FACTORY_FROM_BUKKIT.getDefaultLeatherColor();
    }

    public boolean isInventoryFull(@NonNull Inventory inventory, @NonNull Material material) {
        return isInventoryFull(inventory, material, 1);
    }

    public boolean isInventoryFull(@NonNull Inventory inventory, @NonNull Material material, int amount) {
        return isInventoryFull(inventory, material, amount, (short) 0);
    }

    public boolean isInventoryFull(@NonNull Inventory inventory, @NonNull Material material,
                                   int amount, short damage) {
        int freeItems = 0;
        for (ItemStack slot : inventory.getContents()) {

            if (slot == null) {
                freeItems += material.getMaxStackSize();
            }
            else if (slot.getType() == material && slot.getDurability() == damage) {
                int i = material.getMaxStackSize() - slot.getAmount();

                if (i > 0) {
                    freeItems += i;
                }

            } else continue;

            if (freeItems >= amount) {
                return false;
            }
        }

        return true;
    }

    public boolean isInventoryFull(@NonNull Inventory inventory, @NonNull ItemStack item) {
        int i = inventory.firstEmpty();

        if (i == -1) {
            int available = 0;

            for (ItemStack inventoryItem : inventory.getContents()) {
                if (inventoryItem == null
                        || inventoryItem.getAmount() == inventoryItem.getMaxStackSize()
                        || equalsItemsWithoutAmount(inventoryItem, item)) {

                    continue;
                }

                available += inventoryItem.getMaxStackSize() - inventoryItem.getAmount();

                if (available >= item.getAmount()) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    public boolean isInventoryFull(@NonNull Player player, @NonNull ItemStack item) {
        return isInventoryFull(player.getInventory(), item);
    }

    public int search(@NonNull Inventory inventory, @NonNull Material material) {
        int items = 0;

        for (ItemStack item : inventory.getContents()) {
            if (item == null || item.getType() != material) {
                continue;
            }

            items += item.getAmount();
        }

        return items;
    }

    public int fastSearch(@NonNull Player player, @NonNull Material material) {
        return search(player.getInventory(), material);
    }

    public void depositItem(@NonNull Inventory inventory, @NonNull Material material) {
        depositItem(inventory, material, 1);
    }

    public void depositItem(@NonNull Inventory inventory, @NonNull Material material, int amount) {
        depositItem(inventory, material, amount, (short) 0);
    }

    public void depositItem(@NonNull Inventory inventory, @NonNull Material material, int amount, short damage) {
        if (amount <= 0) {
            return;
        }

        int i = 0;
        ItemStack[] items = new ItemStack[(int) Math.ceil((double) amount / material.getMaxStackSize())];

        while (amount > 0) {
            items[i++] = new ItemStack(material, amount, damage);
            amount -= material.getMaxStackSize();
        }

        inventory.addItem(items);
    }

    public boolean hasInventoryItem(@NonNull Inventory inventory, @NonNull Material material,
                                    int amount, short damage) {
        if (amount <= 0) {
            return true;
        }

        int found = 0;

        for (ItemStack slot : inventory.getContents()) {
            if (slot == null || slot.getType() != material || slot.getDurability() != damage) {
                continue;
            }

            found += slot.getAmount();

            if (found >= amount) {
                return true;
            }
        }

        return false;
    }

    public boolean hasInventoryItem(@NonNull Inventory inventory, @NonNull Material material, int amount) {
        return hasInventoryItem(inventory, material, amount, (short) 0);
    }

    public boolean hasInventoryItem(@NonNull Inventory inventory, @NonNull Material material) {
        return hasInventoryItem(inventory, material, 1);
    }

    public boolean hasInventoryItem(@NonNull Inventory inventory, @NonNull ItemStack item) {
        return hasInventoryItem(inventory, item, item.getAmount());
    }

    public boolean hasInventoryItem(@NonNull Inventory inventory, @NonNull ItemStack item, int amount) {
        int found = 0;

        for (ItemStack slot : inventory.getContents()) {
            if (slot == null || !equalsItemsWithoutAmount(item, slot)) {
                continue;
            }

            found += slot.getAmount();

            if (found >= amount) {
                return true;
            }
        }

        return false;
    }

    public int packSearch(@NonNull Player player, @NonNull ItemStack targetItem) {
        return (int) Math.floor((double) search(player, targetItem) / targetItem.getAmount());
    }

    public int search(@NonNull Player player, @NonNull ItemStack targetItem) {
        int items = 0;

        Inventory inv = player.getInventory();

        for (ItemStack item : inv.getContents()) {
            if (item == null || !equalsItemsWithoutAmount(item, targetItem)) {
                continue;
            }

            items += item.getAmount();
        }

        return items;
    }

    public void withdraw(@NonNull Inventory inventory, @NonNull ItemStack targetItem, int amount) {
        ItemStack[] items = inventory.getContents();

        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];

            if (item == null || !equalsItemsWithoutAmount(targetItem, item)) continue;

            if (item.getAmount() <= amount) {
                items[i] = null;

                amount -= item.getAmount();
            } else {
                item.setAmount(item.getAmount() - amount);

                amount = 0;
            }

            if (amount <= 0) {
                break;
            }
        }

        inventory.setContents(items);
    }

    public void fastWithdraw(@NonNull Inventory inventory, @NonNull Material type) {
        fastWithdraw(inventory, type, 1);
    }

    public void fastWithdraw(@NonNull Inventory inventory, @NonNull Material type, int amount) {
        withdrawItem(inventory, type, amount, (short) 0);
    }

    public void withdrawItem(@NonNull Inventory inventory, @NonNull Material type, int amount, short damage) {
        if (amount <= 0) {
            return;
        }

        ItemStack[] items = inventory.getContents();

        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];

            if (item == null || type != item.getType() || damage != item.getDurability()) continue;

            if (item.getAmount() <= amount) {
                items[i] = null;

                amount -= item.getAmount();
            } else {
                item.setAmount(item.getAmount() - amount);

                amount = 0;
            }

            if (amount <= 0) {
                break;
            }
        }

        inventory.setContents(items);
    }

    public void fastWithdraw(@NonNull Inventory inventory, @NonNull Material type, int amount, short damage) {
        withdrawItem(inventory, type, amount, damage);
    }

    public ItemStack parseItem(ConfigurationSection section) {
        String id = section.getString("id", "");
        Material type = Material.matchMaterial(Objects.requireNonNull(id));

        if (type == null) {
            System.out.println("Material \"" + id + "\" not found at " + section.getCurrentPath());

            type = Material.STONE;
        }

        ItemStack itemStack = new ItemStack(type, section.getInt("amount", 1), (short) section.getInt("durability", 0));

        ItemMeta itemMeta = itemStack.getItemMeta();

        String displayName = section.getString("name");
        List<String> lore = section.getStringList("lore");

        if (displayName != null) {
            itemMeta.setDisplayName(displayName);
        }

        if (lore != null) {
            itemMeta.setLore(lore);
        }

        ConfigurationSection enchants = section.getConfigurationSection("enchants");

        if (enchants != null) {
            for (String enchantment : enchants.getKeys(false)) {
                Enchantment bukkitEnchantment = Enchantment.getByName(enchantment.toUpperCase());

                if (bukkitEnchantment == null) {
                    continue;
                }

                int level = enchants.getInt(enchantment);

                if (itemMeta instanceof EnchantmentStorageMeta) {
                    EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta) itemMeta;
                    enchantmentStorageMeta.addStoredEnchant(bukkitEnchantment, level, true);
                } else {
                    itemMeta.addEnchant(bukkitEnchantment, level, true);
                }
            }
        }

        for (String flagName : section.getStringList("flags")) {
            ItemFlag itemFlag = ItemFlag.valueOf(flagName.toUpperCase());
            itemMeta.addItemFlags(itemFlag);
        }

        //itemMeta.spigot().setUnbreakable(section.getBoolean("unbreakable", false));

        if (itemMeta instanceof PotionMeta) {
            ConfigurationSection potionSection = section.getConfigurationSection("effects");

            if (potionSection != null) {
                for (String effect : potionSection.getKeys(false)) {
                    PotionEffectType effectType = PotionEffectType.getByName(effect);

                    if (effectType == null) {
                        continue;
                    }

                    int duration = potionSection.getInt(effect.concat(".duration"), 0) * 20;
                    int amplifier = potionSection.getInt(effect.concat(".amplifier"), 0);

                    boolean ambient = potionSection.getBoolean(effect.concat(".ambient"));
                    boolean particles = potionSection.getBoolean(effect.concat(".particles"), true);

                    PotionEffect potionEffect
                            = new PotionEffect(effectType, duration, amplifier, ambient, particles);

                    ((PotionMeta) itemMeta).addCustomEffect(potionEffect, true);
                }
            }

            ConfigurationSection baseEffectSection = section.getConfigurationSection("base_effect");

            if (baseEffectSection != null) {
                String effectType = baseEffectSection.getString("type");

                boolean upgraded = baseEffectSection.getBoolean("upgraded", false);
                boolean extended = baseEffectSection.getBoolean("extended", false);
                boolean splash = baseEffectSection.getBoolean("splash", false);

                Potion potion = new Potion(PotionType.valueOf(effectType));
                if (extended) potion.setHasExtendedDuration(true);
                potion.setLevel(upgraded ? 2 : 1);
                potion.setSplash(splash);

                ItemStack potionStack = potion.toItemStack(1);

                PotionMeta meta = (PotionMeta) potionStack.getItemMeta();
                itemStack.setDurability(potionStack.getDurability());
                ((PotionMeta) itemMeta).setMainEffect(potion.getType().getEffectType());

                for (PotionEffect effect : meta.getCustomEffects()) {
                    ((PotionMeta) itemMeta).addCustomEffect(effect, true);
                }
            }
        }

        if (section.contains("color")) {
            Color color = getColor(section);

            if (itemMeta instanceof LeatherArmorMeta) {
                ((LeatherArmorMeta) itemMeta).setColor(color);
            }
        }

        itemStack.setItemMeta(itemMeta);

        if (section.contains("monsterType")) {
            EntityType monsterType = EntityType.valueOf(section.getString("monsterType"));

            itemStack.setDurability(monsterType.getTypeId());
        }

        return itemStack;
    }

    public Color getColor(ConfigurationSection section) {
        return Color.fromRGB((section.getInt("potionColor") & 16777215));
    }
}


