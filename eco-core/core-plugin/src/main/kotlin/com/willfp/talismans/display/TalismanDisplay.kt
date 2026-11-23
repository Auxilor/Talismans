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
        val itemStack = context.getItemStack()

        if (!TalismanUtils.isTalismanMaterial(itemStack.type)) {
            return
        }

        if (!itemStack.hasItemMeta()) {
            return
        }

        val talisman = TalismanChecks.getTalismanOnItem(itemStack) ?: return
        val fis = itemStack.fast()

        fis.setDisplayName(talisman.name.formatEco(context.getPlaceholderContext()))

        talisman.itemStack.fast().getCustomModelData()?.let { fis.setCustomModelData(it) }

        context.getLore().prepend(
            talisman.description.map { StringUtils.formatToRichComponent(it, context.getPlaceholderContext()) }
        )

        val player = context.getPlayer() ?: return
        val lines = ItemProvidedHolder(talisman, itemStack).getNotMetLineComponents(player)

        if (lines.isNotEmpty()) {
            context.getLore().append(listOf(Component.empty()) + lines)
        }
    }
}
