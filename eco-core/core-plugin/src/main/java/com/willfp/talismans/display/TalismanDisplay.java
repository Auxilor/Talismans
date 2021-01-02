package com.willfp.talismans.display;

import com.willfp.eco.util.ProxyUtils;
import com.willfp.eco.util.config.Configs;
import com.willfp.talismans.proxy.proxies.SkullProxy;
import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.util.TalismanChecks;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class TalismanDisplay {
    /**
     * The prefix for all talisman lines to have in lore.
     */
    public static final String PREFIX = "§z";

    /**
     * Revert display.
     *
     * @param item The item to revert.
     * @return The item, updated.
     */
    public static ItemStack revertDisplay(@Nullable final ItemStack item) {
        if (item == null || item.getType() != Material.PLAYER_HEAD || item.getItemMeta() == null) {
            return item;
        }

        ItemMeta meta = item.getItemMeta();
        List<String> itemLore;

        if (meta.hasLore()) {
            itemLore = meta.getLore();
        } else {
            itemLore = new ArrayList<>();
        }

        if (itemLore == null) {
            itemLore = new ArrayList<>();
        }

        itemLore.removeIf(s -> s.startsWith(PREFIX));

        if (meta.hasEnchant(Enchantment.DURABILITY)) {
            meta.removeEnchant(Enchantment.DURABILITY);
            meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        meta.setLore(itemLore);
        item.setItemMeta(meta);

        return item;
    }

    /**
     * Show talisman in item lore, set display name, and set texture.
     *
     * @param item The item to update.
     * @return The item, updated.
     */
    public static ItemStack displayTalisman(@Nullable final ItemStack item) {
        if (item == null || item.getItemMeta() == null || item.getType() != Material.PLAYER_HEAD) {
            return item;
        }

        revertDisplay(item);

        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if (meta == null) {
            return item;
        }

        List<String> itemLore = new ArrayList<>();

        if (meta.hasLore()) {
            itemLore = meta.getLore();
        }

        if (itemLore == null) {
            itemLore = new ArrayList<>();
        }

        Talisman talisman = TalismanChecks.getTalismanOnItem(item);

        if (talisman == null || !talisman.isEnabled()) {
            item.setType(Material.AIR);
            item.setAmount(0);
            return item;
        }

        if (Configs.CONFIG.getBool("strengths." + talisman.getStrength().name().toLowerCase() + ".shine")) {
            meta.addEnchant(Enchantment.DURABILITY, 0, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        ProxyUtils.getProxy(SkullProxy.class).setTalismanTexture(meta, talisman.getSkullBase64());

        meta.setDisplayName(talisman.getFormattedName());

        List<String> lore = new ArrayList<>();

        lore.addAll(talisman.getFormattedDescription());

        lore.addAll(itemLore);
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }
}
