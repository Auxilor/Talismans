package com.willfp.talismans.bag

import com.willfp.eco.core.data.keys.PersistentDataKey
import com.willfp.eco.core.data.keys.PersistentDataKeyType
import com.willfp.eco.core.data.profile
import com.willfp.eco.core.drops.DropQueue
import com.willfp.eco.core.gui.SlotLocation
import com.willfp.eco.core.gui.component.closeButton
import com.willfp.eco.core.gui.getSlotLocations
import com.willfp.eco.core.gui.menu
import com.willfp.eco.core.gui.menu.Menu
import com.willfp.eco.core.gui.menu.MenuBuilder
import com.willfp.eco.core.gui.onLeftClick
import com.willfp.eco.core.gui.slot
import com.willfp.eco.core.gui.slot.ConfigSlot
import com.willfp.eco.core.integrations.placeholder.PlaceholderManager
import com.willfp.eco.core.items.Items
import com.willfp.eco.core.items.builder.ItemStackBuilder
import com.willfp.eco.core.placeholder.PlayerPlaceholder
import com.willfp.eco.core.recipe.parts.EmptyTestableItem
import com.willfp.eco.util.getNumericalPermission
import com.willfp.ecomponent.menuStateVar
import com.willfp.talismans.plugin
import com.willfp.talismans.talismans.util.TalismanChecks
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import java.util.*
import java.util.concurrent.ConcurrentHashMap

private val Menu.talismanBag by menuStateVar<List<ItemStack>>(
    emptyList()
)

private val Menu.talismanBagLoaded by menuStateVar(false)

object TalismanBag : Listener {
    private val menus = ConcurrentHashMap<Int, Menu>()

    private val bagSlotCounts = ConcurrentHashMap<Int, Int>()

    @Volatile
    private lateinit var legacyKey: PersistentDataKey<List<String>>

    @Volatile
    private lateinit var key: PersistentDataKey<List<String>>

    @Volatile
    private lateinit var bagSlotsKey: PersistentDataKey<Int>

    @Volatile
    private lateinit var emptyItem: ItemStack

    @Volatile
    private var upgrade: BagUpgrade? = null

    private val savedItems = ConcurrentHashMap<UUID, List<ItemStack>>()

    private val Player.baseBagSize: Int
        get() {
            val configSize = plugin.configYml.getInt("bag.size")
            if (configSize > 0) {
                return configSize
            }

            val highest = this.getNumericalPermission("talismans.bagsize", -1.0).toInt()
            return if (highest < 0) {
                UNLIMITED_BAG_SIZE
            } else {
                highest
            }
        }

    private val Player.bagSize: Int
        get() = effectiveBagSize(base = baseBagSize, bought = getBoughtSlots(this))

    private val BagUpgrade?.reservedLocations: List<SlotLocation>
        get() = this?.locations.orEmpty()

    private fun ItemStack.isDroppedFromBag(): Boolean =
        TalismanChecks.getTalismanOnItem(this) == null && !isSimilar(emptyItem)

    fun getBoughtSlots(player: OfflinePlayer): Int = player.profile.read(bagSlotsKey)

    fun setBoughtSlots(player: OfflinePlayer, slots: Int) {
        player.profile.write(bagSlotsKey, slots.coerceAtLeast(0))
    }

    internal fun update() {
        legacyKey = PersistentDataKey(
            plugin.namespacedKeyFactory.create("talisman_bag"),
            PersistentDataKeyType.STRING_LIST,
            emptyList()
        )

        key = PersistentDataKey(
            plugin.namespacedKeyFactory.create("bag"),
            PersistentDataKeyType.STRING_LIST,
            emptyList()
        )

        bagSlotsKey = PersistentDataKey(
            plugin.namespacedKeyFactory.create("bag_slots"),
            PersistentDataKeyType.INT,
            0
        )

        emptyItem = ItemStackBuilder(Items.lookup(plugin.configYml.getString("bag.blocked-item")))
            .addLoreLines(plugin.configYml.getStrings("bag.blocked-item-lore"))
            .build()

        val customSlots = plugin.configYml.getSubsections("bag.custom-slots")
            .map { ConfigSlot(it) to it.getSlotLocations() }

        val close = closeButton(plugin.configYml.getSubsection("bag.close"), enabledByDefault = false)

        val bagUpgrade = readUpgrade().also { upgrade = it }

        val reserved = customSlots.flatMap { it.second }.toSet() +
                (if (close.isEnabled) close.locations else emptyList()) +
                bagUpgrade.reservedLocations

        for (rows in 1..6) {
            val bagLocations = (1..rows)
                .flatMap { row -> (1..9).map { column -> SlotLocation(row, column) } }
                .filterNot { it in reserved }

            bagSlotCounts[rows] = bagLocations.size

            menus[rows] = menu(rows) {
                title = plugin.configYml.getFormattedString("bag.title")

                allowChangingHeldItem()

                for ((index, location) in bagLocations.withIndex()) {
                    setSlot(location.row, location.column, slot({ player, menu ->
                        if (index >= player.bagSize) {
                            emptyItem
                        } else {
                            menu.talismanBag[player].getOrNull(index)?.clone() ?: ItemStack(Material.AIR)
                        }
                    }) {
                        setCaptive(true)
                        notCaptiveFor { index >= it.bagSize }

                        setCaptiveFilter { _, _, itemStack ->
                            TalismanChecks.getTalismanOnItem(itemStack) != null
                        }
                    })
                }

                for ((slot, locations) in customSlots) {
                    for ((row, column) in locations.filter { it.row <= rows }) {
                        setSlot(row, column, slot)
                    }
                }

                if (close.isEnabled) {
                    for ((row, column) in close.locations.filter { it.row <= rows }) {
                        setSlot(row, column, close.getSlotAt(row, column))
                    }
                }

                addUpgradeSlot(rows, bagUpgrade)

                onOpen { player, menu ->
                    menu.talismanBag[player] = player.profile.read(legacyKey).map { Items.lookup(it).item } +
                            player.profile.read(key).mapNotNull { Items.fromSNBT(it) }
                    menu.talismanBagLoaded[player] = true
                }

                onRender { player, menu ->
                    if (menu.talismanBagLoaded[player]) {
                        val toWrite = menu.getCaptiveItems(player)
                            .filterNot { EmptyTestableItem().matches(it) }
                            .filter { TalismanChecks.getTalismanOnItem(it) != null }

                        if (savedItems.put(player.uniqueId, toWrite) != toWrite) {
                            player.profile.write(key, toWrite.map { Items.toSNBT(it) })
                        }
                    }
                }

                onClose { event, menu ->
                    val player = event.player as Player

                    val items = menu.getCaptiveItems(player)
                        .filterNot { EmptyTestableItem().matches(it) }

                    if (menu.talismanBagLoaded[player]) {
                        val toWrite = items.filter { TalismanChecks.getTalismanOnItem(it) != null }

                        player.profile.write(key, toWrite.map { Items.toSNBT(it) })
                        player.profile.write(legacyKey, emptyList())
                    }

                    val toDrop = items.filter { it.isDroppedFromBag() }

                    DropQueue(player)
                        .setLocation(player.eyeLocation)
                        .forceTelekinesis()
                        .addItems(toDrop)
                        .push()
                }
            }
        }

        PlaceholderManager.registerPlaceholder(
            PlayerPlaceholder(
                plugin,
                "bagsize"
            ) { it.bagSize.toString() }
        )

        PlaceholderManager.registerPlaceholder(
            PlayerPlaceholder(
                plugin,
                "bag_slots"
            ) { getBoughtSlots(it).toString() }
        )
    }

    private fun readUpgrade(): BagUpgrade? = plugin.configYml.getSubsectionOrNull("bag.upgrade")
        ?.takeIf { it.getBool("enabled") }
        ?.let { BagUpgrade(it) }

    private val BagUpgrade.cap: Int
        get() = upgradeCap(maxSize = maxSize, displayableSlots = bagSlotCounts.getValue(MAX_BAG_ROWS))

    private fun MenuBuilder.addUpgradeSlot(rows: Int, bagUpgrade: BagUpgrade?) {
        bagUpgrade ?: return

        val upgradeSlot = slot({ player, _ ->
            bagUpgrade.buildItem(player, size = player.bagSize, cap = bagUpgrade.cap, bought = getBoughtSlots(player))
        }) {
            onLeftClick { player, _, _, _ -> buySlot(player, bagUpgrade) }
        }

        for ((row, column) in bagUpgrade.locations.filter { it.row <= rows }) {
            setSlot(row, column, upgradeSlot)
        }
    }

    private fun Player.bagRows(): Int =
        bagRows(size = bagSize, minimumRows = upgrade?.minimumRows ?: 1, slotCounts = bagSlotCounts)

    fun open(player: Player) {
        menus.getValue(player.bagRows()).open(player)
    }

    private fun buySlot(player: Player, bagUpgrade: BagUpgrade) {
        val size = player.bagSize
        val price = bagUpgrade.priceFor(size + 1)

        when {
            size >= bagUpgrade.cap -> {
                player.sendMessage(plugin.langYml.getMessage("bag-upgrade-max"))
            }

            !price.canAfford(player) -> {
                player.sendMessage(
                    plugin.langYml.getMessage("bag-upgrade-cannot-afford")
                        .replace(oldValue = "%price%", newValue = price.getDisplay(player))
                )
            }

            else -> {
                price.pay(player)
                setBoughtSlots(player, getBoughtSlots(player) + 1)

                player.sendMessage(
                    plugin.langYml.getMessage("bag-upgrade-success")
                        .replace(oldValue = "%size%", newValue = player.bagSize.toString())
                )

                player.closeInventory()
                plugin.scheduler.on(player).runLater(1) {
                    if (player.isOnline) {
                        open(player)
                    }
                }
            }
        }
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val uuid = event.player.uniqueId

        plugin.scheduler.global().runLater(1) {
            if (Bukkit.getPlayer(uuid) == null) {
                savedItems.remove(uuid)
            }
        }
    }

    fun getTalismans(player: Player): List<ItemStack> {
        return savedItems.computeIfAbsent(player.uniqueId) {
            val legacyItems = player.profile.read(legacyKey)
                .map { Items.lookup(it).item }
                .filterNot { EmptyTestableItem().matches(it) }
                .filter { TalismanChecks.getTalismanOnItem(it) != null }

            val items = player.profile.read(key)
                .mapNotNull { Items.fromSNBT(it) }
                .filter { TalismanChecks.getTalismanOnItem(it) != null }

            (legacyItems + items).toList()
        }
    }
}
