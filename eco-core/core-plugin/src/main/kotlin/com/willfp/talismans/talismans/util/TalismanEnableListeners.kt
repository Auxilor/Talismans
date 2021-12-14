@file:Suppress("UNUSED_PARAMETER")

package com.willfp.talismans.talismans.util

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.events.ArmorChangeEvent
import com.willfp.libreforge.updateEffects
import com.willfp.talismans.talismans.Talismans.values
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class TalismanEnableListeners(private val plugin :EcoPlugin) : Listener {
    @EventHandler
    fun onItemPickup(event: EntityPickupItemEvent) {
        if (event.entity !is Player) {
            return
        }
        val player = event.entity as Player
        if (!TalismanUtils.isTalismanMaterial(event.item.itemStack.type)) {
            return
        }
        refreshPlayer(player)
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        refresh()
    }

    @EventHandler
    fun onPlayerLeave(event: PlayerQuitEvent) {
        refresh()
        val player = event.player
        for (talisman in values()) {
            for ((effect1) in talisman.effects) {
                effect1.disableForPlayer(player)
            }
        }
    }

    @EventHandler
    fun onInventoryDrop(event: PlayerDropItemEvent) {
        if (!TalismanUtils.isTalismanMaterial(event.itemDrop.itemStack.type)) {
            return
        }
        refreshPlayer(event.player)
    }

    @EventHandler
    fun onChangeSlot(event: PlayerItemHeldEvent) {
        refreshPlayer(event.player)
        plugin.scheduler.run { refreshPlayer(event.player) }
    }

    @EventHandler
    fun onArmorChange(event: ArmorChangeEvent) {
        refreshPlayer(event.player)
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.whoClicked !is Player) {
            return
        }
        refreshPlayer(event.whoClicked as Player)
    }

    private fun refresh() {
        plugin.server.onlinePlayers.forEach { player: Player -> refreshPlayer(player) }
    }

    private fun refreshPlayer(player: Player) {
        TalismanChecks.clearCache(player)
        player.updateEffects()
    }
}