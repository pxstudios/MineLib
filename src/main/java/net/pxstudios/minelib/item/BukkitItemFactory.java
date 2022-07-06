package net.pxstudios.minelib.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public final class BukkitItemFactory {

    public BukkitItem getByStack(ItemStack itemStack) {
        return new BukkitItem(itemStack);
    }

    public BukkitItem getByTypeAndData(Material material, int durability) {
        return new BukkitItem(new ItemStack(material, 1, (byte) durability));
    }

    public BukkitItem getByType(Material material) {
        return getByTypeAndData(material, 0);
    }

    public BukkitItem getByDataAndAmount(MaterialData materialData, int amount) {
        return new BukkitItem(materialData.toItemStack(amount));
    }

    public BukkitItem getByData(MaterialData materialData) {
        return getByDataAndAmount(materialData, 1);
    }

    @SuppressWarnings("unchecked")
    public <Meta extends ItemMeta> BukkitItem getByStackAndMeta(ItemStack itemStack, Consumer<Meta> metaHandler) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        metaHandler.accept((Meta) itemMeta);

        itemStack.setItemMeta(itemMeta);

        return getByStack(itemStack);
    }

    public BukkitItem getByStackAndName(ItemStack itemStack, String name) {
        return getByStackAndMeta(itemStack, itemMeta -> itemMeta.setDisplayName(name));
    }

    public BukkitItem getByStackAndLoreList(ItemStack itemStack, List<String> loreList) {
        return getByStackAndMeta(itemStack, itemMeta -> itemMeta.setLore(loreList));
    }

    public BukkitItem getByStackAndLoreArray(ItemStack itemStack, String... loreArray) {
        return getByStackAndLoreList(itemStack, Arrays.asList(loreArray));
    }
}
