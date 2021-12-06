package com.willfp.talismans.talismans.util;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.config.updating.ConfigUpdater;
import com.willfp.talismans.TalismansPlugin;
import com.willfp.talismans.talismans.Talisman;
import com.willfp.talismans.talismans.Talismans;
import org.bukkit.block.BlockState;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.function.Function;

public class TalismanChecks {
    /**
     * Cached talismans.
     */
    public static final Map<UUID, Set<Talisman>> CACHED_TALISMANS = Collections.synchronizedMap(new HashMap<>());

    /**
     * Cached items.
     */
    public static final Map<UUID, Set<ItemStack>> CACHED_TALISMAN_ITEMS = Collections.synchronizedMap(new WeakHashMap<>());

    /**
     * All providers.
     */
    private static final Set<Function<Player, List<ItemStack>>> PROVIDERS = new HashSet<>();

    /**
     * If ender chests should be checked.
     */
    private static boolean readEnderChest = true;

    /**
     * If shulker boxes should be read.
     */
    private static boolean readShulkerBoxes = true;

    /**
     * If only offhand should be read.
     */
    private static boolean offhandOnly = false;

    /**
     * Instance of talismans.
     */
    private static final EcoPlugin PLUGIN = TalismansPlugin.getInstance();

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

        if (!TalismanUtils.isTalismanMaterial(item.getType())) {
            return null;
        }

        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return null;
        }

        PersistentDataContainer container = meta.getPersistentDataContainer();
        String id = container.get(PLUGIN.getNamespacedKeyFactory().create("talisman"), PersistentDataType.STRING);
        if (id == null) {
            return null;
        }

        Talisman talisman = Talismans.getByID(id);

        return talisman;
    }

    /**
     * Get all talismans that a player has active.
     *
     * @param player The player to query.
     * @return A set of all found talismans.
     */
    public static Set<Talisman> getTalismansOnPlayer(@NotNull final Player player) {
        return getTalismansOnPlayer(player, true);
    }


    /**
     * Get all talismans ItemStacks that a player has active.
     *
     * @param player   The player to query.
     * @param useCache If the cache should be checked.
     * @param extra    Bonus items.
     * @return A set of all found talismans.
     */
    public static Set<ItemStack> getTalismanItemsOnPlayer(@NotNull final Player player,
                                                         final boolean useCache,
                                                         @NotNull final ItemStack... extra) {
        if (useCache) {
            Set<ItemStack> cached = CACHED_TALISMAN_ITEMS.get(player.getUniqueId());
            if (cached != null) {
                return cached;
            }
        }

        List<ItemStack> contents = new ArrayList<>();

        List<ItemStack> rawContents = new ArrayList<>(Arrays.asList(player.getInventory().getContents()));

        if (readEnderChest) {
            Inventory enderChest = player.getEnderChest();

            // Not always true, bug reported where it was null.
            if (enderChest != null) {
                rawContents.addAll(Arrays.asList(enderChest.getContents()));
            }
        }

        if (offhandOnly) {
            rawContents.clear();
            rawContents.add(player.getInventory().getItemInOffHand());
        }

        rawContents.addAll(Arrays.asList(extra));

        for (Function<Player, List<ItemStack>> provider : PROVIDERS) {
            rawContents.addAll(provider.apply(player));
        }

        for (ItemStack rawContent : rawContents) {
            if (rawContent == null) {
                continue;
            }
            if (readShulkerBoxes) {
                ItemMeta meta = rawContent.getItemMeta();
                if (meta instanceof BlockStateMeta shulkerMeta) {
                    if (!shulkerMeta.hasBlockState()) {
                        continue;
                    }

                    BlockState state = shulkerMeta.getBlockState();
                    if (state instanceof ShulkerBox shulkerBox) {
                        contents.addAll(Arrays.asList(shulkerBox.getInventory().getContents()));
                        continue;
                    }
                }
            }
            contents.add(rawContent);
        }

        Set<ItemStack> items = new HashSet<>();

        for (ItemStack itemStack : contents) {
            Talisman talisman = getTalismanOnItem(itemStack);
            if (talisman == null) {
                continue;
            }

            if (items.size() >= TalismanUtils.getLimit(player)) {
                break;
            }

            items.add(itemStack);
        }

        if (useCache) {
            CACHED_TALISMAN_ITEMS.put(player.getUniqueId(), items);
            PLUGIN.getScheduler().runLater(() -> CACHED_TALISMAN_ITEMS.remove(player.getUniqueId()), 40);
        }

        return items;
    }

    /**
     * Get all talismans that a player has active.
     *
     * @param player   The player to query.
     * @param useCache If the cache should be checked.
     * @param extra    Bonus items.
     * @return A set of all found talismans.
     */
    public static Set<Talisman> getTalismansOnPlayer(@NotNull final Player player,
                                                          final boolean useCache,
                                                          @NotNull final ItemStack... extra) {
        if (useCache) {
            Set<Talisman> cached = CACHED_TALISMANS.get(player.getUniqueId());
            if (cached != null) {
                return cached;
            }
        }

        Set<ItemStack> contents = getTalismanItemsOnPlayer(player, useCache, extra);
        Set<Talisman> found = new HashSet<>();

        for (ItemStack itemStack : contents) {
            Talisman talisman = getTalismanOnItem(itemStack);
            if (talisman == null) {
                continue;
            }

            if (found.size() >= TalismanUtils.getLimit(player)) {
                break;
            }

            found.add(talisman);
        }

        if (useCache) {
            CACHED_TALISMANS.put(player.getUniqueId(), found);
            PLUGIN.getScheduler().runLater(() -> CACHED_TALISMANS.remove(player.getUniqueId()), 40);
        }

        return found;
    }

    /**
     * Clear cache for a player.
     *
     * @param player The player.
     */
    public static void clearCache(@NotNull final Player player) {
        CACHED_TALISMAN_ITEMS.remove(player.getUniqueId());
        CACHED_TALISMANS.remove(player.getUniqueId());
    }

    /**
     * Register ItemStack provider (inventory extension, eg talisman bag).
     *
     * @param provider The provider.
     */
    public static void regsiterItemStackProvider(@NotNull final Function<Player, List<ItemStack>> provider) {
        PROVIDERS.add(provider);
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

    /**
     * Update config values.
     *
     * @param plugin Instance of Talismans.
     */
    @ConfigUpdater
    public static void reload(@NotNull final EcoPlugin plugin) {
        readEnderChest = plugin.getConfigYml().getBool("read-enderchest");
        readShulkerBoxes = plugin.getConfigYml().getBool("read-shulkerboxes");
        offhandOnly = plugin.getConfigYml().getBool("offhand-only");
    }
}
