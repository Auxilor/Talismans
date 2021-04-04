package com.willfp.talismans.talismans.util;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

public class BlockPlaceListener implements Listener {
    /**
     * Called on block place.
     * @param event The event to listen for.
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onAttemptTalismanPlace(@NotNull final BlockPlaceEvent event) {
        if (TalismanChecks.getTalismanOnItem(event.getItemInHand()) != null) {
            event.setCancelled(true);
        }
    }
}
