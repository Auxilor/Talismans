package com.willfp.talismans.talismans.util

import com.willfp.talismans.plugin
import com.willfp.talismans.talismans.util.TalismanUtils.convert
import org.bukkit.Tag
import org.bukkit.block.ShulkerBox
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BlockStateMeta

object BlockPlaceListener : Listener {
    @EventHandler(priority = EventPriority.LOW)
    fun onAttemptTalismanPlace(event: BlockPlaceEvent) {
        if (TalismanChecks.getTalismanOnItem(event.itemInHand) != null) {
            event.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onAttemptTalismanShulkerPlace(event: BlockPlaceEvent) {
        if (plugin.configYml.getBool("prevent-talisman-shulker-place") && event.itemInHand.holdsTalisman()) {
            event.isCancelled = true
        }
    }

    private fun ItemStack.holdsTalisman(): Boolean {
        val meta = if (Tag.SHULKER_BOXES.isTagged(type)) itemMeta as? BlockStateMeta else null
        if (meta == null || !meta.hasBlockState()) {
            return false
        }

        val shulkerBox = meta.blockState as? ShulkerBox
        return shulkerBox != null && shulkerBox.inventory.contents.any { item ->
            convert(item)
            TalismanChecks.getTalismanOnItem(item) != null
        }
    }
}
