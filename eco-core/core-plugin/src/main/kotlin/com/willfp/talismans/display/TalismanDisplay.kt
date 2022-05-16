package com.willfp.talismans.display

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.display.Display
import com.willfp.eco.core.display.DisplayModule
import com.willfp.eco.core.display.DisplayPriority
import com.willfp.talismans.talismans.util.TalismanChecks
import com.willfp.talismans.talismans.util.TalismanUtils
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class TalismanDisplay(plugin: EcoPlugin) : DisplayModule(plugin, DisplayPriority.LOWEST) {
    override fun display(
        itemStack: ItemStack,
        player: Player?,
        vararg args: Any
    ) {
        if (!TalismanUtils.isTalismanMaterial(itemStack.type)) {
            return
        }

        val meta = itemStack.itemMeta ?: return
        val itemLore = meta.lore ?: mutableListOf()

        val talisman = TalismanChecks.getTalismanOnItem(itemStack) ?: return

        meta.setDisplayName(talisman.name)
        if (talisman.itemStack.itemMeta?.hasCustomModelData() == true) {
            meta.setCustomModelData(talisman.itemStack.itemMeta?.customModelData)
        }

        val lore = mutableListOf<String>()

        lore.addAll(talisman.description)
        lore.addAll(itemLore)

        if (player != null) {
            val lines = talisman.getNotMetLines(player).map { Display.PREFIX + it }

            if (lines.isNotEmpty()) {
                lore.add(Display.PREFIX)
                lore.addAll(lines)
            }
        }

        meta.lore = lore
        itemStack.itemMeta = meta
    }
}