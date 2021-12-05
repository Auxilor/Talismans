package com.willfp.talismans.talismans.util

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent

class BlockPlaceListener : Listener {
    @EventHandler(priority = EventPriority.LOW)
    fun onAttemptTalismanPlace(event: BlockPlaceEvent) {
        if (TalismanChecks.getTalismanOnItem(event.itemInHand) != null) {
            event.isCancelled = true
        }
    }
}