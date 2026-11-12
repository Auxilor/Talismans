package com.willfp.talismans.display

import com.willfp.talismans.plugin
import com.willfp.talismans.talismans.util.TalismanChecks
import com.willfp.talismans.talismans.util.TalismanUtils
import net.kyori.adventure.text.Component
import wiki.ecohub.game.item.fast
import wiki.ecohub.rules.holder.ItemProvidedHolder
import wiki.ecohub.ui.display.DisplayContext
import wiki.ecohub.ui.display.DisplayModule
import wiki.ecohub.ui.display.DisplayPriority
import wiki.ecohub.ui.text.StringUtils
import wiki.ecohub.ui.text.formatEco

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

        context.lore.prepend(talisman.description.map { StringUtils.formatToRichComponent(it, context.placeholderContext) })

        val player = context.player ?: return
        val lines = ItemProvidedHolder(talisman, itemStack).getNotMetLineComponents(player)

        if (lines.isNotEmpty()) {
            context.lore.append(listOf(Component.empty()) + lines)
        }
    }
}
