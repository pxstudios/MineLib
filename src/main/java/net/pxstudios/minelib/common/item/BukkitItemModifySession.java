package net.pxstudios.minelib.common.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class BukkitItemModifySession {

    private final BukkitItem bukkitItem;
    private final ItemStack itemStack;

    public BukkitItemModifySession withType(Material material) {
        itemStack.setType(material);
        return this;
    }

    public BukkitItemModifySession withTypeName(String typeName) {
        return withType(Material.matchMaterial(typeName));
    }

    public BukkitItemModifySession withTypeId(int typeID) {
        return withType(Material.getMaterial(typeID));
    }

    public BukkitItemModifySession withDurability(short durability) {
        itemStack.setDurability(durability);
        return this;
    }

    public BukkitItemModifySession withDurabilityAsInt(int durability) {
        return withDurability((byte) durability);
    }

    public BukkitItemModifySession withAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public BukkitItemModifySession withData(MaterialData materialData) {
        itemStack.setData(materialData);
        return this;
    }

    @SuppressWarnings("unchecked")
    private <Meta extends ItemMeta> void modifyMeta(Consumer<Meta> metaConsumer) {
        if (metaConsumer == null) {
            return;
        }

        Meta meta = (Meta) itemStack.getItemMeta();
        metaConsumer.accept(meta);

        itemStack.setItemMeta(meta);
    }

    public BukkitItemModifySession withName(String name) {
        modifyMeta(itemMeta -> itemMeta.setDisplayName(name));
        return this;
    }

    public BukkitItemModifySession withColoredName(char altColorChar, String name) {
        return withName(ChatColor.translateAlternateColorCodes(altColorChar, name));
    }

    public BukkitItemModifySession withColoredName(String name) {
        return withColoredName('&', name);
    }

    public BukkitItemModifySession withLoreList(List<String> loreList) {
        modifyMeta(itemMeta -> itemMeta.setLore(loreList));
        return this;
    }

    public BukkitItemModifySession withColoredLoreList(char altColorChar, List<String> loreList) {
        List<String> loreClone = new ArrayList<>(loreList);
        loreClone.replaceAll(line -> ChatColor.translateAlternateColorCodes(altColorChar, line));

        return withLoreList(loreClone);
    }

    public BukkitItemModifySession withColoredLoreList(List<String> loreList) {
        return withColoredLoreList('&', loreList);
    }

    public BukkitItemModifySession withLoreArray(String... loreArray) {
        return withLoreList(Arrays.asList(loreArray));
    }

    public BukkitItemModifySession withColoredLoreArray(char altColorChar, String... loreArray) {
        return withColoredLoreList(altColorChar, Arrays.asList(loreArray));
    }

    public BukkitItemModifySession withColoredLoreArray(String... loreArray) {
        return withColoredLoreList(Arrays.asList(loreArray));
    }

    public BukkitItemModifySession withUnbreakable(boolean flag) {
        modifyMeta(itemMeta -> itemMeta.setUnbreakable(flag));
        return this;
    }

    public BukkitItemModifySession withUnbreakable() {
        return withUnbreakable(true);
    }

    public BukkitItemModifySession withFlag(ItemFlag itemFlag) {
        modifyMeta(itemMeta -> itemMeta.addItemFlags(itemFlag));
        return this;
    }

    public BukkitItemModifySession withFlagsArray(ItemFlag... itemFlagsArray) {
        modifyMeta(itemMeta -> itemMeta.addItemFlags(itemFlagsArray));
        return this;
    }

    public BukkitItemModifySession withEnchant(Enchantment enchantment, int level, boolean flag) {
        modifyMeta(itemMeta -> itemMeta.addEnchant(enchantment, level, flag));
        return this;
    }

    public BukkitItemModifySession withEnchant(Enchantment enchantment, boolean flag) {
        return withEnchant(enchantment, 1, flag);
    }

    public BukkitItemModifySession withEnchant(Enchantment enchantment, int level) {
        return withEnchant(enchantment, level, true);
    }

    public BukkitItemModifySession withEnchant(Enchantment enchantment) {
        return withEnchant(enchantment, 1);
    }

    public BukkitItem complete() {
        return bukkitItem;
    }
}
