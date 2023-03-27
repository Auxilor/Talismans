package com.willfp.talismans.talismans.util

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.updating.ConfigUpdater
import com.willfp.eco.core.fast.fast
import com.willfp.libreforge.ItemProvidedHolder
import com.willfp.talismans.TalismansPlugin.Companion.instance
import com.willfp.talismans.talismans.Talisman
import com.willfp.talismans.talismans.Talismans.getByID
import com.willfp.talismans.talismans.util.TalismanUtils.convert
import com.willfp.talismans.talismans.util.TalismanUtils.getLimit
import com.willfp.talismans.talismans.util.TalismanUtils.isTalismanMaterial
import org.bukkit.block.ShulkerBox
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BlockStateMeta
import org.bukkit.persistence.PersistentDataType
import java.util.concurrent.TimeUnit
import java.util.function.Function

object TalismanChecks {
    private val CACHED_TALISMANS: Cache<Player, Set<ItemProvidedHolder>> = Caffeine.newBuilder()
        .expireAfterWrite(2, TimeUnit.SECONDS)
        .build()

    private val CACHED_TALISMAN_ITEMS: Cache<Player, Set<ItemStack>> = Caffeine.newBuilder()
        .expireAfterWrite(2, TimeUnit.SECONDS)
        .build()

    private val PROVIDERS: MutableSet<Function<Player, List<ItemStack>>> = HashSet()

    private var readShulkerBoxes = true
    private var offhandOnly = false

    private val PLUGIN: EcoPlugin = instance

    /**
     * Does the specified ItemStack have a certain Talisman present?
     *
     * @param item The [ItemStack] to check
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
        return container.has(talisman.id, PersistentDataType.INTEGER)
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

        val container = item.fast().persistentDataContainer

        val id = container.get(
            PLUGIN.namespacedKeyFactory.create("talisman"),
            PersistentDataType.STRING
        ) ?: return null

        return getByID(id)
    }

    /**
     * Get all talismans ItemStacks that a player has active.
     *
     * @param player The player to query.
     * @param extra Bonus items.
     * @return A set of all found talismans.
     */
    @JvmStatic
    fun getTalismanItemsOnPlayer(
        player: Player,
        vararg extra: ItemStack?
    ): Set<ItemStack> {
        return CACHED_TALISMAN_ITEMS.get(player) {
            val contents = mutableListOf<ItemStack>()
            val rawContents = mutableListOf<ItemStack?>()

            if (offhandOnly) {
                rawContents.clear()
                rawContents.add(it.inventory.itemInOffHand)
            }

            rawContents.addAll(extra)
            for (provider in PROVIDERS) {
                rawContents.addAll(provider.apply(it))
            }

            for (rawContent in rawContents) {
                if (rawContent == null) {
                    continue
                }

                if (readShulkerBoxes) {
                    val meta = rawContent.itemMeta
                    if (meta is BlockStateMeta) {
                        if (!meta.hasBlockState()) {
                            continue
                        }
                        val state = meta.blockState
                        if (state is ShulkerBox) {
                            contents.addAll(state.inventory.contents.filterNotNull())
                            continue
                        }
                    }
                }

                contents.add(rawContent)
            }

            val items = mutableMapOf<Talisman, ItemStack>()

            for (itemStack in contents) {
                convert(itemStack)
                val talis = getTalismanOnItem(itemStack) ?: continue
                if (items.size >= getLimit(it)) {
                    break
                }
                items[talis] = itemStack
            }

            if (PLUGIN.configYml.getBool("top-level-only")) {
                for ((talisman, _) in items.toMap()) {
                    var lowerLevel = talisman.lowerLevel
                    while (lowerLevel != null) {
                        items.remove(lowerLevel)
                        lowerLevel = lowerLevel.lowerLevel
                    }
                }
            }

            items.values.toSet()
        }
    }

    /**
     * Get all talismans that a player has active.
     *
     * @param player The player to query.
     * @param extra Bonus items.
     * @return A set of all found talismans.
     */
    @JvmStatic
    fun getTalismansOnPlayer(
        player: Player,
        vararg extra: ItemStack?
    ): Set<ItemProvidedHolder> {
        return CACHED_TALISMANS.get(player) {
            getTalismanItemsOnPlayer(player, *extra)
                .associateWith { itemStack -> getTalismanOnItem(itemStack) }
                .filterValues { it != null }
                .map { (itemStack, talisman) -> ItemProvidedHolder(talisman!!, itemStack) }
                .toSet()
        }
    }

    /**
     * Clear cache for a player.
     *
     * @param player The player.
     */
    @JvmStatic
    fun clearCache(player: Player) {
        CACHED_TALISMAN_ITEMS.invalidate(player)
        CACHED_TALISMANS.invalidate(player)
    }

    /**
     * Register ItemStack provider (inventory extension, e.g. talisman
     * bag).
     *
     * @param provider The provider.
     */
    @JvmStatic
    fun registerItemStackProvider(provider: Function<Player, List<ItemStack>>) {
        PROVIDERS.add(provider)
    }

    /**
     * Get if a player has a specific talisman active.
     *
     * @param player The player to query.
     * @param talisman The talisman to search for.
     * @return A set of all found talismans.
     */
    @JvmStatic
    fun hasTalisman(
        player: Player,
        talisman: Talisman
    ): Boolean {
        return getTalismansOnPlayer(player)
            .map { it.holder }
            .contains(talisman)
    }

    /**
     * Update config values.
     *
     * @param plugin Instance of Talismans.
     */
    @ConfigUpdater
    @JvmStatic
    fun reload(plugin: EcoPlugin) {
        if (plugin.configYml.getBool("read-inventory")) {
            registerItemStackProvider {
                it.inventory.contents.filterNotNull()
            }
        }

        if (plugin.configYml.getBool("read-enderchest")) {
            registerItemStackProvider {
                @Suppress("UNNECESSARY_SAFE_CALL", "USELESS_ELVIS") // Was null once
                it.enderChest?.contents?.filterNotNull() ?: emptyList()
            }
        }

        readShulkerBoxes = plugin.configYml.getBool("read-shulkerboxes")
        offhandOnly = plugin.configYml.getBool("offhand-only")
    }
}
