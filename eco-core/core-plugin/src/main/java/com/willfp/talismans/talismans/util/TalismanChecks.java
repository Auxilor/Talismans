package com.willfp.talismans.talismans.util;


import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.Talismans;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class TalismanChecks {
    /**
     * Does the specified ItemStack have a certain Talisman present?
     *
     * @param item     The {@link ItemStack} to check
     * @param talisman The talisman to query
     * @return If the item has the queried talisman
     */
    public static boolean item(@Nullable final ItemStack item,
                               @NotNull final Talisman talisman) {
        if (item == null) {
            return false;
        }

        if (item.getType() != Material.PLAYER_HEAD) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return false;
        }

        PersistentDataContainer container = meta.getPersistentDataContainer();

        return container.has(talisman.getKey(), PersistentDataType.INTEGER);
    }

    /**
     * What talisman is on an item?
     *
     * @param item The item to query.
     * @return The talisman, or null if no talisman is present.
     */
    public static Talisman getTalismanOnItem(@Nullable final ItemStack item) {
        if (item == null) {
            return null;
        }

        if (item.getType() != Material.PLAYER_HEAD) {
            return null;
        }

        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return null;
        }

        PersistentDataContainer container = meta.getPersistentDataContainer();

        NamespacedKey talismanKey = container.getKeys().stream().filter(namespacedKey -> namespacedKey.getNamespace().equals("talismans")).findFirst().orElse(null);
        return Talismans.getByKey(talismanKey);
    }

    /**
     * Get all talismans that a player has active.
     *
     * @param player The player to query.
     * @return A set of all found talismans.
     */
    public static Set<Talisman> getTalismansOnPlayer(@NotNull final Player player) {
        Set<Talisman> found = new HashSet<>();

        for (ItemStack itemStack : player.getInventory()) {
            Talisman talisman = getTalismanOnItem(itemStack);
            if (talisman == null) {
                continue;
            }

            found.add(talisman);
        }

        return found;
    }

    /**
     * Get if a player has a specific talisman active.
     *
     * @param player   The player to query.
     * @param talisman The talisman to search for.
     * @return A set of all found talismans.
     */
    public static boolean hasTalisman(@NotNull final Player player,
                                      @NotNull final Talisman talisman) {
        return getTalismansOnPlayer(player).contains(talisman);
    }
}
