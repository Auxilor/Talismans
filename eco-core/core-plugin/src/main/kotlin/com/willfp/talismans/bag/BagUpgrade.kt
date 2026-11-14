package com.willfp.talismans.bag

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import wiki.ecohub.config.Config
import wiki.ecohub.core.price.ConfiguredPrice
import wiki.ecohub.game.item.Items
import wiki.ecohub.game.item.builder.ItemStackBuilder
import wiki.ecohub.modules.menus.SlotLocation
import wiki.ecohub.modules.menus.getSlotLocations
import wiki.ecohub.ui.placeholder.type.StaticPlaceholder
import wiki.ecohub.ui.text.formatEco

class BagUpgrade(config: Config) {
    val locations: List<SlotLocation> = config.getSlotLocations(default = listOf(SlotLocation(row = 1, column = 9)))

    val minimumRows = locations.maxOfOrNull { it.row } ?: 1

    val maxSize = config.getIntOrNull("max-size") ?: 27

    private val item = config.getString("item")

    private val lore = config.getStrings("lore")

    private val maxItem = config.getStringOrNull("max-item") ?: item

    private val maxLore = config.getStringsOrNull("max-lore") ?: lore

    private val priceConfig = config.getSubsectionOrNull("price")

    fun priceFor(slot: Int): ConfiguredPrice {
        val injected = priceConfig?.clone() ?: return ConfiguredPrice.FREE
        injected.injectPlaceholders(StaticPlaceholder("slot") { slot.toString() })
        return ConfiguredPrice.create(injected) ?: ConfiguredPrice.FREE
    }

    fun buildItem(player: Player, size: Int, cap: Int, bought: Int): ItemStack {
        val maxed = size >= cap
        val price = priceFor(size + 1).getDisplay(player)

        return ItemStackBuilder(Items.lookup(if (maxed) maxItem else item))
            .addLoreLines(
                (if (maxed) maxLore else lore).map { line ->
                    line.replace(oldValue = "%size%", newValue = size.toString())
                        .replace(oldValue = "%max_size%", newValue = cap.toString())
                        .replace(oldValue = "%price%", newValue = price)
                        .replace(oldValue = "%bought%", newValue = bought.toString())
                        .formatEco(player, formatPlaceholders = true)
                }
            )
            .build()
    }
}
