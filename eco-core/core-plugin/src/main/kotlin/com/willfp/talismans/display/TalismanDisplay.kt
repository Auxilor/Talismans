package com.willfp.talismans.display

import com.willfp.eco.core.display.DisplayContext
import com.willfp.eco.core.display.DisplayModule
import com.willfp.eco.core.display.DisplayPriority
import com.willfp.eco.core.fast.fast
import com.willfp.eco.util.formatEco
import com.willfp.eco.util.formatEcoRich
import com.willfp.libreforge.ItemProvidedHolder
import com.willfp.talismans.plugin
import com.willfp.talismans.talismans.util.TalismanChecks
import com.willfp.talismans.talismans.util.TalismanUtils
import net.kyori.adventure.text.Component

object TalismanDisplay : DisplayModule(plugin, DisplayPriority.LOWEST) {
    override fun display(context: DisplayContext) {
        val itemStack = context.itemStack

        if (!TalismanUtils.isTalismanMaterial(itemStack.type)) {
            return
        }

        if (!itemStack.hasItemMeta()) {
            return
        }

        val talisman = TalismanChecks.getTalismanOnItem(itemStack) ?: return
        val fis = itemStack.fast()

        fis.displayName = talisman.name.formatEco(context.placeholderContext)

        talisman.itemStack.fast().customModelData?.let { fis.customModelData = it }

        context.lore.prepend(talisman.description.formatEcoRich(context.placeholderContext))

        val player = context.player ?: return
        val lines = ItemProvidedHolder(talisman, itemStack).getNotMetLineComponents(player)

        if (lines.isNotEmpty()) {
            context.lore.append(listOf(Component.empty()) + lines)
        }
    }
}
