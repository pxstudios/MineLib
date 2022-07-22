package net.pxstudios.minelib.common.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.core.util.ReflectionUtil;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Consumer;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class BukkitItemModifySession {

    private final BukkitItem bukkitItem;
    private final ItemStack itemStack;

    @Getter
    private BiFunction<Player, BukkitItemModifySession, BukkitItem> customItemModifier;

    public BukkitItemModifySession withCustomModifications(BiFunction<Player, BukkitItemModifySession, BukkitItem> customItemModifier) {
        this.customItemModifier = customItemModifier;
        return this;
    }

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

    public BukkitItemModifySession withAllFlags() {
        return withFlagsArray(ItemFlag.values());
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

    public BukkitItemModifySession withSkullOwner(String skullOwner) {
        this.<SkullMeta>modifyMeta(itemMeta -> itemMeta.setOwner(skullOwner));
        return this;
    }

    public BukkitItemModifySession withGameProfile(GameProfile gameProfile) {
        this.<SkullMeta>modifyMeta(itemMeta -> {

            try {
                ReflectionUtil.setFieldValue(gameProfile.getClass().getDeclaredField("profile"), itemMeta, gameProfile);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        return this;
    }

    public BukkitItemModifySession withTextureAndSignature(String value, String signature) {
        String uuidString = value;

        if (signature != null) {
            uuidString = (value + signature);
        }

        GameProfile gameProfile = new GameProfile(UUID.nameUUIDFromBytes(uuidString.getBytes()), null);

        if (signature != null) {
            gameProfile.getProperties().put("textures", new Property("textures", value, signature));
        }
        else {
            gameProfile.getProperties().put("textures", new Property("textures", value));
        }

        return withGameProfile(gameProfile);
    }

    public BukkitItemModifySession withTextureValue(String value) {
        return withTextureAndSignature(value, null);
    }

    public BukkitItemModifySession withLeatherColor(Color color) {
        this.<LeatherArmorMeta>modifyMeta(itemMeta -> itemMeta.setColor(color));
        return this;
    }

    public BukkitItemModifySession withLeatherColorMixed(Color mainColor, Color... mixedColors) {
        this.<LeatherArmorMeta>modifyMeta(itemMeta -> itemMeta.setColor(mainColor.mixColors(mixedColors)));
        return this;
    }

    public BukkitItemModifySession withLeatherDyeColorMixed(Color mainColor, DyeColor... mixedColors) {
        this.<LeatherArmorMeta>modifyMeta(itemMeta -> itemMeta.setColor(mainColor.mixDyes(mixedColors)));
        return this;
    }

    public BukkitItemModifySession withLeatherRGB(int red, int green, int blue) {
        return withLeatherColor(Color.fromRGB(red, green, blue));
    }

    public BukkitItemModifySession withLeatherRGB(int rgb) {
        return withLeatherColor(Color.fromRGB(rgb));
    }

    public BukkitItem complete() {
        return bukkitItem;
    }
}
