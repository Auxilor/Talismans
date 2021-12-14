package com.willfp.talismans.talismans.util

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.updating.ConfigUpdater
import com.willfp.talismans.TalismansPlugin.Companion.instance
import com.willfp.talismans.talismans.Talisman
import com.willfp.talismans.talismans.Talismans.getByID
import com.willfp.talismans.talismans.util.TalismanUtils.convert
import com.willfp.talismans.talismans.util.TalismanUtils.getLimit
import com.willfp.talismans.talismans.util.TalismanUtils.isTalismanMaterial
import org.bukkit.block.ShulkerBox
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BlockStateMeta
import org.bukkit.persistence.PersistentDataType
import java.util.Arrays
import java.util.Collections
import java.util.UUID
import java.util.WeakHashMap
import java.util.function.Function

object TalismanChecks {
    private val CACHED_TALISMANS = Collections.synchronizedMap(HashMap<UUID, Set<Talisman>>())
    private val CACHED_TALISMAN_ITEMS = Collections.synchronizedMap(WeakHashMap<UUID, Set<ItemStack>>())
    private val PROVIDERS: MutableSet<Function<Player, List<ItemStack>>> = HashSet()

    private var readEnderChest = true
    private var readShulkerBoxes = true
    private var offhandOnly = false

    private val PLUGIN: EcoPlugin = instance

    /**
     * Does the specified ItemStack have a certain Talisman present?
     *
     * @param item     The [ItemStack] to check
     * @param talisman The talisman to query
     * @return If the item has the queried talisman
     */
    @JvmStatic
    fun item(
        item: ItemStack?,
        talisman: Talisman
    ): Boolean {
        if (item == null) {
            return false
        }
        val meta = item.itemMeta ?: return false
        val container = meta.persistentDataContainer
        return container.has(talisman.key, PersistentDataType.INTEGER)
    }

    /**
     * What talisman is on an item?
     *
     * @param item The item to query.
     * @return The talisman, or null if no talisman is present.
     */
    @JvmStatic
    fun getTalismanOnItem(item: ItemStack?): Talisman? {
        if (item == null) {
            return null
        }

        if (!isTalismanMaterial(item.type)) {
            return null
        }

        val meta = item.itemMeta ?: return null

        val container = meta.persistentDataContainer

        val id = container.get(
            PLUGIN.namespacedKeyFactory.create("talisman"),
            PersistentDataType.STRING
        ) ?: return null

        return getByID(id)
    }

    /**
     * Get all talismans that a player has active.
     *
     * @param player The player to query.
     * @return A set of all found talismans.
     */
    @JvmStatic
    fun getTalismansOnPlayer(player: Player): Set<Talisman> {
        return getTalismansOnPlayer(player, true)
    }

    /**
     * Get all talismans ItemStacks that a player has active.
     *
     * @param player   The player to query.
     * @param useCache If the cache should be checked.
     * @param extra    Bonus items.
     * @return A set of all found talismans.
     */
    @JvmStatic
    fun getTalismanItemsOnPlayer(
        player: Player,
        useCache: Boolean,
        vararg extra: ItemStack?
    ): Set<ItemStack> {
        if (useCache) {
            val cached = CACHED_TALISMAN_ITEMS[player.uniqueId]
            if (cached != null) {
                return cached
            }
        }

        val contents = mutableListOf<ItemStack>()
        val rawContents = player.inventory.contents.toMutableList()

        if (readEnderChest) {
            val enderChest = player.enderChest as Inventory?

            // Not always true, bug reported where it was null.
            if (enderChest != null) {
                rawContents.addAll(enderChest.contents)
            }
        }

        if (offhandOnly) {
            rawContents.clear()
            rawContents.add(player.inventory.itemInOffHand)
        }

        rawContents.addAll(extra)
        for (provider in PROVIDERS) {
            rawContents.addAll(provider.apply(player))
        }

        for (rawContent in rawContents) {
            if (rawContent == null) {
                continue
            }
            if (readShulkerBoxes) {
                val meta = rawContent.itemMeta
                if (meta is BlockStateMeta) {
                    val shulkerMeta = meta
                    if (!shulkerMeta.hasBlockState()) {
                        continue
                    }
                    val state = shulkerMeta.blockState
                    if (state is ShulkerBox) {
                        contents.addAll(Arrays.asList(*state.inventory.contents))
                        continue
                    }
                }
            }
            contents.add(rawContent)
        }

        val items: MutableSet<ItemStack> = HashSet()

        for (itemStack in contents) {
            convert(itemStack)
            getTalismanOnItem(itemStack) ?: continue
            if (items.size >= getLimit(player)) {
                break
            }
            items.add(itemStack)
        }

        if (useCache) {
            CACHED_TALISMAN_ITEMS[player.uniqueId] = items
            PLUGIN.scheduler.runLater({ CACHED_TALISMAN_ITEMS.remove(player.uniqueId) }, 40)
        }

        return items
    }

    /**
     * Get all talismans that a player has active.
     *
     * @param player   The player to query.
     * @param useCache If the cache should be checked.
     * @param extra    Bonus items.
     * @return A set of all found talismans.
     */
    @JvmStatic
    fun getTalismansOnPlayer(
        player: Player,
        useCache: Boolean,
        vararg extra: ItemStack?
    ): Set<Talisman> {
        if (useCache) {
            val cached = CACHED_TALISMANS[player.uniqueId]
            if (cached != null) {
                return cached
            }
        }

        val contents = getTalismanItemsOnPlayer(player, useCache, *extra)
        val found: MutableSet<Talisman> = HashSet()

        for (itemStack in contents) {
            val talisman = getTalismanOnItem(itemStack) ?: continue
            if (found.size >= getLimit(player)) {
                break
            }
            found.add(talisman)
        }

        if (useCache) {
            CACHED_TALISMANS[player.uniqueId] = found
            PLUGIN.scheduler.runLater({ CACHED_TALISMANS.remove(player.uniqueId) }, 40)
        }

        return found
    }

    /**
     * Clear cache for a player.
     *
     * @param player The player.
     */
    @JvmStatic
    fun clearCache(player: Player) {
        CACHED_TALISMAN_ITEMS.remove(player.uniqueId)
        CACHED_TALISMANS.remove(player.uniqueId)
    }

    /**
     * Register ItemStack provider (inventory extension, eg talisman bag).
     *
     * @param provider The provider.
     */
    @JvmStatic
    fun regsiterItemStackProvider(provider: Function<Player, List<ItemStack>>) {
        PROVIDERS.add(provider)
    }

    /**
     * Get if a player has a specific talisman active.
     *
     * @param player   The player to query.
     * @param talisman The talisman to search for.
     * @return A set of all found talismans.
     */
    @JvmStatic
    fun hasTalisman(
        player: Player,
        talisman: Talisman
    ): Boolean {
        return getTalismansOnPlayer(player).contains(talisman)
    }

    /**
     * Update config values.
     *
     * @param plugin Instance of Talismans.
     */
    @ConfigUpdater
    @JvmStatic
    fun reload(plugin: EcoPlugin) {
        readEnderChest = plugin.configYml.getBool("read-enderchest")
        readShulkerBoxes = plugin.configYml.getBool("read-shulkerboxes")
        offhandOnly = plugin.configYml.getBool("offhand-only")
    }
}